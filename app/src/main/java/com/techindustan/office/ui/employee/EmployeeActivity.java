package com.techindustan.office.ui.employee;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.techindustan.model.employee.Employee;
import com.techindustan.model.employee.Response;
import com.techindustan.office.R;
import com.techindustan.office.network.ApiClient;
import com.techindustan.office.network.ApiInterface;
import com.techindustan.office.ui.base.BaseActivity;
import com.techindustan.office.ui.base.BaseFragment;
import com.techindustan.office.ui.login.LoginActivity;
import com.techindustan.office.utils.Constants;
import com.techindustan.office.utils.Utilities;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class EmployeeActivity extends BaseFragment implements EmployeeAdapter.EmployeesInterface {

    @BindView(R.id.rvEmployee)
    RecyclerView rvEmployee;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    EmployeeAdapter adapter;
    List<Response> employess = new ArrayList<>();
    ApiInterface apiInterface;
    String tempNumber;
    int CALL_REQ_CODE = 100;
    GsonBuilder gsonBuilder;
    Gson gson;
    @BindView(R.id.searchView)
    SearchView searchView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_employee, null);
        //setContentView(R.layout.activity_employee);
        ButterKnife.bind(this, v);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        adapter = new EmployeeAdapter(this, employess);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvEmployee.setLayoutManager(mLayoutManager);
        rvEmployee.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEmployee(true);
            }
        });
        getEmployee(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });
        return v;
    }


    private void search(String query) {
        query = query.toLowerCase();
        List<Response> temp = new ArrayList();
        for (Response employee : employess) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (employee.getFirst_name().toLowerCase().contains(query) || employee.getLast_name().toLowerCase().contains(query)) {
                temp.add(employee);
            }
        }
        adapter.updateResult(temp);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_mainscreen, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
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
        }
        return super.onOptionsItemSelected(item);
    }

    void getEmployee(boolean isSwipe) {
        String access_token = getUserDetail().getAccess_token();
        if (!isSwipe) {
            showProgress();
            //get employee data from local persistant data
            if (!Utilities.getStringPref(getActivity(), Constants.ALL_EMPLOYEE).isEmpty()) {
                Type listType = new TypeToken<List<Response>>() {
                }.getType();
                List<Response> emp = gson.fromJson(Utilities.getStringPref(getActivity(), Constants.ALL_EMPLOYEE), listType);
                employess.clear();
                employess.addAll(emp);
                adapter.notifyDataSetChanged();
                hideProgress();
                return;
            }
        }
        Call<Employee> getEmployee = apiInterface.getEmployees(access_token);
        getEmployee.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, retrofit2.Response<Employee> response) {
                hideProgress();
                //Log.e("response", response.raw().toString());
                if (response.code() == 200) {
                    employess.clear();
                    Employee employee = response.body();
                    //Log.e("res", employee.toString());
                    if (employee.getResponse() != null) {
                        employess.addAll(response.body().getResponse());
                        Utilities.updateStringPref(getActivity(), Constants.ALL_EMPLOYEE, gson.toJson(employess));
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    try {
                        Toast.makeText(getActivity(), Utilities.getErrorMessage(response.errorBody().string()), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                hideProgress();
                //Log.e("error", t.getMessage());
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onCallClick(String number) {
        tempNumber = number;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_REQ_CODE);
            }
            return;
        }
        startActivity(intent);
    }

    @Override
    public void openMessageDialog(Response employee) {
        MessageDialog messageDialog = new MessageDialog(getActivity(), R.style.Theme_Dialog, employee);
        messageDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onCallClick(tempNumber);
            } else {
                new AlertDialog.Builder(getActivity()).setMessage(getString(R.string.app_name) + " needs the permission. Please allow.")
                        .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE);
                                    if (!showRationale) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                        intent.setData(uri);
                                        startActivityForResult(intent, 10);
                                    } else {
                                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_REQ_CODE);
                                    }
                                }

                            }
                        }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        }
    }


}
