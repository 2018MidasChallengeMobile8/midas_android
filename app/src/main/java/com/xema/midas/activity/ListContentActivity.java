package com.xema.midas.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.xema.midas.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListContentActivity extends AppCompatActivity {
    @BindView(R.id.iv_main)
    ImageView ivMain;
    @BindView(R.id.tl_category)
    TabLayout tlCategory;
    @BindView(R.id.container)
    ViewPager container;
    @BindView(R.id.btn_apply)
    Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_information);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        Bitmap bitmap = (Bitmap)b.get("image");

        ivMain.setImageBitmap(bitmap);
    }
}
