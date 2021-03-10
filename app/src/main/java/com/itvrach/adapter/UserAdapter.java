package com.itvrach.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itvrach.model.User;
import com.itvrach.www.itvrach.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.itvrach.services.APIClient.URI_SEGMENT_UPLOAD;

/**
 * Created by engineer on 8/1/2018.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> mList;
    private Context ctx;

    public UserAdapter(List<User> mList, Context ctx) {
        this.mList = mList;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mList.get(position);

        holder.tv_username.setText("Username:"+ user.getUsername());
        //holder.tv_type.setText(user.getType());
        holder.tv_email.setText("E-mail:"+ user.getEmail());

        Picasso.with(ctx)

                .load(URI_SEGMENT_UPLOAD+user.getFile()).resize(630, 600)
                .into(holder.img_file);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_username;
        public TextView tv_type;
        public TextView tv_email;
        public ImageView img_file;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            //tv_type     = (TextView) itemView.findViewById(R.id.tv_type);
            tv_email    = (TextView) itemView.findViewById(R.id.tv_email);
            img_file    = (ImageView) itemView.findViewById(R.id.img_file);
            tv_username.setBackgroundColor(Color.BLUE);


        }
    }
}
