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
 * Created by engineer on 3/6/2019.
 */

public class PagerAdapter1 extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList1 = new ArrayList<Fragment>();
    private final List<String> fragmentTitleList1 = new ArrayList<>();
    private final List<Integer> fragmentIconList1 = new ArrayList<>();
    private Context context;


    public PagerAdapter1(FragmentManager fm, Context context){
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
        return fragmentList1.get(position);
    }


    public void addFragment1 (Fragment fragment1, String title, int tabIcon) {
        fragmentList1.add(fragment1);
        fragmentTitleList1.add(title);
        fragmentIconList1.add(tabIcon);
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
        return fragmentList1.size();
    }


    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
        tabTextView.setText(fragmentTitleList1.get(position));
        ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(fragmentIconList1.get(position));
        return view;
    }

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tabTextView =(TextView) view.findViewById(R.id.tabTextView);
        tabTextView.setText(fragmentTitleList1.get(position));
        // tabTextView.setTextColor(ContextCompat.getColor(context, R.color.mycolor));
        ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(fragmentIconList1.get(position));
        //tabImageView.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        return view;
    }
}





