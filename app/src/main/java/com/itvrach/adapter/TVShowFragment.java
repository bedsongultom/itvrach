package com.itvrach.adapter;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itvrach.model.Books;
import com.itvrach.www.itvrach.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by engineer on 10/30/2018.
 */

public class TVShowFragment extends DialogFragment {
/*
    String[] tvshows={"Crisis","Blindspot","BlackList","Game of Thrones","Gotham","Banshee"};
    String[] tvdsts={"1","2","3","4","5","6"};*/

    private List<Books> booksList = new ArrayList<>();
    RecyclerView rv;
    BooksAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fraglayout, container);

        //RECYCER
        rv = (RecyclerView) rootView.findViewById(R.id.mrecyclerID);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BooksAdapter(booksList, getActivity());
        rv.setAdapter(adapter);

        this.getDialog().setTitle("TV Shows");
        return rootView;

    }
}