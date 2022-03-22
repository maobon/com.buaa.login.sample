package com.buaa.sample.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * create by xin on 2022-3-22
 * Base Activity
 */

public class BaseActivity extends AppCompatActivity {

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
    }

    private void initViews() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("请稍后")
                .setMessage("正在请求服务端");
        alertDialog = builder.create();
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showLoadingBar() {
        alertDialog.show();
    }

    public void dismissLoadingBar() {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
        alertDialog = null;
    }
}
