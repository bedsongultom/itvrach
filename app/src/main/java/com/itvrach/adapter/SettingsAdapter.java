package com.itvrach.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itvrach.www.itvrach.R;

import java.util.ArrayList;
import java.util.List;

import com.itvrach.model.Settings;

/**
 * Created by engineer on 3/22/2019.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

        private ArrayList<Settings> settingsList = new ArrayList<Settings>();
        private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtItem_name;
        private Settings currentItem;
        Context context;

        ViewHolder(View itemView, Context context, List<Settings> settingsList) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context = context;
            txtItem_name = (TextView) itemView.findViewById(R.id.txtItem_name);

        }

        void bind(Settings item) {
        txtItem_name.setText(String.valueOf(item.getSettings_name()));
        currentItem = item;

        }

        @Override
        public void onClick(View v) {

        }
    }

}
