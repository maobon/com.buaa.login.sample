package com.buaa.sample;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class BaseActivity extends AppCompatActivity {

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Please wait")
                .setMessage("requesting server");
        alertDialog = builder.create();
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void requestServer() {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        } else {
            alertDialog.show();
            new Handler().postDelayed(() -> {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
            }, 800 * new Random().nextInt(3));
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
