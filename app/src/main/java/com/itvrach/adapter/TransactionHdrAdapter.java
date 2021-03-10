package com.itvrach.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itvrach.model.Transaction_hdr;
import com.itvrach.www.itvrach.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by engineer on 3/20/2019.
 */

public class TransactionHdrAdapter extends RecyclerView.Adapter<TransactionHdrAdapter.ViewHolder> {

    private List<Transaction_hdr> transaction_hdrList = new ArrayList();
    private Context context;

    public TransactionHdrAdapter(List<Transaction_hdr> transaction_hdrList, Context context) {
        this.transaction_hdrList = transaction_hdrList;
        this.context = context;
    }


    @Override
    public TransactionHdrAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_payment, parent, false);
        TransactionHdrAdapter.ViewHolder viewHolder = new TransactionHdrAdapter.ViewHolder(v, context, transaction_hdrList);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Transaction_hdr item = transaction_hdrList.get(position);
      //  holder.bind(item);


    }


    @Override
    public int getItemCount() {
        return transaction_hdrList.size();
    }


    public int grandItem(){
        int count = transaction_hdrList.size();
        return count;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView, Context context, List<Transaction_hdr> transaction_hdrList) {
            super(itemView);
        }
    }
}


