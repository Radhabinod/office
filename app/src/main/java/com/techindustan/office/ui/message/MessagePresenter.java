package com.techindustan.office.ui.message;

import android.widget.Toast;

import com.techindustan.model.notification.Notification;
import com.techindustan.office.network.ApiClient;
import com.techindustan.office.network.ApiInterface;
import com.techindustan.office.ui.base.BasePresenter;
import com.techindustan.office.ui.employee.EmployeeActivity;
import com.techindustan.office.utils.Utilities;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by binod on 4/4/18.
 */

public class MessagePresenter extends BasePresenter implements MesssagePresenterInterface {

    MessageView view;
    ApiInterface apiInterface;

    MessagePresenter(MessageView view) {
        this.view = view;
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

    }

    @Override
    public void sendReply(String employeeId, String message, String accessToken) {
        Call<Notification> loginCall = apiInterface.sendReply(accessToken, message, employeeId + "", "reply");
        loginCall.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, retrofit2.Response<Notification> response) {
                view.hideProgress();

                if (response.code() == 200) {
                    Notification notification = response.body();
                    if (notification.getStatus() == 200) {
                        view.showToast(notification.getResponse());

                    }
                } else {
                    try {
                        view.showToast(Utilities.getErrorMessage(response.errorBody().string()).toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                view.hideProgress();
            }
        });
    }
}
