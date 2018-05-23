package com.xema.midas.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xema.midas.R;
import com.xema.midas.common.GlideApp;
import com.xema.midas.common.PreferenceHelper;
import com.xema.midas.model.ApiResult;
import com.xema.midas.model.Profile;
import com.xema.midas.network.ApiUtil;
import com.xema.midas.util.LoadingProgressDialog;
import com.xema.midas.util.PermissionUtil;
import com.xema.midas.widget.GrayScaleImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by xema0 on 2018-05-20.
 */

public class MyPageFragment extends Fragment {
    @BindView(R.id.giv_background)
    GrayScaleImageView givBackground;
    @BindView(R.id.iv_circle)
    RoundedImageView ivCircle;
    Unbinder unbinder;
    @BindView(R.id.iv_edit_profile_image)
    ImageView ivEditProfileImage;

    private final static int GALLERY_REQUEST_CODE_PROFILE = 203;
    private final static int CROP_REQUEST_CODE_PROFILE = 303;

    private Context mContext;
    private File mProfileImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, null);
        unbinder = ButterKnife.bind(this, view);

        Profile profile = PreferenceHelper.loadMyProfile(getContext());
        if (profile != null && profile.getProfileImage() != null) {
            GlideApp.with(this).load(profile.getProfileImage()).into(ivCircle);
            GlideApp.with(this).load(profile.getProfileImage()).into(givBackground);
        }

        ivEditProfileImage.setOnClickListener(this::attemptEditProfileImage);

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.PERMISSION_GALLERY:
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    startGallery(GALLERY_REQUEST_CODE_PROFILE);
                } else {
                    PermissionUtil.showRationalDialog(getContext(), getString(R.string.permission_need_permission));
                }
                break;
        }
    }

    private void attemptEditProfileImage(View view) {
        if (PermissionUtil.checkAndRequestPermission(this, PermissionUtil.PERMISSION_GALLERY, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            startGallery(GALLERY_REQUEST_CODE_PROFILE);
        }
    }

    private void startGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
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

        if (requestCode == GALLERY_REQUEST_CODE_PROFILE) {
            mProfileImage = new File(mContext.getCacheDir(), System.currentTimeMillis() + "_crop");
            UCrop.of(originalUri, Uri.fromFile(mProfileImage)).withOptions(options).withAspectRatio(1, 1).withMaxResultSize(500, 500).start(mContext, this, CROP_REQUEST_CODE_PROFILE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == GALLERY_REQUEST_CODE_PROFILE) && (resultCode == RESULT_OK) && data != null) {
            if (data.getData() != null) {
                startCrop(data.getData(), requestCode);
            } else {
                Toast.makeText(mContext, getString(R.string.error_common), Toast.LENGTH_SHORT).show();
            }
        } else if ((requestCode == CROP_REQUEST_CODE_PROFILE) && (resultCode == RESULT_OK)) {
            //final Uri resultUri = UCrop.getOutput(data);
            uploadProfileImage();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            //UCrop.getError(data).printStackTrace();
            Toast.makeText(mContext, getString(R.string.error_common), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfileImage() {
        if (mProfileImage == null) {
            Toast.makeText(mContext, getString(R.string.error_common), Toast.LENGTH_SHORT).show();
            return;
        }

        LoadingProgressDialog.showProgress(mContext);
        ivEditProfileImage.setEnabled(false);

        MediaType multipart = MediaType.parse("multipart/form-data");
        RequestBody requestFile = RequestBody.create(multipart, mProfileImage);
        MultipartBody.Part image = MultipartBody.Part.createFormData("photo", mProfileImage.getName(), requestFile);

        MultipartBody.Part id = MultipartBody.Part.createFormData("id", PreferenceHelper.loadId(mContext));
        MultipartBody.Part pw = MultipartBody.Part.createFormData("pw", PreferenceHelper.loadPw(mContext));

        ApiUtil.getAccountService().uploadProfileImage(id, pw, image).enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(@NonNull Call<ApiResult> call, @NonNull Response<ApiResult> response) {
                LoadingProgressDialog.hideProgress();
                ivEditProfileImage.setEnabled(true);
                if (response.code() == 200) {
                    // TODO: 2018-05-23 서버에서 받아온 데이터로 이미지 갱신해야함
                    GlideApp.with(mContext).load(mProfileImage).into(ivCircle);
                    GlideApp.with(mContext).load(mProfileImage).into(givBackground);
                } else {
                    Toast.makeText(mContext, getString(R.string.error_common), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResult> call, @NonNull Throwable t) {
                LoadingProgressDialog.hideProgress();
                ivEditProfileImage.setEnabled(true);
                t.printStackTrace();
                Toast.makeText(mContext, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
