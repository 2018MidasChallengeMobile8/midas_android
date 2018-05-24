package com.xema.midas.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xema.midas.R;
import com.xema.midas.activity.EditProfileActivity;
import com.xema.midas.common.GlideApp;
import com.xema.midas.common.PreferenceHelper;
import com.xema.midas.model.Profile;
import com.xema.midas.widget.GrayScaleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_comment)
    TextView tvComment;

    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public void onResume() {
        super.onResume();

        Profile profile = PreferenceHelper.loadMyProfile(getContext());
        if (profile == null) return;

        if (profile.getProfileImage() != null) {
            GlideApp.with(this).load(profile.getProfileImage()).error(R.drawable.ic_profile_default).into(ivCircle);
            GlideApp.with(this).load(profile.getProfileImage()).error(R.drawable.ic_profile_default).into(givBackground);
        }
        if (!TextUtils.isEmpty(profile.getName())) {
            tvName.setText(profile.getName());
        }
        if (!TextUtils.isEmpty(profile.getComment())) {
            tvComment.setText(profile.getComment());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, null);
        unbinder = ButterKnife.bind(this, view);

        ivEditProfileImage.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
