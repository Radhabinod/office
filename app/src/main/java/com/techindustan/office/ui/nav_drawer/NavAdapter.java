package com.techindustan.office.ui.nav_drawer;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.techindustan.office.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by binod on 5/4/18.
 */

public class NavAdapter extends BaseAdapter {
    Context context;
    List<String> names = new ArrayList<>();
    Typeface loginFont;
    TextView title;
    List<Integer> icon = new ArrayList<>();

    public NavAdapter(Context mainActivity, List<String> names) {
        this.context = mainActivity;
        this.names = names;



        //loginFont = Typeface.createFromAsset(.getAssets(), "fonts/Lato-Regular.ttf");



        icon.add(R.drawable.email);
        icon.add(R.drawable.logout);
        icon.add(R.drawable.skype);

    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_nav_item, null);
        title = (TextView) convertView.findViewById(R.id.textView5);
    //    title.setTypeface(loginFont);
        title.setText(names.get(position));
        int selectedIndex = 0;
        ImageView icons = (ImageView) convertView.findViewById(R.id.imageView10);
        icons.setScaleType(ImageView.ScaleType.FIT_XY);
        icons.setBackgroundResource(icon.get(position));
        return convertView;


    }
}

