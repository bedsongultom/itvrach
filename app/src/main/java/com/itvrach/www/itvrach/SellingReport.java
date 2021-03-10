package com.itvrach.www.itvrach;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.itvrach.adapter.SellingAdapter;

import com.itvrach.model.ResponseTransactionHdr;
import com.itvrach.model.Transaction_hdr;
import com.itvrach.services.APIClient;
import com.itvrach.services.TransactionService;
import com.itvrach.sessions.SessionManagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.itvrach.www.itvrach.R.style.DatePickerDialogTheme;
import static com.itvrach.www.itvrach.R.style.DatePickerDialogTheme2;


/**
 * A simple {@link Fragment} subclass.
 */
public class SellingReport extends Fragment implements View.OnClickListener {
    private static String TAG = SellingReport.class.getSimpleName();

    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private EditText edFromDate, edToDate;

    private List<Transaction_hdr> transaction_hdrList = new ArrayList<>();
    Context context;
    private RecyclerView recyclerView;
    private SellingAdapter sellingAdapter;
    private RecyclerView.LayoutManager mManager;
    private ImageView imgSearch;


    public static SellingReport newInstance() {
        return new SellingReport();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate Selling Report Fragment");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public SellingReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.selling_report, container, false);
        initViews(view);
        return view;
    }

    @SuppressLint("SimpleDateFormat")
    private void initViews(View view){
        SessionManagement session = new SessionManagement(getContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        String user_id = user.get(SessionManagement.KEY_USERID);


        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        new Locale("id", "id");
        
        imgSearch = (ImageView) view.findViewById(R.id.imgSearch); 
        imgSearch.setOnClickListener(this);


        edFromDate = (EditText) view.findViewById(R.id.edFromDate);
        edToDate = (EditText) view.findViewById(R.id.edToDate);

        recyclerView   = (RecyclerView) view.findViewById(R.id.recyclerView);
        mManager       = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(mManager);
        sellingAdapter = new SellingAdapter(getContext(), transaction_hdrList);
        recyclerView.setAdapter(sellingAdapter);
        
        

        edFromDate.setOnClickListener(this);
        edToDate.setOnClickListener(this);

        final ProgressDialog pdSellingReport;
        pdSellingReport = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        pdSellingReport.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdSellingReport.setCancelable(false);
        pdSellingReport.setIndeterminate(true);
        pdSellingReport.getWindow().setGravity(Gravity.CENTER);
        pdSellingReport.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        pdSellingReport.show();
        pdSellingReport.getWindow().setLayout(245, 200);
        pdSellingReport.getWindow().setGravity(Gravity.CENTER);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pdSellingReport.dismiss();
            }
        }, 2000);


        TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
        Call<ResponseTransactionHdr> call = transactionService.getFindAllDoneTransaction();
        call.enqueue(new Callback<ResponseTransactionHdr>() {
            @Override
            public void onResponse(Call<ResponseTransactionHdr> call, Response<ResponseTransactionHdr> response) {
                if (response.isSuccessful()) {
                    ResponseTransactionHdr responseTransactionHdr = response.body();
                    if (responseTransactionHdr.getSuccess().equals("true")) {
                        transaction_hdrList = response.body().getResult();
                        if (transaction_hdrList.size() != 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                sellingAdapter = new SellingAdapter(getContext(), transaction_hdrList);
                                recyclerView.setAdapter(sellingAdapter);
                                sellingAdapter.notifyDataSetChanged();
                                pdSellingReport.dismiss();
                        }
                    }
                }
                else {
                    Log.d(TAG," : data is empty" );
                    
                    recyclerView.setVisibility(View.GONE);
                    pdSellingReport.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseTransactionHdr> call, Throwable t) {
                Log.d(TAG," say : no Internet connection" );
               
                recyclerView.setVisibility(View.GONE);
                pdSellingReport.dismiss();
            }
        });
    }

    @Override
    public void onResume(){
        Log.i(TAG,"onResume");
        super.onResume();
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



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edFromDate:

                calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), DatePickerDialogTheme2, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String c = simpleDateFormat.format(calendar.getTime());

                        edFromDate.setText(c);
                    }
                }, year, month, dayOfMonth);

                datePickerDialog.show();
                datePickerDialog.setCancelable(false);
                datePickerDialog.getWindow().setLayout(525,910);

                Log.d(TAG,"date selected from: "+ edFromDate.toString());

                break;

            case R.id.edToDate:

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getContext(),DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String c = simpleDateFormat.format(calendar.getTime());

                        edToDate.setText(c);
                    }
                }, year, month, dayOfMonth);

                datePickerDialog.show();
                datePickerDialog.setCancelable(false);
                datePickerDialog.getWindow().setLayout(525,910);
                Log.d(TAG,"date selected To: "+ edToDate.toString());

                break;
            
            case R.id.imgSearch:

                String fromDate = edFromDate.getText().toString();
                String endDate = edToDate.getText().toString();
                Search();
                break;

            default:
                Log.d(TAG, "not found id");
                break;
        }

    }
    
    
    private void Search(){
        final ProgressDialog pdSellingReport2;
        pdSellingReport2 = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        pdSellingReport2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdSellingReport2.setCancelable(false);
        pdSellingReport2.setIndeterminate(true);
        pdSellingReport2.getWindow().setGravity(Gravity.CENTER);
        pdSellingReport2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        pdSellingReport2.show();
        pdSellingReport2.getWindow().setLayout(245, 200);
        pdSellingReport2.getWindow().setGravity(Gravity.CENTER);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pdSellingReport2.dismiss();
            }
        }, 2000);

        String params1 = edFromDate.getText().toString();
        String params2 = edToDate.getText().toString();


        Transaction_hdr transaction_hdr = new Transaction_hdr();
        transaction_hdr.setTransaction_date(params1);
        transaction_hdr.setTransaction_date(params2);

        TransactionService transactionService = APIClient.getClient().create(TransactionService.class);


        Call<ResponseTransactionHdr> call = transactionService.getFindTransactionByDate(transaction_hdr);

        call.enqueue(new Callback<ResponseTransactionHdr>() {
            @Override
            public void onResponse(Call<ResponseTransactionHdr> call, Response<ResponseTransactionHdr> response) {
                if (response.isSuccessful()) {
                    ResponseTransactionHdr responseTransactionHdr = response.body();
                    if (responseTransactionHdr.getSuccess().equals("true")) {
                        transaction_hdrList = response.body().getResult();
                        if (transaction_hdrList.size() != 0) {


                            recyclerView.setVisibility(View.VISIBLE);
                            sellingAdapter = new SellingAdapter(getContext(), transaction_hdrList);
                            recyclerView.setAdapter(sellingAdapter);
                            sellingAdapter.notifyDataSetChanged();
                            pdSellingReport2.dismiss();

                        }
                    }

                }
                else {
                    Log.d(TAG," : data is empty" );
                    recyclerView.setVisibility(View.GONE);
                    pdSellingReport2.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseTransactionHdr> call, Throwable t) {
                Log.d(TAG," say : no Internet connection" );
                recyclerView.setVisibility(View.GONE);
                pdSellingReport2.dismiss();

            }
        });

    }


    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);


        final MenuItem searchItem     = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);

        MenuItem qrcodeItem     = menu.findItem(R.id.action_qrcode);
        qrcodeItem.setVisible(false);

        MenuItem cartItem     = menu.findItem(R.id.action_cart);
        cartItem.setVisible(false);

        MenuItem shareItem     = menu.findItem(R.id.action_share);
        shareItem.setVisible(true);
        shareItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        MenuItem settingsItem = menu.findItem(R.id.menu_settings);
        settingsItem.setVisible(false);




        super.onCreateOptionsMenu(menu, inflater);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return false;
            case R.id.action_cart:
                return false;
            case R.id.action_qrcode:
                return false;

            case R.id.action_share:
                return false;

            case R.id.menu_settings:
                return false;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }









}
