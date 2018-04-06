package com.techindustan.office.ui.message;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.techindustan.model.employee.Employee;
import com.techindustan.office.R;
import com.techindustan.office.ui.base.BaseActivity;
import com.techindustan.office.ui.employee.EmployeeActivity;
import com.techindustan.office.ui.employee.MessageDialog;
import com.techindustan.office.ui.splash.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends BaseActivity implements MessageView {

    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.tvVenue)
    TextView tvVenue;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvSenderName)
    TextView tvSenderName;
    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.btnOk)
    Button btnOk;
    @BindView(R.id.btnReply)
    Button btnReply;
    AudioManager mAudioManager;
    private int defVolume;
    //MediaPlayer mp = null;
    private Vibrator vib;
    int stopVib = 0;
    NotificationManager notificationManager;
    boolean isOkClick = false;
    final Handler handler = new Handler();
    Runnable runnable;
    MessagePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        presenter = new MessagePresenter(this);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        defVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        ButterKnife.bind(this);
        Bundle bd = getIntent().getExtras();
        if (bd != null) {
            try {
                final JSONObject object = new JSONObject(bd.getString("data"));
                tvMessage.setText(Html.fromHtml(object.getString("message")));
                tvVenue.setText(object.getString("venue"));
                tvTime.setText(object.getString("time"));

                String str = "Message from ";
                String str2 = object.getString("sender_name");
                Spannable employeeName = new SpannableString(str + str2);
                employeeName.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.techblue)), str.length(), employeeName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvSenderName.setText(employeeName);

                Glide.with(this).load(object.getString("sender_image")).asBitmap().placeholder(R.drawable.ic_profile_bg).
                        into(new BitmapImageViewTarget(ivProfile) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivProfile.setImageDrawable(circularBitmapDrawable);
                            }

                        });
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopVib = 1;
                        // stopAlarm();
                        try {
                            notificationManager.cancel(112);
                            setVolumeBackToDefault();
                            isOkClick = true;
                        } catch (Exception e) {

                        }
                        finish();
                    }
                });
                btnReply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopVib = 1;
                        notificationManager.cancel(112);
                        isOkClick = true;
                        ReplyDialog replyDialog = new ReplyDialog(MessageActivity.this, R.style.Theme_Dialog, presenter,object);
                        replyDialog.show();
                    }
                });

                if (!bd.getBoolean("from_notification", false)) {
                    startAlarm();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Log.e("dessssssss", "destroyed");
            setVolumeBackToDefault();
            if (runnable != null)
                handler.removeCallbacks(runnable);

        } catch (Exception e) {

        }
    }

    void setVolumeBackToDefault() {
        if (mAudioManager != null)
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, defVolume, 0);

    }

    void startAlarm() {
        // Uri myUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.beep);
        //mp = new MediaPlayer();
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume / 2, 0);
        //try {
            /*mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(getApplicationContext(), myUri);
            mp.setLooping(false);
            mp.prepare();
            mp.start();*/
        runnable = new Runnable() {
            @Override
            public void run() {
                if (stopVib == 0) {
                    vib.vibrate(500);
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(runnable, 500);

        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (stopVib == 0) {
                    vib.vibrate(500);
                    handler.postDelayed(this, 1000);
                }
            }
        }, 500);*/
       /* } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    void stopAlarm() {
        /*if (mp != null) {
            mp.stop();
        }*/
    }

}
