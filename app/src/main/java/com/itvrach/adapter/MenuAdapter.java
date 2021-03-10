package com.itvrach.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itvrach.model.Menu;
import com.itvrach.www.itvrach.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.itvrach.services.APIClient.URI_SEGMENT_UPLOAD;

/**
 * Created by engineer on 2/19/2019.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    List<Menu> menuList = new ArrayList<Menu>();
    private Context context;


    public MenuAdapter(List<Menu> menuList, Context context) {
        this.menuList = menuList;
        this.context = context;
    }


   /* public BooksAdapter(Context applicationContext, List<Books> booksList) {
        this.booksList = this.booksList;
        this.context = context;
    }*/


    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_list, parent, false);
        MenuAdapter.ViewHolder viewHolder = new MenuAdapter.ViewHolder(v, context, menuList);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final MenuAdapter.ViewHolder holder, final int position) {
        Menu item = menuList.get(position);
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
        TextView tvApprovedCount;
        ImageView icon;

        private Menu currentItem;
        Context context;

        ViewHolder(View itemView, Context context, List<Menu> menuList) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context = context;

            icon = (ImageView) itemView.findViewById(R.id.icon);
            icon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#48367d")));
           // txtid = (TextView) itemView.findViewById(R.id.txtId);
            txtmenu_name    = (TextView) itemView.findViewById(R.id.txtmenu_name);
            tvApprovedCount = (TextView) itemView.findViewById(R.id.tvApprovedCount);



        }

        void bind(Menu item) {
           // txtid.setText(String.valueOf(item.getId()));
            txtmenu_name.setText(String.valueOf(item.getMenu_name()));
            Picasso.with(this.context).load(URI_SEGMENT_UPLOAD + item.getIcon()).resize(50, 50).into(icon);
            String itemName = txtmenu_name.getText().toString();
            if(itemName.equals("Approved")) {
                tvApprovedCount.setVisibility(View.VISIBLE);
            }else{
                tvApprovedCount.setVisibility(View.GONE);
            }

            currentItem = item;
        }

        @Override
        public void onClick(View v) {

        }
    }

}

