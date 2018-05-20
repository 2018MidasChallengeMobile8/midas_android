package com.xema.midas.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xema.midas.R;
import com.xema.midas.util.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.edt_id)
    EditText edtId;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.cb_auto_sign_in)
    CheckBox cbAutoSignIn;
    @BindView(R.id.btn_action)
    Button btnAction;
    @BindView(R.id.tv_help_left)
    TextView tvHelpLeft;
    @BindView(R.id.tv_help_right)
    TextView tvHelpRight;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        initListeners();
    }

    private void initListeners() {
        llBottom.setOnClickListener(this::convertPageMode);
        btnAction.setOnClickListener(this::attemptAction);
        edtId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String id = s.toString();
                String password = edtPassword.getText().toString();
                changeSignInButton(CommonUtils.isValidId(id) && CommonUtils.isValidPassword(password));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                String id = edtId.getText().toString();
                changeSignInButton(CommonUtils.isValidId(id) && CommonUtils.isValidPassword(password));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void changeSignInButton(boolean enable) {
        btnAction.setEnabled(enable);
        if (enable) {
            btnAction.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        } else {
            btnAction.setTextColor(ContextCompat.getColor(this, R.color.colorBlue));
        }
    }


    private void attemptAction(View view) {
        String id = edtId.getText().toString();
        String password = edtPassword.getText().toString();

        if (cbAutoSignIn.getVisibility() == View.GONE) {
            // TODO: 2018-05-20 회원가입 액션
        } else {
            // TODO: 2018-05-20 로그인 액션
        }
    }

    private void convertPageMode(View view) {
        if (cbAutoSignIn.getVisibility() == View.GONE) {
            changeSignInPage();
        } else {
            changeSignUpPage();
        }
    }

    private void changeSignUpPage() {
        edtPassword.setText("");
        edtId.setText("");
        edtId.requestFocus();

        cbAutoSignIn.setVisibility(View.GONE);
        btnAction.setText(getString(R.string.common_sign_up));
        tvHelpLeft.setText("이미 계정이 있으신가요?");
        tvHelpRight.setText("로그인하기.");
    }

    private void changeSignInPage() {
        edtPassword.setText("");
        edtId.setText("");
        edtId.requestFocus();

        cbAutoSignIn.setVisibility(View.VISIBLE);
        btnAction.setText(getString(R.string.common_sign_in));
        tvHelpLeft.setText("계정이 없으신가요?");
        tvHelpRight.setText("가입하기.");
    }
}
