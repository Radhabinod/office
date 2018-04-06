package com.techindustan.office.ui.employee;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.techindustan.model.employee.Response;
import com.techindustan.model.notification.Notification;
import com.techindustan.office.R;
import com.techindustan.office.network.ApiClient;
import com.techindustan.office.network.ApiInterface;
import com.techindustan.office.ui.nav_drawer.MainActivity;
import com.techindustan.office.utils.Utilities;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by binod on 28/3/18.
 */

public class MessageDialog extends Dialog {
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.spTime)
    Spinner spTime;
    @BindView(R.id.spVenue)
    Spinner spVenue;
    @BindView(R.id.btnSendMessage)
    Button btnSendMessage;
    String[] vanues = {"At my Desk", "Director’s Cabin", "HR Cabin", "VP Engg’s Cabin", "Conference Room", "Main Working area"};
    String[] times = {"Now", "1 min", "2 mins", "5 mins", "10 mins"};
    Context context;
    Response employee;
    ApiInterface apiInterface;
    @BindView(R.id.tvSendTo)
    TextView tvSendTo;

    public MessageDialog(@NonNull Context context, int themeResId, Response employee) {
        super(context, themeResId);
        this.context = context;
        this.employee = employee;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_dialog);
        ButterKnife.bind(this);
        arrageVenueOrderByDesignation();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (employee != null) {
            String str = "Send message to ";
            String str2 = employee.getFirst_name();
            Spannable employeeName = new SpannableString(str + str2);
            //employeeName.setSpan(new ForegroundColorSpan(Color.RED), 1, employeeName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            employeeName.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.techblue)), str.length(), employeeName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvSendTo.setText(employeeName);
            ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_textview, vanues);
            spVenue.setAdapter(adapter);
            ArrayAdapter adapterTime = new ArrayAdapter(context, R.layout.spinner_textview, times);
            spTime.setAdapter(adapterTime);
            etMessage.setSelection(etMessage.getText().toString().length());
            etMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        if (etMessage.getText().toString().equals("Please come.")) {
                            etMessage.setText("");
                        }
                    }
                }
            });
        }
    }

    void arrageVenueOrderByDesignation() {
        String job_title = ((MainActivity) context).getUserDetail().getJob_title().toLowerCase();
        String temp = vanues[0];
        if (job_title.contains("ceo") || job_title.contains("cto")) {
            vanues[0] = vanues[1];
            vanues[1] = temp;
        } else if (job_title.contains("hr")) {
            vanues[0] = vanues[2];
            vanues[2] = temp;
        } else if (job_title.contains("head of engineering")) {
            vanues[0] = vanues[3];
            vanues[3] = temp;
        }
    }

    @OnClick(R.id.btnSendMessage)
    public void onViewClicked() {
        if (isValid()) {
            String access_token = ((MainActivity) context).getUserDetail().getAccess_token();
            ((MainActivity) context).showProgress();
            Call<Notification> loginCall = apiInterface.sendMessage(access_token, spVenue.getSelectedItem().toString(), etMessage.getText().toString(), spTime.getSelectedItem().toString(), employee.getId() + "");
            loginCall.enqueue(new Callback<Notification>() {
                @Override
                public void onResponse(Call<Notification> call, retrofit2.Response<Notification> response) {
                    ((MainActivity) context).hideProgress();
                    if (response.code() == 200) {
                        Notification notification = response.body();
                        if (notification.getStatus() == 200) {
                            Toast.makeText(context, "" + notification.getResponse(), Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    } else {
                        try {
                            Toast.makeText(context, Utilities.getErrorMessage(response.errorBody().string()), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Notification> call, Throwable t) {
                    ((MainActivity) context).hideProgress();
                }
            });
        }

    }

    //validation of fields
    boolean isValid() {
        if (etMessage.getText().toString().trim().length() < 1) {
            Toast.makeText(context, "Message field can not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
