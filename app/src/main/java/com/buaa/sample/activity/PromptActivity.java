package com.buaa.sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.buaa.sample.databinding.ActivityPromptBinding;

public class PromptActivity extends BaseActivity {

    public static void launch(Activity activity, String username) {
        Intent intent = new Intent(activity, PromptActivity.class);
        intent.putExtra("username", username);
        activity.startActivity(intent);
        activity.finish();
    }

    private ActivityPromptBinding activityPromptBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPromptBinding = ActivityPromptBinding.inflate(getLayoutInflater());
        setContentView(activityPromptBinding.getRoot());

        String username = getIntent().getStringExtra("username");
        if (TextUtils.isEmpty(username)) return;
        activityPromptBinding.tvPrompt.setText(String.format("Welcome!!\nusername:%s", username));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityPromptBinding = null;
    }
}