package com.buaa.sample;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;

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
        assert loginBinding.btnRegisterOrLogin != null;
        loginBinding.btnRegisterOrLogin.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginBinding = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_register_or_login) {
            toast("haha");


            assert loginBinding.etUsername != null;
            String username = loginBinding.etUsername.getText().toString();
            assert loginBinding.etPassword != null;
            String password = loginBinding.etPassword.getText().toString();

            // todo 判空


            if (sharedPreferenceUtils.isUsernameSaved(username)) {
                // todo login ...
                String s = sharedPreferenceUtils.get(username);

                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");
                    byte[] digest1 = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                    String s1 = Base64.encodeToString(digest1, Base64.URL_SAFE | Base64.NO_CLOSE | Base64.NO_WRAP);
                    if (s1.equals(s)) {
                        toast("登录成功");
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


}