package com.techindustan.office.ui.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techindustan.model.login.Response;
import com.techindustan.office.utils.Constants;
import com.techindustan.office.utils.Utilities;
import com.techindustan.office.utils.ui.DialogUtils;


public class BaseActivity extends AppCompatActivity implements BaseView {

    private ProgressDialog progressDialog;
    NetworkInfo activeNetwork;

    Response userdetail = null;
    Gson gson;
    GsonBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void showProgress() {
        progressDialog = DialogUtils.showLoadingDialog(this);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public boolean isNetworkConnected()
    {
        return true;
    }
    public Response getUserDetail() {
        if (userdetail == null) {
            builder = new GsonBuilder();
            gson = builder.create();
            userdetail = gson.fromJson(Utilities.getStringPref(this, Constants.USER_DETAILS), Response.class);
        }
        return userdetail;
    }
}
