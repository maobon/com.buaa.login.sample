package com.buaa.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.buaa.sample.databinding.ActivityPromptBinding;

/**
 * create by xin on 2022-3-22
 * Prompt Activity
 */

public class PromptActivity extends BaseActivity {

    private ActivityPromptBinding binding;

    public static void launch(Activity activity, String username) {
        Intent intent = new Intent(activity, PromptActivity.class);
        intent.putExtra("username", username);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPromptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String username = getIntent().getStringExtra("username");
        if (TextUtils.isEmpty(username)) return;
        binding.tvPrompt.setText(String.format("欢迎!!\n 用户名:%s", username));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}