package com.techindustan.office.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.techindustan.office.R;
import com.techindustan.office.ui.employee.EmployeeActivity;
import com.techindustan.office.ui.login.LoginActivity;
import com.techindustan.office.ui.nav_drawer.MainActivity;
import com.techindustan.office.utils.Constants;
import com.techindustan.office.utils.Utilities;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utilities.getBooleanPref(SplashActivity.this, Constants.IS_LOGIN)) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 1500);
    }
}
