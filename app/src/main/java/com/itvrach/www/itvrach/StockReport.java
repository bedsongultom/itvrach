package com.itvrach.www.itvrach;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itvrach.sessions.SessionManagement;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockReport extends Fragment {
    private ImageView imgFile;
    private TextView tvTitle;


    public StockReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.stockreports, container, false);
        initViews(view);
        return view;
    }

    @SuppressLint("PrivateResource")
    public void initViews(View view) {
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //noinspection deprecation
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);


        tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        tvTitle.setTextSize(14);
        tvTitle.setText(R.string.reports_title);
        tvTitle.setVisibility(View.VISIBLE);
        imgFile = (ImageView) getActivity().findViewById(R.id.imgFile);
        imgFile.setVisibility(View.GONE);


        SessionManagement session = new SessionManagement(getContext());
        session.checkLogin();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        String user_id = user.get(SessionManagement.KEY_USERID);
        String session_id = user.get(SessionManagement.KEY_SESSIONID);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                FragmentTransaction ft;
                ft = getFragmentManager().beginTransaction();

                ft.replace(R.id.fragment_container, new Home());
                ft.addToBackStack(null);
                ft.commit();


                ((Welcome)getActivity()).enableReportsLayoutTabs(false);

                // ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            }
        });

    }





}
