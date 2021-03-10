package com.itvrach.adapter;

import android.content.Context;
import android.media.tv.TvInputService;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itvrach.www.itvrach.R;


/**
 * Created by engineer on 1/21/2019.
 */

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private  LayoutInflater layoutInflater;

    public  SliderAdapter(Context context){
        this.context = context;
    }


    //arrays
    public int[] slide_images = {
            R.drawable.ic_guarante,
            R.drawable.ic_easy,
         R.drawable.ic_secure


    };



    public String[] slide_headings = {
            "GUARANTE",
            "EASY",
      "SECURE"



    };

    public String[] slide_descs = {
      "Lorem ipsum sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna"+
              "aliqua",
      "Lorem ipsum sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna"+
              "aliqua",

      "Lorem ipsum sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna"+
            "aliqua"

    };

    @Override
    public int getCount() {
        return slide_headings.length;
        //return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
       // return false;
        return view == (RelativeLayout) o;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageView   = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading      = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription  = (TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_descs[position]);

        container.addView(view);
        return view;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
