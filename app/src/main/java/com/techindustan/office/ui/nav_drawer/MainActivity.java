package com.techindustan.office.ui.nav_drawer;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.techindustan.office.R;
import com.techindustan.office.ui.SettingsFragment;
import com.techindustan.office.ui.base.BaseActivity;
import com.techindustan.office.ui.dashboard.DashboardFragment;
import com.techindustan.office.ui.employee.EmployeeActivity;
import com.techindustan.office.utils.Constants;
import com.techindustan.office.utils.Utilities;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.lvNav)
    ListView lvNav;
    List<String> alNavItems = new ArrayList<>();
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.swEnableNotification)
    ImageView swEnableNotification;
    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.tvEmployeeName)
    TextView tvEmployeeName;
    @BindView(R.id.tvJobTitle)
    TextView tvJobTitle;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.tvToolbarTitle)
    TextView tvToolbarTitle;
    MenuItem itemNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tvToolbarTitle.setText(getString(R.string.dashboard));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        setNavigationItems();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        swEnableNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.getBooleanPref(MainActivity.this, Constants.IS_NOTIFICATION_ENABLE, true)) {
                    new AlertDialog.Builder(MainActivity.this).setMessage(R.string.disable_notif_alert_message).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setNotifIcon(true);
                            Utilities.updateBooleanPref(MainActivity.this, Constants.IS_NOTIFICATION_ENABLE, true);
                            dialogInterface.dismiss();
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setNotifIcon(false);
                            Utilities.updateBooleanPref(MainActivity.this, Constants.IS_NOTIFICATION_ENABLE, false);

                        }
                    }).show();
                } else {
                    setNotifIcon(true);


                }
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new DashboardFragment()).commit();

    }

    public void setNotifIcon(boolean isEnable) {
        if (isEnable) {
            swEnableNotification.setImageResource(R.drawable.ic_notifications_none_black);
            itemNotification.setIcon(R.drawable.ic_notifications_none_black);
        } else {
            swEnableNotification.setImageResource(R.drawable.ic_notifications_off_black);
            itemNotification.setIcon(R.drawable.ic_notifications_off_black);
        }

    }

    void setNavigationItems() {
        Glide.with(this).load(getUserDetail().getUser_image()).asBitmap().placeholder(R.drawable.ic_profile_bg).
                into(new BitmapImageViewTarget(ivProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        ivProfile.setImageDrawable(circularBitmapDrawable);
                    }
                });
        tvEmployeeName.setText(getUserDetail().getFirst_name() + " " + getUserDetail().getLast_name());
        tvJobTitle.setText("#" + getUserDetail().getJob_title());
        alNavItems.add(getString(R.string.dashboard));
        alNavItems.add(getString(R.string.all_employee));
        alNavItems.add(getString(R.string.settings));
        lvNav.setAdapter(new NavAdapter(MainActivity.this, alNavItems));
        lvNav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawer.closeDrawer(GravityCompat.START);
                switch (i) {
                    case 0:
                        tvToolbarTitle.setText(getString(R.string.dashboard));
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new DashboardFragment()).commit();
                        break;
                    case 1:
                        //startActivity(new Intent(MainActivity.this, EmployeeActivity.class));
                        tvToolbarTitle.setText(getString(R.string.all_employee));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new EmployeeActivity()).commit();
                            }
                        }, 270);

                        break;
                    case 2:
                        tvToolbarTitle.setText(getString(R.string.settings));
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new SettingsFragment()).commit();
                        break;

                }


            }
        });
    }

    public boolean checkOfficeTime() {
        Calendar calendar = Calendar.getInstance();
        Date current = calendar.getTime();

        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, Constants.START_OFFICE_HR);
        calStart.set(Calendar.MINUTE, Constants.START_OFFICE_MIN);
        calStart.set(Calendar.SECOND, 0);
        Date timeStart = calStart.getTime();

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, Constants.END_OFFICE_HR);
        calEnd.set(Calendar.MINUTE, Constants.END_OFFICE_MIN);
        calEnd.set(Calendar.SECOND, 0);
        Date timeEnd = calEnd.getTime();

        /*Log.e("time", "start:" + timeStart + " end:" + timeEnd);
        if (current.after(calStart.getTime()) && current.before(calEnd.getTime())) {
            Log.e("between", "true");
            setNotifIcon(Utilities.getBooleanPref(MainActivity.this, Constants.IS_NOTIFICATION_ENABLE, true));
        } else {
            setNotifIcon(false);
        }*/
        if (itemNotification != null) {
            if (current.after(calStart.getTime()) && current.before(calEnd.getTime())) {
                Log.e("between", "true");
                setNotifIcon(Utilities.getBooleanPref(MainActivity.this, Constants.IS_NOTIFICATION_ENABLE, true));
                return true;
            } else {
                setNotifIcon(false);
                return false;
            }
        }
        return true;

    }

    @Override
    public void onResume() {
        super.onResume();
        checkOfficeTime();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notif_menu, menu);
        itemNotification = menu.findItem(R.id.action_notif_toogle);
        checkOfficeTime();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/
        if (item.getItemId() == R.id.action_notif_toogle) {
            if (!checkOfficeTime()) {
                Toast.makeText(this, "Notification can not be changed in non-working hours!", Toast.LENGTH_SHORT).show();
            } else {
                if (Utilities.getBooleanPref(MainActivity.this, Constants.IS_NOTIFICATION_ENABLE, true)) {
                    new AlertDialog.Builder(MainActivity.this).setMessage(R.string.disable_notif_alert_message).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setNotifIcon(false);
                            EventBus.getDefault().post("notiffalse");
                            Utilities.updateBooleanPref(MainActivity.this, Constants.IS_NOTIFICATION_ENABLE, false);
                        }
                    }).show();
                } else {
                    Utilities.updateBooleanPref(MainActivity.this, Constants.IS_NOTIFICATION_ENABLE, true);
                    setNotifIcon(true);
                    EventBus.getDefault().post("notiftrue");
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       /* if (id == R.id.nav_camera) {
            // Handle the camera action
        } *//*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }*//* else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/
        Toast.makeText(this, "" + id, Toast.LENGTH_SHORT).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
