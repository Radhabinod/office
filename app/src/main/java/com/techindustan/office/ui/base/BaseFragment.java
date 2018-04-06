package com.techindustan.office.ui.base;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techindustan.model.login.Response;
import com.techindustan.office.utils.Constants;
import com.techindustan.office.utils.Utilities;
import com.techindustan.office.utils.ui.DialogUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment implements BaseView {

    private ProgressDialog progressDialog;
    Response userdetail = null;
    Gson gson;
    GsonBuilder builder;

    @Override
    public void showProgress() {
        progressDialog = DialogUtils.showLoadingDialog(getActivity());
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
                getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public Response getUserDetail() {
        if (userdetail == null) {
            builder = new GsonBuilder();
            gson = builder.create();
            userdetail = gson.fromJson(Utilities.getStringPref(getActivity(), Constants.USER_DETAILS), Response.class);
        }
        return userdetail;
    }


}
