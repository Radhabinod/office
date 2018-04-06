package com.techindustan.office.ui.login;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techindustan.model.login.Response;
import com.techindustan.office.R;
import com.techindustan.office.network.ApiInterface;
import com.techindustan.office.ui.ForgotPasswordActivity;
import com.techindustan.office.ui.base.BaseActivity;
import com.techindustan.office.ui.employee.EmployeeActivity;
import com.techindustan.office.ui.nav_drawer.MainActivity;
import com.techindustan.office.utils.Constants;
import com.techindustan.office.utils.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnSignin)
    Button btnSignin;
    @BindView(R.id.email_login_form)
    LinearLayout emailLoginForm;
    @BindView(R.id.ivViewPassword)
    ImageView ivViewPassword;
    ApiInterface apiInterface;
    GsonBuilder gsonBuilder;
    Gson gson;
    @BindView(R.id.tvForgotPassword)
    TextView tvForgotPassword;
    LoginPresenter presenter;
    boolean isPasswordVisible = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter = new LoginPresenter(this);
        tvForgotPassword.setPaintFlags(tvForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        getRememberFields();
    }

    //set email and password from SP
    private void getRememberFields() {
        etPassword.setText(Utilities.getStringPref(LoginActivity.this, Constants.PASSWORD));
        etEmail.setText(Utilities.getStringPref(LoginActivity.this, Constants.EMAIL));
    }

    //validation of fields
    boolean isValid() {
        if (etEmail.getText().toString().trim().length() < 1) {
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etPassword.getText().toString().trim().length() < 1) {
            return false;
        }
        return true;
    }


    @Override
    public void onLoginSuccessfull(Response response) {
        Utilities.updateBooleanPref(LoginActivity.this, Constants.IS_LOGIN, true);
        Utilities.updateStringPref(LoginActivity.this, Constants.EMAIL, etEmail.getText().toString());
        Utilities.updateStringPref(LoginActivity.this, Constants.PASSWORD, etPassword.getText().toString());
        Utilities.updateStringPref(LoginActivity.this, Constants.USER_DETAILS, gson.toJson(response));
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @OnClick({R.id.btnSignin, R.id.tvForgotPassword, R.id.ivViewPassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSignin:
                if (!isValid())
                    return;
                String device_id = Utilities.getStringPref(LoginActivity.this, Constants.DEVICE_ID);
                if (device_id.isEmpty())
                    device_id = FirebaseInstanceId.getInstance().getToken();
                presenter.login(etEmail.getText().toString(), etPassword.getText().toString(), device_id);
                break;
            case R.id.tvForgotPassword:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.ivViewPassword:
                if (!isPasswordVisible) {
                    etPassword.setTransformationMethod(null);
                    ivViewPassword.setImageResource(R.drawable.password_s_icn);
                    isPasswordVisible = true;
                } else {
                    etPassword.setTransformationMethod(new PasswordTransformationMethod());
                    ivViewPassword.setImageResource(R.drawable.password_icn);
                    isPasswordVisible = false;
                }
                break;
        }
    }
}

