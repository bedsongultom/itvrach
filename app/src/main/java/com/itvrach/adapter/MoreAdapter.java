package com.itvrach.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itvrach.model.Menu;
import com.itvrach.www.itvrach.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by engineer on 3/24/2019.
 */

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.ViewHolder> {

    List<Menu> menuList = new ArrayList<Menu>();
    private Context context;


    public MoreAdapter(List<Menu> menuList, Context context) {
        this.menuList = menuList;
        this.context = context;
    }


   /* public BooksAdapter(Context applicationContext, List<Books> booksList) {
        this.booksList = this.booksList;
        this.context = context;
    }*/


    @Override
    public MoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.more_list, parent, false);
        MoreAdapter.ViewHolder viewHolder = new MoreAdapter.ViewHolder(v, context, menuList);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final MoreAdapter.ViewHolder holder, final int position) {
        Menu item = menuList.get(position);
        if(position %2 ==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.txtmenu_name.setTextColor(Color.parseColor("#FF4081"));

        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#7dbebebe"));
            holder.txtmenu_name.setTextColor(Color.parseColor("#FF4081"));
        }

        holder.bind(item);



    }




    @Override
    public int getItemCount() {
        //     return (booksList != null) ? 0 : booksList.size();
        return menuList.size();
    }





    public void setFilter(List<Menu> newList) {
        menuList = new ArrayList<>();
        menuList.addAll(newList);
        notifyDataSetChanged();
    }


    public void removeItem(int position) {
       /*int count = CustomAdapter.getItemCount();
        for(int i=0; i < count; i++){
            usersList.remove();
        }*/

        menuList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, menuList.size());
    }


    public void editItem(int position) {
        menuList.get(position);
        notifyItemChanged(position, menuList.size());
    }






    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtid;
        TextView txtmenu_name;
        ImageView icon;

        private Menu currentItem;
        Context context;

        ViewHolder(View itemView, Context context, List<Menu> menuList) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context = context;

            icon = (ImageView) itemView.findViewById(R.id.icon);
            // txtid = (TextView) itemView.findViewById(R.id.txtId);
            txtmenu_name = (TextView) itemView.findViewById(R.id.txtmenu_name);

        }

        void bind(Menu item) {
            // txtid.setText(String.valueOf(item.getId()));
            txtmenu_name.setText(String.valueOf(item.getMenu_name()));
        //    Picasso.with(this.context).load(URI_SEGMENT_UPLOAD + item.getIcon()).resize(100, 100).into(icon);
            currentItem = item;

        }

        @Override
        public void onClick(View v) {

        }
    }

}


