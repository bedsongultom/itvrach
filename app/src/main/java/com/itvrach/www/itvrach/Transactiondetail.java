package com.itvrach.www.itvrach;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itvrach.adapter.BooksAdapter;
import com.itvrach.adapter.TransactionAdapter;
import com.itvrach.model.Books;
import com.itvrach.model.ResponseTransaction;
import com.itvrach.model.Transaction;
import com.itvrach.services.APIClient;
import com.itvrach.services.TransactionService;
import com.itvrach.sessions.SessionManagement;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.GRAY;
import static com.itvrach.www.itvrach.R.style.DatePickerDialogTheme;

/**
 * Created by engineer on 2/4/2019.
 */

public class Transactiondetail extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mManager;
    private List<Transaction> transactionList = new ArrayList<>();
    private TransactionAdapter adapter;
    private ArrayList<Transaction> lista;
    private EditText editTextSearch;
    private RecyclerView mrecylerID;
    private RecyclerView.LayoutManager mManager2;
    private List<Books> booksList = new ArrayList<>();
    private BooksAdapter adapter2;
    private ViewTooltip tooltipView = null;
    private ImageButton btnProducts;

    private TextView subTotal;
    private TextView subQty;

    private  int mSubTotal = 0;
    private int mSubQty   = 0;
    private int mSubDiscRp   = 0;
    private int count = 0;
    private TextView txtPrice;
    private TextView txtNo;
    private TextView txtSub_Qty;
    private Paint p = new Paint();

    private TextView txtUser_id;
    private TextView txtItem_code;
    private TextView txtPrice1;
    private TextView txtQty;
    private TextView txtTotal_price;
    private TextView TextUser_id;
    private TextView TextEmail;
    private TextView txtsession_id;
    private int user_idx, user_id, qty, price, total_price;
    private String item_code, session_id;
    private TextView textViewEmail;
    private Context context;


    private int year;
    private int month;
    private int dayOfMonth, selectedId;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat;



    private int no = 0;
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "MyPrefsFile";

    private SessionManagement session;
    private TextView txt_userid;

    private Drawable iconY;
    private Drawable iconX;

    private ProgressDialog DelProgress;
    private Button btnCancel;
    private Button btnProcess;
    private TextView txtSub_DiscRp;
    private Locale local;
    private NumberFormat fmt;
    private ProgressDialog lProgress;
    private ProgressDialog pd;


    public static Transactiondetail newInstance() {
        Transactiondetail fragment = new Transactiondetail();
        return fragment;
    }



    public Transactiondetail() {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.transactiondetail,container,false);

        lProgress = new ProgressDialog(getContext());
        lProgress.setMessage("loading ...");
        lProgress.setCancelable(false);
        lProgress.setIndeterminate(true);


        local = new Locale("id", "id");
        fmt = NumberFormat.getCurrencyInstance(local);

        iconY = getContext().getResources().getDrawable(R.drawable.ic_close_white_m);
        iconX = getContext().getResources().getDrawable(R.drawable.ic_check_white_m);

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete();
            }
        });


        btnProcess = (Button) view.findViewById(R.id.btnProcess);
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPayment();

            }
        });


        session = new SessionManagement(getContext());

        HashMap<String, String> user = session.getUserDetails();
        String sessionid = user.get(SessionManagement.KEY_SESSIONID);
        String userid = user.get(SessionManagement.KEY_USERID);
        String emailid = user.get(SessionManagement.KEY_EMAIL);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        txtsession_id = (TextView) view.findViewById(R.id.txtsession_id);
        txtsession_id.setText(sessionid);
        txtsession_id.setVisibility(View.GONE);

        txt_userid = (TextView) view.findViewById(R.id.txt_userid);
        txt_userid.setText(userid);
        txt_userid.setVisibility(View.GONE);


        //java.lang.CharSequence)' on a null object reference we must add the component or object exp )Textview )textViewEmail in layout
        textViewEmail = (TextView) view.findViewById(R.id.textViewEmail);
        textViewEmail.setText(emailid);
        textViewEmail.setVisibility(View.GONE);


        final String user_id     = txt_userid.getText().toString();
        final String session_id  = txtsession_id.getText().toString();
        final String email       = textViewEmail.getText().toString();


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mManager);
        adapter = new TransactionAdapter(transactionList, getContext());
        recyclerView.setAdapter(adapter);



        session.checkLogin();

        initSwipe();

        subTotal = (TextView) view.findViewById(R.id.txtTotal);
        txtNo = (TextView) view.findViewById(R.id.txtNo);
        txtSub_Qty = (TextView) view.findViewById(R.id.txtSub_Qty);
        txtSub_DiscRp = (TextView) view.findViewById(R.id.txtSub_DiscRp);
        subQty = (TextView) view.findViewById(R.id.txtSub_item);



        pd = new ProgressDialog(getContext());
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.setProgressStyle(android.R.attr.progressBarStyle);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        int secondsDelayed = 1;
        pd.show();
        pd.getWindow().setLayout(245, 200);
        pd.getWindow().setGravity(Gravity.CENTER);
       // new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                pd.dismiss();
            }
        },  2000);
        TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
        Call<ResponseTransaction> call = transactionService.getFindAll(session_id);
        call.enqueue(new Callback<ResponseTransaction>() {
            @Override
            public void onResponse(Call<ResponseTransaction> call, Response<ResponseTransaction> response) {
                if (response.isSuccessful()) {
                    ResponseTransaction responseTransaction = response.body();
                    if(responseTransaction.getSuccess().equals("true")) {
                        transactionList = response.body().getResult();
                        ////  if( session_id.equals(responseTransaction.getSession_id()) && responseTransaction.getSuccess().equals("true")){                            transactionList = response.body().getResult();
                        if (transactionList.size() != 0) {
                            adapter = new TransactionAdapter(transactionList, getContext());
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            count = adapter.grandItem();
                            txtSub_Qty.setText(String.valueOf(count));


                            mSubDiscRp = adapter.grandDiscRp();
                            txtSub_DiscRp.setText(String.valueOf("Rp. "+ fmt.format(mSubDiscRp)).replaceAll("[Rp\\s]", ""));


                            int counts = 0;
                            counts=Integer.parseInt(txtSub_Qty.getText().toString());
                           /* Search search = new Search();
                            search.updateContTextView(counts);
*/

                            mSubTotal = adapter.grandTotal();
                            subTotal.setText(String.valueOf("Rp. "+ fmt.format(mSubTotal)).replaceAll("[Rp\\s]", ""));
                            mSubQty = adapter.grandQty();
                            subQty.setText(String.valueOf(mSubQty));
                            // pd.dismiss();

                            //Toast.makeText(TransationActivity.this,, Toast.LENGTH_SHORT).show();
                        }

                    }

                }else {
                    Toast.makeText(getContext(), "DATA IS EMPTY", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseTransaction> call, Throwable t) {
                //Toast.makeText(TransationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Server Disconnected", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });




        return view;
    }

   /* public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }*/


    private void dialogPayment(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(0, 0, 0, 0);

        TextView title = new TextView(getContext());
        title.setBackgroundColor(Color.parseColor("#48367d"));
        title.setText("PAYMENT");
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setPadding(40, 30, 30, 30);

        final ScrollView scrollView = new ScrollView(getContext());


        LinearLayout layoutbody = new LinearLayout(getContext());
        layoutbody.setOrientation(LinearLayout.VERTICAL);
        layoutbody.setGravity(Gravity.CLIP_VERTICAL);
        layoutbody.setPadding(30, 30, 30, 30);


        mSubTotal = adapter.grandTotal();




       /* final TextView textPaymenttotal = new TextView(getContext());
        textPaymenttotal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textPaymenttotal.setTextColor(Color.BLACK);
        textPaymenttotal.setText("Payment Total");
        textPaymenttotal.setTypeface(null, Typeface.BOLD);
        layoutbody.addView(textPaymenttotal);
*/

        final TextView textPaymentdate = new TextView(getContext());
        textPaymentdate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textPaymentdate.setTextColor(Color.BLACK);
        textPaymentdate.setText("Payment Date");
        layoutbody.addView(textPaymentdate);


        String value = "";//any text you are pre-filling in the EditText

        final EditText edPaymentdate = new EditText(getContext());

        edPaymentdate.setSingleLine(true);
        edPaymentdate.setRawInputType(InputType.TYPE_CLASS_DATETIME|InputType.TYPE_DATETIME_VARIATION_DATE);

        edPaymentdate.setEnabled(true);
        //  editTextdateofbirth.setBackgroundResource(R.drawable.underline);
        edPaymentdate.setText(value);
        final Drawable y = getResources().getDrawable(R.drawable.ic_calendar);//your x image, this one from standard android images looks pretty good actually
        y.setBounds(0, 0, y.getIntrinsicWidth(), y.getIntrinsicHeight());

        edPaymentdate.setCompoundDrawables(null, null, value.equals("") ? null : y, null);
        edPaymentdate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_calendar, 0);
        layoutbody.addView(edPaymentdate);
        edPaymentdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edPaymentdate.setEnabled(true);
                if (edPaymentdate.getCompoundDrawables()[2] == null) {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() > edPaymentdate.getWidth() - edPaymentdate.getPaddingRight() - y.getIntrinsicWidth()) {
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

                            edPaymentdate.setText(c);
                        }
                    },year, month, dayOfMonth);
                    datePickerDialog.show();
                    datePickerDialog.setCancelable(false);
                    datePickerDialog.getWindow().setLayout(525,910);

                }
                return false;
            }
        });



        final TextView textRekeningno = new TextView(getContext());
        textRekeningno.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textRekeningno.setTextColor(Color.BLACK);
        textRekeningno.setText("Rekening No");
        layoutbody.addView(textRekeningno);


        final EditText editRekeningno = new EditText(getContext());
        editRekeningno.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //editRekeningno.setTextSize(14);
        editRekeningno.setTextColor(Color.BLACK);
        editRekeningno.setSingleLine(true);
        layoutbody.addView(editRekeningno);


        final TextView textRekeningno2 = new TextView(getContext());
        textRekeningno2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textRekeningno2.setTextColor(Color.BLACK);
        textRekeningno2.setText("Rekening No");
        layoutbody.addView(textRekeningno2);


        final EditText editRekeningno2 = new EditText(getContext());
        editRekeningno.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //editRekeningno.setTextSize(14);
        editRekeningno2.setTextColor(Color.BLACK);
        editRekeningno2.setSingleLine(true);
        layoutbody.addView(editRekeningno2);
      //  editRekeningno.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        final TextView textRekeningno3 = new TextView(getContext());
        textRekeningno3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textRekeningno3.setTextColor(Color.BLACK);
        textRekeningno3.setText("Rekening No");
        layoutbody.addView(textRekeningno3);


        final EditText editRekeningno3 = new EditText(getContext());
        editRekeningno3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //editRekeningno.setTextSize(14);
        editRekeningno3.setTextColor(Color.BLACK);
        editRekeningno3.setSingleLine(true);
        layoutbody.addView(editRekeningno3);
        //  editRekeningno.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        final TextView textTotaltransfer = new TextView(getContext());
        textTotaltransfer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textTotaltransfer.setTextColor(Color.BLACK);
        textTotaltransfer.setText("Pay");
        layoutbody.addView(textTotaltransfer);



        final EditText edTotalbayar = new EditText(getContext());


        edTotalbayar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        edTotalbayar.setTextColor(Color.BLACK);
        edTotalbayar.setInputType(InputType.TYPE_CLASS_NUMBER);
        edTotalbayar.setRawInputType(Configuration.KEYBOARD_12KEY);
        edTotalbayar.setSingleLine(true);
       // edTotalbayar.setTextSize(14);
        edTotalbayar.setTypeface(null, Typeface.BOLD);
        layoutbody.addView(edTotalbayar);

        edTotalbayar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String current = "";
                if (!s.toString().equals(current)) {
                    edTotalbayar.removeTextChangedListener(this);


                    String replaceable = String.format("[Rp,.\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String cleanString = s.toString().replaceAll(replaceable,
                            "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    NumberFormat formatter = NumberFormat
                            .getCurrencyInstance(local);
                    formatter.setMaximumFractionDigits(0);
                    formatter.setParseIntegerOnly(true);
                    String formatted = formatter.format((parsed));

                    String replace = String.format("[Rp\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String clean = formatted.replaceAll(replace, "");

                    current = formatted;
                    edTotalbayar.setText("Rp. "+ clean);
                    edTotalbayar.setSelection(clean.length());
                    edTotalbayar.addTextChangedListener(this);
                }
            }
        });


        /*final EditText editTotal = new EditText(getContext());
        editTotal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        // editTotal.setTextSize(20);
        editTotal.setTextColor(Color.BLACK);
        editTotal.setEnabled(false);
        editTotal.setBackgroundResource(R.drawable.underline);
        editTotal.setTypeface(null, Typeface.BOLD);*/
        String Total = ("Rp. " + String.valueOf(fmt.format(mSubTotal)).replaceAll("[Rp\\s]", ""));
     //   layoutbody.addView(editTotal);


        EditText textPayment = new EditText(getContext());
        LinearLayout.LayoutParams ll =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textPayment.setBackgroundColor(Color.parseColor("#F39C12"));
        textPayment.setLayoutParams(ll);
        textPayment.setText("PAYMENT TYPE");
        textPayment.setTypeface(null, Typeface.BOLD);
        textPayment.setPadding(20,20,20,20);
        textPayment.setEnabled(false);
        textPayment.setTextColor(Color.WHITE);
        layout.addView(textPayment);




        final RadioGroup radioGroup= new RadioGroup(getContext());
        radioGroup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        final RadioButton atmBCAdioButton = new RadioButton(getContext());
        radioGroup.setPadding(20,20,20,20);
        atmBCAdioButton.setText("ATM BCA");
      //  atmBCAdioButton.setPadding(30,10,10,10);
        //   maleradioButton.setId(1);
        radioGroup.addView(atmBCAdioButton);

        final RadioButton atmMANDIRIRadioButton = new RadioButton(getContext());
        atmMANDIRIRadioButton.setText("ATM MANDIRI");
        radioGroup.addView(atmMANDIRIRadioButton);

        final RadioButton cashRadioButton = new RadioButton(getContext());
        cashRadioButton.setText("CASH DIRECT");
        radioGroup.addView(cashRadioButton);
        layout.addView(radioGroup);





        EditText textbaner = new EditText(getContext());
        LinearLayout.LayoutParams ls =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textbaner.setBackgroundColor(Color.parseColor("#F39C12"));
        textbaner.setLayoutParams(ls);
       // textbaner.setTextSize(14);
        textbaner.setText("PAYMENT TOTAL : "+ Total);
        textbaner.setPadding(20,20,20,20);
        textbaner.setEnabled(false);
        textbaner.setTextColor(Color.WHITE);

        layout.addView(textbaner);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int radioGroup) {
                // find which radio button is selected
                final String payment;
                if (radioGroup==1) {
                    payment="ATM BCA";
                   // Toast.makeText(MainActivity.this, gender,Toast.LENGTH_SHORT).show();
                } else if (radioGroup == 2) {
                    payment="ATM MANDIRI";
                   // Toast.makeText(MainActivity.this, gender,Toast.LENGTH_SHORT).show();
                }
            }
        });
        layout.addView(layoutbody);

        scrollView.addView(layout);
        builder.setView(scrollView);
        builder.setCustomTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton("Process",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null,null,null);
        positiveButton.setPadding(40,0,40,0);

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

        negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null,null,null);
        negativeButton.setPadding(40,0,40,0);

        negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
        negativeButton.setLayoutParams(params1);
        negativeButton.setBackgroundColor(Color.parseColor("#48367d"));
        negativeButton.setTextColor(Color.WHITE);
        negativeButton.invalidate();
        // layout.setPaddingRelative(22,0,22,0);

        dialog.getWindow().getDecorView().setPaddingRelative(8,32,8,24);

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

}

    private  void dialogDelete(){
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
    final LinearLayout layout = new LinearLayout(getContext());
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setGravity(Gravity.CLIP_VERTICAL);
    layout.setPadding(40, 30, 40, 30);


        TextView title = new TextView(getContext());
    title.setBackgroundColor(Color.parseColor("#48367d"));
    title.setText("CANCEL TRANSACTION");
    title.setTextColor(Color.WHITE);
    title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    title.setPadding(40, 30, 30, 30);
    builder.setCustomTitle(title);

    builder.setMessage("Are you sure to Cancel this transaction ?")
            //.setMessage("----------------------------------------------------------")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {


                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                    //SwipeableItemViewHolder.setSwipeItemHorizontalSlideAmount()
                }
            });
    final AlertDialog dialog = builder.create();
    dialog.show();

        dialog.getWindow().getDecorView().setPaddingRelative(8,32,8,24);


        Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
    //positiveButton.setCompoundDrawables(iconX,null,null,null);

    // positiveButton.setPadding(20,20,20,40);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    //positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
    params.weight = 14.0f;
    //params.gravity = Gravity.CENTER_HORIZONTAL; //this is layout_gravity
    positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null, null, null);
    positiveButton.setPadding(40, 0, 40, 0);


    positiveButton.setGravity(Gravity.CENTER);
    positiveButton.setLayoutParams(params);
    positiveButton.setBackgroundColor(Color.parseColor("#f39c12"));
    positiveButton.setTextColor(Color.WHITE);
    positiveButton.invalidate();


    Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
    // negativeButton.setPadding(20,20,20,40);
    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    // positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
    params1.weight = 14.0f;
    //params1.gravity= Gravity.CENTER;
    negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null, null, null);
    negativeButton.setPadding(40, 0, 40, 0);
    negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
    negativeButton.setLayoutParams(params1);
    negativeButton.setBackgroundColor(Color.parseColor("#48367d"));
    negativeButton.setTextColor(Color.WHITE);
    negativeButton.invalidate();

    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteTransaction();
            dialog.dismiss();
        }
    });

}



    private Boolean checkdata(int user_id){

        if(String.valueOf(user_id) == null){
            Toast.makeText(getContext(),"SORRY DATA IS EMPTY", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    public List<Transaction> getTransactionList (String key) {
        if (sharedPreferences != null) {

            Gson gson = new Gson();
            List<Transaction> companyList;

            String string = sharedPreferences.getString(key, null);
            Type type = new TypeToken<List<Transaction>>() {
            }.getType();
            companyList = gson.fromJson(string, type);
            return companyList;
        }
        return null;
    }


    private int grandTotal(List<Books> booksList) {
        int totalPrice = 0;
        for (int i = 0; i < booksList.size(); i++) {
            totalPrice += booksList.get(i).getPrice();
        }
        return totalPrice;
    }

    private Paint createPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new LinearGradient(0, 0, 0, 0, GRAY, GRAY, Shader.TileMode.MIRROR));
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }




    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0
                , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {

                final int position = viewHolder.getAdapterPosition();
                Transaction item = transactionList.get(position);
                final String item_code  = item.getItem_code();
                final String session_id = item.getSession_id();

                if (direction == ItemTouchHelper.LEFT) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    final LinearLayout layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setGravity(Gravity.CLIP_VERTICAL);
                    layout.setPadding(40, 30, 40, 30);

                    TextView title = new TextView(getContext());
                    title.setBackgroundColor(Color.parseColor("#48367d"));
                    title.setText("REMOVE MESSAGE");
                    title.setTextColor(Color.WHITE);
                    title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    title.setPadding(40, 30, 30, 30);
                    builder.setCustomTitle(title);

                    builder.setMessage("Are you sure to Remove this item :" + item_code + "?")
                            //.setMessage("----------------------------------------------------------")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                    //SwipeableItemViewHolder.setSwipeItemHorizontalSlideAmount()
                                }
                            });
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getWindow().getDecorView().setPaddingRelative(8,32,8,24);
                    Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    //positiveButton.setCompoundDrawables(iconX,null,null,null);

                    // positiveButton.setPadding(20,20,20,40);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    //positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                    params.weight = 14.0f;
                    //params.gravity = Gravity.CENTER_HORIZONTAL; //this is layout_gravity
                    positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null, null, null);
                    positiveButton.setPadding(40, 0, 40, 0);


                    positiveButton.setGravity(Gravity.CENTER);
                    positiveButton.setLayoutParams(params);
                    positiveButton.setBackgroundColor(Color.parseColor("#f39c12"));
                    positiveButton.setTextColor(Color.WHITE);
                    positiveButton.invalidate();


                    Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                    // negativeButton.setPadding(20,20,20,40);
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    // positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                    params1.weight = 14.0f;
                    //params1.gravity= Gravity.CENTER;
                    negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null, null, null);
                    negativeButton.setPadding(40, 0, 40, 0);
                    negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
                    negativeButton.setLayoutParams(params1);
                    negativeButton.setBackgroundColor(Color.parseColor("#48367d"));
                    negativeButton.setTextColor(Color.WHITE);
                    negativeButton.invalidate();

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
                            Call<ResponseTransaction> call = transactionService.delete(item_code, session_id);
                            call.enqueue(new Callback<ResponseTransaction>() {
                                @Override
                                public void onResponse(Call<ResponseTransaction> call, Response<ResponseTransaction> response) {
                                    ResponseTransaction responseTransaction = response.body();

                                    adapter.removeItem(position);
                                    count = adapter.grandItem();
                                    txtSub_Qty.setText(String.valueOf(count));
                                    int counts = Integer.parseInt(txtSub_Qty.getText().toString());
                                   /* Search search = new Search();
                                    search.updateContTextView(counts);
*/
                                    //((Welcome) getActivity())
                                    mSubDiscRp = adapter.grandDiscRp();
                                    txtSub_DiscRp.setText(String.valueOf("Rp."+fmt.format(mSubDiscRp)).replaceAll("[Rp\\s]", ""));

                                    mSubTotal = adapter.grandTotal();

                                    subTotal.setText(String.valueOf(fmt.format(mSubTotal)).replaceAll("[Rp\\s]", ""));


                                }

                                @Override
                                public void onFailure(Call<ResponseTransaction> call, Throwable t) {
                                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        }
                    });


                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    final LinearLayout layout = new LinearLayout(getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setGravity(Gravity.CLIP_VERTICAL);
                    layout.setPadding(30, 30, 30, 30);

                    TextView title = new TextView(getContext());
                    title.setBackgroundColor(Color.parseColor("#48366d"));
                    title.setText("EDIT MESSAGE");
                    title.setTextColor(Color.WHITE);
                    title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    title.setPadding(30, 30, 30, 30);
                    builder.setCustomTitle(title);

     /*
      Transaction item = transactionList.get(position);
                                    adapter.editItem(position);
                                    adapter.notifyItemChanged(position);

                                    final ProgressDialog pd = new ProgressDialog(getContext());
                                    //pd.setTitle("Please Wait ....");
                                    pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    pd.setCancelable(false);
                                    pd.setIndeterminate(true);
                                    pd.getWindow().setGravity(Gravity.CENTER);
                                    pd.setProgressStyle(android.R.attr.progressBarStyleLarge);

                                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                    LinearLayout layout = new LinearLayout(getContext());
                                    layout.setOrientation(LinearLayout.VERTICAL);
                                    layout.setGravity(Gravity.CLIP_VERTICAL);
                                    layout.setPadding(30, 30, 30, 30);

                                    TextView title = new TextView(getContext());
                                    title.setBackgroundColor(Color.parseColor("#48367d"));
                                    title.setText("EDIT TRANSACTION");
                                    title.setTextColor(Color.WHITE);
                                    title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    title.setPadding(30, 30, 30, 30);

                                    final ScrollView scrollView = new ScrollView(getContext());


                                    final TextView tvId = new TextView(getContext());
                                    tvId.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    tvId.setText("ID");
                                    tvId.setHintTextColor(Color.GRAY);
                                    layout.addView(tvId);


                                    final EditText Id = new EditText(getContext());
                                    Id.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    Id.setText(String.valueOf(item.getId()));
                                    Id.setHintTextColor(Color.GRAY);
                                    layout.addView(Id);


                                    final TextView tvItem_code = new TextView(getContext());
                                    tvItem_code.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    tvItem_code.setText("ITEM CODE");
                                    tvItem_code.setHintTextColor(Color.GRAY);
                                    layout.addView(tvItem_code);


                                    final EditText Item_code = new EditText(getContext());
                                    Item_code.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    Item_code.setText(item.getItem_code());
                                    Item_code.setHintTextColor(Color.GRAY);
                                    layout.addView(Item_code);

                                    final TextView tvPrice = new TextView(getContext());
                                    tvPrice.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    tvPrice.setText("PRICE");
                                    tvPrice.setHintTextColor(Color.GRAY);
                                    layout.addView(tvPrice);

                                    final EditText Price = new EditText(getContext());
                                    Price.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    Price.setText(String.valueOf(item.getPrice()));
                                    Price.setHintTextColor(Color.GRAY);
                                    layout.addView(Price);


                                    final TextView tvQty = new TextView(getContext());
                                    tvQty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    tvQty.setText("QTY");
                                    tvQty.setHintTextColor(Color.GRAY);
                                    layout.addView(tvQty);

                                    final EditText Qty = new EditText(getContext());
                                    Qty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    Qty.setText(String.valueOf(item.getQty()));
                                    Qty.setHintTextColor(Color.GRAY);
                                    layout.addView(Qty);



                                    scrollView.addView(layout);
                                    builder.setView(scrollView);

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

                                    final AlertDialog dialogs = builder.create();
                                    dialogs.show();
                                    //  dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));

                                    Button positiveButton = ((AlertDialog) dialogs).getButton(DialogInterface.BUTTON_POSITIVE);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    //positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                                    params.weight = 14.0f;

                                    positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null,null,null);
                                    positiveButton.setPadding(40,0,40,0);

                                    //params.gravity = Gravity.CENTER_HORIZONTAL; //this is layout_gravity
                                    positiveButton.setGravity(Gravity.CENTER);
                                    positiveButton.setLayoutParams(params);
                                    positiveButton.setBackgroundColor(Color.parseColor("#f39c12"));
                                    positiveButton.setTextColor(Color.WHITE);
                                    positiveButton.invalidate();


                                    Button negativeButton = ((AlertDialog) dialogs).getButton(DialogInterface.BUTTON_NEGATIVE);
                                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    // positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                                    params1.weight = 14.0f;


                                    negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null,null,null);
                                    negativeButton.setPadding(40,0,40,0);
                                    //params1.gravity= Gravity.CENTER;
                                    negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
                                    negativeButton.setLayoutParams(params1);
                                    negativeButton.setBackgroundColor(Color.parseColor("#48367d"));
                                    negativeButton.setTextColor(Color.WHITE);
                                    negativeButton.invalidate();
                                    layout.setPaddingRelative(20, 20, 20, 10);

                                    dialogs.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Boolean wantToCloseDialog = false;
                                        }
                                    });
                                }


                            })
      */


                    builder.setMessage("Are you sure to Edit this item :" + item_code + "?")
                            //.setMessage("----------------------------------------------------------")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                    //SwipeableItemViewHolder.setSwipeItemHorizontalSlideAmount()
                                }
                            });
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                    //positiveButton.setCompoundDrawables(iconX,null,null,null);

                    // positiveButton.setPadding(20,20,20,40);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    //positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                    params.weight = 14.0f;
                    //params.gravity = Gravity.CENTER_HORIZONTAL; //this is layout_gravity
                    positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null, null, null);
                    positiveButton.setPadding(40, 0, 40, 0);


                    positiveButton.setGravity(Gravity.CENTER);
                    positiveButton.setLayoutParams(params);
                    positiveButton.setBackgroundColor(Color.parseColor("#f39c12"));
                    positiveButton.setTextColor(Color.WHITE);
                    positiveButton.invalidate();


                    Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                    // negativeButton.setPadding(20,20,20,40);
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    // positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                    params1.weight = 14.0f;
                    //params1.gravity= Gravity.CENTER;
                    negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null, null, null);
                    negativeButton.setPadding(40, 0, 40, 0);
                    negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
                    negativeButton.setLayoutParams(params1);
                    negativeButton.setBackgroundColor(Color.parseColor("#48367d"));
                    negativeButton.setTextColor(Color.WHITE);
                    negativeButton.invalidate();

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Transaction item = transactionList.get(position);
                            adapter.editItem(position);
                            adapter.notifyItemChanged(position);

                            final ProgressDialog pd = new ProgressDialog(getContext());
                            //pd.setTitle("Please Wait ....");
                            pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            pd.setCancelable(false);
                            pd.setIndeterminate(true);
                            pd.getWindow().setGravity(Gravity.CENTER);
                            pd.setProgressStyle(android.R.attr.progressBarStyleLarge);

                            pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                            LinearLayout layout = new LinearLayout(getContext());
                            layout.setOrientation(LinearLayout.VERTICAL);
                            layout.setGravity(Gravity.CLIP_VERTICAL);
                            layout.setPadding(30, 30, 30, 30);

                            TextView title = new TextView(getContext());
                            title.setBackgroundColor(Color.parseColor("#48367d"));
                            title.setText("EDIT TRANSACTION");
                            title.setTextColor(Color.WHITE);
                            title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            title.setPadding(30, 30, 30, 30);

                            final ScrollView scrollView = new ScrollView(getContext());


                            final TextView tvId = new TextView(getContext());
                            tvId.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            tvId.setText("ID");
                            tvId.setHintTextColor(Color.GRAY);
                            layout.addView(tvId);


                            final EditText Id = new EditText(getContext());
                            Id.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            Id.setText(String.valueOf(item.getId()));
                            Id.setHintTextColor(Color.GRAY);
                            layout.addView(Id);


                            final TextView tvItem_code = new TextView(getContext());
                            tvItem_code.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            tvItem_code.setText("ITEM CODE");
                            tvItem_code.setHintTextColor(Color.GRAY);
                            layout.addView(tvItem_code);


                            final EditText Item_code = new EditText(getContext());
                            Item_code.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            Item_code.setText(item.getItem_code());
                            Item_code.setHintTextColor(Color.GRAY);
                            layout.addView(Item_code);

                            final TextView tvPrice = new TextView(getContext());
                            tvPrice.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            tvPrice.setText("PRICE");
                            tvPrice.setHintTextColor(Color.GRAY);
                            layout.addView(tvPrice);

                            final EditText Price = new EditText(getContext());
                            Price.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            Price.setText(String.valueOf(item.getPrice()));
                            Price.setHintTextColor(Color.GRAY);
                            layout.addView(Price);


                            final TextView tvQty = new TextView(getContext());
                            tvQty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            tvQty.setText("QTY");
                            tvQty.setHintTextColor(Color.GRAY);
                            layout.addView(tvQty);

                            final EditText Qty = new EditText(getContext());
                            Qty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            Qty.setText(String.valueOf(item.getQty()));
                            Qty.setHintTextColor(Color.GRAY);
                            layout.addView(Qty);



                            scrollView.addView(layout);
                            builder.setView(scrollView);

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

                            final AlertDialog dialogs = builder.create();
                            dialogs.show();
                            //  dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));

                            Button positiveButton = ((AlertDialog) dialogs).getButton(DialogInterface.BUTTON_POSITIVE);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            //positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                            params.weight = 14.0f;

                            positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null,null,null);
                            positiveButton.setPadding(40,0,40,0);

                            //params.gravity = Gravity.CENTER_HORIZONTAL; //this is layout_gravity
                            positiveButton.setGravity(Gravity.CENTER);
                            positiveButton.setLayoutParams(params);
                            positiveButton.setBackgroundColor(Color.parseColor("#f39c12"));
                            positiveButton.setTextColor(Color.WHITE);
                            positiveButton.invalidate();


                            Button negativeButton = ((AlertDialog) dialogs).getButton(DialogInterface.BUTTON_NEGATIVE);
                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            // positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                            params1.weight = 14.0f;


                            negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null,null,null);
                            negativeButton.setPadding(40,0,40,0);
                            //params1.gravity= Gravity.CENTER;
                            negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
                            negativeButton.setLayoutParams(params1);
                            negativeButton.setBackgroundColor(Color.parseColor("#48367d"));
                            negativeButton.setTextColor(Color.WHITE);
                            negativeButton.invalidate();
                            layout.setPaddingRelative(20, 20, 20, 10);

                            dialogs.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Boolean wantToCloseDialog = false;
                                }
                            });
                            dialog.dismiss();
                        }


                    });
                }
            }

                    @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                //final TextView textView = null;
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#48366d"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);

                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                        //  textView.setText("DELETE");
                    } else {

                        p.setColor(Color.parseColor("#f39c12"));//D32F2F//f39c12
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                        //textView.setText("EDIT");
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    private void deleteTransaction(){

        for (Transaction item : transactionList) {
           /* user_id = item.getUser_id();
            item.setUser_id(user_id);*/
            session_id = item.getSession_id();
            item.setSession_id(session_id);
        };

        pd = new ProgressDialog(getContext());
        String titleId="Processing...";
        pd.setTitle(titleId);
        pd.setMessage("Canceling...");
        pd.setCancelable(false);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.setProgressStyle(android.R.attr.progressBarStyleLarge);


        // int secondsDelayed = 1;
        pd.show();
       // pd.getWindow().setLayout(245, 200);
        pd.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        // new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                pd.dismiss();
            }
        },  2000);


        TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
        Call<ResponseTransaction> call = transactionService.deleteuser_id(session_id);
        call.enqueue(new Callback<ResponseTransaction>() {

            @Override
            public void onResponse(Call<ResponseTransaction> call, Response<ResponseTransaction> response) {
                if (response.isSuccessful()) {
                    //  mProgress.dismiss();
                    ResponseTransaction responseTransaction = response.body();
                    if (responseTransaction.getSuccess().equals("true")) {
                        adapter.clear();
                        count      = adapter.grandItem();
                        txtSub_Qty.setText(String.valueOf(count));
                        int counts = Integer.parseInt(txtSub_Qty.getText().toString());
                        /*Search search = new Search();
                        search.updateContTextView(counts);*/
                        //    ((Welcome) getActivity()).updateContTextView(counts);

                                               mSubDiscRp = adapter.grandDiscRp();
                        txtSub_DiscRp.setText(String.valueOf("Rp. "+fmt.format(mSubDiscRp)).replaceAll("[Rp\\s]", ""));

                        mSubTotal = adapter.grandTotal();
                        subTotal.setText(String.valueOf("Rp. "+fmt.format(mSubTotal)).replaceAll("[Rp\\s]", ""));

                        mSubQty = adapter.grandQty();
                        subQty.setText(String.valueOf(mSubQty));

                        Toast.makeText(getContext(), responseTransaction.getMessage(), Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), (CharSequence) responseTransaction.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseTransaction> call, Throwable t) {
                //Toast.makeText(TransationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Server Disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        count = adapter.grandItem();
        txtSub_Qty.setText(String.valueOf(count));

     //   ((Welcome) getActivity()).updateContTextView(txtSub_Qty.getText().toString());

    }

   /* public void selfRefesh(){
        Transactiondetail fragment = new Transactiondetail();
        getFragmentManager().beginTransaction().replace(R.id.transactiondetail_layout, fragment).commit();
    }
*/
    public void filter(String newText) {
        newText = newText.toLowerCase();
        List<Books> newList = new ArrayList<>();
        for (Books item : booksList){
            String name = item.getItem_name().toLowerCase();
            if (name.contains(newText)){
                newList.add(item);
            }
        }
        adapter2.setFilter(newList);
    }


    public void onBackPressed() {
    }


    public void callParentMethod(){
        getActivity().onBackPressed();
    }



    public void transList() {
        for (int i = 0; i < transactionList.size(); i++) {
            Transaction item = new Transaction();
            item.setUser_id(Integer.parseInt(i + "user_id"));
            item.setItem_code(i + "item_code");
            item.setPrice(Integer.parseInt(i + "price"));
            item.setQty(Integer.parseInt(i+"qty"));
            item.setTotal_price(Integer.parseInt(i+"total_price"));
            transactionList.add(item);
        }
    }

}
