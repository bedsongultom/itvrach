package com.itvrach.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itvrach.model.Books;
import com.itvrach.www.itvrach.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.itvrach.services.APIClient.URI_SEGMENT_UPLOAD;

/**
 * Created by engineer on 2/2/2019.
 */



public class CostumGridUpdater extends RecyclerView.Adapter<CostumGridUpdater.ViewHolder> {
    private List<Books> list_members;
    private Context context;
    View view;

    public CostumGridUpdater(List<Books> list_members, Context context) {
        this.list_members = list_members;
        this.context = context;
    }

    @Override
    public CostumGridUpdater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, context, list_members);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Books item = list_members.get(position);
        holder.bind(item);

    }


    @Override
    public int getItemCount() {
        return list_members.size();
    }


    public int grandItem(){
        int count = list_members.size();
        return count;
    }


    public void setFilter(List<Books> newList) {
        list_members = new ArrayList<>();
        list_members.addAll(newList);
        notifyDataSetChanged();
    }


    public void removeItem(int position) {
        list_members.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list_members.size());
    }

    public void editItem(int position) {
        list_members.get(position);
        notifyItemChanged(position, list_members.size());
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_item_code;
        ImageView img_file;
        TextView tv_disc;
        TextView tv_price;
        TextView tv_price_disc;
        TextView tv_item_name;

        double priceX;
        float discount;



        private Books currentItem;
        Context context;


        ViewHolder(View itemView, Context context, List<Books> list_members) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context = context;

            tv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
            img_file = (ImageView) itemView.findViewById(R.id.img_file);
            tv_disc  = (TextView) itemView.findViewById(R.id.tv_disc);

            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_price.setPaintFlags(tv_price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            tv_price.setTextColor(R.color.colorGrey);

            tv_price_disc = (TextView) itemView.findViewById(R.id.tv_price_disc);





        }

        void bind(Books item) {

            tv_item_name.setText(String.valueOf(item.getItem_name()));
            final Locale local = new Locale("id", "id");

            final NumberFormat fmt = NumberFormat.getCurrencyInstance(local);
            tv_price.setText("Rp. "+ String.valueOf(fmt.format(item.getPrice())).replaceAll("[Rp\\s]", ""));



            Picasso.with(this.context).load(URI_SEGMENT_UPLOAD + item.getFile()).resize(400, 400).into(img_file);
            tv_disc.setText(String.valueOf(fmt.format(item.getDisc())+"%").replaceAll("[Rp\\s]", ""));

            discount=Float.parseFloat(tv_disc.getText().toString().replaceAll("[^0-9]", ""));
                   if(discount<=0) {
                       tv_disc.setVisibility(View.GONE);
                   }else{
                       tv_disc.setVisibility(View.VISIBLE);
                       }
            priceX = Double.parseDouble(tv_price.getText().toString().replaceAll("[^0-9]", ""));


            double price_after_disc = 0;
            price_after_disc = priceX - ((priceX*discount)/100) ;
            tv_price_disc.setText("Rp. "+ String.valueOf(fmt.format(price_after_disc)).replaceAll("[Rp\\s]", ""));

                    currentItem = item;
        }

        @Override
        public void onClick(View v) {
                   }

    }
}

