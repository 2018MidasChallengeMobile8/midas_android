package com.xema.midas.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xema.midas.R;
import com.xema.midas.common.GlideApp;
import com.xema.midas.common.PreferenceHelper;
import com.xema.midas.model.ApiResult;
import com.xema.midas.network.ApiUtil;
import com.xema.midas.util.LoadingProgressDialog;
import com.xema.midas.util.PermissionUtil;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xema0 on 2018-05-25.
 */

public class PostUploadActivity extends AppCompatActivity {
    @BindView(R.id.edt_title)
    EditText edtTitle;
    @BindView(R.id.edt_information)
    EditText edtInformation;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.iv_gallery)
    ImageView ivGallery;
    @BindView(R.id.btn_upload)
    Button btnUpload;

    private Context mContext;

    private final static int GALLERY_REQUEST_CODE = 203;
    private final static int CROP_REQUEST_CODE = 303;

    private File mPostImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);
        mContext = this;
        ButterKnife.bind(this);

        initListeners();
    }

    private void initListeners() {
        ivGallery.setOnClickListener(this::attemptGallery);
        btnUpload.setOnClickListener(this::attemptUpload);
    }

    private void attemptGallery(View view) {
        if (PermissionUtil.checkAndRequestPermission(this, PermissionUtil.PERMISSION_GALLERY, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            startGallery(GALLERY_REQUEST_CODE);
        }
    }

    private void attemptUpload(View view) {
        if (mPostImage == null) {
            Toast.makeText(mContext, "이미지를 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }


        String title = edtTitle.getText().toString();
        String text = edtInformation.getText().toString();

        MediaType multipart = MediaType.parse("multipart/form-data");
        RequestBody requestFile = RequestBody.create(multipart, mPostImage);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", mPostImage.getName(), requestFile);

        MultipartBody.Part idPart = MultipartBody.Part.createFormData("id", PreferenceHelper.loadId(mContext));
        MultipartBody.Part pwPart = MultipartBody.Part.createFormData("pw", PreferenceHelper.loadPw(mContext));
        MultipartBody.Part titlePart = MultipartBody.Part.createFormData("title", title);
        MultipartBody.Part textPart = MultipartBody.Part.createFormData("text", text);

        LoadingProgressDialog.showProgress(mContext);
        btnUpload.setEnabled(false);

        ApiUtil.getPostService().uploadPost(idPart, pwPart, titlePart, textPart, imagePart).enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(@NonNull Call<ApiResult> call, @NonNull Response<ApiResult> response) {
                LoadingProgressDialog.hideProgress();
                btnUpload.setEnabled(true);
                if (response.code() == 200) {
                    Toast.makeText(mContext, "업로드 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(mContext, getString(R.string.error_common), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResult> call, @NonNull Throwable t) {
                LoadingProgressDialog.hideProgress();
                btnUpload.setEnabled(true);
                Toast.makeText(mContext, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.PERMISSION_GALLERY:
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    startGallery(GALLERY_REQUEST_CODE);
                } else {
                    PermissionUtil.showRationalDialog(mContext, getString(R.string.permission_need_permission));
                }
                break;
        }
    }

    private void startCrop(Uri originalUri, int requestCode) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.colorWhite));
        options.setToolbarWidgetColor(getResources().getColor(R.color.colorGray3));
        options.setToolbarTitle(getString(R.string.title_crop_image));
        options.setLogoColor(getResources().getColor(R.color.colorBlack));
        options.setActiveWidgetColor(getResources().getColor(R.color.colorGray4));
        options.setStatusBarColor(getResources().getColor(R.color.colorBlack));
        options.setCompressionQuality(90);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        if (requestCode == GALLERY_REQUEST_CODE) {
            mPostImage = new File(mContext.getCacheDir(), System.currentTimeMillis() + "_crop");
            UCrop.of(originalUri, Uri.fromFile(mPostImage)).withOptions(options).withAspectRatio(4, 2).withMaxResultSize(800, 400).start(this, CROP_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == GALLERY_REQUEST_CODE) && (resultCode == RESULT_OK) && data != null) {
            if (data.getData() != null) {
                startCrop(data.getData(), requestCode);
            } else {
                Toast.makeText(mContext, getString(R.string.error_common), Toast.LENGTH_SHORT).show();
            }
        } else if ((requestCode == CROP_REQUEST_CODE) && (resultCode == RESULT_OK)) {
            if (mPostImage != null) {
                ivGallery.setVisibility(View.GONE);
                GlideApp.with(this).load(mPostImage).into(ivImage);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Toast.makeText(mContext, getString(R.string.error_common), Toast.LENGTH_SHORT).show();
        }
    }
}
