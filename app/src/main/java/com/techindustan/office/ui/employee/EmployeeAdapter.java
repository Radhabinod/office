package com.techindustan.office.ui.employee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.techindustan.model.employee.Response;
import com.techindustan.office.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by binod on 27/3/18.
 */

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {


    List<Response> employees;
    Context context;
    EmployeesInterface employeesInterface;
    List<Response> temp;


    public EmployeeAdapter(Context context, List<Response> employess) {
        this.employees = employess;
        this.temp = employess;
        this.context = context;
        employeesInterface = (EmployeesInterface) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employee_new, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        final ViewHolder holder = h;
        final Response employee = employees.get(position);
        Spannable employeeName = new SpannableString(employee.getFirst_name() + " " + employee.getLast_name());
        employeeName.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.techblue)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //employeeName.setSpan(new ForegroundColorSpan(Color.RED), 1, employeeName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        employeeName.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.techblue)), employee.getFirst_name().length() + 1, employee.getFirst_name().length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvName.setText(employeeName);
        holder.tvJobTitle.setText(employee.getJob_title());
        holder.tvEmail.setText(employee.getEmail().trim());
        holder.tvSkype.setText(employee.getSkype().trim());
        Glide.with(context).load(employee.getUser_image()).asBitmap().placeholder(R.drawable.techindustan_logo).
                into(holder.ivProfile);
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employeesInterface.onCallClick(employee.getPhone());
            }
        });
        holder.btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employeesInterface.openMessageDialog(employee);
            }
        });
        holder.tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!employee.getEmail().isEmpty()) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", employee.getEmail(), null));
                    context.startActivity(Intent.createChooser(emailIntent, "Send email"));
                }
            }
        });
        holder.tvSkype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPackageInstalled("com.skype.raider", context.getPackageManager())) {
                    Intent skype_intent = new Intent("android.intent.action.VIEW");
                    skype_intent.setClassName("com.skype.raider", "com.skype.raider.Main");
                    skype_intent.setData(Uri.parse("skype:" + employee.getSkype() + "?chat"));
                    context.startActivity(skype_intent);
                } else {
                    Toast.makeText(context, "Skype not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProfile)
        ImageView ivProfile;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvJobTitle)
        TextView tvJobTitle;
        @BindView(R.id.tvSkype)
        TextView tvSkype;
        @BindView(R.id.tvEmail)
        TextView tvEmail;
        @BindView(R.id.btnCall)
        ImageView btnCall;
        @BindView(R.id.btnMessage)
        ImageView btnMessage;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface EmployeesInterface {
        void onCallClick(String number);

        void openMessageDialog(Response employee);
    }

    void updateResult(List<Response> result) {

      /*  keyword = keyword.toLowerCase();
        employees.clear();
        if (keyword.length() == 0) {
            employees.addAll(temp);
        } else {
            for (Response employee : temp) {
                if (employee.getFirst_name().toLowerCase().contains(keyword) || employee.getLast_name().toLowerCase().contains(keyword)) {
                    employees.add(employee);
                }
            }
        }*/
        employees = result;
        notifyDataSetChanged();

    }


}

