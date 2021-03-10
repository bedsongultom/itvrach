package com.itvrach.www.itvrach;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itvrach.adapter.InboxAdapter;
import com.itvrach.model.ResponseTransactionHdr;
import com.itvrach.model.Transaction_hdr;
import com.itvrach.interfaces.OnItemClickListener2;
import com.itvrach.services.APIClient;
import com.itvrach.services.TransactionService;
import com.itvrach.sessions.SessionManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.parseColor;

@SuppressWarnings("SuspiciousNameCombination")
public class Inbox extends Fragment {
    private List<Transaction_hdr> transaction_hdrList = new ArrayList<>();
    Context context;
    private String TAG = "Inbox";
    private RecyclerView recyclerView;
    private InboxAdapter inboxAdapter;
    private RecyclerView.LayoutManager mManager;
    private Drawable iconY, iconX;

    private TextView tvInbox;
    private int count = 0, updateCount=0;


    private ImageView imgEmpty;
    private TextView tvConnection;
    private RelativeLayout emptyRelativeLayout, connectionRelativeLayout;



    public static Inbox newInstance() {
        return new Inbox();
    }

    public Inbox() {

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        setUpToolbarMenu();
        initViews(view);

        return view;
    }



    public void initViews(View view) {


        SessionManagement session = new SessionManagement(getContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        final String user_id = user.get(SessionManagement.KEY_USERID);

        emptyRelativeLayout = (RelativeLayout) view.findViewById(R.id.emptyRelativeLayout);
        emptyRelativeLayout.setVisibility(View.GONE);


        connectionRelativeLayout = (RelativeLayout) view.findViewById(R.id.connectionRelativeLayout);
        connectionRelativeLayout.setVisibility(View.GONE);


        tvInbox  = (TextView) view.findViewById(R.id.txtInbox);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);



       /* recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mManager);
        inboxAdapter = new InboxAdapter(getContext(), transaction_hdrList);
        recyclerView.setAdapter(inboxAdapter);
*/


       ImageView imgConnection = (ImageView) view.findViewById(R.id.imgConnection);
       tvConnection = (TextView) view.findViewById(R.id.tvConnection);
       imgEmpty = (ImageView) view.findViewById(R.id.imgEmpty);
       TextView tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);


        
        final ProgressDialog pdInbox;
        pdInbox = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        pdInbox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdInbox.setCancelable(false);
        pdInbox.setIndeterminate(true);
        pdInbox.getWindow().setGravity(Gravity.CENTER);
        pdInbox.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        pdInbox.show();
        pdInbox.getWindow().setLayout(245, 200);
        pdInbox.getWindow().setGravity(Gravity.CENTER);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pdInbox.dismiss();
            }
        }, 2000);



        TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
        Call<ResponseTransactionHdr> call = transactionService.getFindInboxTransaction(user_id);
        call.enqueue(new Callback<ResponseTransactionHdr>() {
            @Override
            public void onResponse(Call<ResponseTransactionHdr> call, Response<ResponseTransactionHdr> response) {
                if (response.isSuccessful()) {
                    ResponseTransactionHdr responseTransactionHdr = response.body();
                    if (responseTransactionHdr.getSuccess().equals("true")) {
                        transaction_hdrList = response.body().getResult();
                        if (transaction_hdrList.size() != 0) {

                            getClicked();

                            count = inboxAdapter.grandItem();
                            tvInbox.setText(String.valueOf(count));
                            tvInbox.invalidate();


                            updateCount = Integer.parseInt(tvInbox.getText().toString());

                            ((Welcome) getActivity()).tvInboxTextView(updateCount);
                            inboxAdapter.notifyDataSetChanged();
                            
                            pdInbox.dismiss();

                        }
                    }

                }
                else {

                    Log.d(TAG," : data is empty" );

                    emptyRelativeLayout.setVisibility(View.VISIBLE);
                    connectionRelativeLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    pdInbox.dismiss();



                }
            }

            @Override
            public void onFailure(Call<ResponseTransactionHdr> call, Throwable t) {
                Log.d(TAG," say : no Internet connection" );
                emptyRelativeLayout.setVisibility(View.GONE);
                connectionRelativeLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                pdInbox.dismiss();

            }
        });


    /*    chkConfirmed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(buttonView.isChecked()){
               //     transaction_hdrList = getModel(true);
                    getClicked();
                }
                else{

                    //transaction_hdrList = getModel(false);
                    getClicked();
                }

                inboxAdapter.notifyDataSetChanged();
            }
        });
        */


    }

   /* private void getClicked() {
        inboxAdapter = new InboxAdapter(getContext(), transaction_hdrList, new OnItemClickListener2() {

            @Override
            public void onItemClick(View v, int position) {
                transaction_hdrList.get(position);
            }

        });
    }*/


    @Override
    public void onResume(){
        Log.i(TAG,"onResume");
        super.onResume();
        recyclerView.invalidate();
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    public void onStart(){
        super.onStart();
        recyclerView.invalidate();
        Log.d(TAG,"onStart");
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        Log.d(TAG, "onAttach");
    }


    @Override
    public void onDetach(){
        super.onDetach();
        Log.d(TAG, "onDetach");
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d(TAG,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG,"onLowMemory");
    }


    private void getClicked() {

       inboxAdapter = new InboxAdapter(getContext(), transaction_hdrList);
       inboxAdapter.setOnItemClickListener(new  OnItemClickListener2() {
            @SuppressLint("SetTextI18n")
            @Override
           public void onItemClick(View v, final int position) {
                final Transaction_hdr item = transaction_hdrList.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                final LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CENTER_HORIZONTAL);
                layout.setPadding(40, 30, 40, 30);

                TextView title = new TextView(getContext());
                title.setBackgroundColor(parseColor("#48367d"));
                title.setText("MESSAGE");
                title.setTextColor(Color.WHITE);
                title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                title.setPadding(45, 30, 30, 30);


                TextView tvMessage = new TextView(getContext());
                tvMessage.setText("CONFIRMED");
                tvMessage.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvMessage.setTextColor(Color.BLACK);
                tvMessage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(tvMessage);


                TextView tvMessage2 = new TextView(getContext());
                tvMessage2.setText("This Transaction : #"+item.getTransaction_id()+" ?");
                tvMessage2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvMessage2.setTextColor(Color.BLACK);
                tvMessage2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(tvMessage2);


                builder.setView(layout);
                builder.setCustomTitle(title);
                builder.setCancelable(false);
                builder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // transaction_hdrList = getModel(false);
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton

                final AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getWindow().getDecorView().setPaddingRelative(8, 32, 8, 24);
                //dialog.getWindow().setLayout(685, 1050);
                //noinspection deprecation
                iconY = getContext().getResources().getDrawable(R.drawable.ic_close_white_m);
                //noinspection deprecation
                iconX = getContext().getResources().getDrawable(R.drawable.ic_check_white_m);


                @SuppressWarnings("deprecation")
                Drawable iconXy = getResources().getDrawable(R.drawable.ic_check_white);
                iconXy.setBounds(40, 0, 40, 0);

                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.weight = 14.0f;
                positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null, null, null);
                positiveButton.setPadding(40, 0, 80, 0);
                positiveButton.setGravity(Gravity.CENTER);
                positiveButton.setLayoutParams(params);
                positiveButton.setBackgroundColor(parseColor("#f39c12"));
                positiveButton.setTextColor(Color.WHITE);
                positiveButton.invalidate();


                Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params1.weight = 14.0f;

                //noinspection SuspiciousNameCombination
                negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null, null, null);
                negativeButton.setPadding(40, 0, 40, 0);
                negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
                negativeButton.setLayoutParams(params1);
                negativeButton.setBackgroundColor(parseColor("#48367d"));
                negativeButton.setTextColor(Color.WHITE);
                negativeButton.invalidate();


                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SessionManagement session = new SessionManagement(getContext());
                        session.checkLogin();
                        HashMap<String, String> user = session.getUserDetails();
                        final String user_id = user.get(SessionManagement.KEY_USERID);

                        TextView tvConfirmation = new TextView(getContext());
                        tvConfirmation.setText("1");
                        String confirmation = tvConfirmation.getText().toString();

                        TextView tvSession_id = new TextView(getContext());
                        tvSession_id.setText(item.getSession_id());
                        String session_id = tvSession_id.getText().toString();

                        final Transaction_hdr transaction_hdr = new Transaction_hdr();
                        transaction_hdr.setConfirmation(Integer.parseInt(confirmation));
                        transaction_hdr.setSession_id(session_id);


                        TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
                        Call<ResponseTransactionHdr> call = transactionService.Confirmation(session_id, transaction_hdr);
                        call.enqueue(new Callback<ResponseTransactionHdr>() {
                            @Override
                            public void onResponse(Call<ResponseTransactionHdr> call, Response<ResponseTransactionHdr> response) {
                                if (response.isSuccessful()) {
                                    ResponseTransactionHdr responseTransactionHdr = response.body();
                                    if (responseTransactionHdr.getSuccess().equals("true")) {

                                        Toast toast = Toast.makeText(getContext(), responseTransactionHdr.getMessage(), Toast.LENGTH_SHORT);
                                        toast.show();

                                        inboxAdapter.removeItem(position);
                                        inboxAdapter.notifyItemChanged(position);

                                        count = inboxAdapter.grandItem();
                                        tvInbox.setText(String.valueOf(count));
                                        tvInbox.invalidate();
                                        updateCount = Integer.parseInt(tvInbox.getText().toString());
                                        ((Welcome) getActivity()).tvInboxTextView(updateCount);

                                        recyclerView.invalidate();
                                        inboxAdapter.notifyDataSetChanged();

                                    } else {
                                        Toast toast = Toast.makeText(getContext(), "opps", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                } else {
                                    Toast toast = Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseTransactionHdr> call, Throwable t) {

                                Log.d(TAG, " say : no internet connection");
                                connectionRelativeLayout.setVisibility(View.VISIBLE);
                                emptyRelativeLayout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);

                            }
                        });
                        dialog.dismiss();
                    }


                });


            }

        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setAdapter(inboxAdapter);
    }




    /*private void getClicked(){
        inboxAdapter = new InboxAdapter(getContext(), transaction_hdrList, new OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG,"clicked:"+position);


                //noinspection deprecation
                iconY = getContext().getResources().getDrawable(R.drawable.ic_close_white_m);
                //noinspection deprecation
                iconX = getContext().getResources().getDrawable(R.drawable.ic_check_white_m);

                final ProgressDialog pd = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
                pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                pd.getWindow().setGravity(Gravity.CENTER);
                pd.setProgressStyle(android.R.attr.progressBarStyleLarge);
                pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));



                //Toast.makeText(getContext(),"clicked :"+item.getConfirmation(),Toast.LENGTH_SHORT).show();
                final  Transaction_hdr item = transaction_hdrList.get(position);
                inboxAdapter.editItem(position);
                inboxAdapter.notifyItemChanged(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                final LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CENTER_HORIZONTAL);
                layout.setPadding(40, 30, 40, 30);

                TextView title = new TextView(getContext());
                title.setBackgroundColor(parseColor("#48367d"));
                title.setText("CONFIRMATION");
                title.setTextColor(Color.WHITE);
                title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                title.setPadding(45, 30, 30, 30);



                TextView tvTotal = new TextView(getContext());
                tvTotal.setText("Total");
                tvTotal.setTextColor(Color.GRAY);
                layout.addView(tvTotal);


                final Locale local = new Locale("id", "id");
                final NumberFormat fmt = NumberFormat.getCurrencyInstance(local);

                final EditText edTotal = new EditText(getContext());
                edTotal.setTextSize(14);
                edTotal.setEnabled(false);
                edTotal.setBackgroundResource(R.drawable.underline);
                edTotal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                edTotal.setText(String.valueOf(fmt.format(item.getPayment_total()) + "%").replaceAll("[Rp\\s]", ""));
                edTotal.setTextColor(Color.BLACK);
                layout.addView(edTotal);

                builder.setView(layout);
                builder.setCustomTitle(title);
                builder.setCancelable(false);
                builder.setPositiveButton("CONFIRMED",
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

                dialog.getWindow().getDecorView().setPaddingRelative(8, 32, 8, 24);
                //dialog.getWindow().setLayout(685, 1050);



                @SuppressWarnings("deprecation")
                Drawable iconXy = getResources().getDrawable(R.drawable.ic_check_white);
                iconXy.setBounds(40, 0, 40, 0);

                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.weight = 14.0f;
                positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null, null, null);
                positiveButton.setPadding(40, 0, 80, 0);
                positiveButton.setGravity(Gravity.CENTER);
                positiveButton.setLayoutParams(params);
                positiveButton.setBackgroundColor(parseColor("#f39c12"));
                positiveButton.setTextColor(Color.WHITE);
                positiveButton.invalidate();


                Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params1.weight = 14.0f;
                negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null, null, null);
                negativeButton.setPadding(40, 0, 40, 0);
                negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
                negativeButton.setLayoutParams(params1);
                negativeButton.setBackgroundColor(parseColor("#48367d"));
                negativeButton.setTextColor(Color.WHITE);
                negativeButton.invalidate();


            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setAdapter(inboxAdapter);
    }
*/


   /* private void getClicked(){

    }*/


    /*private ArrayList<Transaction_hdr> getModel(boolean isChecked){
       ArrayList<Transaction_hdr> trList = new ArrayList<>();
       int count = transaction_hdrList.size();
        for(int i = 0; i < count; i++){
            Transaction_hdr transaction_hdr = new Transaction_hdr();
            transaction_hdr.setChecked(isChecked);
            transaction_hdr.setTransaction_id(transaction_hdrList.get(i).getTransaction_id());
            transaction_hdr.setTransaction_date(transaction_hdrList.get(i).getTransaction_date());
            transaction_hdr.setConfirmation(transaction_hdrList.get(i).getConfirmation());
            trList.add(transaction_hdr);
        }
        return trList;
    }

*/
    @SuppressLint("PrivateResource")
    public void setUpToolbarMenu() {
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(R.color.white);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        TextView tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        tvTitle.setTextSize(14);
        tvTitle.setText(R.string.inbox_title);
        tvTitle.setVisibility(View.VISIBLE);
        ImageView imgFile = (ImageView) getActivity().findViewById(R.id.imgFile);
        imgFile.setVisibility(View.GONE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Home())
                .addToBackStack(null)
                .commit();

                ((Welcome)getActivity()).enableInboxLayoutTabs(false);

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            }
        });
    }


}