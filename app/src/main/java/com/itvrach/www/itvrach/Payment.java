package com.itvrach.www.itvrach;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bluejamesbond.text.DocumentView;
import com.itvrach.model.ResponseTransaction;
import com.itvrach.model.ResponseTransactionHdr;
import com.itvrach.model.Transaction_hdr;
import com.itvrach.services.APIClient;
import com.itvrach.services.TransactionService;
import com.itvrach.sessions.SessionManagement;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class Payment extends Fragment {
    private int transaction_id;
    private String session_id;
    private String transaction_date;
    private String shipping_options;
    private String payment_options;
    private String payment_detail_from;
    private String payment_detail_to;
    private String payment_datetime;
    private double shipping_charge;
    private double payment_total;

    private TextView tv_fullname;
    private TextView tv_total, tv_hp, tv_address;
    private TextView tv_payment_detail_from,tv_payment_detail_to, edPaymentTotal;

    private EditText tv_totalfinish;
    private EditText tv_charge;
    private EditText tv_weight;
    private EditText ed_payment_detail_from, ed_payment_detail_to;
    private EditText edPaymentDate;

    private String   sub_item   = null;
    private String   sub_disc = null;
    private String   subTotal=null;
    private String   item_weight=null;
    private float   sub_weight;


    private String edMessageFrom, tvMessageFrom, tvMessageTo, edMessageTo;
    private double edMessagePaymentTotal;


    private String shippingOptions;
    private String Time;
    private String user_id=null;
    private String fullname=null;
    private String hp=null;
    private String address =null;
    private int age;
    private double salary;
    private double shippingPrice =0;
    private double charge=0;
    private double itemWeight;
    private double total=0;
    private double totalFinish;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat simpleDateFormat;
    private int year;
    private int month;
    private int dayOfMonth;
    private int hour,minute, hourFinal, minuteFinal, yearFinal, monthFinal, dayFinal;

    private RadioGroup radioGroup, radioGroup2;
    private RadioButton posRadioButton, tikiRadioButton, otherRadioButton,
            bcaRadioButton, mandiriRadioButton, codRadioButton;
    private Context context;
    private LinearLayout ll_tvpayment_detail_to, ll_edpayment_detail_to;
    private Button buttonSubmit;
    private SessionManagement session;

    String tvMessageFromDefault ="Other Detail.";
    String tvMessageToDefault          ="Please payment/ transfer to";

    private ProgressDialog  pd;
    private Drawable iconX, iconY;
    private TextView txtSessionId;

    private TextView tv_Sub_Item;
    private TextView tv_Sub_DiscRp;



    public static Payment newInstance() {
        Payment fragment = new Payment();
        return fragment;
    }

    public Payment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle args = getArguments();

        sub_item = args.getString("KEY_SUBITEM");
        sub_disc   = args.getString("KEY_SUBDISC");
        item_weight = args.getString("KEY_WEIGHT");
        subTotal  = args.getString("KEY_SUBTOTAL");
        user_id  = args.getString("KEY_USERID");
        fullname = args.getString("KEY_FULLNAME");
        hp       = args.getString("KEY_HP");
        address  = args.getString("KEY_ADDRESS");

    }

   /* @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_payment, container, false);

        iconX = getContext().getResources().getDrawable(R.drawable.ic_check_white_m);
        iconY = getContext().getResources().getDrawable(R.drawable.ic_close_white_m);

        session = new SessionManagement(getContext());
        session.checkLogin();



        final Toolbar toolbar =(Toolbar) getActivity().findViewById(R.id.toolbar);
        //noinspection deprecation
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        buttonSubmit         = (Button) viewGroup.findViewById(R.id.buttonSubmit);



        tv_Sub_Item   = new TextView(getContext());
        tv_Sub_Item.setVisibility(View.GONE);
        tv_Sub_DiscRp = new TextView(getContext());
        tv_Sub_DiscRp.setVisibility(View.GONE);


        tv_total             = (TextView) viewGroup.findViewById(R.id.tv_total);
        tv_charge            = (EditText) viewGroup.findViewById(R.id.tv_charge);
        tv_fullname          = (TextView) viewGroup.findViewById(R.id.tv_fullname);
        tv_hp                = (TextView) viewGroup.findViewById(R.id.tv_hp);
        tv_totalfinish       = (EditText) viewGroup.findViewById(R.id.tv_totalfinish);
        tv_weight            = (EditText) viewGroup.findViewById(R.id.tv_weight);

        tv_weight.setEnabled(false);
        tv_weight.setBackgroundResource(R.drawable.underline);

        tv_totalfinish.setEnabled(false);
        tv_totalfinish.setBackgroundResource(R.drawable.underline);

        tv_charge.setEnabled(false);
        tv_charge.setBackgroundResource(R.drawable.underline);
        tv_charge.setTextColor(Color.WHITE);


        radioGroup            = (RadioGroup) viewGroup.findViewById(R.id.radioGroup);
        posRadioButton        = (RadioButton) radioGroup.findViewById(R.id.radio_POS);
        posRadioButton.setId(0);
        tikiRadioButton       = (RadioButton) radioGroup.findViewById(R.id.radio_TIKI);
        tikiRadioButton.setId(1);

        otherRadioButton = (RadioButton) radioGroup.findViewById(R.id.radio_Other);
        otherRadioButton.setId(2);

        //payment options
        radioGroup2              = (RadioGroup) viewGroup.findViewById(R.id.radioGroup2);
        mandiriRadioButton       = (RadioButton) viewGroup.findViewById(R.id.mandiriRadioButton);
        bcaRadioButton           = (RadioButton) viewGroup.findViewById(R.id.bcaRadioButton);
        codRadioButton           = (RadioButton) viewGroup.findViewById(R.id.codRadioButton);
        ed_payment_detail_from   = (EditText) viewGroup.findViewById(R.id.ed_payment_detail_from);
        ed_payment_detail_from.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
       // ed_payment_detail_from.setSingleLine(true);

        tv_payment_detail_from   = (TextView) viewGroup.findViewById(R.id.tv_payment_detail_from);
        ed_payment_detail_to     = (EditText) viewGroup.findViewById(R.id.ed_payment_detail_to);
        ed_payment_detail_to.setEnabled(false);
        ed_payment_detail_to.setBackgroundResource(R.drawable.underline);
        ed_payment_detail_to.setTextColor(Color.BLACK);
        tv_payment_detail_to   = (TextView) viewGroup.findViewById(R.id.tv_payment_detail_to);
        edPaymentTotal         = (TextView) viewGroup.findViewById(R.id.edPaymentTotal);

        calendar = Calendar.getInstance();

       // simpleDateFormat = new  SimpleDateFormat("yyyy-MM-dd");

        edPaymentDate = (EditText) viewGroup.findViewById(R.id.edPaymentDate);
        edPaymentDate.setSingleLine(true);
        edPaymentDate.setRawInputType(InputType.TYPE_CLASS_DATETIME|InputType.TYPE_DATETIME_VARIATION_DATE);
        edPaymentDate.setFocusable(false);

        edPaymentDate.setEnabled(true);
        String value = "";
        edPaymentDate.setText(value);
        final Drawable y = getResources().getDrawable(R.drawable.ic_calendar);//your x image, this one from standard android images looks pretty good actually
        y.setBounds(0, 0, y.getIntrinsicWidth(), y.getIntrinsicHeight());

        edPaymentDate.setCompoundDrawables(null, null, value.equals("") ? null : y, null);
        edPaymentDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_calendar, 0);
        edPaymentDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edPaymentDate.setFocusable(false);
                edPaymentDate.setEnabled(true);
                if (edPaymentDate.getCompoundDrawables()[2] == null) {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() > edPaymentDate.getWidth() - edPaymentDate.getPaddingRight() - y.getIntrinsicWidth()) {
                    calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    hour = calendar.get(Calendar.HOUR_OF_DAY);
                    minute = calendar.get(Calendar.MINUTE);

                    datePickerDialog = new DatePickerDialog(getContext(), R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    timePickerDialog =  new TimePickerDialog(getContext(), R.style.TimePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                            calendar.set(Calendar.MINUTE, minute);
                                            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            final String c = simpleDateFormat.format(calendar.getTime());
                                            edPaymentDate.setText(c);

                                        }
                                    }, hour, minute, false);
                                    timePickerDialog.show();
                                    timePickerDialog.getWindow().getDecorView().setPaddingRelative(0, 0, 0, 0);
                                    timePickerDialog.getWindow().setLayout(450, 730);
                                }
                            }, 100);
                            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                            final String c = simpleDateFormat.format(calendar.getTime());
                            edPaymentDate.setText(c);
                        }

                    }, year, month, dayOfMonth);
                    datePickerDialog.show();
                    datePickerDialog.getWindow().getDecorView().setPaddingRelative(0, 0, 0, 0);
                    //  datePickerDialog.getWindow().setLayout(525,900);
                    datePickerDialog.getWindow().setLayout(518, 930);
                }
                return false;
            }
        });


        if(!sub_disc.equals(null)){
            tv_Sub_DiscRp.setText(sub_disc);
        }

        if(!sub_item.equals(null)){
            tv_Sub_Item.setText(sub_item);
        }


        if(!subTotal.equals(null)) {
            tv_total.setText(subTotal);
        }

        if(!item_weight.equals(null)){
            tv_weight.setText(item_weight+ " Kg");
        }

        if(!fullname.equals(null)){
            tv_fullname.setText(fullname);
        }

        if(!hp.equals(null)){
            tv_hp.setText(hp);
        }

        DocumentView documentView = (DocumentView) viewGroup.findViewById(R.id.tv_address);
       // documentView.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);


        if(!address.equals(null)){
            documentView.setText(address);
        }

        Locale local = new Locale("id", "id");
        final NumberFormat fmt2 = NumberFormat.getCurrencyInstance(local);


        total  = Double.parseDouble(tv_total.getText().toString().replaceAll("[^0-9]", ""));
        tv_charge.setText(String.valueOf("Rp. " + fmt2.format(charge).replaceAll("[Rp\\s]", "")));

        totalFinish = total + charge;



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {


                // radioGroup.getCheckedRadioButtonId();

                   // int radio = radioGroup.getCheckedRadioButtonId();
                if(radioGroup.getChildAt(0).isPressed()) {

                    shippingOptions = "POS Indonesia";
                    itemWeight = Double.parseDouble(tv_weight.getText().toString().replaceAll("[^0-9]", ""));
                    shippingPrice = (22000 * itemWeight) / 100;

                    radioGroup2.getChildAt(0).setEnabled(true);
                    radioGroup2.getChildAt(1).setEnabled(true);
                    radioGroup2.getChildAt(2).setEnabled(false);
                    radioGroup2.clearCheck();

                    tv_payment_detail_from.setText(tvMessageFromDefault);
                    tv_payment_detail_to.setText(tvMessageToDefault);

                    ed_payment_detail_from.setText("");
                    ed_payment_detail_to.setText("");





                }else if (radioGroup.getChildAt(1).isPressed()) {
                    shippingOptions = "TIKI";
                    itemWeight = Double.parseDouble(tv_weight.getText().toString().replaceAll("[^0-9]", ""));
                    shippingPrice = (23000 * itemWeight) / 100;

                    radioGroup2.getChildAt(0).setEnabled(true);
                    radioGroup2.getChildAt(1).setEnabled(true);
                    radioGroup2.getChildAt(2).setEnabled(false);
                    radioGroup2.clearCheck();

                    tv_payment_detail_from.setText(tvMessageFromDefault);
                    tv_payment_detail_to.setText(tvMessageToDefault);

                    ed_payment_detail_from.setText("");
                    ed_payment_detail_to.setText("");




                }else if (radioGroup.getChildAt(2).isPressed()) {
                    shippingOptions = "Other Expedition";
                    itemWeight = Double.parseDouble(tv_weight.getText().toString().replaceAll("[^0-9]", ""));
                    shippingPrice = (21000 * itemWeight) / 100;

                    radioGroup2.getChildAt(0).setEnabled(false);
                    radioGroup2.getChildAt(1).setEnabled(false);
                    radioGroup2.getChildAt(2).setEnabled(true);
                    radioGroup2.clearCheck();

                    tv_payment_detail_from.setText(tvMessageFromDefault);
                    tv_payment_detail_to.setText(tvMessageToDefault);

                    ed_payment_detail_from.setText("");
                    ed_payment_detail_to.setText("");


                }


                tv_charge.setText(String.valueOf("Rp. " + fmt2.format(shippingPrice).replaceAll("[Rp\\s]", "")));
                charge = Double.parseDouble(tv_charge.getText().toString().replaceAll("[^0-9]", ""));

                totalFinish = total + charge;
                tv_totalfinish.setText(String.valueOf("Rp. " + fmt2.format(totalFinish).replaceAll("[Rp\\s]", "")));

            }
        });


        tv_totalfinish.setText(String.valueOf("Rp. " + fmt2.format(totalFinish).replaceAll("[Rp\\s]", "")));
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup2, int checkedId) {


                if (radioGroup2.getChildAt(0).isPressed()) {


                    tvMessageFrom = "Mandiri Rekening :";
                    edMessageFrom = "";
                    tvMessageTo   = "Please paying/ transfer to :";
                    edMessageTo   = "0418727125 [Bedson Gultom] ";

                    tv_payment_detail_from.setText(tvMessageFrom);
                    tv_payment_detail_to.setText(tvMessageTo);

                    edMessagePaymentTotal =  Double.parseDouble(tv_totalfinish.getText().toString().replaceAll("[^0-9]", ""));
                    ed_payment_detail_from.requestFocus();
                    ed_payment_detail_from.setEnabled(true);
                   // ed_payment_detail_from.setText(edMessageTo);


                } else if (radioGroup2.getChildAt(1).isPressed()) {
                    tvMessageFrom = "Bca Rekening :";
                    edMessageFrom = "";
                    tvMessageTo   = "Please paying/ transfer to :";
                    edMessageTo   = "0342306452 [Bedson Gultom]";


                    tv_payment_detail_from.setText(tvMessageFrom);
                    tv_payment_detail_to.setText(tvMessageTo);

                    edMessagePaymentTotal =  Double.parseDouble(tv_totalfinish.getText().toString().replaceAll("[^0-9]", ""));
                    ed_payment_detail_from.requestFocus();
                    ed_payment_detail_from.setEnabled(true);
                    ed_payment_detail_from.setText(edMessageTo);
                    // ed_payment_detail_from.setBackgroundResource(R.drawable.underline);


                } else if (radioGroup2.getChildAt(2).isPressed()) {
                    tvMessageFrom = "Payment Detail from :";
                    edMessageFrom =fullname;
                    tvMessageTo   = "Please paying/ transfer to : ";
                    edMessageTo   = "Our Courier ";
                    tv_payment_detail_from.setText(tvMessageFrom);
                    tv_payment_detail_to.setText(tvMessageTo);

                    ed_payment_detail_from.setText(edMessageFrom);
                    ed_payment_detail_from.setEnabled(false);
                    ed_payment_detail_from.setBackgroundResource(R.drawable.underline);
                    edMessagePaymentTotal =  Double.parseDouble(tv_totalfinish.getText().toString().replaceAll("[^0-9]", ""));


                }



               // ed_payment_detail_from.setText(edMessageFrom, TextView.BufferType.SPANNABLE);

                ed_payment_detail_to.setText(edMessageTo);
                edPaymentTotal.setText(String.valueOf("Rp. " + fmt2.format(edMessagePaymentTotal).replaceAll("[Rp\\s]", "")));


            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().getThemedContext();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Payment");

        TextView tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        tvTitle.setTextSize(14);
        tvTitle.setText(R.string.payment_title);
        tvTitle.setVisibility(View.VISIBLE);
        ImageView imgFile = (ImageView) getActivity().findViewById(R.id.imgFile);
        imgFile.setVisibility(View.GONE);




        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction ft;
                ft = getFragmentManager().beginTransaction();
                Search fragment = new Search();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();


               // ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            }
        });


        HashMap<String, String> user = session.getUserDetails();
        String sessionid = user.get(SessionManagement.KEY_SESSIONID);
        final String userid = user.get(SessionManagement.KEY_USERID);
        String emailid = user.get(SessionManagement.KEY_EMAIL);
        final String fullname = user.get(SessionManagement.KEY_FULLNAME);
        final String hp = user.get(SessionManagement.KEY_HP);
        final String address = user.get(SessionManagement.KEY_ADDRESS);




        txtSessionId = new TextView(getContext());
        txtSessionId.setText(sessionid);
        txtSessionId.setVisibility(View.GONE);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPayment();
            }
        });


        return viewGroup;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static boolean checkDate(String dateofbirth){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setLenient(false);
        try {
            simpleDateFormat.parse(dateofbirth.trim());
        }catch (ParseException pe){
            return false;
        }
        return true;
    }


    private  void doPayment() {


       /* AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(40, 30, 30, 30);

        TextView title = new TextView(getContext());
        title.setBackgroundColor(Color.parseColor("#48367d"));
        title.setText("PAYMENT ");
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setPadding(40, 30, 30, 30);
        builder.setCustomTitle(title);
*/
        // builder.setCustomTitle(title);
  /*      builder.setMessage(" Are you sure  doing this transaction ?");
        builder.setCancelable(false);
        builder.setPositiveButton("SUBMIT",
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
*/
  /*      final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getWindow().getDecorView().setPaddingRelative(8, 32, 8, 24);

        Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 14.0f;

        positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null, null, null);
        positiveButton.setPadding(40, 0, 40, 0);

        positiveButton.setGravity(Gravity.CENTER);
        positiveButton.setLayoutParams(params);
        positiveButton.setBackgroundColor(Color.parseColor("#F39C12"));
        positiveButton.setTextColor(Color.WHITE);
        positiveButton.invalidate();


        Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.weight = 14.0f;
        negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
        negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null, null, null);
        negativeButton.setPadding(40, 0, 40, 0);
        negativeButton.setLayoutParams(params1);
        negativeButton.setBackgroundColor(Color.parseColor("#48367d"));
        negativeButton.setTextColor(Color.WHITE);
        negativeButton.invalidate();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
*/
        Boolean wantToCloseDialog = false;

        session_id = txtSessionId.getText().toString();
        if (posRadioButton.isChecked()) {
            shipping_options = posRadioButton.getText().toString();
        } else if (tikiRadioButton.isChecked()) {
            shipping_options = tikiRadioButton.getText().toString();
        } else if (otherRadioButton.isChecked()) {
            shipping_options = otherRadioButton.getText().toString();
        }

        if (mandiriRadioButton.isChecked()) {
            payment_options = mandiriRadioButton.getText().toString();

        } else if (bcaRadioButton.isChecked()) {
            payment_options = bcaRadioButton.getText().toString();
        } else if (codRadioButton.isChecked()) {
            payment_options = codRadioButton.getText().toString();
        }



        sub_disc = tv_Sub_DiscRp.getText().toString().replaceAll("[^0-9]", "");
        sub_item   = tv_Sub_Item.getText().toString().replaceAll("[^0-9]", "");








        sub_weight = Float.parseFloat(tv_weight.getText().toString().replaceAll("[^0-9]", ""))/100;



        payment_detail_from = ed_payment_detail_from.getText().toString();
        payment_detail_to = ed_payment_detail_to.getText().toString();
        payment_datetime = edPaymentDate.getText().toString();
        shipping_charge = Double.parseDouble(tv_charge.getText().toString().replaceAll("[^0-9]", ""));
        payment_total = Double.parseDouble(edPaymentTotal.getText().toString().replaceAll("[^0-9]", ""));


        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Shipping  is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (radioGroup2.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Payment  is required", Toast.LENGTH_SHORT).show();
            return;

        }


        if (payment_detail_from == null || payment_detail_from.trim().length() == 0) {
            Toast.makeText(getContext(), "Please Fill this options", Toast.LENGTH_SHORT).show();
            ed_payment_detail_from.requestFocus();
            return;
        }


        if (payment_detail_to == null || payment_detail_to.trim().length() == 0) {
            Toast.makeText(getContext(), "Payment details is required", Toast.LENGTH_SHORT).show();
            ed_payment_detail_to.requestFocus();
            return;

        }

        if (payment_datetime == null || payment_datetime.trim().length() == 0) {
            Toast.makeText(getContext(), "Payment Date is required", Toast.LENGTH_SHORT).show();
            edPaymentDate.requestFocus();
            return;
        }
        if (!checkDate(payment_datetime)) {
            Toast.makeText(getContext(), "InValid Date Format", Toast.LENGTH_SHORT).show();
            edPaymentDate.requestFocus();
            return;
        } else {

            pd =new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
            String titleId = "Processing...";
            pd.setTitle(titleId);
            pd.setMessage("Submitted...");
            pd.setCancelable(false);
            //pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pd.setIndeterminate(true);

            pd.getWindow().setGravity(Gravity.CENTER);
            //pd.setProgressStyle(android.R.attr.progressBarStyleLarge);
            pd.show();

            final Transaction_hdr transaction_hdr = new Transaction_hdr();
            transaction_hdr.setSession_id(session_id);
            transaction_hdr.setSub_item(Integer.parseInt(sub_item));
            transaction_hdr.setSub_disc(Double.parseDouble(sub_disc));
            transaction_hdr.setSub_weight(sub_weight);
            transaction_hdr.setPayment_options(payment_options);
            transaction_hdr.setShipping_options(shipping_options);
            transaction_hdr.setPayment_detail_from(payment_detail_from);
            transaction_hdr.setPayment_detail_to(payment_detail_to);
            transaction_hdr.setPayment_datetime(payment_datetime);
            transaction_hdr.setShipping_charge(shipping_charge);
            transaction_hdr.setPayment_total(payment_total);


            TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
            Call<ResponseTransactionHdr> call = transactionService.update(session_id, transaction_hdr);
            call.enqueue(new Callback<ResponseTransactionHdr>() {
                @Override
                public void onResponse(Call<ResponseTransactionHdr> call, Response<ResponseTransactionHdr> response) {
                    if (response.isSuccessful()) {
                        final ResponseTransactionHdr responseTransactionHdr = response.body();
                        if (responseTransactionHdr.getSuccess().equals("true")) {

                            Toast.makeText(getContext(), responseTransactionHdr.getMessage(), Toast.LENGTH_SHORT).show();
                            deleteTemporary();
                            Search fragment = new Search();
                            FragmentTransaction ft;
                            ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_container, fragment);
                            ft.addToBackStack(null);
                            ft.commit();
                            pd.dismiss();

                        } else {
                            Toast.makeText(getContext(), responseTransactionHdr.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseTransactionHdr> call, Throwable t) {
                    Toast.makeText(getContext(), "Server Disconnect", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }
    }





    public void deleteTemporary(){
        TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
        Call<ResponseTransaction> call = transactionService.delete_temp(session_id);
        call.enqueue(new Callback<ResponseTransaction>() {

            @Override
            public void onResponse(Call<ResponseTransaction> call, Response<ResponseTransaction> response) {
                if (response.isSuccessful()) {
                    //  mProgress.dismiss();
                    ResponseTransaction responseTransaction = response.body();
                    if (responseTransaction.getSuccess().equals("true")) {

                    } else {

                    }
                } else {
                    Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseTransaction> call, Throwable t) {

            }
        });
    }


   /* public void onAttach(Activity activity){
        super.onAttach(activity);
    }
*/
   /* @Override

    public void onResume(){
        super.onResume();

        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("PAYMENT");
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    } */


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu, menu);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            /*case R.id.action_qrcode:

                return true;
*/
            case R.id.action_search:
                return true;

            case R.id.action_cart:
                return true;

            case R.id.menu_settings:
                return false;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    public void onBackPressed() {
    }

    public void callParentMethod(){
        getActivity().onBackPressed();
    }


}
