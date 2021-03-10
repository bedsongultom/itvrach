package com.itvrach.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.itvrach.model.User;
import com.itvrach.www.itvrach.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import static com.itvrach.services.APIClient.URI_SEGMENT_UPLOAD;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<User> usersList;
    private Context context;

    public CustomAdapter(List<User> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, context, usersList);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        User item = usersList.get(position);
        holder.bind(item);

    }


    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setFilter(List<User> newList) {
        usersList = new ArrayList<>();
        usersList.addAll(newList);
        notifyDataSetChanged();
    }

    /*public void addItem(String country) {
        names.add(country);
        notifyItemInserted(names.size());
    }

    public void removeItem(int position) {
        names.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, names.size());
    }*/

    public void removeItem(int position) {
       /*int count = CustomAdapter.getItemCount();
        for(int i=0; i < count; i++){
            usersList.remove();
        }*/

        usersList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, usersList.size());
    }

    public void editItem(int position) {
        usersList.get(position);
        notifyItemChanged(position, usersList.size());
    }

   /* public void removeItem(User data) {
        int position = usersList.indexOf(data);
        usersList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, usersList.size());
    }*/

    /*public void addItem(User user) {
        usersList.add(user);
        notifyItemInserted(usersList.size());
    }*/

    /*void moveItem(int oldPos, int newPos){
        User user = usersList.get(oldPos);

        usersList.remove(oldPos);
        usersList.add(newPos, user);
        adapter.notifyItemMoved(oldPos, newPos);
    }

    void deleteItem(final int position){
        usersList.remove(position);
        adapter.notifyItemRemoved(position);
    }*/


    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewUserid;
        ImageView img_file;
        private User currentItem;
        Context context;

        ViewHolder(View itemView, Context context, List<User> usersList) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context = context;
            img_file = (ImageView) itemView.findViewById(R.id.img_file);
            textViewUserid = (TextView) itemView.findViewById(R.id.textViewUserid);
        }

        void bind(User item) {
            textViewUserid.setText(String.valueOf(item.getUser_id()));
            Picasso.with(this.context).load(URI_SEGMENT_UPLOAD + item.getFile()).into(img_file);
            currentItem = item;
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(this.context, "Hai "+currentItem.getUsername(), Toast.LENGTH_LONG).sh
           /* Intent intent = new Intent(context, SearchActivity.class);
            this.context.startActivity(intent);
*/
                   /* AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                    builder1.setMessage("Do you want to remove ?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    //Do your code...
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }*/

            /*intent.putExtra("user_id", currentItem.getUser_id());// <-- makes use of currentItem instead of adapter
            intent.putExtra("username", currentItem.getUsername());
            intent.putExtra("type", currentItem.getType());
            intent.putExtra("firstname", currentItem.getFirstname());
            intent.putExtra("lastname", currentItem.getLastname());
            intent.putExtra("fullname", currentItem.getFullname());
            intent.putExtra("place", currentItem.getPlace());
            intent.putExtra("dateofbirth", currentItem.getDateofbirth());
            intent.putExtra("gender", currentItem.getGender());
            intent.putExtra("religions", currentItem.getReligions());
            intent.putExtra("marital_status", currentItem.getMarital_status());
            intent.putExtra("email", currentItem.getEmail());*/


            final ProgressDialog pd = new ProgressDialog(v.getContext());
            //pd.setTitle("Please Wait ....");
            pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.getWindow().setGravity(Gravity.CENTER);
            pd.setProgressStyle(android.R.attr.progressBarStyleLarge);

            pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CLIP_VERTICAL);
            layout.setPadding(30, 30, 30, 30);

            TextView title = new TextView(context);
            title.setBackgroundColor(Color.parseColor("#303F9F"));
            title.setText("CHANGE E-MAIL PASSWORD");
            title.setTextColor(Color.WHITE);
            title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            title.setPadding(30, 30, 30, 30);


            final ScrollView scrollView = new ScrollView(v.getContext());
            final ImageView img = new ImageView(v.getContext());

            img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            Picasso.with(v.getContext()).load(URI_SEGMENT_UPLOAD + currentItem.getFile()).into(img);
            layout.addView(img);


            final EditText username_text = new EditText(v.getContext());
            username_text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            username_text.setText(currentItem.getUsername());
          //  username_text.setHint("New Password");
            //username_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
           /* password_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, 0, 0);*/
            username_text.setHintTextColor(Color.GRAY);
            layout.addView(username_text);

           /* final EditText userid_text = new EditText(v.getContext());
            userid_text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            userid_text.setText(currentItem.getUser_id());
           // userid_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        *//* verify_password_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_verify_password, 0, 0, 0);*//*
           // userid_text.setHintTextColor(Color.GRAY);
            layout.addView(userid_text);
*/
            scrollView.addView(layout);


            builder.setView(scrollView);
            //builder.setTitle("CHANGE E-MAIL PASSWORD");

            builder.setCustomTitle(title);

            builder.setCancelable(false);
            builder.setPositiveButton("CHANGE",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            }); //End of alert.setNegativeButton

            final AlertDialog dialog = builder.create();
            dialog.show();
            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));

            Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
            params.weight = 14.0f;
            //params.gravity = Gravity.CENTER_HORIZONTAL; //this is layout_gravity
            positiveButton.setGravity(Gravity.CENTER);
            positiveButton.setLayoutParams(params);
            positiveButton.setBackgroundColor(Color.parseColor("#F39C12"));
            positiveButton.setTextColor(Color.WHITE);
            positiveButton.invalidate();


            Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            // positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
            params1.weight = 14.0f;
            //params1.gravity= Gravity.CENTER;
            negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
            negativeButton.setLayoutParams(params1);
            negativeButton.setBackgroundColor(Color.parseColor("#303F9F"));
            negativeButton.setTextColor(Color.WHITE);
            negativeButton.invalidate();
            layout.setPaddingRelative(20, 20, 20, 10);

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Boolean wantToCloseDialog = false;


                }
            });
        }

    }
}


