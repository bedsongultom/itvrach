package com.itvrach.www.itvrach;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.GestureDetector;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.itvrach.adapter.BooksAdapter;
import com.itvrach.adapter.CostumGridUpdater;
import com.itvrach.adapter.TransactionAdapter;
import com.itvrach.model.Books;
import com.itvrach.model.ResponseBook;
import com.itvrach.model.ResponseTransaction;
import com.itvrach.model.Transaction;
import com.itvrach.interfaces.OnGetEditText;
import com.itvrach.services.APIClient;
import com.itvrach.services.BookService;
import com.itvrach.services.TransactionService;
import com.itvrach.sessions.SessionManagement;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.graphics.Color.parseColor;
import static com.itvrach.services.APIClient.URI_SEGMENT_UPLOAD;


/**
 * Created by engineer on 1/23/2019.
 */

public class Search extends Fragment implements View.OnClickListener{

    private View view;
    private RecyclerView recyclerViewTransaction, mrecylerID;
    private RecyclerView.LayoutManager mManagerTansaction, mManager2;
    private List<Transaction> transactionList2 = new ArrayList<>();
    private TransactionAdapter transactionAdapter;
    private ArrayList<Transaction> lista;

    private List<Books> booksList = new ArrayList<>();
    private BooksAdapter booksAdapter;
    private ViewTooltip tooltipView = null;
    private Paint p = new Paint();
    private ImageButton btnProducts;
    private EditText editTextSearch;
    private int mSubTotal = 0, mSubQty = 0, mSubDiscRp = 0, user_idx, year, month, dayOfMonth,
            selectedId, no = 0;

    private ImageView imgEmpty;
    private TextView tvEmpty;

    private float mSubWeight = 0;

    private TextView txtPrice, txtNo, txtSub_Item, txtSub_Weight, subTotal, subQty, txtUser_id, txtsession_id,
            txtItem_code, txtPrice1, txtQty, txtTotal_price, TextUser_id, TextEmail, textViewEmail,
            txtSub_DiscRp, txt_userid;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat;
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "MyPrefsFile";
    private ProgressDialog DelProgress, lProgress, pd, pdProcess;
    private Button btnCancel, btnProcess;
    private Locale local;
    private NumberFormat fmt;

    private TextView tvTitle;
    private ImageView imgFile;


    RecyclerView recyclerView;
    CostumGridUpdater adapter;
    private List<Books> listContentArr = new ArrayList<>();
    private RecyclerView.LayoutManager mManager;
    private DialogInterface dialog;
    private Toolbar toolbar;
    private OnGetEditText mListener;
    private List<Transaction> transactionList;//= new ArrayList<>();


    private TextView tv_item_code, tv_price, txt_sessionid, Vcount, txtCount;
    private EditText tv_total_price, tv_total_pricex, tv_qty, tv_user_id, edSearch, searchEdittext;
    private int amnt, amntp, sub, amntd, total_pricex, count = 0, mCartItemCount = 10;
    private String total_p, price, disc, subs, item_code, total_price, session_id, sfile, user_id;
    private static String qty;
    private ImageView imgSearch2, imgBack;
    private Button btn_Add;
    private Context context, mContext;
    private TextWatcher tw;
    private SessionManagement session;
    private ImageView imgCarts;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragmentManager fm2;
    private FragmentTransaction ft2;
    private Drawable iconY, iconX;
    private MenuItem searchItem;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;


    private LinearLayout itemLayout, transactionDetailLayout, transactionLayout;



    private View myView;
    private boolean isUp;
    private TextView tv_disconnected;

    private TextView tvScanFormat, tvScanContent, tvResult;
    private String codeFormat, codeContent;
    private FrameLayout cameraLayout;
    private CompoundBarcodeView compoundBarcodeView;
    private String toast;
    private Bitmap bitmap = null;
    private ImageView outputImage;
    private CameraManager cameraManager;
    private FloatingActionButton fabCapture;
    private BarcodeView barcodeView;
    private IntentIntegrator integrator;
    private TextView tvCloseWindow;
    private CameraPreview cameraPreview;
    private Button btnScan, btnStop;
    private BeepManager beepManager;
    private String lastText;
    private SurfaceView cameraView;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    private int REQUEST_CODE=100;
    private String FRAGMENT_TAG = Search.class.getCanonicalName();
    private ImageView imgConnection;
    private TextView tvConnection;
    private LinearLayout connectionLayout;


  /*  private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            barcodeView.setStatusText(result.getText());

            beepManager.playBeepSoundAndVibrate();

            //Added preview of scanned barcode
           // ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            //imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

*/

    public static Search newInstance() {
        Search fragment = new Search();
        return fragment;
    }

    /*public static Search newInstance() {
        return new Search();
    }
*/

    public Search() {
        // Required empty public constructor
    }


   /* @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        displayToast();
       // barcodeView.decodeSingle(callback);

    }
*/

   /* private boolean isPortrait(){
        if(getView().getWidth() < getView().getHeight())
            return true;
        else
            return false;

    }
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view     = inflater.inflate(R.layout.search, container, false);


        imgConnection = (ImageView) view.findViewById(R.id.imgConnection);
        connectionLayout = (LinearLayout) view.findViewById(R.id.connectionLayout);

       // connectionLayout.setVisibility(View.GONE);



      /*  tvConnection  = (TextView) view.findViewById(R.id.tvConnection);
        imgConnection.setVisibility(View.GONE);
        tvConnection.setVisibility(View.GONE);
*/
        cameraLayout  = (FrameLayout) view.findViewById(R.id.cameraLayout);
        cameraPreview = (CameraPreview) view.findViewById(R.id.cameraPreview);
        tvResult      = (TextView) view.findViewById(R.id.tvResult);
     //   tvCloseWindow = (TextView) view.findViewById(R.id.tvCloseWindow);
        outputImage   = (ImageView) view.findViewById(R.id.imageView);
        btnScan       =  (Button)   view.findViewById(R.id.btnScan);
        btnStop       = (Button) view.findViewById(R.id.btnStop);

        btnScan.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        final View myView = getActivity().findViewById(R.id.my_view);
        TextView tv_disconnected = (TextView) getActivity().findViewById(R.id.tv_disconnected);
        imgEmpty = (ImageView) view.findViewById(R.id.imgEmpty);
        imgEmpty.setVisibility(View.GONE);

        tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
        tvEmpty.setVisibility(View.GONE);


        cameraLayout.setVisibility(View.GONE);


        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);


        tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.GONE);
        imgFile = (ImageView) getActivity().findViewById(R.id.imgFile);
        imgFile.setVisibility(View.GONE);


        local = new Locale("id", "id");
        fmt = NumberFormat.getCurrencyInstance(local);
        itemLayout = (LinearLayout) view.findViewById(R.id.itemLayout);
        itemLayout.setVisibility(View.VISIBLE);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);

        recyclerViewTransaction = (RecyclerView) view.findViewById(R.id.recyclerView2);
        transactionDetailLayout = (LinearLayout) view.findViewById(R.id.transactionDetailLayout);
        transactionDetailLayout.setVisibility(View.GONE);
        recyclerViewTransaction.setVisibility(View.GONE);


        iconY = getContext().getResources().getDrawable(R.drawable.ic_close_white_m);
        iconX = getContext().getResources().getDrawable(R.drawable.ic_check_white_m);

        session = new SessionManagement(getContext());
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();
        final String userid = user.get(SessionManagement.KEY_USERID);
        final String username = user.get(SessionManagement.KEY_USERNAME);
        final String type = user.get(SessionManagement.KEY_TYPE);
        final String firstname = user.get(SessionManagement.KEY_FIRSTNAME);
        final String lastname = user.get(SessionManagement.KEY_LASTNAME);
        final String fullname = user.get(SessionManagement.KEY_FULLNAME);
        final String emailid = user.get(SessionManagement.KEY_EMAIL);
        final String sessionid = user.get(SessionManagement.KEY_SESSIONID);
        final String fileImg = user.get(SessionManagement.KEY_FILE);
        final String hp = user.get(SessionManagement.KEY_HP);
        final String address = user.get(SessionManagement.KEY_ADDRESS);


        adapter = new CostumGridUpdater(listContentArr, getContext());
        mManager = new GridLayoutManager(context, 3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


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


        final String user_id = txt_userid.getText().toString();
        String session_id    = txtsession_id.getText().toString();
        final String email   = textViewEmail.getText().toString();


        mManagerTansaction = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewTransaction.setHasFixedSize(true);
        recyclerViewTransaction.setLayoutManager(mManagerTansaction);

        transactionAdapter = new TransactionAdapter(transactionList2, getContext());
        recyclerViewTransaction.setAdapter(transactionAdapter);
        recyclerViewTransaction.setVisibility(View.GONE);

        initSwipe();

        subTotal = (TextView) view.findViewById(R.id.txtTotal);
        txtNo = (TextView) view.findViewById(R.id.txtNo);
        txtSub_Item = (TextView) view.findViewById(R.id.txtSub_Item);
        txtSub_Weight = (TextView) view.findViewById(R.id.txtSub_weight);


        txtSub_DiscRp = (TextView) view.findViewById(R.id.txtSub_DiscRp);
        subQty = (TextView) view.findViewById(R.id.txtSub_Qty);
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
                pd = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
                pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                pd.setCancelable(false);
                pd.setIndeterminate(true);

                pd.setProgressStyle(android.R.attr.progressBarStyle);
                pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                pd.show();
                pd.getWindow().setLayout(245, 200);
                pd.getWindow().setGravity(Gravity.CENTER);


                // new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        Payment payment = new Payment();
                        Bundle args = new Bundle();

                        args.putString("KEY_SUBITEM", txtSub_Item.getText().toString());
                        args.putString("KEY_SUBDISC", txtSub_DiscRp.getText().toString());
                        args.putString("KEY_SUBTOTAL", subTotal.getText().toString());
                        args.putString("KEY_WEIGHT", txtSub_Weight.getText().toString());
                        args.putString("KEY_USERID", userid);
                        args.putString("KEY_FULLNAME", fullname);
                        args.putString("KEY_HP", hp);
                        args.putString("KEY_ADDRESS", address);
                        payment.setArguments(args);

                        FragmentTransaction ft;
                        ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container, payment);
                        // ft.addToBackStack(null);
                        ft.commit();
                    }
                }, 2000);

            }
        });




        /*getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                transactionAdapter.notifyDataSetChanged();
            }
        });*/

        final TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
        Call<ResponseTransaction> call2 = transactionService.getFindAll(session_id);
        call2.enqueue(new Callback<ResponseTransaction>() {
            @Override
            public void onResponse(Call<ResponseTransaction> call, Response<ResponseTransaction> response) {
                if (response.isSuccessful()) {
                    ResponseTransaction responseTransaction = response.body();
                    if (responseTransaction.getSuccess().equals("true")) {
                        transactionList2 = response.body().getResult();

                        if (transactionList2.size() != 0) {
                            transactionAdapter = new TransactionAdapter(transactionList2, getContext());
                            recyclerViewTransaction.setAdapter(transactionAdapter);
                            transactionAdapter.notifyDataSetChanged();

                            count = transactionAdapter.grandItem();
                            txtSub_Item.setText(String.valueOf(count));


                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                            mSubWeight = transactionAdapter.grandWeight();
                            String s = decimalFormat.format(mSubWeight);


                            txtSub_Weight.setText(s);


                            mSubDiscRp = transactionAdapter.grandDiscRp();
                            txtSub_DiscRp.setText(String.valueOf("Rp. " + fmt.format(mSubDiscRp).replaceAll("[Rp\\s]", "")));

                            count = transactionAdapter.grandItem();
                            txtSub_Item.setText(String.valueOf(count));





                            int counts = 0;
                            counts = Integer.parseInt(txtSub_Item.getText().toString());
                            ((Welcome) getActivity()).updateContTextView(counts);

                            transactionAdapter.notifyDataSetChanged();
                            mSubTotal = transactionAdapter.grandTotal();

                            // txtPrice.setText(String.valueOf("@ Rp."+ fmt.format(item.getPrice()).replaceAll("[Rp\\s]", "")));

                            subTotal.setText(String.valueOf("Rp. " + fmt.format(mSubTotal).replaceAll("[Rp\\s]", "")));

                            mSubQty = transactionAdapter.grandQty();
                            subQty.setText(String.valueOf(mSubQty));
                        }
                    }

                } else {
                    recyclerViewTransaction.setVisibility(View.GONE);
                    imgEmpty.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<ResponseTransaction> call, Throwable t) {

                Log.d("Search"," say : no Internet connection" );
                connectionLayout.setVisibility(View.VISIBLE);
                recyclerViewTransaction.setVisibility(View.GONE);

               /* imgConnection.setVisibility(View.VISIBLE);
                tvConnection.setVisibility(View.VISIBLE);
*/

                /*((Welcome) getActivity()).slideUp(myView);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ((Welcome) getActivity()).slideDown(myView);
                    }
                }, 3000);*/
            }
        });


        final EditText tv_sessionid = new EditText(getContext());
        final EditText tv_total_price = new EditText(getContext());
        final EditText tv_total_pricex = new EditText(getContext());
        tv_total_pricex.setTextSize(14);

        tv_total_pricex.setEnabled(false);
        tv_total_pricex.setBackgroundResource(R.drawable.underline);

        final ProgressDialog pdrcylerview;
        pdrcylerview = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        pdrcylerview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdrcylerview.setCancelable(false);
        pdrcylerview.setIndeterminate(true);
        pdrcylerview.getWindow().setGravity(Gravity.CENTER);
        // pdrcylerview.setProgressStyle(android.R.attr.progressBarStyle);
        pdrcylerview.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        int secondsDelayed = 1;
        pdrcylerview.show();
        pdrcylerview.getWindow().setLayout(245, 200);
        pdrcylerview.getWindow().setGravity(Gravity.CENTER);
        // new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                pdrcylerview.dismiss();
            }
        }, 2000);


        BookService bookService = APIClient.getClient().create(BookService.class);
        Call<ResponseBook> call = bookService.getFindAll();
        call.enqueue(new Callback<ResponseBook>() {
            @Override
            public void onResponse(Call<ResponseBook> call, Response<ResponseBook> response) {
                if (response.isSuccessful()) {
                    ResponseBook responseBook = response.body();
                    if (responseBook.getSuccess().equals("true")) {
                        listContentArr = response.body().getResult();
                        if (listContentArr.size() != 0) {
                            adapter = new CostumGridUpdater(listContentArr, getContext());
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            pdrcylerview.dismiss();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "DATA IS EMPTY", Toast.LENGTH_SHORT).show();
                  /*  imgConnection.setVisibility(View.GONE);
                    tvConnection.setVisibility(View.GONE);*/
                  //  connectionLayout.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ResponseBook> call, Throwable t) {
                /*((Welcome) getActivity()).slideUp(myView);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ((Welcome) getActivity()).slideDown(myView);
                    }
                }, 3000);*/
                pdrcylerview.dismiss();
                Log.d("Search"," say : no Internet connection" );
                connectionLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

               /* imgConnection.setVisibility(View.VISIBLE);
                tvConnection.setVisibility(View.VISIBLE);


                recyclerView.setVisibility(View.GONE);*/
            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context,
                recyclerView, new ClickListener() {


            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(final View view, int position) {

                final ProgressDialog pd = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
                pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                pd.getWindow().setGravity(Gravity.CENTER);
                pd.setProgressStyle(android.R.attr.progressBarStyleLarge);

                pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                final Books item = listContentArr.get(position);
                adapter.editItem(position);
                adapter.notifyItemChanged(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                final LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CENTER_HORIZONTAL);
                layout.setPadding(40, 30, 40, 30);

                TextView title = new TextView(getContext());
                title.setBackgroundColor(parseColor("#48367d"));
                title.setText("ADD TO CART");
                title.setTextColor(Color.WHITE);
                title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                title.setPadding(45, 30, 30, 30);

                final ScrollView scrollView = new ScrollView(getContext());

                final ImageView image_file = new ImageView(getContext());
                image_file.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Picasso.with(view.getContext()).load(URI_SEGMENT_UPLOAD + item.getFile()).into(image_file);
                layout.addView(image_file);

                final RatingBar ratingBar = new RatingBar(getContext(), null, R.attr.ratingBarStyleSmall);
                LinearLayout.LayoutParams LnRating = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                ratingBar.setLayoutParams(LnRating);
                ratingBar.setNumStars(5);
                ratingBar.setStepSize(0.1f);
                ratingBar.setIsIndicator(false);
                ratingBar.setProgressBackgroundTintList(ColorStateList.valueOf(parseColor("#f39c12")));
                ratingBar.setProgressTintList(ColorStateList.valueOf(parseColor("#48367d")));
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                        String rateValue = String.valueOf(ratingBar.getRating());
                        Toast.makeText(getContext(), "Rate is" + rateValue, Toast.LENGTH_SHORT).show();
                    }
                });


                layout.addView(ratingBar);


                TextView textDescription = new TextView(getContext());
                LinearLayout.LayoutParams lDescription = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textDescription.setLayoutParams(lDescription);
                textDescription.setText("Description");
                textDescription.setTextColor(Color.GRAY);
                layout.addView(textDescription);

                // Create span
                //      String span =(item.getDescription());
               /* span.setSpan(new ForegroundColorSpan(Color.BLUE),
                        0, span.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
*/


                //  DocumentView documentView = new DocumentView(getContext(), DocumentView.FORMATTED_TEXT);
              /*  documentView.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
                documentView.getDocumentLayoutParams().setInsetPaddingRight(10);
                documentView.getDocumentLayoutParams().setInsetPaddingLeft(10);
                documentView.getDocumentLayoutParams().setInsetPaddingBottom(5);
                documentView.getDocumentLayoutParams().setInsetPaddingTop(5);
                documentView.getDocumentLayoutParams().setAntialias(true);
                documentView.getDocumentLayoutParams().setTextSize(14);
                documentView.setText(span); // Set to `true` to enable justification

                LinearLayout.LayoutParams lexpTv1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lexpTv1.setMargins(5,5,5,5);
                ReadMoreTextView readMoreTextView = new ReadMoreTextView (getContext());
                LinearLayout lnrm = new LinearLayout(getContext());
                lnrm.setBackgroundResource(R.drawable.edittext_border);
                lnrm.setBackgroundColor(parseColor("#7D48367D"));
                readMoreTextView.setTrimExpandedText("\nShow Less");
                readMoreTextView.setPadding(10,20,10,20);

                readMoreTextView.setTrimCollapsedText("\nShow More");
                readMoreTextView.setTrimMode(1);
                readMoreTextView.setTrimLength(0);
                readMoreTextView.setLayoutParams(lexpTv1);
                readMoreTextView.setColorClickableText(parseColor("#48367d"));
              //  String ss = documentView.getText().toString();
                readMoreTextView.setText(ss);
                lnrm.addView(readMoreTextView);
                layout.addView(lnrm);

*/


                //      String span =("ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss"
                //            +"sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss"
                //          +"sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
               /* span.setSpan(new ForegroundColorSpan(Color.BLUE),
                        0, span.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
*/

                String span = (item.getDescription());
                //span.setSpan(new JustifiedSpan(), 0,span.length(), span.SPAN_INCLUSIVE_EXCLUSIVE);


                DocumentView documentView = new DocumentView(getContext(), DocumentView.FORMATTED_TEXT);
                documentView.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
                documentView.setText(span);

                ReadMoreTextView readMoreTextView = new ReadMoreTextView(getContext());
                LinearLayout lnrm = new LinearLayout(getContext());
                lnrm.setBackgroundResource(R.drawable.edittext_border);
                LinearLayout.LayoutParams lexpTv1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                lnrm.setBackgroundColor(parseColor("#7D48367D"));

                readMoreTextView.setTrimExpandedText("\nShow Less");
                readMoreTextView.setPadding(10, 20, 10, 20);
                readMoreTextView.setTrimCollapsedText("\nShow More");

                readMoreTextView.setTrimMode(1);
                readMoreTextView.setTrimLength(1);
                readMoreTextView.setLayoutParams(lexpTv1);

                readMoreTextView.setColorClickableText(Color.parseColor("#48367d"));
                String ss = documentView.getText().toString();
                readMoreTextView.setText(ss);
                lnrm.addView(readMoreTextView);
                layout.addView(lnrm);

                //visible view of user_id
                TextView user_idtxt = new TextView(getContext());
                user_idtxt.setText("User ID");
                user_idtxt.setTextColor(Color.GRAY);
                layout.addView(user_idtxt);
                user_idtxt.setVisibility(View.GONE);


                // get user data from session
                HashMap<String, String> user = session.getUserDetails();
                String sessionid = user.get(SessionManagement.KEY_SESSIONID);
                String userid = user.get(SessionManagement.KEY_USERID);
                String email = user.get(SessionManagement.KEY_EMAIL);


                //visible view of user_id editext

                final EditText tv_user_id = new EditText(getContext());
                tv_user_id.setEnabled(false);
                tv_user_id.setBackgroundResource(R.drawable.underline);

                //to load
               /* SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                String userid = settings.getString("user_id","user_id");
*/
                tv_user_id.setTextColor(Color.BLACK);
                tv_user_id.setText(userid);
                layout.addView(tv_user_id);
                tv_user_id.setVisibility(View.GONE);


                TextView session_idtxt = new TextView(getContext());
                session_idtxt.setText("Session ID");
                session_idtxt.setTextColor(Color.GRAY);
                layout.addView(session_idtxt);
                session_idtxt.setVisibility(View.GONE);

                tv_sessionid.setBackgroundResource(R.drawable.underline);
                tv_sessionid.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tv_sessionid.setText(sessionid);
                tv_sessionid.setTextColor(Color.BLACK);
                if (tv_sessionid.getParent() != null) {
                    ((ViewGroup) tv_sessionid.getParent()).removeView(tv_sessionid); // <- fix
                }
                layout.addView(tv_sessionid);
                tv_sessionid.setVisibility(View.GONE);

                TextView item_producttxt = new TextView(getContext());
                item_producttxt.setText("Item Product");
                item_producttxt.setTextColor(Color.GRAY);
                layout.addView(item_producttxt);


                final EditText tv_item_code = new EditText(getContext());
                tv_item_code.setEnabled(false);
                tv_item_code.setBackgroundResource(R.drawable.underline);
                tv_item_code.setTextSize(14);
                tv_item_code.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tv_item_code.setText(String.valueOf(item.getItem_code()));
                tv_item_code.setTextColor(Color.BLACK);
                // tv_item_code.setHintTextColor(Color.BLACK);
                layout.addView(tv_item_code);
                tv_item_code.setVisibility(View.GONE);

                final EditText tv_item_name = new EditText(getContext());
                tv_item_name.setEnabled(false);
                tv_item_name.setBackgroundResource(R.drawable.underline);
                tv_item_name.setTextSize(14);
                tv_item_name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tv_item_name.setText(String.valueOf(item.getItem_name()));
                tv_item_name.setTextColor(Color.BLACK);
                // tv_item_code.setHintTextColor(Color.BLACK);
                layout.addView(tv_item_name);


                TextView item_descriptiontxt = new TextView(getContext());
                item_descriptiontxt.setText("Description");
                item_descriptiontxt.setTextColor(Color.GRAY);
                layout.addView(item_descriptiontxt);

                TextView item_pricetxt = new TextView(getContext());
                item_pricetxt.setText("Price");
                item_pricetxt.setTextColor(Color.GRAY);
                layout.addView(item_pricetxt);

                final EditText tv_price = new EditText(getContext());
                tv_price.setTextSize(14);
                tv_price.setEnabled(false);
                tv_price.setBackgroundResource(R.drawable.underline);


                tv_price.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                final Locale local = new Locale("id", "id");
                final NumberFormat fmt = NumberFormat.getCurrencyInstance(local);
                //  tv_price.setText(String.valueOf(item.getPrice()).replaceAll("[^0-9]", ""));
                tv_price.setText("Rp." + String.valueOf(fmt.format(item.getPrice())).replaceAll("[Rp\\s]", ""));
                //  tv_price.setHintTextColor(Color.BLACK);
                tv_price.setTextColor(Color.BLACK);
                layout.addView(tv_price);


                TextView textDisc = new TextView(getContext());
                textDisc.setText("Discount");
                textDisc.setTextColor(Color.GRAY);
                layout.addView(textDisc);

                final EditText editDisc = new EditText(getContext());
                editDisc.setTextSize(14);
                editDisc.setEnabled(false);
                editDisc.setBackgroundResource(R.drawable.underline);
                editDisc.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                final NumberFormat fmtDisc = NumberFormat.getCurrencyInstance(local);
                editDisc.setText(String.valueOf(fmt.format(item.getDisc()) + "%").replaceAll("[Rp\\s]", ""));
                editDisc.setTextColor(Color.BLACK);
                layout.addView(editDisc);


                TextView item_qtytxt = new TextView(getContext());
                item_qtytxt.setText("Qty");
                item_qtytxt.setTextColor(Color.GRAY);
                layout.addView(item_qtytxt);

                final EditText tv_qty = new EditText(getContext());
                tv_qty.setTextSize(14);
                tv_qty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tv_qty.setHintTextColor(Color.BLACK);

                int maxLength = 4;
                tv_qty.setInputType(InputType.TYPE_CLASS_NUMBER);
                tv_qty.setRawInputType(Configuration.KEYBOARD_12KEY);
                tv_qty.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

                layout.addView(tv_qty);
                tv_qty.requestFocus();


                TextView item_totaltxt = new TextView(getContext());
                item_totaltxt.setText("Total");
                item_totaltxt.setTextColor(Color.GRAY);
                layout.addView(item_totaltxt);
                tv_total_pricex.setText("Rp." + 0);


                tw = new TextWatcher() {
                    // boolean flag;
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        qty = s.toString();
                        price = tv_price.getText().toString().replaceAll("[^0-9]", "");
                        disc = editDisc.getText().toString().replaceAll("[^0-9]", "");
                        sub = 0;
                        total_pricex = 0;


                        try {
                            amnt = Integer.parseInt((qty));
                            amntp = Integer.parseInt((price));
                            amntd = Integer.parseInt((disc));
                            sub = amnt * amntp - (((amnt * amntp) * amntd) / 100);
                            total_pricex = sub;

                        } catch (NumberFormatException e) {
                            s.clear();
                        }
                        subs = String.valueOf(Integer.parseInt(String.valueOf(sub)));
                        if (!qty.isEmpty() || !qty.equals("0")) {
                            tv_total_price.removeTextChangedListener(tw);

                            tv_total_price.setText(subs);
                            tv_total_pricex.setText("Rp." + String.valueOf(fmt.format(total_pricex).replaceAll("[Rp\\s]", "")));
                        } else {
                            tv_total_price.setText(subs);
                            tv_total_pricex.setText("Rp." + total_pricex);
                        }
                    }
                };


                tv_qty.addTextChangedListener(tw);

                tv_total_price.setVisibility(View.GONE);
                tv_total_price.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                // tv_total_price.setHintTextColor(Color.BLACK);
                tv_total_price.setTextColor(Color.BLACK);
                if (tv_total_price.getParent() != null) {
                    ((ViewGroup) tv_total_price.getParent()).removeView(tv_total_price); // <- fix
                }
                layout.addView(tv_total_price);

                tv_total_pricex.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                //tv_total_pricex.setHintTextColor(Color.BLACK);
                tv_total_pricex.setTextColor(Color.BLACK);
                tv_total_price.setTextSize(14);
               /*
                This below is solusution for handler error of
                The specified child already has a parent.
                You must call removeView() on the child's parent first (Android)

                */
                if (tv_total_pricex.getParent() != null) {
                    ((ViewGroup) tv_total_pricex.getParent()).removeView(tv_total_pricex); // <- fix
                }
                layout.addView(tv_total_pricex);


                scrollView.addView(layout);
                builder.setView(scrollView);
                builder.setCustomTitle(title);
                builder.setCancelable(false);
                builder.setPositiveButton("BUY NOW",
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
                //  dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);


                dialog.getWindow().getDecorView().setPaddingRelative(8, 32, 8, 24);
                dialog.getWindow().setLayout(685, 1050);


                Drawable iconXy = getResources().getDrawable(R.drawable.ic_check_white);
                iconXy.setBounds(40, 0, 40, 0);


                Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                //positiveButton.setCompoundDrawables(iconX,null,null,null);

                // positiveButton.setPadding(20,20,20,40);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                params.weight = 14.0f;
                //params.gravity = Gravity.CENTER_HORIZONTAL; //this is layout_gravity
                positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null, null, null);
                positiveButton.setPadding(40, 0, 80, 0);


                positiveButton.setGravity(Gravity.CENTER);
                positiveButton.setLayoutParams(params);
                positiveButton.setBackgroundColor(parseColor("#f39c12"));
                positiveButton.setTextColor(Color.WHITE);
                positiveButton.invalidate();


                Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                // negativeButton.setPadding(20,20,20,40);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                // positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                params1.weight = 14.0f;
                //params1.gravity= Gravity.CENTER;
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
                        Boolean wantToCloseDialog = false;

                        final String session_id = tv_sessionid.getText().toString();
                        final String user_id = tv_user_id.getText().toString();
                        item_code = tv_item_code.getText().toString();
                        qty = tv_qty.getText().toString();
                        disc = editDisc.getText().toString().replaceAll("[^0-9]", "");
                        price = tv_price.getText().toString().replaceAll("[^0-9]", "");
                        total_price = tv_total_price.getText().toString().replaceAll("[^0-9]", "");//v_total_price.setText(total_price)

                        if (qty == null || qty.trim().length() == 0 || qty.isEmpty() || qty.equals("0")) {
                            Toast.makeText(getContext(), "Qty can't be Empty or Zero ", Toast.LENGTH_SHORT).show();
                            tv_qty.requestFocus();
                            return;
                        } else {

                            pd.show();
                            pd.getWindow().setLayout(245, 200);
                            pd.getWindow().setGravity(Gravity.CENTER);

                            final Transaction transaction = new Transaction();

                            transaction.setSession_id(String.valueOf(session_id));
                            transaction.setUser_id(Integer.parseInt(user_id));
                            transaction.setItem_code(item_code);
                            transaction.setQty(Integer.parseInt(qty));
                            transaction.setDisc(Float.parseFloat(disc));
                            transaction.setPrice(Integer.parseInt(price));
                            transaction.setTotal_price(Integer.parseInt(total_price));


                            TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
                            Call<ResponseTransaction> call = transactionService.Create(transaction);
                            call.enqueue(new Callback<ResponseTransaction>() {

                                @Override
                                public void onResponse(Call<ResponseTransaction> call, Response<ResponseTransaction> response) {
                                    if (response.isSuccessful()) {
                                        ResponseTransaction responseTransaction = response.body();
                                        if (responseTransaction.getSuccess().equals("true")) {
                                            pd.dismiss();
                                            dialog.dismiss();

                                            Toast.makeText(getContext(), responseTransaction.getMessage(), Toast.LENGTH_SHORT).show();
                                            recyclerViewTransaction.invalidateItemDecorations();
                                            transactionAdapter.notifyDataSetChanged();

/*
                                            LayoutInflater inflater1 = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            final View view1 =inflater1.inflate(R.layout.badge_layout_for_cart, null);
                                            TextView txtCount = (TextView) view1.findViewById(R.id.txtCount);
*/

                                            /*Search obj = new Search();
                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                            ft.replace(R.id.fragment_container, obj);
                                            ft.addToBackStack(null);
                                            ft.commit();
*/
                                            count = transactionAdapter.grandItem();
                                            txtSub_Item.setText(String.valueOf(count));
                                            int counts = 0;

                                            counts = Integer.parseInt(txtSub_Item.getText().toString());
                                            ((Welcome) getActivity()).updateContTextView(counts);

                                            transactionAdapter.notifyDataSetChanged();
                                            transactionDetailLayout.invalidate();
                                            recyclerViewTransaction.invalidate();

                                            FragmentTransaction ft3;
                                            ft3 = getFragmentManager().beginTransaction();
                                            ft3.detach(Search.this).attach(Search.this).commit();

                                           /* transactionDetailLayout.setVisibility(View.VISIBLE);
                                            recyclerViewTransaction.setVisibility(View.VISIBLE);

                                            transactionDetailLayout.setVisibility(View.GONE);
                                            recyclerViewTransaction.setVisibility(View.GONE);

*/

                                           /* TransactionAdapter adapter2 = (TransactionAdapter) recyclerView.getAdapter();
                                            adapter2.getItemCount();
                                            adapter2.notifyDataSetChanged();
                                        //    recyclerView.invalidate();
                                           */


                                            /*// String qty = tv_qty.getText().toString();
                                            Events.FragmentActivityMessage fragmentActivityMessage  =
                                                    new Events.FragmentActivityMessage(String.valueOf(txtCount.getText()));

                                            GlobalBus.getsBus().post(fragmentActivityMessage );
*/
                                                /*Transactiondetail fragment = new Transactiondetail();
                                                getFragmentManager().beginTransaction().replace(R.id.search_fragment, fragment).commit();
*/
                                        } else {
                                            Toast.makeText(getContext(), responseTransaction.getMessage(), Toast.LENGTH_SHORT).show();
                                            pd.dismiss();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                }


                                @Override
                                public void onFailure(Call<ResponseTransaction> call, Throwable t) {
                                    slideUp(myView);
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            slideDown(myView);
                                        }
                                    }, 3000);
                                    pd.dismiss();
                                }

                            });

                        }

                        if (wantToCloseDialog) {
                            dialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));












        /*toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
       */ // setSupportActionBar(toolbar);
        // toolbar.setNavigationIcon(R.drawable.back);
        //      imgBack = (ImageView) view.findViewById(R.id.imgBack);


        /*Welcome activity =(Welcome)getActivity();
       // Bundle results = activity.getDataEdSearch();*/




        /*Bundle bundle = getArguments();
        edSearch = (EditText) view.findViewById(R.id.edSearch);
        if(bundle !=null) {
            sfile = this.getArguments().getString("message");

        }
        edSearch.setText(sfile);
        String s = edSearch.getText().toString();
*/

  /*      final Drawable dw = getContext().getResources().getDrawable(R.drawable.ic_search);
        edSearch.setCompoundDrawablesWithIntrinsicBounds(dw, null, null, null);
        edSearch.setVisibility(View.VISIBLE);
        imgBack.setVisibility(View.GONE);
        edSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edSearch.setEnabled(true);
                    //toolbar.setNavigationIcon(R.drawable.back);
                    imgBack.setVisibility(v.VISIBLE);
                    Drawable dw = getContext().getResources().getDrawable(R.drawable.ic_search);
                    edSearch.setCompoundDrawables(null, null, null, null);
                } else {

                    Drawable dw = getContext().getResources().getDrawable(R.drawable.ic_search);
                    edSearch.setCompoundDrawables(dw, null, null, null);
                    imgBack.setVisibility(v.GONE);
                    //    toolbar.setNavigationIcon(null);
                }

            }

        });
*/



  /*      imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/
      /*  edSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //toolbar.setNavigationIcon(R.drawable.back);
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                for (UnderlineSpan span : s.getSpans(0, s.length(), UnderlineSpan.class)) {
                    s.removeSpan(span);
                }

            // sfile = edSearch.toString();
             filter(s.toString());
            }
        });
*/

/*

        Bundle bundle = getArguments();
        edSearch = (EditText) view.findViewById(R.id.edSearch);
        if(bundle !=null) {
            sfile = this.getArguments().getString("message");
        }
        subTotal.setText(sfile);
        String s = subTotal.getText().toString();

*/

      /*  Bundle bundle = new Bundle();
        bundle.putString("message", tv_total_price.getText().toString());
        Payment payment = new Payment();
        payment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, payment).addToBackStack(null).commit();
*/


        return view;
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (cameraPreview != null) {
            if (isVisibleToUser) {
                cameraPreview.resume();
            } else {
                cameraPreview.pauseAndWait();

            }
        }
    }


     @Override
    public void onStart() {
        super.onStart();
        Log.d(FRAGMENT_TAG,"is starting");
    }

   /* @Override
    public void onPause() {
        super.onPause();
        cameraPreview.pauseAndWait();
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraPreview.resume();
        if(cameraPreview !=null){

        }
    }


    private void displayToast() {
        if (getActivity() == null && toast == null) {
            toast = null;
            tvResult.setText(toast);
        } else {
            // Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            try {
                tvResult.setText(toast);
                bitmap = encodeAsBitmap(toast, BarcodeFormat.QR_CODE, 512, 512);

                //   outputImage.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
                //outputImage.setImageBitmap(bitmap);

            } catch (WriterException e) {

                e.printStackTrace();
            }
        }
    }
*/

  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                toast = "Cancelled from fragment";
                Log.d("QRSCAN", "Cancelled scan");

            } else {
                toast = "FORMAT: " + result.getFormatName() + " \nCONTENT: " + result.getContents();
                Log.d("QRSCAN", "Scanned from fragment" + result.getFormatName());
            }
            tvResult.setText(toast);
        }else{
            super.onActivityResult(requestCode, resultCode, data);
           // displayToast();
        }
    }
*/


  @Override
  public void onPause(){
      super.onPause();
      if(cameraSource !=null)
      {
          cameraSource.stop();
        //  cameraSource.release();
      }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
      if(requestCode == REQUEST_CODE && resultCode ==RESULT_OK){
          if(data !=null){
              final Barcode barcode = data.getParcelableExtra("barcode");
              searchEdittext.post(new Runnable() {
                  @Override
                  public void run() {
                      searchEdittext.setText("HAI");
                  }
              });
          }
      }
  }

    public void scanFromFragment() {
        setUserVisibleHint(true);
        integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a Barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }


    public Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int imgWidth, int imgHeight) throws WriterException {
        if (contents == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contents, format, imgWidth, imgHeight, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }





    private void getToolbar() {
        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        //noinspection deprecation
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Cart");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);


        TextView tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        tvTitle.setTextSize(14);
        tvTitle.setText(R.string.cart_title);
        tvTitle.setVisibility(View.VISIBLE);
        ImageView imgFile = (ImageView) getActivity().findViewById(R.id.imgFile);
        imgFile.setVisibility(View.GONE);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft;
                ft = getFragmentManager().beginTransaction();

                ft.replace(R.id.fragment_container, new Search());
                ft.addToBackStack(null);
                ft.commit();

                // ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            }
        });

    }



    public void getSubTotal() {

        // getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        boolean displayItem = true;

        final MenuItem qrcodeItem = menu.findItem(R.id.action_qrcode);
        qrcodeItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        final MenuItem cartItem = menu.findItem(R.id.action_cart);
        TextView txtCount = (TextView) cartItem.getActionView().findViewById(R.id.txtCount);
        final MenuItem settingsItem = menu.findItem(R.id.menu_settings);

        transactionDetailLayout.setVisibility(View.GONE);
        recyclerViewTransaction.setVisibility(View.GONE);

        if (displayItem) {
            qrcodeItem.setVisible(true);
            searchItem.setVisible(true);
            cartItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            cartItem.setVisible(true);
            
        } else {
            searchItem.setVisible(false);
            cartItem.setVisible(false);
            settingsItem.setVisible(false);
        }

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setGravity(Gravity.LEFT);

        int searchImgId = getResources().getIdentifier("android:id/searc_mag_icon", null, null);
        ImageView vIcon = (ImageView) searchView.findViewById(searchImgId);
        if (vIcon != null) {
            vIcon.setImageResource(R.drawable.ic_find2);
        }

        searchEdittext = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEdittext.setTextColor(getResources().getColor(R.color.colorGrey));
        searchEdittext.setTextSize(14);
        searchEdittext.setHint("Search Item ...");
        searchEdittext.setHintTextColor(getResources().getColor(R.color.colorGrey));
        searchEdittext.setEms(10);

        searchView.requestFocus();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchView != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            // searchView.setIconifiedByDefault(false);

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String newText) {
                    Log.i("onQuery Textsubmit", newText);
                    searchView.clearFocus();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQuery onQueryTextChange", newText);
                    filter(newText.toString());
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }

        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean visiblelayout = true;

                if (visiblelayout) {
                    //  ((Welcome) getActivity()).clearTab1();

                    itemLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);

                    transactionDetailLayout.setVisibility(View.VISIBLE);
                    recyclerViewTransaction.setVisibility(View.VISIBLE);
                    getToolbar();

                    qrcodeItem.setVisible(false);
                    searchItem.setVisible(false);
                    cartItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                    cartItem.setVisible(true);

                } else {

                    itemLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_qrcode:
                searchView.setIconified(false);
               // cameraLayout.setVisibility(View.VISIBLE);
                scanQrCode();
                return false;


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

    public void callParentMethod() {
        getActivity().onBackPressed();
    }


    public String lashCalculate() {
        double qty = 0;
        double price = 0;
        double total = 0;
        if (!tv_qty.getText().toString().equals("") && !tv_price.getText().toString().equals("")) {
            qty = Double.parseDouble(tv_qty.getText().toString());
            price = Double.parseDouble(tv_price.getText().toString());
            total = (qty * price);
        }
        return String.valueOf(total);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnScan:
                setUserVisibleHint(false);
               scanFromFragment();
                break;
            case R.id.btnStop:
                cameraLayout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    private interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

  /*  public void searching(){
        edSearch = new EditText(getContext());
        edSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //toolbar.setNavigationIcon(R.drawable.back);
            }

            @Override
            public void afterTextChanged(Editable edSearch) {
                for (UnderlineSpan span : edSearch.getSpans(0, edSearch.length(), UnderlineSpan.class)) {
                    edSearch.removeSpan(span);
                }

                filter(edSearch.toString());
            }
        });

    }
*/


    public void filter(String newText) {

        /*adapter = new CostumGridUpdater(listContentArr, getContext());
        recyclerView.setAdapter(adapter);*/
        newText = newText.toLowerCase();
        List<Books> newList = new ArrayList<>();
        for (Books list : listContentArr) {
            String name = list.getItem_name().toLowerCase();
            if (name.contains(newText)) {
                newList.add(list);
            }
        }
        adapter.setFilter(newList);
    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0
                , ItemTouchHelper.LEFT) {//| ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerViewTransaction, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction) {

                final int position = viewHolder.getAdapterPosition();
                Transaction item = transactionList2.get(position);
                final String item_code = item.getItem_code();
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
                                    transactionAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                    //SwipeableItemViewHolder.setSwipeItemHorizontalSlideAmount()
                                }
                            });
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getWindow().getDecorView().setPaddingRelative(8, 32, 8, 24);
                    Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
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


                    Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
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

                                    transactionAdapter.removeItem(position);
                                    count = transactionAdapter.grandItem();
                                    txtSub_Item.setText(String.valueOf(count));
                                    int counts = Integer.parseInt(txtSub_Item.getText().toString());

                                    counts = Integer.parseInt(txtSub_Item.getText().toString());
                                    ((Welcome) getActivity()).updateContTextView(counts);


                                    mSubDiscRp = transactionAdapter.grandDiscRp();
                                    txtSub_DiscRp.setText(String.valueOf("Rp." + fmt.format(mSubDiscRp)).replaceAll("[Rp\\s]", ""));
                                    mSubTotal = transactionAdapter.grandTotal();
                                    subTotal.setText(String.valueOf(fmt.format(mSubTotal)).replaceAll("[Rp\\s]", ""));


                                }

                                @Override
                                public void onFailure(Call<ResponseTransaction> call, Throwable t) {
                                    slideUp(myView);
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            slideDown(myView);
                                        }
                                    }, 3000);
                                    //  pd.dismiss();

                                }
                            });
                            dialog.dismiss();
                        }
                    });


                } /*else {

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

     *//*
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
      *//*


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
                                    transactionAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
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
                            Transaction item = transactionList2.get(position);
                            transactionAdapter.editItem(position);
                            transactionAdapter.notifyItemChanged(position);

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
                */
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerViewTransaction, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                //final TextView textView = null;
                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX < 0) {
                        /*p.setColor(Color.parseColor("#48366d"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);

                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                        //  textView.setText("DELETE");
                    } else {
*/
                        p.setColor(Color.parseColor("#f39c12"));//D32F2F//f39c12
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                        //textView.setText("EDIT");
                    }
                }
                super.onChildDraw(c, recyclerViewTransaction, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewTransaction);
    }


    private void deleteTransaction() {
        pd = new ProgressDialog(getContext());
        String titleId = "Processing...";
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
        }, 2000);


        for (Transaction item : transactionList2) {
           /* user_id = item.getUser_id();
            item.setUser_id(user_id);*/
            session_id = item.getSession_id();
            item.setSession_id(session_id);
        }


        TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
        Call<ResponseTransaction> call = transactionService.deleteuser_id(session_id);
        call.enqueue(new Callback<ResponseTransaction>() {

            @Override
            public void onResponse(Call<ResponseTransaction> call, Response<ResponseTransaction> response) {
                if (response.isSuccessful()) {
                    //  mProgress.dismiss();
                    ResponseTransaction responseTransaction = response.body();
                    if (responseTransaction.getSuccess().equals("true")) {
                        transactionAdapter.clear();
                        count = transactionAdapter.grandItem();
                        txtSub_Item.setText(String.valueOf(count));
                        int counts = Integer.parseInt(txtSub_Item.getText().toString());
                        counts = Integer.parseInt(txtSub_Item.getText().toString());


                        mSubDiscRp = transactionAdapter.grandDiscRp();
                        txtSub_DiscRp.setText(String.valueOf("Rp. " + fmt.format(mSubDiscRp).replaceAll("[Rp\\s]", "")));

                        mSubTotal = transactionAdapter.grandTotal();
                        subTotal.setText(String.valueOf("Rp. " + fmt.format(mSubTotal).replaceAll("[Rp\\s]", "")));

                        mSubQty = transactionAdapter.grandQty();
                        subQty.setText(String.valueOf(mSubQty));

                        ((Welcome) getActivity()).updateContTextView(counts);
                        transactionAdapter.notifyDataSetChanged();
                        transactionDetailLayout.invalidate();
                        recyclerViewTransaction.invalidate();
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                        recyclerViewTransaction.setLayoutParams(params);
                        final String responseT = responseTransaction.getMessage();

                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Toast.makeText(getContext(), responseT, Toast.LENGTH_SHORT).show();
                            }
                        }, 2050);


                    } else {
                        Toast.makeText(getContext(), responseTransaction.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTransaction> call, Throwable t) {
                slideUp(myView);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        slideDown(myView);
                    }
                }, 3000);
                //pd.dismiss();


            }
        });
    }

    public void slideUp(View view) {
        // ((Welcome) getActivity()).bottomNavigationView.setVisibility(View.GONE);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, view.getHeight(), 0);
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        view.startAnimation(translateAnimation);
        view.setVisibility(View.GONE);
    }

    public void slideDown(View view) {

        //boolean visibile = false;
        // ((Welcome) getActivity()).navigationView();
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, view.getHeight());
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        view.startAnimation(translateAnimation);
        view.setVisibility(View.GONE);

    }


    private void dialogDelete() {
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

        dialog.getWindow().getDecorView().setPaddingRelative(8, 32, 8, 24);


        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

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


        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
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


    private void scanQrCode() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fragment_qrscan, null);

        final ProgressDialog pd = new ProgressDialog(getContext());
        //pd.setTitle("Please Wait ....");
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.setProgressStyle(android.R.attr.progressBarStyleLarge);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        TextView title = new TextView(getContext());
        title.setBackgroundColor(Color.parseColor("#48367d"));
        title.setText("SCAN QRCODE");
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        title.setPadding(40, 30, 30, 30);

        cameraView  = (SurfaceView) dialogView.findViewById(R.id.cameraView);
        createCameraSource();

        builder.setView(dialogView);
        builder.setCustomTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton("SCAN",
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

        dialog.getWindow().getDecorView().setPaddingRelative(8,32,8,24);
        dialog.getWindow().setLayout(685,1050);

        /*Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 14.0f;
        positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null,null,null);
        positiveButton.setPadding(40,0,40,0);
        positiveButton.setGravity(Gravity.CENTER);
        positiveButton.setLayoutParams(params);
        positiveButton.setBackgroundColor(Color.parseColor("#F39C12"));
        positiveButton.setTextColor(Color.WHITE);
        positiveButton.invalidate();
*/

        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
      //  params1.weight = 14.0f;
        negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null,null,null);
        negativeButton.setPadding(40,0,40,0);
        negativeButton.setGravity(Gravity.CENTER);
        negativeButton.setLayoutParams(params1);
        negativeButton.setBackgroundColor(Color.parseColor("#F39C12"));
        negativeButton.setTextColor(Color.WHITE);
        negativeButton.invalidate();
/*
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                createCameraSource();
            }
        });*/

    }


    private void  createCameraSource(){
        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setAutoFocusEnabled(true)
                .setFacing(0)
                .setRequestedFps(15f)
                //.setRequestedPreviewSize(600, 1000)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {

                    /*if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                      ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1001);
*/
                        cameraSource.start(cameraView.getHolder());
                  //  }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.i(FRAGMENT_TAG, "is Stop");
                cameraSource.stop();
            }

        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCode = detections.getDetectedItems();

                if(qrCode.size() !=0){

                    searchEdittext.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(5);
                            searchEdittext.setText(qrCode.valueAt(0).displayValue);
                        }
                    });

                    Log.i(FRAGMENT_TAG, String.valueOf(qrCode.valueAt(0).displayValue));
                }
            }

        });
    }

}