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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xema.midas.R;
import com.xema.midas.common.GlideApp;
import com.xema.midas.common.PreferenceHelper;
import com.xema.midas.model.ApiResult;
import com.xema.midas.model.Photo;
import com.xema.midas.model.Profile;
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
 * Created by xema0 on 2018-05-24.
 */

public class EditProfileActivity extends AppCompatActivity {
    @BindView(R.id.iv_profile)
    RoundedImageView ivProfile;
    @BindView(R.id.iv_edit_profile_image)
    ImageView ivEditProfileImage;
    @BindView(R.id.edt_nickname)
    EditText edtNickname;
    @BindView(R.id.btn_edit_name)
    Button btnEditName;
    @BindView(R.id.edt_comment)
    EditText edtComment;
    @BindView(R.id.btn_edit_comment)
    Button btnEditComment;

    private File mProfileImage;
    private Context mContext;

    private final static int GALLERY_REQUEST_CODE = 203;
    private final static int CROP_REQUEST_CODE = 303;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        mContext = this;

        Profile profile = PreferenceHelper.loadMyProfile(this);
        if (profile != null && profile.getProfileImage() != null) {
            GlideApp.with(this).load(profile.getProfileImage()).error(R.drawable.ic_profile_default).into(ivProfile);
        }

        btnEditName.setOnClickListener(this::attemptEditProfileName);
        btnEditComment.setOnClickListener(this::attemptEditProfileComment);
        ivEditProfileImage.setOnClickListener(this::attemptEditProfileImage);
    }

    private void attemptEditProfileName(View view) {
        String name = edtNickname.getText().toString();
        // TODO: 2018-05-24 invalid 체크

        String id = PreferenceHelper.loadId(this);
        String pw = PreferenceHelper.loadPw(this);
        ApiUtil.getAccountService().changeProfileName(id, pw, name).enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(@NonNull Call<ApiResult> call, @NonNull Response<ApiResult> response) {
                if (response.code() == 200) {
                    Toast.makeText(mContext, "닉네임이 수정되었습니다", Toast.LENGTH_SHORT).show();
                    Profile profile = PreferenceHelper.loadMyProfile(EditProfileActivity.this);
                    profile.setName(name);
                    PreferenceHelper.saveMyProfile(EditProfileActivity.this, profile);
                } else {
                    Toast.makeText(mContext, getString(R.string.error_common), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResult> call, @NonNull Throwable t) {
                Toast.makeText(mContext, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptEditProfileComment(View view) {
        String comment = edtComment.getText().toString();
        // TODO: 2018-05-24 invalid 체크

        String id = PreferenceHelper.loadId(this);
        String pw = PreferenceHelper.loadPw(this);
        ApiUtil.getAccountService().changeProfileComment(id, pw, comment).enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(@NonNull Call<ApiResult> call, @NonNull Response<ApiResult> response) {
                if (response.code() == 200) {
                    Toast.makeText(mContext, "자기소개가 수정되었습니다", Toast.LENGTH_SHORT).show();
                    Profile profile = PreferenceHelper.loadMyProfile(EditProfileActivity.this);
                    profile.setComment(comment);
                    PreferenceHelper.saveMyProfile(EditProfileActivity.this, profile);
                } else {
                    Toast.makeText(mContext, getString(R.string.error_common), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResult> call, @NonNull Throwable t) {
                Toast.makeText(mContext, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptEditProfileImage(View view) {
        if (PermissionUtil.checkAndRequestPermission(this, PermissionUtil.PERMISSION_GALLERY, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            startGallery(GALLERY_REQUEST_CODE);
        }
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

        if (requestCode == GALLERY_REQUEST_CODE) {
            mProfileImage = new File(mContext.getCacheDir(), System.currentTimeMillis() + "_crop");
            UCrop.of(originalUri, Uri.fromFile(mProfileImage)).withOptions(options).withAspectRatio(1, 1).withMaxResultSize(500, 500).start(this, CROP_REQUEST_CODE);
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

        ApiUtil.getAccountService().uploadProfileImage(id, pw, image).enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(@NonNull Call<Photo> call, @NonNull Response<Photo> response) {
                LoadingProgressDialog.hideProgress();
                ivEditProfileImage.setEnabled(true);
                if (response.code() == 200) {
                    Photo photo = response.body();
                    if (photo == null) return;
                    String profileImage = photo.getProfileImage();
                    if (!TextUtils.isEmpty(profileImage)) {
                        GlideApp.with(mContext).load(profileImage).error(R.drawable.ic_profile_default).into(ivProfile);
                        Profile profile = PreferenceHelper.loadMyProfile(mContext);
                        profile.setProfileImage(profileImage);
                        PreferenceHelper.saveMyProfile(EditProfileActivity.this, profile);
                    }
                } else {
                    Toast.makeText(mContext, getString(R.string.error_common), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Photo> call, @NonNull Throwable t) {
                LoadingProgressDialog.hideProgress();
                ivEditProfileImage.setEnabled(true);
                t.printStackTrace();
                Toast.makeText(mContext, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
