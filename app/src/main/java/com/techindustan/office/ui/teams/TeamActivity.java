package com.techindustan.office.ui.teams;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.techindustan.office.R;
import com.techindustan.office.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;


public class TeamActivity extends AppCompatActivity implements TeamSection.TeamsItemListener {

    SectionedRecyclerViewAdapter adapter;
    JSONArray teams = new JSONArray();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        ButterKnife.bind(this);
        adapter = new SectionedRecyclerViewAdapter();
        try {
            JSONObject object = new JSONObject(Constants.SampleData);
            teams = object.getJSONArray("all");
            for (int i = 0; i < teams.length(); i++) {
                JSONObject mTeam = teams.getJSONObject(i);
                adapter.addSection(new TeamSection(TeamActivity.this, mTeam.getString("team_name"), mTeam.getJSONArray("teams")));
            }
            GridLayoutManager glm = new GridLayoutManager(TeamActivity.this, 2);
            glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (adapter.getSectionItemViewType(position)) {
                        case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                            return 2;
                        default:
                            return 1;
                    }
                }
            });
            recyclerView.setLayoutManager(glm);
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

    }

    @Override
    public void onProfileClick(JSONObject obj) {
        final Dialog dialogBuilder = new Dialog(TeamActivity.this);
        dialogBuilder.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialogBuilder.setContentView(R.layout.profile_dialog);
        final ImageView ivProfile = (ImageView) dialogBuilder.findViewById(R.id.ivProfile);
        final TextView tvName = (TextView) dialogBuilder.findViewById(R.id.tvName);
        final Button btnCall = (Button) dialogBuilder.findViewById(R.id.btnCall);

        dialogBuilder.show();
        try {
            tvName.setText(obj.getString("name"));
            circleImage(ivProfile, obj.getString("url"), true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogBuilder.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogBuilder.getWindow().setAttributes(lp);
        dialogBuilder.show();
    }

    private static Bitmap addBorder(Bitmap resource, Context context) {
        int w = resource.getWidth();
        int h = resource.getHeight();
        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);
        Paint p = new Paint();
        p.setAntiAlias(true);
        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        c.drawBitmap(resource, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(ContextCompat.getColor(context, R.color.white));
        p.setStrokeWidth(3);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);
        return output;
    }

    public static <T> void circleImage(final ImageView imageView, T uri, final boolean border) {
        Glide.with(imageView.getContext()).load(uri).asBitmap().centerCrop().placeholder(R.drawable.ic_profile_bg).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), border ? addBorder(resource, imageView.getContext()) : resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
}
