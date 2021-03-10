package com.itvrach.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.itvrach.model.Books;
import com.itvrach.www.itvrach.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.itvrach.services.APIClient.URI_SEGMENT_UPLOAD;

/**
 * Created by engineer on 9/27/2018.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    ArrayList<Books> booksList = new ArrayList<Books>();

    //private List<Books> booksList = new ArrayList<>();
    private Context context;



    public BooksAdapter(List<Books> bookList, Context context) {
        this.booksList = booksList;
        this.context = context;
    }





   /* public BooksAdapter(Context applicationContext, List<Books> booksList) {
        this.booksList = this.booksList;
        this.context = context;
    }*/


    @Override
    public BooksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.books_list, parent, false);
        BooksAdapter.ViewHolder viewHolder = new BooksAdapter.ViewHolder(v, context, booksList);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Books item = booksList.get(position);
        holder.bind(item);



    }




    @Override
    public int getItemCount() {
   //     return (booksList != null) ? 0 : booksList.size();
   return booksList.size();
    }





    public void setFilter(List<Books> newList) {
        booksList = new ArrayList<>();
        booksList.addAll(newList);
        notifyDataSetChanged();
    }


    public void removeItem(int position) {
       /*int count = CustomAdapter.getItemCount();
        for(int i=0; i < count; i++){
            usersList.remove();
        }*/

        booksList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, booksList.size());
    }


    public void editItem(int position) {
        booksList.get(position);
        notifyItemChanged(position, booksList.size());
    }






    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtId;
        TextView txtItem_name;
        ImageView img;
        TextView txtPrice;
        EditText editTextSearch;

        private Books currentItem;
        Context context;

        ViewHolder(View itemView, Context context, List<Books> booksList) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context = context;
            img = (ImageView) itemView.findViewById(R.id.file);
            txtId = (TextView) itemView.findViewById(R.id.txtId);
            txtItem_name = (TextView) itemView.findViewById(R.id.txtItem_name);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            editTextSearch = (EditText) itemView.findViewById(R.id.editTextSearch);

        }

        void bind(Books item) {
            txtId.setText(String.valueOf(item.getId()));
            txtItem_name.setText(String.valueOf(item.getItem_name()));
            txtPrice.setText(String.valueOf(item.getPrice()));
            Picasso.with(this.context).load(URI_SEGMENT_UPLOAD + item.getFile()).into(img);
            //total +=txtPrice.getText().toString();

            // grandTotal(total, item.getPrice());
            //  editTextSearch.setText(editTextSearch.getText().toString());

            currentItem = item;

        }

        private void grandTotal(String total, float price) {

        }


        @Override
        public void onClick(View v) {

        }
    }

}





