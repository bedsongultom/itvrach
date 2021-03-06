package com.itvrach.adapter;

import android.content.Context;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.itvrach.www.itvrach.R;
import com.itvrach.www.itvrach.UsersListActivity;

import java.util.ArrayList;

/**
 * Created by engineer on 8/5/2018.
 */

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.ViewHolder>{

    public Context context;
    public ArrayList<String> steps;

    public UsersListAdapter(Context context, ArrayList<String> steps) {
        this.context = context;
        this.steps = steps;
    }



    @Override
    public UsersListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_users_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UsersListAdapter.ViewHolder holder, int position) {
         int x = holder.getLayoutPosition();

        if(steps.get(x).length()>0){
            holder.step.setText(steps.get(x));
        }
        else
        {
            holder.step.setText(null);
            holder.step.setHint("Next Step");
            holder.step.requestFocus();
        }

    }

    public ArrayList<String> getStepList(){
        return steps;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton plus, minus;
        public EditText step;


        public ViewHolder(View itemView){
            super(itemView);
            plus  = (ImageButton) itemView.findViewById(R.id.plus);
            minus = (ImageButton) itemView.findViewById(R.id.minus);
            step  = (EditText) itemView.findViewById(R.id.step);

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try {
                        steps.remove(position);
                        notifyItemRemoved(position);
                    }catch (ArrayIndexOutOfBoundsException e){e.printStackTrace();}


                }
            });

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try {
                        steps.add(position + 1, "");
                        notifyItemInserted(position + 1);
                    }catch (ArrayIndexOutOfBoundsException e){e.printStackTrace();}
                }
            });

            step.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                        steps.set(getAdapterPosition(),s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        }





    }
}


