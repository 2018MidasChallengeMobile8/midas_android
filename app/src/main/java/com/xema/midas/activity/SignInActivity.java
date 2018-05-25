package com.xema.midas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xema.midas.R;
import com.xema.midas.common.PreferenceHelper;
import com.xema.midas.model.ApiResult;
import com.xema.midas.model.Profile;
import com.xema.midas.network.ApiUtil;
import com.xema.midas.util.CommonUtils;
import com.xema.midas.util.LoadingProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        //자동로그인
        if (PreferenceHelper.loadAutoSignInEnabled(this)) {
            attemptSignIn(PreferenceHelper.loadId(this), PreferenceHelper.loadPw(this));
        } else {
            String id = PreferenceHelper.loadId(this);
            if (!TextUtils.isEmpty(id)) {
                edtId.setText(id);
                edtPassword.requestFocus();
            }
        }
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

        if (isSignUpMode()) {
            attemptSignUp(id, password);
        } else {
            attemptSignIn(id, password);
        }
    }

    private void attemptSignIn(String id, String password) {
        LoadingProgressDialog.showProgress(this);
        ApiUtil.getAccountService().signIn(id, password).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                LoadingProgressDialog.hideProgress();
                if (response.code() == 200) {
                    PreferenceHelper.saveAutoSignInEnabled(SignInActivity.this, cbAutoSignIn.isChecked());
                    PreferenceHelper.saveId(SignInActivity.this, id);
                    PreferenceHelper.savePw(SignInActivity.this, password);

                    Profile profile = response.body();
                    if (profile == null) return;
                    PreferenceHelper.saveMyProfile(SignInActivity.this, profile);

                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignInActivity.this, "아이디 혹은 패스워드가 다릅니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                Toast.makeText(SignInActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                LoadingProgressDialog.hideProgress();
            }
        });
    }

    private void attemptSignUp(String id, String password) {
        LoadingProgressDialog.showProgress(this);
        ApiUtil.getAccountService().signUp(id, password).enqueue(new Callback<ApiResult>() {
            @Override
            public void onResponse(@NonNull Call<ApiResult> call, @NonNull Response<ApiResult> response) {
                LoadingProgressDialog.hideProgress();
                if (response.code() == 200) {
                    Toast.makeText(SignInActivity.this, "회원가입 되었습니다.", Toast.LENGTH_SHORT).show();
                    changeSignInPage();
                    edtId.setText(id);
                    edtPassword.setText(password);
                    btnAction.requestFocus();
                } else {
                    Toast.makeText(SignInActivity.this, "이미 존재하는 아이디입니다.\n다른 아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResult> call, @NonNull Throwable t) {
                Toast.makeText(SignInActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                LoadingProgressDialog.hideProgress();
            }
        });
    }

    private boolean isSignUpMode() {
        return cbAutoSignIn.getVisibility() == View.GONE;
    }

    private void convertPageMode(View view) {
        if (isSignUpMode()) {
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
