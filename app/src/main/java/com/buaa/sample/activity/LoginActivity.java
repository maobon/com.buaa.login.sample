package com.buaa.sample.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;

import com.buaa.sample.Constant;
import com.buaa.sample.R;
import com.buaa.sample.databinding.ActivityLoginBinding;
import com.buaa.sample.net.NetworkRunnable;
import com.buaa.sample.utils.SharedPreferenceUtil;
import com.buaa.sample.utils.UiUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * create by xin on 2022-3-22
 * Login Activity
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;

    private SharedPreferenceUtil mPreferenceUtil;
    private NetworkRunnable mRequestRunnable;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mHandler = new Handler();
        mPreferenceUtil = SharedPreferenceUtil.getInstance(this, "user");

        initViews();
    }

    private void initViews() {
        binding.btnRegisterOrLogin.setOnClickListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, mPreferenceUtil.queryAllUsernames());
        binding.etUsername.setThreshold(0);
        binding.etUsername.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_register_or_login) {
            try {
                String[] userInputs = checkInputs();
                mRequestRunnable = new NetworkRunnable(() -> {
                    dismissLoadingBar();
                    process(userInputs);
                });
                requestServer(mRequestRunnable);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private String[] checkInputs() throws IllegalAccessException {
        UiUtil.hideKeyboard(this);
        final String username = binding.etUsername.getText().toString();
        if (TextUtils.isEmpty(username)) {
            binding.etUsername.setError(getString(R.string.invalid_username));
            throw new IllegalAccessException("invalid username");
        }
        final String password = binding.etPassword.getText().toString();
        if (TextUtils.isEmpty(password) || password.trim().length() < 5) {
            binding.etPassword.setError(getString(R.string.invalid_password));
            throw new IllegalAccessException("invalid password");
        }
        return new String[]{username, password};
    }

    private void process(String[] inputs) {
        if (mPreferenceUtil.isUsernameSaved(inputs[0])) {
            try {
                String pwd = Base64.encodeToString(MessageDigest.getInstance("SHA-256")
                        .digest(inputs[1].getBytes(StandardCharsets.UTF_8)), Constant.BASE64_FLAG);
                if (pwd.equals(mPreferenceUtil.getPassword(inputs[0]))) {
                    toast(getString(R.string.welcome));
                    PromptActivity.launch(LoginActivity.this, inputs[0]);
                } else {
                    clearUserInputs();
                    toast(getString(R.string.login_failed));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mPreferenceUtil.savePassword(inputs[0], inputs[1])) {
                toast(getString(R.string.welcome_2));
                PromptActivity.launch(LoginActivity.this, inputs[0]);
            }
        }
    }

    public void requestServer(NetworkRunnable runnable) {
        showLoadingBar();
        mHandler.postDelayed(runnable, 1000);
    }

    private void clearUserInputs() {
        binding.etUsername.setText("");
        binding.etPassword.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandler != null && mRequestRunnable != null)
            mHandler.removeCallbacks(mRequestRunnable);
        binding = null;
    }
}