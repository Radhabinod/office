package com.techindustan.office.ui.teams;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.techindustan.office.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;


/**
 * Created by binod on 26/3/18.
 */

public class TeamSection extends StatelessSection

{

    String title;
    JSONArray persons;
    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvProfile)
    TextView tvProfile;
    Context context;
    TeamsItemListener itemListener;

    public TeamSection(Context context, String title, JSONArray team) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.item_person)
                .headerResourceId(R.layout.item_header)
                .build());
        this.context = context;
        itemListener = (TeamsItemListener) context;
        persons = team;
        this.title = title;
    }

    @Override
    public int getContentItemsTotal() {
        return persons.length();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ViewHolderPerson(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder h, int position) {
        final ViewHolderPerson holder = (ViewHolderPerson) h;
        try {
            final JSONObject obj = persons.getJSONObject(position);
            holder.tvName.setText(obj.getString("name"));
            holder.tvProfile.setText(obj.getString("profile"));
            holder.ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.onProfileClick(obj);
                }
            });
            Glide.with(context).load(obj.getString("url")).asBitmap().placeholder(R.drawable.ic_profile_bg).
                    into(new BitmapImageViewTarget(holder.ivProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.ivProfile.setImageDrawable(circularBitmapDrawable);
                        }

                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTeamName.setText(title);

    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTeamName)
        TextView tvTeamName;


        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolderPerson extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProfile)
        ImageView ivProfile;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvProfile)
        TextView tvProfile;

        ViewHolderPerson(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    interface TeamsItemListener {
        void onProfileClick(JSONObject obj);
    }
}
