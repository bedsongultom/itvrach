package com.itvrach.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itvrach.www.itvrach.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by engineer on 5/4/2019.
 */

public class PagerAdapter5 extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList5 = new ArrayList<Fragment>();
    private final List<String>   fragmentTitleList5 = new ArrayList<>();
    private final List<Integer>  fragmentIconList5 = new ArrayList<>();
    private Context context;


    public PagerAdapter5(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList5.get(position);
    }


    public void addFragment5 (Fragment fragment5, String title, int tabIcon) {
        fragmentList5.add(fragment5);
        fragmentTitleList5.add(title);
        fragmentIconList5.add(tabIcon);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //return fragmentTitleList.get(position);
        return null;
    }


    @Override
    public int getCount() {
        return fragmentList5.size();
    }


    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
        tabTextView.setText(fragmentTitleList5.get(position));
        ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(fragmentIconList5.get(position));
        return view;
    }

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTextView =(TextView) view.findViewById(R.id.tabTextView);
        tabTextView.setText(fragmentTitleList5.get(position));
        // tabTextView.setTextColor(ContextCompat.getColor(context, R.color.mycolor));
        ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(fragmentIconList5.get(position));
        //tabImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        return view;
    }
}



