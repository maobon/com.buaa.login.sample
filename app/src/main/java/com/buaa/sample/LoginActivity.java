package com.buaa.sample;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;

import com.buaa.sample.databinding.ActivityLoginBinding;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ActivityLoginBinding activityLoginBinding;
    private SharedPreferenceUtils sharedPreferenceUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        sharedPreferenceUtils = SharedPreferenceUtils.getInstance(this, "user");
        initViews();
    }

    private void initViews() {
        activityLoginBinding.btnRegisterOrLogin.setOnClickListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, sharedPreferenceUtils.queryAllUsernames());
        activityLoginBinding.etUsername.setThreshold(0);
        activityLoginBinding.etUsername.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        UiUtils.hideKeyboard(this);
        if (view.getId() == R.id.btn_register_or_login) {
            final String username = activityLoginBinding.etUsername.getText().toString();
            if (TextUtils.isEmpty(username)) {
                activityLoginBinding.etUsername.setError(getString(R.string.invalid_username));
                return;
            }
            final String password = activityLoginBinding.etPassword.getText().toString();
            if (TextUtils.isEmpty(password) || password.trim().length() < 5) {
                activityLoginBinding.etPassword.setError(getString(R.string.invalid_password));
                return;
            }
            requestServer();
            if (sharedPreferenceUtils.isUsernameSaved(username)) {
                try {
                    String pwd = Base64.encodeToString(MessageDigest.getInstance("SHA-256")
                            .digest(password.getBytes(StandardCharsets.UTF_8)), Constant.BASE64_FLAG);
                    if (pwd.equals(sharedPreferenceUtils.getPassword(username)))
                        PromptActivity.launch(LoginActivity.this, username);
                    else toast(getString(R.string.login_failed));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (sharedPreferenceUtils.savePassword(username, password)) {
                    clearUserInputs();
                    toast(getString(R.string.welcome));
                    PromptActivity.launch(LoginActivity.this, username);
                }
            }
        }
    }

    private void clearUserInputs() {
        activityLoginBinding.etUsername.setText("");
        activityLoginBinding.etPassword.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityLoginBinding = null;
    }
}