package com.itvrach.www.itvrach;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.itvrach.adapter.ShippingAdapter;
import com.itvrach.model.ResponseModel;
import com.itvrach.model.ResponseShipping;
import com.itvrach.model.ResponseTransactionHdr;
import com.itvrach.model.Shipping;
import com.itvrach.model.Transaction_hdr;
import com.itvrach.interfaces.OnItemClickListener3;
import com.itvrach.services.APIClient;
import com.itvrach.services.ShippingService;
import com.itvrach.services.TransactionService;
import com.itvrach.services.UserService;
import com.itvrach.sessions.SessionManagement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.parseColor;
import static com.itvrach.www.itvrach.R.style.DatePickerDialogTheme;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShippingFragment extends Fragment {
    private List<Transaction_hdr> transaction_hdrList = new ArrayList<>();
    Context context;
    private String TAG = "Shipping", VIOLET ="#48367d";
    private RecyclerView recyclerView;
    private ShippingAdapter shippingAdapter;
    private RecyclerView.LayoutManager mManager;
    private Drawable iconY, iconX;
    private TextView tvInbox, tvEmpty, tvConnection;
    private ImageView imgConnection, imgEmpty;
    private int count=0,  updateCount=0;

    private SignaturePad mSignaturePad;
    private int year;
    private int month;
    private int dayOfMonth, selectedId;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat;
    private Locale local;
    private NumberFormat fmt;
    private static final int REQUEST_EXTERNAL_STORAGE =1;
    private static final int REQUEST_MEDIA_SCANNER =1;
    private static String[] PERMISION_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private RelativeLayout emptyRelativeLayout, connectionRelativeLayout;
    private Uri filePath;
    private String user_id;




    public static ShippingFragment newInstance() {
        return new ShippingFragment();
    }

    public ShippingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);
        setUpToolbarMenu();
        initViews(view);
        return view;
    }


    public void initViews(View view){
        SessionManagement session = new SessionManagement(getContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        user_id = user.get(SessionManagement.KEY_USERID);

        emptyRelativeLayout = (RelativeLayout) view.findViewById(R.id.emptyRelativeLayout);
        emptyRelativeLayout.setVisibility(View.GONE);

        connectionRelativeLayout = (RelativeLayout) view.findViewById(R.id.connectionRelativeLayout);
        connectionRelativeLayout.setVisibility(View.GONE);


        tvInbox        = (TextView) view.findViewById(R.id.txtInbox);
        recyclerView   = (RecyclerView) view.findViewById(R.id.recyclerView);
        mManager       = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

       /* imgConnection  = (ImageView) view.findViewById(R.id.imgConnection);
        tvConnection   = (TextView) view.findViewById(R.id.tvConnection);
        imgEmpty       = (ImageView) view.findViewById(R.id.imgEmpty);
        tvEmpty        = (TextView) view.findViewById(R.id.tvEmpty);
        imgConnection.setVisibility(View.GONE);
        tvConnection.setVisibility(View.GONE);
        imgEmpty.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
*/

        final ProgressDialog pdShipping;
        pdShipping = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        pdShipping.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdShipping.setCancelable(false);
        pdShipping.setIndeterminate(true);
        pdShipping.getWindow().setGravity(Gravity.CENTER);
        pdShipping.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        pdShipping.show();
        pdShipping.getWindow().setLayout(245, 200);
        pdShipping.getWindow().setGravity(Gravity.CENTER);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pdShipping.dismiss();
            }
        }, 2000);




        TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
        Call<ResponseTransactionHdr> call = transactionService.getFindShippingList(user_id);
        call.enqueue(new Callback<ResponseTransactionHdr>() {
            @Override
            public void onResponse(Call<ResponseTransactionHdr> call, Response<ResponseTransactionHdr> response) {
                if (response.isSuccessful()) {
                    ResponseTransactionHdr responseTransactionHdr = response.body();
                    if (responseTransactionHdr.getSuccess().equals("true")) {
                        transaction_hdrList = response.body().getResult();
                        if (transaction_hdrList.size() != 0) {


                            getClicked();

                            count = shippingAdapter.grandItem();
                            tvInbox.setText(String.valueOf(count));
                            tvInbox.invalidate();

                            /*updateCount = Integer.parseInt(tvInbox.getText().toString());
                            ((Welcome) getActivity()).tvInboxTextView(updateCount);*/

                            shippingAdapter.notifyDataSetChanged();
                            pdShipping.dismiss();
                        }
                    }
                }
                else {
                    Log.d(TAG," : data is empty" );
                    emptyRelativeLayout.setVisibility(View.VISIBLE);
                    connectionRelativeLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    pdShipping.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseTransactionHdr> call, Throwable t) {
                Log.d(TAG," say : no Internet connection" );
                emptyRelativeLayout.setVisibility(View.GONE);
                connectionRelativeLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                pdShipping.dismiss();
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




    private void getClicked() {
        shippingAdapter = new ShippingAdapter(context, transaction_hdrList);
        shippingAdapter.setOnItemClickListener(new OnItemClickListener3() {
            @Override
            public void onItemClick(View v, final int position) {

                local = new Locale("id", "id");
                fmt = NumberFormat.getCurrencyInstance(local);
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                final Transaction_hdr item = transaction_hdrList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                final LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CENTER_HORIZONTAL);
                layout.setPadding(40, 30, 40, 30);

                TextView title = new TextView(getContext());
                title.setBackgroundColor(parseColor(VIOLET));
                title.setText("Shipping's detail");
                title.setTextColor(Color.WHITE);
                title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                title.setPadding(45, 30, 30, 30);


                final ScrollView scrollView = new ScrollView(getContext());


                TextView tvTransactionId = new TextView(getContext());
                tvTransactionId.setText("Transaction Id");
                tvTransactionId.setTextColor(Color.BLACK);
                tvTransactionId.setTextSize(14);
                tvTransactionId.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(tvTransactionId);

                final EditText edTransactionId = new EditText(getContext());
                edTransactionId.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                edTransactionId.setTextSize(14);
                edTransactionId.setText("#" + String.valueOf(item.getTransaction_id()));
                edTransactionId.setTextColor(Color.BLACK);
                edTransactionId.setEnabled(false);
                edTransactionId.setBackgroundResource(R.drawable.underline);
                layout.addView(edTransactionId);

                TextView tvCustumerName = new TextView(getContext());
                tvCustumerName.setText("Custumer's Name");
                tvCustumerName.setTextColor(Color.BLACK);
                tvCustumerName.setTextSize(14);
                tvCustumerName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(tvCustumerName);

                EditText edCustumersName = new EditText(getContext());
                edCustumersName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                edCustumersName.setTextSize(14);
                edCustumersName.setText(String.valueOf(item.getApproved_id()));
                edCustumersName.setTextColor(Color.BLACK);
                edCustumersName.setEnabled(false);
                edCustumersName.setBackgroundResource(R.drawable.underline);
                layout.addView(edCustumersName);



                TextView tvReceivedDateTime = new TextView(getContext());
                tvReceivedDateTime.setText("Received Datetime");
                tvReceivedDateTime.setTextColor(Color.BLACK);
                tvReceivedDateTime.setTextSize(14);
                tvReceivedDateTime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                layout.addView(tvReceivedDateTime);


                String value = "";//any text you are pre-filling in the EditText

                final EditText edCustomersSignatureDateTime = new EditText(getContext());
                edCustomersSignatureDateTime.requestFocus();
                edCustomersSignatureDateTime.setSingleLine(true);
                edCustomersSignatureDateTime.setRawInputType(InputType.TYPE_CLASS_DATETIME|InputType.TYPE_DATETIME_VARIATION_DATE);
                edCustomersSignatureDateTime.setTextSize(14);
                edCustomersSignatureDateTime.setEnabled(true);
                edCustomersSignatureDateTime.setText(value);
                @SuppressWarnings("deprecation")
                final Drawable y = getResources().getDrawable(R.drawable.ic_calendar);//your x image, this one from standard android images looks pretty good actually
                y.setBounds(0, 0, y.getIntrinsicWidth(), y.getIntrinsicHeight());
                edCustomersSignatureDateTime.setCompoundDrawables(null, null, value.equals("") ? null : y, null);
                edCustomersSignatureDateTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_calendar, 0);

                layout.addView(edCustomersSignatureDateTime);
                edCustomersSignatureDateTime.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        edCustomersSignatureDateTime.setEnabled(true);
                        if (edCustomersSignatureDateTime.getCompoundDrawables()[2] == null) {
                            return false;
                        }
                        if (event.getAction() != MotionEvent.ACTION_UP) {
                            return false;
                        }
                        if (event.getX() > edCustomersSignatureDateTime.getWidth() - edCustomersSignatureDateTime.getPaddingRight() - y.getIntrinsicWidth()) {
                            // editTextdateofbirth.setText("aa");
                            //editTextdateofbirth.setCompoundDrawables(null, null, null, null);

                            calendar = Calendar.getInstance();
                            year   = calendar.get(Calendar.YEAR);
                            month  = calendar.get(Calendar.MONTH);
                            dayOfMonth    = calendar.get(Calendar.DAY_OF_MONTH);
                            datePickerDialog = new DatePickerDialog(getContext(),DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener(){
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
                                    calendar.set(Calendar.YEAR, year);
                                    calendar.set(Calendar.MONTH, month);
                                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                    String c = simpleDateFormat.format(calendar.getTime());
                                    edCustomersSignatureDateTime.setText(c);
                                }
                            },year, month, dayOfMonth);
                            datePickerDialog.show();
                            datePickerDialog.setCancelable(false);
                            datePickerDialog.getWindow().setLayout(525,910);

                        }
                        return false;
                    }
                });




                final EditText tvCustomerSignature = new EditText(getContext());
                tvCustomerSignature.setText("Customer's Signature :");
                tvCustomerSignature.setTextColor(Color.BLACK);
                tvCustomerSignature.setTextSize(14);
                tvCustomerSignature.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tvCustomerSignature.setEnabled(false);
                tvCustomerSignature.setBackgroundResource(R.drawable.underline);
                tvCustomerSignature.setPadding(10,10,5,10);

                @SuppressWarnings("deprecation")
                final Drawable z = getResources().getDrawable(R.drawable.ic_clear_black);//your x image, this one from standard android images looks pretty good actually
                z.setBounds(0, 0, z.getIntrinsicWidth(), z.getIntrinsicHeight());
             //   tvCustomerSignature.setCompoundDrawables(null, null, value.equals("") ? null : z, null);
                tvCustomerSignature.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_black_mini, 0);
                layout.addView(tvCustomerSignature);

                tvCustomerSignature.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        tvCustomerSignature.setEnabled(true);
                        if (tvCustomerSignature.getCompoundDrawables()[2] == null) {
                            return false;
                        }
                        if (event.getAction() != MotionEvent.ACTION_UP) {
                            return false;
                        }
                        if (event.getX() > tvCustomerSignature.getWidth() - tvCustomerSignature.getPaddingRight() - z.getIntrinsicWidth()) {
                            mSignaturePad.clear();
                            return true;
                        }
                        return false;
                    }
                });



                mSignaturePad = new SignaturePad(getContext(),null);
                mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
                    @Override
                    public void onStartSigning() {
                        Log.d(TAG,"is Starting");
                    }

                    @Override
                    public void onSigned() {
                        tvCustomerSignature.setEnabled(true);
                        Log.d(TAG,": is Signed ");
                    }

                    @Override
                    public void onClear() {
                        tvCustomerSignature.setEnabled(false);
                        Log.d(TAG,": is Clear");
                    }

                });

                mSignaturePad.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 450));
                mSignaturePad.setPenColor(Color.parseColor("#48367d"));
                layout.addView(mSignaturePad);




                scrollView.addView(layout);
                builder.setView(scrollView);
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
                dialog.getWindow().setLayout(685, 1070);
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
                        final String userid = user.get(SessionManagement.KEY_USERID);
                        final String sessionid = user.get(SessionManagement.KEY_SESSIONID);

                        //save signaturePad to bitmap

                        Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                        addJpgSignatureToGallery(signatureBitmap);


                        Toast.makeText(getContext(),"Signature saved into the Gallery", Toast.LENGTH_SHORT);


                        String session_id         = sessionid;
                        TextView tvUser_id = new TextView(getContext());
                        tvUser_id.setText(userid);
                        String user_id            = tvUser_id.getText().toString();

                        String transaction_id     = edTransactionId.getText().toString().replaceAll("[^0-9]", "");
                        String shipping_datetime  = edCustomersSignatureDateTime.getText().toString();
                        String signature_datetime = edCustomersSignatureDateTime.getText().toString();
                        int shipped               = 1;


                        Shipping shipping = new Shipping();

                        shipping.setSession_id(session_id);
                        shipping.setUser_id(Integer.parseInt(user_id));
                        shipping.setTransaction_id(Integer.parseInt(transaction_id));
                        shipping.setShipping_datetime(shipping_datetime);
                        shipping.setSignature_datetime(signature_datetime);
                        shipping.setShipped(String.valueOf(shipped));

                        ShippingService shippingService = APIClient.getClient().create(ShippingService.class);
                        Call<ResponseShipping> call = shippingService.Create(shipping);
                        call.enqueue(new Callback<ResponseShipping>() {
                            @Override
                            public void onResponse(Call<ResponseShipping> call, Response<ResponseShipping> response) {
                                if (response.isSuccessful()) {
                                    ResponseShipping responseShipping = response.body();

                                    if (responseShipping.getSuccess().equals("true")) {
                                        Toast toast = Toast.makeText(getContext(), responseShipping.getMessage(), Toast.LENGTH_SHORT);
                                        toast.show();

                                        shippingAdapter.removeItem(position);
                                        shippingAdapter.notifyItemChanged(position);


                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                uploadMultipart();
                                            }
                                        }, 3000);


                                        count = shippingAdapter.grandItem();
                                        tvInbox.setText(String.valueOf(count));
                                        tvInbox.invalidate();
                                       /* updateCount = Integer.parseInt(tvInbox.getText().toString());
                                        ((Welcome) getActivity()).tvInboxTextView(updateCount);
                                       */

                                        recyclerView.invalidate();
                                        shippingAdapter.notifyDataSetChanged();

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
                            public void onFailure(Call<ResponseShipping> call, Throwable t) {
                                Log.d(TAG, " say : no internet connection");
                                emptyRelativeLayout.setVisibility(View.GONE);
                                connectionRelativeLayout.setVisibility(View.VISIBLE);
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
        recyclerView.setAdapter(shippingAdapter);


    }


    public File getAlbumStorageDir(String albumName){
        //GET the directory for the user 's public pictures directory
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName );
            if(!file.mkdirs()){
                Log.d("SignaturPad", "Directory not created");
            }
        return file;
    }


    private void scanMediaFile(File photo){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        filePath = Uri.fromFile(photo);
        Log.d(TAG, ""+filePath);
        mediaScanIntent.setData(filePath);
        getContext().sendBroadcast(mediaScanIntent);
    }


   /* private void scanMediaFile2(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        startActivityForResult(mediaScanIntent, REQUEST_MEDIA_SCANNER);

    }
   */



    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_MEDIA_SCANNER){
             //   uploadMultipart();
            }
        }
    }

    public void saveBitmapToJpg(Bitmap bitmap, File photo) throws IOException{
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap,0,0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }


    public boolean addJpgSignatureToGallery(Bitmap signature){
        boolean result = false;
        try{
            File photo = new File (getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJpg(signature, photo);
            scanMediaFile(photo);
            result = true;
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContext().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

        Log.d(TAG,""+path);
        cursor.close();
        return path;
    }

    public void uploadMultipart() {

        String path = getPath(filePath);
        File file = new File(path);

        TextView tvUser_id = new TextView(getContext());
        tvUser_id.setText(user_id);

        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        RequestBody user_idPart = RequestBody.create(MultipartBody.FORM, tvUser_id.getText().toString());

        ShippingService shippingService = APIClient.getClient().create(ShippingService.class);
        Call<ResponseShipping> call = shippingService.Upload(body, user_idPart);
        call.enqueue(new Callback<ResponseShipping>() {
            @Override
            public void onResponse(Call<ResponseShipping> call, Response<ResponseShipping> response) {
                if (response.isSuccessful()) {

                    ResponseShipping responseShipping = response.body();
                    if (responseShipping.getStatus().equals("true")) {

                        Toast.makeText(getContext(), responseShipping.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(),responseShipping.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(getContext(), "Any Problem For this Picture \n Select another Picture.. !", Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onFailure(Call<ResponseShipping> call, Throwable t) {
                // Toast.makeText(WelcomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(),"Server Disconnected", Toast.LENGTH_SHORT).show();
            }
        });

    }

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
        tvTitle.setText(R.string.shipping_title);
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

                ((Welcome) getActivity()).enableShippingLayoutTabs(false);

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        });
    }



}