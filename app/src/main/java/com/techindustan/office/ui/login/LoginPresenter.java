package com.techindustan.office.ui.login;

import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techindustan.model.login.Login;
import com.techindustan.office.network.ApiClient;
import com.techindustan.office.network.ApiInterface;
import com.techindustan.office.ui.base.BasePresenter;
import com.techindustan.office.ui.employee.EmployeeActivity;
import com.techindustan.office.utils.Constants;
import com.techindustan.office.utils.Utilities;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by binod on 4/4/18.
 */

public class LoginPresenter extends BasePresenter implements LoginPesenterInterface {

    LoginView loginView;
    ApiInterface apiInterface;

    LoginPresenter(LoginView view) {
        this.loginView = view;
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public void login(String email, String password, String devicetoken) {
        loginView.showProgress();
        Call<Login> loginCall = apiInterface.login(email, password, Constants.DEVICE_TYPE, devicetoken);
        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                loginView.hideProgress();
                if (response.code() == 200 && response.body().getStatus() == 200) {
                    loginView.onLoginSuccessfull(response.body().getResponse());
                } else {
                    try {
                        loginView.showToast(Utilities.getErrorMessage(response.errorBody().string()).toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                loginView.hideProgress();
            }
        });
    }
}
