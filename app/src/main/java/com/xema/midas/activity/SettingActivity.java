package com.xema.midas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.xema.midas.R;
import com.xema.midas.common.PreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xema0 on 2018-05-25.
 */

public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.ll_sign_out)
    LinearLayout llSignOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        llSignOut.setOnClickListener(v -> {
            PreferenceHelper.resetAll(this);
            Intent intent = new Intent(this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}
