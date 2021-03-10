package com.itvrach.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.itvrach.www.itvrach.R;

/**
 * Created by engineer on 10/30/2018.
 */

public class MyHolder extends RecyclerView.ViewHolder {

    TextView nameTxt;
   /* TextView valueTxt;

    TextView txtId;
    TextView txtItem_name;
    ImageView img;
    TextView txtPrice;*/


    public MyHolder(View itemView) {
        super(itemView);

       // img = (ImageView) itemView.findViewById(R.id.file);
      /*  txtId = (TextView) itemView.findViewById(R.id.txtId);
        txtItem_name = (TextView) itemView.findViewById(R.id.txtItem_name);
        txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);*/

        nameTxt= (TextView) itemView.findViewById(R.id.nameTxt);
        //valueTxt = (TextView) itemView.findViewById(R.id.valueTxt);*/
    }
}