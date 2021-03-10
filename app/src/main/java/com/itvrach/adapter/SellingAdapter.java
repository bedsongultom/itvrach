package com.itvrach.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.itvrach.interfaces.OnItemClickListener2;
import com.itvrach.model.Transaction_hdr;
import com.itvrach.www.itvrach.R;

import java.util.ArrayList;
import java.util.List;


public class SellingAdapter extends RecyclerView.Adapter<SellingAdapter.ViewHolder> {

    Context context;
    private List<Transaction_hdr> transaction_hdrList = new ArrayList();

   /* private OnItemClickListener2 mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener2  mItemClickListener){
        this.mItemClickListener = mItemClickListener;

    }*/


    public SellingAdapter(Context context, List<Transaction_hdr> transaction_hdrList) {
        this.context = context;
        this.transaction_hdrList = transaction_hdrList;
    }

    @Override
    public SellingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selling_list, parent, false);
        return new SellingAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final SellingAdapter.ViewHolder holder, final int position) {
        final Transaction_hdr item = transaction_hdrList.get(position);
        holder.bind(item);

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#25bfbfbf"));

        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#7dbebebe"));
        }

    }


    public Transaction_hdr getItem(int position) {
        return transaction_hdrList.get(position);
    }

    @Override
    public int getItemCount() {
        return transaction_hdrList.size();
    }


    public void removeItem(int position) {
        transaction_hdrList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, transaction_hdrList.size());
    }

    public int grandConfirmation(){
        int mConfirmation = 0;
        for(int i = 0 ; i < transaction_hdrList.size(); i++) {
            mConfirmation += transaction_hdrList.get(i).getConfirmation();
        }
        return mConfirmation;
    }


    public List<Transaction_hdr> getTransaction_hdrList() {
        return transaction_hdrList;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int grandItem() {
        int count = transaction_hdrList.size();
        return count;
    }


    public void setFilter(List<Transaction_hdr> newList) {
        transaction_hdrList = new ArrayList<>();
        transaction_hdrList.addAll(newList);
        notifyDataSetChanged();
    }


    public void selectAll() {
        for (Transaction_hdr transaction_hdr : transaction_hdrList) {
            transaction_hdr.setChecked(true);
        }
        notifyDataSetChanged();
    }

    public boolean isEnabled(int position){
        return false;
    }


    public void editItem(int position) {
        transaction_hdrList.get(position);
        notifyItemChanged(position, transaction_hdrList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView tvNo;
        TextView tvTransactionId;
        TextView tvTransactionDate;
        TextView tvSessionId;
        TextView tvTotal;
        String confirmed;
        Transaction_hdr currentItem;


        ViewHolder(View itemView) {
            super(itemView);
            tvNo = (TextView) itemView.findViewById(R.id.txtNo);
            tvTransactionId = (TextView) itemView.findViewById(R.id.txtTransactionId);
            tvTransactionDate = (TextView) itemView.findViewById(R.id.txtTransactionDate);
            tvSessionId = (TextView) itemView.findViewById(R.id.txtSessionId);
            tvTotal     = (TextView) itemView.findViewById(R.id.tv_total);
           // btnConfirmed = (Button) itemView.findViewById(R.id.btn_Confirmed);

            tvTransactionDate.isEnabled();
        }


        void bind(final Transaction_hdr item) {

            tvNo.setText(String.valueOf(getPosition() + 1));
            tvTransactionId.setText("#" + String.valueOf(item.getTransaction_id()));
            tvTransactionDate.setText(item.getTransaction_date());
            tvSessionId.setText(item.getSession_id());
            //tvTotal.setText(item.getSub_total());
            confirmed = String.valueOf(item.getConfirmation());
            currentItem = item;

        }

        /*@Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
*/
    }
}