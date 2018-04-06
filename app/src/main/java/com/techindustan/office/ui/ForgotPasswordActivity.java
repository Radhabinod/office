package com.techindustan.office.ui;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.techindustan.model.reset.ResetResponse;
import com.techindustan.office.R;
import com.techindustan.office.network.ApiClient;
import com.techindustan.office.network.ApiInterface;
import com.techindustan.office.ui.base.BaseActivity;
import com.techindustan.office.utils.Utilities;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.btnForgot)
    Button btnForgot;
    ApiInterface apiInterface;
    @BindView(R.id.tvBackToLogin)
    TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        tvBackToLogin.setPaintFlags(tvBackToLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        apiInterface = ApiClient.getClientFP().create(ApiInterface.class);
        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @OnClick(R.id.btnForgot)
    public void onViewClicked() {
        if (!isValid())
            return;
        showProgress();
        Call<ResetResponse> resetPassword = apiInterface.resetPassword(etEmail.getText().toString(), "1");
        resetPassword.enqueue(new Callback<ResetResponse>() {
            @Override
            public void onResponse(Call<ResetResponse> call, Response<ResetResponse> response) {
                hideProgress();
                if (response.code() == 200) {
                    ResetResponse resetResponse = response.body();
                    Toast.makeText(ForgotPasswordActivity.this, Html.fromHtml(resetResponse.getMessage()), Toast.LENGTH_SHORT).show();
                    if (resetResponse.getSuccess()) {
                        finish();
                    }
                } else {
                    try {
                        Toast.makeText(ForgotPasswordActivity.this, Utilities.getErrorMessage(response.errorBody().string()), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResetResponse> call, Throwable t) {
                hideProgress();
            }
        });

    }

    //validation of fields
    boolean isValid() {
        if (etEmail.getText().toString().trim().length() < 1) {
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
