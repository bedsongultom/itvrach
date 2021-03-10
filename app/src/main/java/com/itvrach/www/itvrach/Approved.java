package com.itvrach.www.itvrach;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itvrach.adapter.ApprovedAdapter;
import com.itvrach.model.ResponseTransactionHdr;
import com.itvrach.model.Transaction_hdr;
import com.itvrach.interfaces.OnItemClickListener;
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
import static com.itvrach.services.RecievedCookiesInterceptor.context;


/**
 * A simple {@link Fragment} subclass.
 */
public class Approved extends Fragment{
    private List<Transaction_hdr> transaction_hdrList = new ArrayList<>();
    private String TAG = "Approved", user_id1, session_id1, user_id ;
    private TextView tvInbox;
    private int updateCount=0, count=0,  mApproval   = 0,
            approved,approved_id,transaction_id;
    private RecyclerView recyclerView;
    private ApprovedAdapter approvedAdapter;
    private RecyclerView.LayoutManager mManager;

    private Drawable iconY, iconX;
    private LinearLayout chkLayout, btnLayout;

    private Button btnApproved;
    private ImageView imgEmpty, imgConnection;
    private TextView tvApproval,tvEmpty, tvConnection;
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "MyPrefsFile";
    private Transaction_hdr transaction_hdr;
    private RelativeLayout emptyRelativeLayout, connectionRelativeLayout;



    public static Approved newInstance() {
        Approved fragment = new Approved();
        return fragment;
    }

    public Approved() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_approved, container, false);
        initViews(view);
        return view;
    }

    @SuppressLint("PrivateResource")
    public void initViews(View view) {

        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //noinspection deprecation
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);


        TextView tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        tvInbox = (TextView) view.findViewById(R.id.txtInbox);
        tvTitle.setTextSize(14);
        tvTitle.setText(R.string.approve_title);
        tvTitle.setVisibility(View.VISIBLE);
        ImageView imgFile = (ImageView) getActivity().findViewById(R.id.imgFile);
        imgFile.setVisibility(View.GONE);


        SessionManagement session = new SessionManagement(getContext());
        session.checkLogin();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        user_id1 = user.get(SessionManagement.KEY_USERID);
        session_id1 = user.get(SessionManagement.KEY_SESSIONID);


        emptyRelativeLayout = (RelativeLayout) view.findViewById(R.id.emptyRelativeLayout);
        emptyRelativeLayout.setVisibility(View.GONE);

        connectionRelativeLayout = (RelativeLayout) view.findViewById(R.id.connectionRelativeLayout);
        connectionRelativeLayout.setVisibility(View.GONE);



        btnApproved = (Button) view.findViewById(R.id.btnApproved);
        chkLayout    = (LinearLayout) view.findViewById(R.id.chkLayout);
        chkLayout.setVisibility(View.VISIBLE);

        btnLayout = (LinearLayout) view.findViewById(R.id.btnLayout);
        btnLayout.setVisibility(View.GONE);

       /* imgConnection = (ImageView) view.findViewById(R.id.imgConnection);
        tvConnection = (TextView) view.findViewById(R.id.tvConnection);
        imgConnection.setVisibility(View.GONE);
        tvConnection.setVisibility(View.GONE);
*/

  /*      imgEmpty = (ImageView) view.findViewById(R.id.imgEmpty);
        tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
        imgEmpty.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
*/

        tvApproval = (TextView) view.findViewById(R.id.txtApproval);
        final CheckBox chkApproved = (CheckBox) view.findViewById(R.id.chk_Approved);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);



        final TextView tvUser_id = new TextView(getContext());
        tvUser_id.setText(user_id1);

        final TextView tvSession_id = new TextView(getContext());
        tvSession_id.setText(session_id1);


        user_id = tvUser_id.getText().toString();

        //transaction_hdrList = getModel(false);
        final ProgressDialog pdApproved;
        pdApproved = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        pdApproved.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdApproved.setCancelable(false);
        pdApproved.setIndeterminate(true);
        pdApproved.getWindow().setGravity(Gravity.CENTER);
        pdApproved.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        pdApproved.show();
        pdApproved.getWindow().setLayout(245, 200);
        pdApproved.getWindow().setGravity(Gravity.CENTER);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pdApproved.dismiss();
            }
        }, 2000);



        TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
        Call<ResponseTransactionHdr> call = transactionService.getFindApproveTransaction(user_id);

        call.enqueue(new Callback<ResponseTransactionHdr>() {
            @Override
            public void onResponse(Call<ResponseTransactionHdr> call, Response<ResponseTransactionHdr> response) {
                if (response.isSuccessful()) {
                    ResponseTransactionHdr responseTransactionHdr = response.body();
                    if (responseTransactionHdr.getSuccess().equals("true")) {
                        transaction_hdrList = response.body().getResult();
                        if (transaction_hdrList.size() != 0) {
                           
                            
                            chkApproved.setEnabled(true);
                            getClicked();
                            count = approvedAdapter.grandItem();
                            tvApproval.setText(String.valueOf(count));
                            tvApproval.invalidate();
                            approvedAdapter.notifyDataSetChanged();
                            pdApproved.dismiss();
                        }
                    }

                } else {
                    Log.d(TAG, " : data is empty");
                    chkApproved.setEnabled(false);
                    emptyRelativeLayout.setVisibility(View.VISIBLE);
                    connectionRelativeLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    pdApproved.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseTransactionHdr> call, Throwable t) {

                Log.d(TAG, " say : Not Connected");
                emptyRelativeLayout.setVisibility(View.GONE);
                connectionRelativeLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                pdApproved.dismiss();

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft;
                ft = getFragmentManager().beginTransaction();

                ft.replace(R.id.fragment_container, new Home());
                ft.addToBackStack(null);
                ft.commit();

               // disableTabs(true);

                ((Welcome)getActivity()).enableApprovedLayoutTabs(false);


                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            }
        });




        btnApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                tvMessage.setText("APPROVED");
                tvMessage.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvMessage.setTextColor(Color.BLACK);
                tvMessage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(tvMessage);


                TextView tvMessage2 = new TextView(getContext());
                tvMessage2.setText("All Transaction ?");
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

                        dialog.cancel();

                    }
                }); //End of alert.setNegativeButton

                final AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getWindow().getDecorView().setPaddingRelative(8, 32, 8, 24);
                //noinspection deprecation
                iconY = getContext().getResources().getDrawable(R.drawable.ic_close_white_m);
                //noinspection deprecation
                iconX = getContext().getResources().getDrawable(R.drawable.ic_check_white_m);


                @SuppressWarnings("deprecation")
                Drawable iconXy = getResources().getDrawable(R.drawable.ic_check_white);
                iconXy.setBounds(40, 0, 40, 0);

                final Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.weight = 14.0f;
                positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null, null, null);
                positiveButton.setPadding(40, 0, 80, 0);
                positiveButton.setGravity(Gravity.CENTER);
                positiveButton.setLayoutParams(params);
                positiveButton.setBackgroundColor(parseColor("#f39c12"));
                positiveButton.setTextColor(Color.WHITE);
                positiveButton.invalidate();


                final Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params1.weight = 14.0f;

                //noinspection SuspiciousNameCombination
                negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null, null, null);
                negativeButton.setPadding(40, 0, 40, 0);
                negativeButton.setGravity(Gravity.CENTER);
                negativeButton.setLayoutParams(params1);
                negativeButton.setBackgroundColor(parseColor("#48367d"));
                negativeButton.setTextColor(Color.WHITE);
                negativeButton.invalidate();


                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final List<Transaction_hdr> transList = new ArrayList<Transaction_hdr>();

                        for (final Transaction_hdr transaction_Hdr : transaction_hdrList) {
                            if (transaction_Hdr.isChecked()) {

                                transaction_Hdr.setChecked(true);
                                approved = 1;
                                approved_id = Integer.parseInt(tvUser_id.getText().toString());
                                transaction_id = transaction_Hdr.getTransaction_id();
                                transaction_Hdr.setTransaction_id(transaction_id);
                                transaction_Hdr.setApproved(approved);
                                transaction_Hdr.setApproved_id(approved_id);
                            }

                            transList.add(transaction_Hdr);
                            approvedAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(approvedAdapter);

                            TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
                            Call<ResponseTransactionHdr> call = transactionService.ChangeApproved(transaction_Hdr);
                            call.enqueue(new Callback<ResponseTransactionHdr>() {

                                @Override
                                public void onResponse(Call<ResponseTransactionHdr> call, Response<ResponseTransactionHdr> response) {
                                    if (response.isSuccessful()) {
                                        ResponseTransactionHdr responseTransactionHdr = response.body();
                                        if (responseTransactionHdr.getSuccess().equals("true")) {

                                            Log.d(TAG, ": " + responseTransactionHdr.getMessage());
                                            Toast toast = Toast.makeText(getContext(), responseTransactionHdr.getMessage(), Toast.LENGTH_SHORT);
                                            toast.show();

                                           /* for(int i=transList.size()-1; i>=0; i--) {
                                                approvedAdapter.removeItem(i);
                                            }*/

                                            /*for (int i = 0; i < transList.size(); i++) {
                                                try {
                                                    approvedAdapter.removeItem(transaction_hdrList.indexOf(transList.get(i)));
                                                } catch (IndexOutOfBoundsException e) {
                                                    e.printStackTrace();
                                                }
                                            }

*/
                                            approvedAdapter.clear();
                                            approvedAdapter.notifyDataSetChanged();

                                            chkLayout.setVisibility(View.VISIBLE);
                                            //chkApproved.setChecked(false);
                                            btnLayout.setVisibility(View.GONE);

                                           // transaction_Hdr.setChecked(false);
                                           // approvedAdapter.addItem(transaction_hdr);
                                            //approvedAdapter.notifyDataSetChanged();



                                           getFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.fragment_container,
                                                            new Approved())
                                                    .addToBackStack(null)
                                                    .commit();

                                            count = approvedAdapter.grandItem();
                                            tvApproval.setText(String.valueOf(count));
                                            tvApproval.invalidate();
                                            recyclerView.invalidate();
                                        }

                                    } else {
                                        Toast toast = Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseTransactionHdr> call, Throwable t) {
                                    Log.d(TAG, " : no internet connection");
                                    recyclerView.setVisibility(View.GONE);
                                    imgConnection.setVisibility(View.VISIBLE);
                                    tvConnection.setVisibility(View.VISIBLE);
                                    //btnApproved.setEnabled(false);
                                }
                            });

                            dialog.dismiss();

                        }
                    }


                });

            }

        });


       /* chkApproved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()) {
                    transaction_hdrList = getModel(true);
                    getClicked();

                }
                else{
                    transaction_hdrList = getModel(false);
                    getClicked();
                }
                approvedAdapter.notifyDataSetChanged();
            }
        });
*/


        chkApproved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                List<Transaction_hdr> transList = new ArrayList<Transaction_hdr>();
                if (buttonView.isChecked()) {
                    for (Transaction_hdr transaction_hdr : transaction_hdrList) {
                        transaction_hdr.isChecked();
                        transaction_hdr.setChecked(true);
                        transList.add(transaction_hdr);
                    }

                    chkLayout.setVisibility(View.GONE);
                    btnLayout.setVisibility(View.VISIBLE);


                } else {
                    for (Transaction_hdr transaction_hdr : transaction_hdrList) {
                        transaction_hdr.setChecked(false);
                        transList.add(transaction_hdr);
                    }
                }

                approvedAdapter.notifyDataSetChanged();
            }

        });
    }



    public void history(){

    }



    private ArrayList<Transaction_hdr> getModel(boolean isChecked){
       ArrayList<Transaction_hdr> trList = new ArrayList<>();
       int count = transaction_hdrList.size();
        for(int i = 0; i < count; i++){
            Transaction_hdr transaction_hdr = new Transaction_hdr();
            transaction_hdr.setChecked(isChecked);
            transaction_hdr.setApproved(1);
            transaction_hdr.setTransaction_id(transaction_hdrList.get(i).getTransaction_id());
            transaction_hdr.setTransaction_date(transaction_hdrList.get(i).getTransaction_date());
            transaction_hdr.setPayment_total(transaction_hdrList.get(i).getPayment_total());
            trList.add(transaction_hdr);
        }
        return trList;
    }

    /*private boolean disableTabs(boolean disable){
        if(disable==true) {

            Log.d(TAG,"disableTabs is true");
            ((Welcome) getActivity()).approvedLinearLayout.setVisibility(View.GONE);
            ((Welcome) getActivity()).highLightCurrentApprovedTab(0);
            ((Welcome) getActivity()).viewPagerApproved.setCurrentItem(0);

            ((Welcome) getActivity()).tabApprovedLayout.setVisibility(View.GONE);
            ((Welcome) getActivity()).viewPagerApproved.setVisibility(View.GONE);
            ((Welcome) getActivity()).viewPagerApproved.setPagingEnabled(false);

            ((ViewGroup) ((Welcome) getActivity()).tabApprovedLayout.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
            ((ViewGroup) ((Welcome) getActivity()).tabApprovedLayout.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
            ((Welcome) getActivity()).tabApprovedLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }else{

            Log.d(TAG, "disableTabs is false");
            ((Welcome) getActivity()).approvedLinearLayout.setVisibility(View.VISIBLE);
            ((Welcome) getActivity()).highLightCurrentApprovedTab(0);
            ((Welcome) getActivity()).viewPagerApproved.setCurrentItem(0);

            ((Welcome) getActivity()).tabApprovedLayout.setVisibility(View.VISIBLE);
            ((Welcome) getActivity()).viewPagerApproved.setVisibility(View.VISIBLE);
            ((Welcome) getActivity()).viewPagerApproved.setPagingEnabled(true);

            ((ViewGroup) ((Welcome) getActivity()).tabApprovedLayout.getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
            ((ViewGroup) ((Welcome) getActivity()).tabApprovedLayout.getChildAt(0)).getChildAt(1).setVisibility(View.VISIBLE);
            ((Welcome) getActivity()).tabApprovedLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }

        return disable;
    }
*/


    @Override
    public void onResume(){
        Log.i(TAG,"onResume");
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
       // Log.d(TAG, "onStop");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"onPause");
    }

    @Override
    public void onStart(){
        super.onStart();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    private void getClicked() {
        approvedAdapter = new ApprovedAdapter(context,transaction_hdrList );
        approvedAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemCheck(View v, final int position) {
                final  Transaction_hdr item = transaction_hdrList.get(position);
                // Toast.makeText(getContext(), "clicked"+currentItem.getConfirmation(), Toast.LENGTH_SHORT).show();

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
                tvMessage.setText("APPROVED");
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
                        transaction_hdrList = getModel(false);
                        Transaction_hdr item = transaction_hdrList.remove(position);
                        // transaction_hdrList.get(position).setChecked(false);
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton

                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().getDecorView().setPaddingRelative(8, 32, 8, 24);
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
                        final String userId = user.get(SessionManagement.KEY_USERID);


                        final TextView tvApproved = new TextView(getContext());
                        tvApproved.setText("1");
                        TextView tvSession_id = new TextView(getContext());
                        tvSession_id.setText(item.getSession_id());


                        TextView tvUser_id = new TextView(getContext());
                        tvUser_id.setText(userId);

                        approved = Integer.parseInt(tvApproved.getText().toString());
                        String session_id = tvSession_id.getText().toString();
                        user_id = tvUser_id.getText().toString();

                        final Transaction_hdr transaction_hdr = new Transaction_hdr();
                        transaction_hdr.setApproved(approved);
                        transaction_hdr.setSession_id(session_id);
                        transaction_hdr.setApproved_id(Integer.parseInt(user_id));

                        TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
                        Call<ResponseTransactionHdr> call = transactionService.Approved(session_id, transaction_hdr);
                        call.enqueue(new Callback<ResponseTransactionHdr>() {
                            @Override
                            public void onResponse(Call<ResponseTransactionHdr> call, Response<ResponseTransactionHdr> response) {
                                if (response.isSuccessful()) {
                                    ResponseTransactionHdr responseTransactionHdr = response.body();
                                    if (responseTransactionHdr.getSuccess().equals("true")) {

                                        approvedAdapter.removeItem(position);
                                        approvedAdapter.notifyItemChanged(position);

                                        count = approvedAdapter.grandItem();
                                        tvApproval.setText(String.valueOf(count));
                                        tvApproval.invalidate();

                                        approvedAdapter.notifyDataSetChanged();
                                        recyclerView.invalidate();

                                        Toast toast = Toast.makeText(getContext(), responseTransactionHdr.getMessage(), Toast.LENGTH_SHORT);
                                        toast.show();
                                    }

                                } else {
                                    Toast toast = Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseTransactionHdr> call, Throwable t) {
                                Log.d(TAG, " : no internet connection");


                            }
                        });

                        dialog.dismiss();
                    }


                });
        }

            @Override
            public void onItemUnCheck(View v, int position) {
                Log.d(TAG, " : Unclicked");
                //Toast.makeText(getContext(), "unclicked", Toast.LENGTH_SHORT).show();
            }
        });


        /*approvedAdapter = new ApprovedAdapter(getContext(), transaction_hdrList, new OnItemClickListener() {

            @Override
            public void onItemCheck(final View v, final int position) {
                //transaction_hdrList.get(position);

                final  Transaction_hdr item = transaction_hdrList.get(position);
               // Toast.makeText(getContext(), "clicked"+currentItem.getConfirmation(), Toast.LENGTH_SHORT).show();

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
                tvMessage.setText("APPROVED");
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
                        transaction_hdrList = getModel(false);
                        Transaction_hdr item = transaction_hdrList.remove(position);
                       // transaction_hdrList.get(position).setChecked(false);
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
                        final String userId = user.get(SessionManagement.KEY_USERID);


                        final TextView tvApproved = new TextView(getContext());
                        tvApproved.setText("1");
                        TextView tvSession_id = new TextView(getContext());
                        tvSession_id.setText(item.getSession_id());


                        TextView tvUser_id = new TextView(getContext());
                        tvUser_id.setText(userId);

                        approved = Integer.parseInt(tvApproved.getText().toString());
                        String session_id = tvSession_id.getText().toString();
                        user_id = tvUser_id.getText().toString();

                        final Transaction_hdr transaction_hdr = new Transaction_hdr();
                        transaction_hdr.setApproved(approved);
                        transaction_hdr.setSession_id(session_id);
                        transaction_hdr.setApproved_id(Integer.parseInt(user_id));

                        TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
                        Call<ResponseTransactionHdr> call = transactionService.Approved(session_id, transaction_hdr);
                        call.enqueue(new Callback<ResponseTransactionHdr>() {
                            @Override
                            public void onResponse(Call<ResponseTransactionHdr> call, Response<ResponseTransactionHdr> response) {
                                if (response.isSuccessful()) {
                                    ResponseTransactionHdr responseTransactionHdr = response.body();
                                    if (responseTransactionHdr.getSuccess().equals("true")) {
                                        approvedAdapter.removeItem(position);
                                        approvedAdapter.notifyItemChanged(position);

                                        count = approvedAdapter.grandItem();
                                        tvApproval.setText(String.valueOf(count));
                                        tvApproval.invalidate();

                                        approvedAdapter.notifyDataSetChanged();
                                        recyclerView.invalidate();
                                        Toast toast = Toast.makeText(getContext(), responseTransactionHdr.getMessage(), Toast.LENGTH_SHORT);
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
                            }
                        });

                        dialog.dismiss();
                    }


                });


            }

            @Override
            public void onItemUnCheck(View v, final int position) {
                Toast.makeText(getContext(), "unclicked", Toast.LENGTH_SHORT).show();

            }

        });*/
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setAdapter(approvedAdapter);
    }






    /*private void getClicked(){
        approvedAdapter = new ApprovedAdapter(getContext(), transaction_hdrList, new OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemCheck(View v, final int position) {
                final  Transaction_hdr item = transaction_hdrList.get(position);

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
                tvMessage.setText("APPROVED");
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
                        transaction_hdrList = getModel(false);
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
                        final String userId = user.get(SessionManagement.KEY_USERID);


                        final TextView tvApproved = new TextView(getContext());
                        tvApproved.setText("1");
                        TextView tvSession_id = new TextView(getContext());
                        tvSession_id.setText(item.getSession_id());
                        TextView tvUser_id = new TextView(getContext());
                        tvUser_id.setText(userId);

                        approved = tvApproved.getText().toString();
                        session_id = tvSession_id.getText().toString();
                        user_id = tvUser_id.getText().toString();

                        final Transaction_hdr transaction_hdr = new Transaction_hdr();
                        transaction_hdr.setApproved(Integer.parseInt(approved));
                        transaction_hdr.setSession_id(session_id);
                        transaction_hdr.setApproved_id(Integer.parseInt(user_id));

                        TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
                        Call<ResponseTransactionHdr> call = transactionService.Approved(session_id, transaction_hdr);
                        call.enqueue(new Callback<ResponseTransactionHdr>() {
                            @Override
                            public void onResponse(Call<ResponseTransactionHdr> call, Response<ResponseTransactionHdr> response) {
                                if (response.isSuccessful()) {
                                    ResponseTransactionHdr responseTransactionHdr = response.body();
                                    if (responseTransactionHdr.getSuccess().equals("true")) {
                                        approvedAdapter.removeItem(position);
                                        approvedAdapter.notifyItemChanged(position);


                                        count = approvedAdapter.grandItem();
                                        tvApproval.setText(String.valueOf(count));
                                        tvApproval.invalidate();

                                        approvedAdapter.notifyDataSetChanged();
                                        recyclerView.invalidate();




                                      *//*   TextView tvApprovalCount = new TextView(getContext());

                                        updateCount = Integer.parseInt(tvApprovalCount.getText().toString());
                                        ((Welcome) getActivity()).tvInboxTextView(updateCount);

                                        approvedAdapter.notifyDataSetChanged();
*//*

                                        Toast toast = Toast.makeText(getContext(), responseTransactionHdr.getMessage(), Toast.LENGTH_SHORT);
                                        toast.show();

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
                            }
                        });

                        dialog.dismiss();
                    }


                });


            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setAdapter(approvedAdapter);
    }
*/


   /* @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                                new Home())
                        .addToBackStack(null)
                        .commit();

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                break;
        }
    }*/
}