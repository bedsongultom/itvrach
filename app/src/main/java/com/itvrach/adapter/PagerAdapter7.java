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


public class PagerAdapter7 extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList7 = new ArrayList<>();
    private final List<Integer>   fragmentTitleList7 = new ArrayList<>();
    private final List<Integer>  fragmentIconList7 = new ArrayList<>();
    private Context context;


    public PagerAdapter7(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList7.get(position);
    }


    public void addFragment7 (Fragment fragment7, Integer title, int tabIcon) {
        fragmentList7.add(fragment7);
        fragmentTitleList7.add(title);
        fragmentIconList7.add(tabIcon);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //return fragmentTitleList.get(position);
        return null;
    }


    @Override
    public int getCount() {
        return fragmentList7.size();
    }


    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
        tabTextView.setText(fragmentTitleList7.get(position));
        tabTextView.setTextSize(12);
        tabTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(fragmentIconList7.get(position));
        tabImageView.setVisibility(View.GONE);
        return view;
    }

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTextView =(TextView) view.findViewById(R.id.tabTextView);
        tabTextView.setText(fragmentTitleList7.get(position));
        tabTextView.setTextSize(12);
        tabTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        // tabTextView.setTextColor(ContextCompat.getColor(context, R.color.mycolor));
        ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(fragmentIconList7.get(position));
        tabImageView.setVisibility(View.GONE);
        //tabImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        return view;
    }
}



