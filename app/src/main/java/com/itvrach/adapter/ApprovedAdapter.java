package com.itvrach.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.itvrach.model.Transaction_hdr;
import com.itvrach.interfaces.OnItemClickListener;
import com.itvrach.www.itvrach.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by engineer on 4/10/2019.
 */

public class ApprovedAdapter extends RecyclerView.Adapter<ApprovedAdapter.ViewHolder> {

    private List<Transaction_hdr> transaction_hdrList = new ArrayList();
    Context context;
    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }


    public ApprovedAdapter(Context context, List<Transaction_hdr> transaction_hdrList ) {
        this.context = context;
        this.transaction_hdrList = transaction_hdrList;
     //   this.mItemClickListener = mItemClickListener;
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.approved_list, parent, false);
        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(final ApprovedAdapter.ViewHolder holder, final int position) {
        Transaction_hdr item = transaction_hdrList.get(position);
        holder.bind(item);

        if(position %2 ==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#25bfbfbf"));

        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#7dbebebe"));
        }


    }



  /*  @Override
    public int getItemCount() {
        return transaction_hdrList.size();
    }
*/

    public int grandApproval(){
        int mApproval = transaction_hdrList.size();
        for(int i = 0 ; i < mApproval; ++i) {
            mApproval += transaction_hdrList.get(i).getApproved();
        }
        return mApproval;
    }


    public int grandItem() {
        int count = transaction_hdrList.size();
        return count;
    }




    public void deleteItem(Transaction_hdr transaction_hdr){
        int position = transaction_hdrList.indexOf(transaction_hdr);
        transaction_hdrList.remove(position);
        notifyItemRemoved(position);
    }




    public void removeItem(int position) {
        transaction_hdrList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        //notifyItemRangeChanged(0, getItemCount());
    }


    public void removeItemAtPosition(int position) {
        int size = transaction_hdrList.size();
        transaction_hdrList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, size);
    }



    public void clear() {
        final int size = transaction_hdrList.size();
        transaction_hdrList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void editItem(int position) {
        transaction_hdrList.get(position);
        notifyItemChanged(position, transaction_hdrList.size());
    }

    public void setData(List<Transaction_hdr> newData) {
        this.transaction_hdrList = newData;
        notifyDataSetChanged();
    }

   /* public void setLoaddata(List<Transaction_hdr> transaction_Hdr, int position) {
        transaction_hdrList.get(position).getApproved();
        transaction_hdrList = new ArrayList<>();
       // transaction_hdrList.addAll(transaction_Hdr);
        notifyDataSetChanged();


    }
*/
   public void addAllItems(List<Transaction_hdr> newList) {
       transaction_hdrList = new ArrayList<>();
       transaction_hdrList.addAll(newList);
       notifyItemInserted(transaction_hdrList.size());
       notifyDataSetChanged();
   }


    public void addItem(Transaction_hdr transaction_hdr) {
        transaction_hdrList.add(transaction_hdr);
        notifyItemInserted(transaction_hdrList.size());
    }


    @Override
    public int getItemCount(){
        if(transaction_hdrList == null){
            return 0;
        }
        return transaction_hdrList.size();
    }


   /* public void addItem(Transaction_hdr transaction_hdr){
        transaction_hdrList.add(transaction_hdr);
        notifyItemInserted(getItemCount() -1);
    }
*/

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        TextView tvNo;
        TextView tvTransactionId;
        TextView tvTransactionDate;
        TextView tvPaymentTotal;
        TextView tvSessionId;
        TextView tvConfirmation;
        CheckBox chkApprove;
        TextView tvApprove;
        TextView txtApproved;
        int approved;
        double paymentTotal;
        Transaction_hdr currentItem;
        Context context;

        ViewHolder(View itemView) {
            super(itemView);
            this.context = context;
            tvNo = (TextView) itemView.findViewById(R.id.txtNo);
            tvTransactionId = (TextView) itemView.findViewById(R.id.txtTransactionId);
            tvTransactionDate = (TextView) itemView.findViewById(R.id.txtTransactionDate);
            tvPaymentTotal = (TextView) itemView.findViewById(R.id.txtPaymentTotal);
            tvConfirmation = (TextView) itemView.findViewById(R.id.txtConfirmation);
            tvSessionId = (TextView) itemView.findViewById(R.id.txtSessionId);
            tvApprove = (TextView) itemView.findViewById(R.id.textApprove);
            txtApproved = (TextView) itemView.findViewById(R.id.txtApproved);

            chkApprove = (CheckBox) itemView.findViewById(R.id.chk_Approve);

           // chkApprove.setOnCheckedChangeListener(this);
            chkApprove.setOnClickListener(this);
            chkApprove.setOnCheckedChangeListener(this);



        }

        void bind(Transaction_hdr item) {
            tvNo.setText(String.valueOf(getPosition() + 1));
            tvTransactionId.setText("#" + item.getTransaction_id());
            tvTransactionDate.setText(item.getTransaction_date());
            tvSessionId.setText(item.getSession_id());
            tvConfirmation.setText(String.valueOf(item.getConfirmation()));
            txtApproved.setText(String.valueOf(item.getApproved()));




            Locale local = new Locale("id", "id");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(local);
            paymentTotal = Double.parseDouble(String.valueOf(item.getPayment_total()));

            tvPaymentTotal.setText(String.valueOf("Rp." + fmt.format(paymentTotal).replaceAll("[Rp\\s]", "")));
            approved = item.getApproved();



            chkApprove.setChecked(item.isChecked());



          /*  if (approved == 1) {
                chkApprove.setChecked(true);
                chkApprove.setEnabled(false);
                tvApprove.setText("Approved");
                tvApprove.setTextColor(ColorStateList.valueOf(Color.parseColor("#48366d")));
                chkApprove.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#48366d")));


            } else if (approved == 0) {
                chkApprove.setChecked(false);
                chkApprove.setEnabled(true);
                chkApprove.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#F39C12")));
                tvApprove.setTextColor(ColorStateList.valueOf(Color.parseColor("#F39C12")));
                tvApprove.setText("Approve");
            }
*/

            currentItem = item;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if ((chkApprove.isChecked()) &&  (mItemClickListener != null))  {
                mItemClickListener.onItemCheck(v,position);
                currentItem.setChecked(true);

            } else if((!chkApprove.isChecked()) && (mItemClickListener==null)){
                mItemClickListener.onItemUnCheck(v,position);
                currentItem.setChecked(false);
            }
        }



        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
           final int position = getAdapterPosition();
            Transaction_hdr transaction_hdr = transaction_hdrList.get(position);

            if(buttonView.isChecked()){
               // transaction_hdrList.get(position).setChecked(true);
                transaction_hdr.setChecked(true);
                transaction_hdrList.set(position, transaction_hdr);


            }else{

                transaction_hdr.setChecked(false);
                transaction_hdrList.set(position, transaction_hdr);
                //transaction_hdrList.get(position).setChecked(false);
            }

        }


        /*@Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
           *//* if (buttonView.isChecked()) {
                buttonView.setChecked(true);
                chkApprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemCheck(v, getAdapterPosition());
                        }
                    }
                });
            }*//*

           *//*if(isChecked){
               transaction_hdrList.get(getAdapterPosition()).setChecked(true);
           }else{
               transaction_hdrList.get(getAdapterPosition()).setChecked(false);
           }*//*


            if (buttonView.isChecked()) {
                buttonView.setChecked(true);
                chkApprove.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        if (chkApprove.isChecked()) {
                            mItemClickListener.onItemCheck(currentItem);
                            currentItem.setChecked(true);
                        } else {
                            mItemClickListener.onItemUnCheck(currentItem);
                            currentItem.setChecked(false);
                        }
                    }

                });
            }
        }*/
    }
}
