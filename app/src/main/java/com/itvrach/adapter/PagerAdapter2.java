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
 * Created by engineer on 2/28/2019.
 */

public class PagerAdapter2 extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList2 = new ArrayList<Fragment>();
    private final List<String> fragmentTitleList2 = new ArrayList<>();
    private final List<Integer> fragmentIconList2 = new ArrayList<>();
    private Context context;


    public PagerAdapter2(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
         /*fragmentList.add(new Home());
         fragmentList.add(new Profile());
         fragmentList.add(new Info());
         fragmentList.add(new Item());
         fragmentList.add(new Offers());
         fragmentList.add(new Search());
         fragmentList.add(new User());
*/
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList2.get(position);
    }


    public void addFragment2 (Fragment fragment2, String title, int tabIcon) {
        fragmentList2.add(fragment2);
        fragmentTitleList2.add(title);
        fragmentIconList2.add(tabIcon);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //return fragmentTitleList.get(position);
        return null;
    }

  /*  @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Tab-1";
        }
        else if (position == 1)
        {
            title = "Tab-2";
        }
        else if (position == 2)
        {
            title = "Tab-3";
        }
        return title;
    }
*/

    @Override
    public int getCount() {
        return fragmentList2.size();
    }


    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
        tabTextView.setText(fragmentTitleList2.get(position));
        ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(fragmentIconList2.get(position));
        return view;
    }

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTextView =(TextView) view.findViewById(R.id.tabTextView);
        tabTextView.setText(fragmentTitleList2.get(position));
       // tabTextView.setTextColor(ContextCompat.getColor(context, R.color.mycolor));
        ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(fragmentIconList2.get(position));
        //tabImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        return view;
    }
}





