package com.itvrach.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itvrach.model.Transaction;
import com.itvrach.www.itvrach.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by engineer on 9/27/2018.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactionList = new ArrayList();
    private Context context;


    public TransactionAdapter(List<Transaction> transactionList, Context context ) {
        this.transactionList = transactionList;
        this.context = context;


    }




    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list, parent, false);
        TransactionAdapter.ViewHolder viewHolder = new TransactionAdapter.ViewHolder(v, context, transactionList);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Transaction item = transactionList.get(position);
        holder.bind(item);

        if(position %2 ==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            //holder.txtmenu_name.setTextColor(Color.parseColor("#FF4081"));

        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#7dbebebe"));
           // holder.txtmenu_name.setTextColor(Color.parseColor("#FF4081"));
        }


    }



    @Override
    public int getItemCount() {
        return transactionList.size();
    }

//    @Override public int getItemCount() { return mGames.length; }

    public int grandTotal(){
        int mSubTotal = 0;
        int mdata =transactionList.size();
        for(int i = 0 ; i < mdata; i++) {
            mSubTotal += transactionList.get(i).getTotal_price();
        }
        return mSubTotal;
    }


    public int grandDiscRp(){
        int mSubDiscRp = 0;
        int mdata =transactionList.size();
        for(int i = 0 ; i < mdata; i++) {
            mSubDiscRp += ((transactionList.get(i).getQty() * transactionList.get(i).getPrice()) * (transactionList.get(i).getDisc())/100);
        }
        return mSubDiscRp;
    }


    public int grandQty(){
        int mSubQty = 0;
        int mdata =transactionList.size();
        for(int i = 0 ; i < mdata; i++) {
            mSubQty += transactionList.get(i).getQty();
        }
        return mSubQty;
    }


    public float grandWeight(){
        float mSubWeight = 0;
        float mdata =transactionList.size();
        for(int i = 0 ; i < mdata; i++) {
            mSubWeight += transactionList.get(i).getWeight()*transactionList.get(i).getQty();
        }
        return mSubWeight;
    }





    public int grandItem(){
        int count = transactionList.size();
        return count;
    }



    public void setFilter(List<Transaction> newList) {
        transactionList = new ArrayList<>();
        transactionList.addAll(newList);
        notifyDataSetChanged();
    }


    public void setLoaddata(List<Transaction> transaction) {
        transactionList = new ArrayList<>();
        transactionList.addAll(transaction);
        notifyDataSetChanged();


    }


    public void removeItem(int position) {
       /*int count = CustomAdapter.getItemCount();
        for(int i=0; i < count; i++){
            usersList.remove();
        }*/

        transactionList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, transactionList.size());
    }
    public void clear() {
        final int size = transactionList.size();
        transactionList.clear();
        notifyItemRangeRemoved(0, size);

        /*final int size = data.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                data.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }*/
    }

    public void editItem(int position) {
        transactionList.get(position);
        notifyItemChanged(position, transactionList.size());
    }

    public void addItem(Transaction transaction) {
        transactionList.add(transaction);
        notifyItemInserted(transactionList.size());

    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNo;
        TextView txtId;
        TextView txtItem_code;
        TextView txtItem_name;
        TextView txtQty;
        TextView txtDisc;
        TextView txtWeight;
        TextView txtPrice;
        TextView txtTotal_price;
        TextView txtsessionID;
        TextView txtQtyPrice;
        double priceX;
        float weight;
        float discount;
        TextView txtDiscRp;
        double discRp;
        int qty;
        double qtyPrice;



        private Transaction currentItem;
        Context context;

        ViewHolder(View itemView, Context context, List<Transaction> transactionList) {
            super(itemView);

            this.context = context;
            txtNo = (TextView) itemView.findViewById(R.id.txtNo);

            txtId = (TextView) itemView.findViewById(R.id.txtId);
            txtId.setVisibility(View.GONE);

            txtsessionID = (TextView) itemView.findViewById(R.id.txtsessionID);
            txtsessionID.setVisibility(View.GONE);

            txtItem_code = (TextView) itemView.findViewById(R.id.txtItem_code);
            txtItem_name = (TextView) itemView.findViewById(R.id.txtItem_name);

            txtWeight    = (TextView) itemView.findViewById(R.id.txtWeight);



            txtQty = (TextView) itemView.findViewById(R.id.txtQty);
            txtDisc = (TextView) itemView.findViewById(R.id.txtDisc);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtQtyPrice = (TextView) itemView.findViewById(R.id.txtQtyPrice);
            txtDiscRp = (TextView) itemView.findViewById(R.id.txtDiscRp);

        }

        void bind(Transaction item) {
            txtNo.setText(String.valueOf(getPosition()+1));
            txtId.setText(String.valueOf(item.getId()));
            txtsessionID.setText(String.valueOf(item.getSession_id()));
            txtItem_code.setText(item.getItem_code());
            txtItem_code.setVisibility(View.GONE);

            txtItem_name.setText(item.getItem_name());
            txtQty.setText(String.valueOf(item.getQty()));
            Locale local = new Locale("id", "id");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(local);

           // txtDisc.setText(String.valueOf(item.getDisc()));

            txtDisc.setText(String.valueOf(fmt.format(item.getDisc())+"%").replaceAll("[Rp\\s]", ""));



            txtWeight.setText(String.format("%.2f",item.getWeight()));


           /* double price_after_disc = 0;


            price_after_disc = priceX - ((priceX*discount)/100) ;
*/

          //  txtPrice.setText("Rp. "+ String.valueOf(fmt.format(price_after_disc)).replaceAll("[Rp\\s]", ""));

            qty = Integer.parseInt(txtQty.getText().toString());
            discount=Float.parseFloat(txtDisc.getText().toString().replaceAll("[^0-9]", ""));
            txtPrice.setText(String.valueOf("@ Rp."+ fmt.format(item.getPrice()).replaceAll("[Rp\\s]", "")));
            priceX = Double.parseDouble(txtPrice.getText().toString().replaceAll("[^0-9]", ""));

            qtyPrice = qty *priceX ;
            txtQtyPrice.setText(String.valueOf("Rp."+ fmt.format(qtyPrice).replaceAll("[Rp\\s]", "")));
            discRp = ((priceX*qty)*discount)/100;
            txtDiscRp.setText(String.valueOf("Rp."+ fmt.format(discRp).replaceAll("[Rp\\s]", "")));
            currentItem = item;
        }

    }

}