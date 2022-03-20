package com.buaa.sample;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;

import com.buaa.sample.databinding.ActivityLoginBinding;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ActivityLoginBinding loginBinding;
    private SharedPreferenceUtils sharedPreferenceUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        sharedPreferenceUtils = SharedPreferenceUtils.getInstance(this, "user");
        initViews();
    }

    private void initViews() {
        loginBinding.btnRegisterOrLogin.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, sharedPreferenceUtils.queryAllUsernames());

        loginBinding.etUsername.setThreshold(1);
        loginBinding.etUsername.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginBinding = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_register_or_login) {

            UiUtils.hideKeyboard(this);

            final String username = loginBinding.etUsername.getText().toString();
            if (isContentInvalid(username)) {
                toast("请检查输入内容");
                loginBinding.etUsername.setError("hahah");
                return;
            }

            final String password = loginBinding.etPassword.getText().toString();

            // todo 判空
            if (isContentInvalid(password)) {
                toast("请检查输入内容");
                return;
            }

            if (sharedPreferenceUtils.isUsernameSaved(username)) {
                // todo login ...
                String s = sharedPreferenceUtils.get(username);

                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] digest1 = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                    String s1 = Base64.encodeToString(digest1, Constants.BASE64_FLAG);
                    if (s1.equals(s)) {
                        toast("登录成功");
                        launchPromptActivity(username);

                    } else {
                        toast("登录失败");
                    }


                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }


            } else {
                // todo register ...
                boolean save = sharedPreferenceUtils.save(username, password);
                if (save) toast(" 注册成功 ...");
            }


        }
    }

    private boolean isContentInvalid(String userInput) {
        return TextUtils.isEmpty(userInput);
    }

    private void launchPromptActivity(String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logging in").setMessage("Please wait");
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
        new Handler().postDelayed(() -> {
            alertDialog.dismiss();
            PromptActivity.launch(LoginActivity.this, username);
        }, 1000);
    }
}