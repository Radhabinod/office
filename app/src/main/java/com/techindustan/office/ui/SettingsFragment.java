package com.techindustan.office.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.techindustan.office.R;
import com.techindustan.office.ui.login.LoginActivity;
import com.techindustan.office.ui.nav_drawer.MainActivity;
import com.techindustan.office.utils.Constants;
import com.techindustan.office.utils.Utilities;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    @BindView(R.id.tvLogout)
    TextView tvLogout;
    Unbinder unbinder;
    @BindView(R.id.txtNotif)
    TextView txtNotif;
    @BindView(R.id.swEnableNotification)
    Switch swEnableNotification;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, null);
        unbinder = ButterKnife.bind(this, view);
        swEnableNotification.setChecked(Utilities.getBooleanPref(getActivity(), Constants.IS_NOTIFICATION_ENABLE, true));
        startSwBotifListener();
        return view;
    }

    void startSwBotifListener() {
        swEnableNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!b) {
                    new AlertDialog.Builder(getActivity()).setMessage(R.string.disable_notif_alert_message).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //setNotifIcon(false);
                            Utilities.updateBooleanPref(getActivity(), Constants.IS_NOTIFICATION_ENABLE, false);
                            ((MainActivity) getActivity()).setNotifIcon(false);
                        }
                    }).show();
                } else {
                    Utilities.updateBooleanPref(getActivity(), Constants.IS_NOTIFICATION_ENABLE, true);
                    ((MainActivity) getActivity()).setNotifIcon(true);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(String s) {
        swEnableNotification.setOnCheckedChangeListener(null);
        if (s.equals("notiftrue")) {
            swEnableNotification.setChecked(true);
        } else if (s.equals("notiffalse")) {
            swEnableNotification.setChecked(false);
        }
        startSwBotifListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.tvLogout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvLogout:
                new AlertDialog.Builder(getActivity()).setMessage(R.string.logout_alert_message).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = Utilities.getStringPref(getActivity(), Constants.EMAIL);
                        String password = Utilities.getStringPref(getActivity(), Constants.PASSWORD);
                        Utilities.clearAllPrefs(getActivity());
                        Utilities.updateStringPref(getActivity(), Constants.EMAIL, email);
                        Utilities.updateStringPref(getActivity(), Constants.PASSWORD, password);
                        Intent it = new Intent(getActivity(), LoginActivity.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                        //finish();
                    }
                }).show();
                break;
        }
    }


}
