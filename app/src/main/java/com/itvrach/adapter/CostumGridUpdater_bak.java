package com.itvrach.adapter;


import android.content.Context;
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

            public class CostumGridUpdater_bak extends RecyclerView.Adapter<CostumGridUpdater_bak.MyViewHolder> {

                //Creating an arraylist of POJO objects
                private ArrayList<Books> list_members = new ArrayList<>();
                private final LayoutInflater inflater;
                View view;
                MyViewHolder holder;
                private Context context;


                public CostumGridUpdater_bak(List<Books> listContentArr, Context context) {
                    this.context = context;
                    inflater = LayoutInflater.from(context);
                }




                //This method inflates view present in the RecyclerView
                @Override
                public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    view = inflater.inflate(R.layout.row_layout, parent, false);
                    holder = new MyViewHolder(view);
                    return holder;
                }

                //Binding the data using get() method of POJO object
                @Override
                public void onBindViewHolder(final MyViewHolder holder, int position) {
                    Books list_items = list_members.get(position);
                    holder.tv_item_name.setText(list_items.getItem_name());
                    final Locale local = new Locale("id", "id");
                    final NumberFormat fmt = NumberFormat.getCurrencyInstance(local);
                    holder.tv_price.setText(String.valueOf(String.valueOf(fmt.format(list_items.getPrice())).replaceAll("[Rp\\s]", "")));
                    Picasso.with(this.context).load(URI_SEGMENT_UPLOAD + list_items.getFile()).resize(400, 400).into(holder.img_file);

                }

                //Setting the arraylist
                public void setListContent(ArrayList<Books> list_members) {
                    this.list_members = list_members;
                   // notifyItemRangeChanged(0, list_members.size());

                }

                public void editItem(int position) {
                    list_members.get(position);
                    notifyItemChanged(position, list_members.size());
                }

                public void setFilter(List<Books> newList) {
                    list_members = new ArrayList<>();
                    list_members.addAll(newList);
                    notifyDataSetChanged();
                }


                @Override
                public int getItemCount() {
                    return list_members.size();
                }


                //View holder class, where all view components are defined
                class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                    TextView tv_item_name;
                    ImageView img_file;
                    TextView tv_price;

                    public MyViewHolder(View itemView) {
                        super(itemView);
                        itemView.setOnClickListener(this);
                        tv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
                        img_file = (ImageView) itemView.findViewById(R.id.img_file);
                        tv_price = (TextView) itemView.findViewById(R.id.tv_price);


                    }

                    @Override
                    public void onClick(View v) {

                    }
                }

                public void removeAt(int position) {
                    list_members.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(0, list_members.size());
                }

            }
