package com.techindustan.office.ui.message;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.techindustan.office.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by binod on 4/4/18.
 */

public class ReplyDialog extends Dialog {

    String[] message = {"Coming", "Busy", "Coming in 2 mins", "Coming in 5 mins", "Coming in 10 mins", "Other"};
    @BindView(R.id.tvSendTo)
    TextView tvSendTo;
    @BindView(R.id.ivMessage)
    ImageView ivMessage;
    @BindView(R.id.spMessage)
    Spinner spMessage;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.llMessage)
    LinearLayout llMessage;
    @BindView(R.id.btnSendMessage)
    Button btnSendMessage;
    MessagePresenter presenter;
    JSONObject object;

    public ReplyDialog(@NonNull Context context, int themeResId, MessagePresenter presenter, JSONObject object) {
        super(context, themeResId);
        setContentView(R.layout.reply_dialog);
        this.presenter = presenter;
        this.object=object;
        ButterKnife.bind(this);
        try {
            String str = context.getString(R.string.reply_to) + " ";
            String str2 = object.getString("sender_name");
            Spannable employeeName = new SpannableString(str + str2);
            employeeName.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.techblue)), str.length(), employeeName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvSendTo.setText(employeeName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter adapter = new ArrayAdapter(context, R.layout.spinner_textview, message);
        spMessage.setAdapter(adapter);
        spMessage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (message[i].toLowerCase().equals("other")) {
                    llMessage.setVisibility(View.VISIBLE);
                } else {
                    llMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @OnClick(R.id.btnSendMessage)
    public void onViewClicked() {

        String message = spMessage.getSelectedItem().toString();
        if (message.toLowerCase().equals("other")) {
            message = etMessage.getText().toString();
        }
        try {
            object.getString("sender_name");
            presenter.sendReply("45212", message, "sadsadsa521421");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
