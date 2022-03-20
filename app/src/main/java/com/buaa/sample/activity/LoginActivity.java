package com.buaa.sample.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;

import com.buaa.sample.Constant;
import com.buaa.sample.net.NetworkRunnable;
import com.buaa.sample.R;
import com.buaa.sample.databinding.ActivityLoginBinding;
import com.buaa.sample.util.SharedPreferenceUtil;
import com.buaa.sample.util.UiUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class LoginActivity extends BaseActivity
        implements View.OnClickListener {

    private ActivityLoginBinding activityLoginBinding;
    private SharedPreferenceUtil sharedPreferenceUtils;

    private NetworkRunnable mRequestRunnable;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        handler = new Handler();
        sharedPreferenceUtils = SharedPreferenceUtil.getInstance(this, "user");
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
        if (view.getId() == R.id.btn_register_or_login) {
            try {
                String[] params = checkInputs();
                mRequestRunnable = new NetworkRunnable(() -> {
                    dismissLoadingBar();
                    process(params[0], params[1]);
                });
                requestServer(mRequestRunnable);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private String[] checkInputs() throws IllegalAccessException {
        UiUtil.hideKeyboard(this);
        final String username = activityLoginBinding.etUsername.getText().toString();
        if (TextUtils.isEmpty(username)) {
            activityLoginBinding.etUsername.setError(getString(R.string.invalid_username));
            throw new IllegalAccessException("invalid username");
        }
        final String password = activityLoginBinding.etPassword.getText().toString();
        if (TextUtils.isEmpty(password) || password.trim().length() < 5) {
            activityLoginBinding.etPassword.setError(getString(R.string.invalid_password));
            throw new IllegalAccessException("invalid password");
        }
        return new String[]{username, password};
    }

    private void process(String username, String password) {

        if (sharedPreferenceUtils.isUsernameSaved(username)) {
            try {
                String pwd = Base64.encodeToString(MessageDigest.getInstance("SHA-256")
                        .digest(password.getBytes(StandardCharsets.UTF_8)), Constant.BASE64_FLAG);
                if (pwd.equals(sharedPreferenceUtils.getPassword(username)))
                    PromptActivity.launch(LoginActivity.this, username);
                else {
                    toast(getString(R.string.login_failed));
                }
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

    public void requestServer(NetworkRunnable runnable) {
        showLoadingBar();
        handler.postDelayed(runnable, 1500);
    }

    private void clearUserInputs() {
        activityLoginBinding.etUsername.setText("");
        activityLoginBinding.etPassword.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (handler != null && mRequestRunnable != null)
            handler.removeCallbacks(mRequestRunnable);

        activityLoginBinding = null;
    }
}