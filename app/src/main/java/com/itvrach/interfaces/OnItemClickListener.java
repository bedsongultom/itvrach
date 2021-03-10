package com.itvrach.interfaces;

import android.view.View;

/**
 * Created by engineer on 4/18/2019.
 */

public interface OnItemClickListener {
       // void onItemCheck(View v, int position);
        void onItemCheck(View v, final int position);
        void onItemUnCheck(View v,final int position);
}
