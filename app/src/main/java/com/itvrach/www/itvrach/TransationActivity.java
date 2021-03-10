package com.itvrach.www.itvrach;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itvrach.adapter.BooksAdapter;
import com.itvrach.adapter.TransactionAdapter;
import com.itvrach.model.Books;
import com.itvrach.model.ResponseBook;
import com.itvrach.model.ResponseTransaction;
import com.itvrach.model.Transaction;
import com.itvrach.services.APIClient;
import com.itvrach.services.BookService;
import com.itvrach.services.TransactionService;
import com.itvrach.sessions.SessionManagement;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.PendingIntent.getActivity;
import static android.graphics.Color.GRAY;
import static android.graphics.Color.WHITE;

/**
 * Created by engineer on 9/15/2018.
 */

public class TransationActivity extends AppCompatActivity {

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


    private Context context;
    private TextView subTotal;
    private TextView subQty;




    private int mSubTotal = 0;
    private int mSubQty   = 0;
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



    private int no = 0;
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public static final String PREFS_NAME = "MyPrefsFile";

    private SessionManagement session;
    private TextView txt_userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        this.setTitle("INVOICE TRANSACTION");

        // final FragmentManager fm=getFragmentManager();
        //final TVShowFragment tv=new TVShowFragment();

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();
        // get user data from session
      //  HashMap<Integer, String> user = session.getUserDetails();

//        final String userid       = user.get(SessionManagement.KEY_USERID);
  //      String email              = user.get(SessionManagement.KEY_EMAIL);


        session.checkLogin();


        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // String user_id = user.get(SessionManagement.KEY_USERID);
        String sessionid    = user.get(SessionManagement.KEY_SESSIONID);
        String email         = user.get(SessionManagement.KEY_EMAIL);


        txtsession_id = (TextView) findViewById(R.id.txtsession_id);
        //  editTextUser_id.setText(user_id);
        txtsession_id.setText(sessionid);


      //  textViewEmail.setText("WELCOME : "+ email);




        txt_userid = (TextView) findViewById(R.id.txt_userid);
        txt_userid.setText(email);


      //  txtsession_id.setText(sessionid);

      //  final String user_id     = txt_userid.getText().toString();
        final String session_id  = txtsession_id.getText().toString();


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mManager);
        adapter = new TransactionAdapter(transactionList, getApplicationContext());
        recyclerView.setAdapter(adapter);
        session.checkLogin();



        subTotal = (TextView) findViewById(R.id.txtTotal);
        txtNo = (TextView) findViewById(R.id.txtNo);
        txtSub_Qty = (TextView) findViewById(R.id.txtSub_Qty);

        subQty = (TextView) findViewById(R.id.txtSub_item);


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
                            adapter = new TransactionAdapter(transactionList, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            count = adapter.grandItem();
                            txtSub_Qty.setText(String.valueOf(count));

                            mSubTotal = adapter.grandTotal();
                            Locale local = new Locale("id", "id");
                            NumberFormat fmt = NumberFormat.getCurrencyInstance(local);
                            subTotal.setText(String.valueOf(fmt.format(mSubTotal)).replaceAll("[Rp\\s]", ""));


                            mSubQty = adapter.grandQty();
                            subQty.setText(String.valueOf(mSubQty));
                            //Toast.makeText(TransationActivity.this,, Toast.LENGTH_SHORT).show();

                        }

                    }

                }else {
                    Toast.makeText(TransationActivity.this, "DATA IS EMPTY", Toast.LENGTH_SHORT).show();
                }

            }



            @Override
            public void onFailure(Call<ResponseTransaction> call, Throwable t) {
                //Toast.makeText(TransationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(TransationActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();
            }
        });



        //Double Price = Double.valueOf(txtPrice.getText().toString());
        // txtPrice = (TextView) findViewById(R.id.txtPrice);
        //getSumofAllitems();
        btnProducts = (ImageButton) findViewById(R.id.btnProducts);
        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout layout = new LinearLayout(TransationActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CLIP_VERTICAL);
                // layout.setPadding(0, 0, 0, 0);

                TextView title = new TextView(TransationActivity.this);
                title.setBackgroundColor(Color.parseColor("#303F9F"));
                title.setText("SAVE TRANSACTION");
                title.setTextColor(WHITE);
                title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                title.setPadding(30, 30, 30, 30);
                layout.addView(title);

                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*FragmentTransaction fragmentTransaction =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new Profile());
                        fragmentTransaction.commit();*/


                       // loadData();
                        saveTransaction();

                       /* recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                        mManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(mManager);
                        adapter = new TransactionAdapter(transactionList, getApplicationContext());
                        recyclerView.setAdapter(adapter);*/

                    }
                });


                TextView title3 = new TextView(TransationActivity.this);
                title3.setBackgroundColor(Color.parseColor("#F39C12"));
                title3.setText("LOAD TRANSACTION");
                title3.setTextColor(WHITE);
                title3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                title3.setPadding(30, 30, 30, 30);
                layout.addView(title3);

                title3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*FragmentTransaction fragmentTransaction =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new Profile());
                        fragmentTransaction.commit();*/

                        /*List<Transactiondetail> transList = new ArrayList<>();
                        Transactiondetail transactiondetail = new Transactiondetail();
                        // loadData();
                        Context ctx = getApplicationContext();
                        SharedPreferences sharedPreferences = ctx.getSharedPreferences(PREFS_NAME, ctx.MODE_PRIVATE);
                        //SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);

                        Gson gson = new Gson();
                        String json = sharedPreferences.getString(PREFS_NAME, "");
                        Transactiondetail obj = gson.fromJson(json, Transactiondetail.class);
                        obj.setUser_id(transactiondetail.getUser_id());
                        Toast.makeText(TransationActivity.this, "Response"+obj.toString(),Toast.LENGTH_SHORT).show();
*/
                     //   getTransactionList();
                       /* recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                        mManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(mManager);
                        adapter = new TransactionAdapter(transactionList, getApplicationContext());
                        recyclerView.setAdapter(adapter);*/

                       getData1();


                    }
                });


                TextView title2 = new TextView(TransationActivity.this);
                title2.setBackgroundColor(Color.parseColor("#303F9F"));
                title2.setText("EDIT PROFILE");
                title2.setTextColor(WHITE);
                title2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
                title2.setPadding(30, 30, 30, 30);
                // title2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(title2);

                title2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // tv.show(fm,"PRODUCTS LIST");
                        inputUser();
                        tooltipView
                                .on(v).close();
                    }
                });


                tooltipView
                        .on(v)
                        // .customView(customView)
                        .position(ViewTooltip.Position.BOTTOM)
                        .arrowSourceMargin(0)
                        .arrowTargetMargin(0)
                        .customView(layout)
                        // .padding(0, 0, 0, 0)
                       /* .clickToHide(true)
                        .autoHide(true, 2000)*/
                        .color(createPaint())
                        .animation(new ViewTooltip.FadeTooltipAnimation(500))
                       /* .onDisplay(new ViewTooltip.ListenerDisplay() {
                            @Override
                            public void onDisplay(View view) {
                                Log.d("ViewTooltip", "onDisplay");
                            }
                        })
                        .onHide(new ViewTooltip.ListenerHide() {
                            @Override
                            public void onHide(View view) {

                                Log.d("ViewTooltip", "onHide");
                            }
                        })*/
                        .show();
            }
        });





       /* no = adapter.noDeret();
        txtNo.setText(String.valueOf(no));*/
        initSwipe();

    }




    private Boolean checkdata(int user_id){

        if(String.valueOf(user_id) == null){
            Toast.makeText(TransationActivity.this,"SORRY DATA IS EMPTY", Toast.LENGTH_SHORT).show();
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



    private void deleteTransaction(){
        for (Transaction item : transactionList) {
            session_id = item.getSession_id();
            item.setSession_id(session_id);
        };

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

                        mSubTotal = adapter.grandTotal();
                        Locale local = new Locale("id", "id");
                        NumberFormat fmt = NumberFormat.getCurrencyInstance(local);
                        subTotal.setText(String.valueOf(fmt.format(mSubTotal)).replaceAll("[Rp\\s]", ""));

                        mSubQty = adapter.grandQty();
                        subQty.setText(String.valueOf(mSubQty));

                        Toast.makeText(TransationActivity.this, responseTransaction.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TransationActivity.this, (CharSequence) responseTransaction.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TransationActivity.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseTransaction> call, Throwable t) {
                //Toast.makeText(TransationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(TransationActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveTransaction() {
        List<Transaction> transList = new ArrayList<>();
        for (Transaction item : transactionList) {
            user_id = item.getUser_id();
            item_code = String.valueOf(item.getItem_code());
            price = item.getPrice();

            qty = item.getQty();
            total_price = item.getTotal_price();

            item.setUser_id(user_id);
            item.setItem_code(String.valueOf(item_code));
            item.setPrice(price);
            item.setQty(qty);
            item.setTotal_price(total_price);
            transList.add(item);
        }

        adapter.setLoaddata(transList);
        Gson gson = new Gson();
        String ListJson = gson.toJson(transList);

        context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFS_NAME, ListJson);
        editor.commit();

        Transaction transactionArray[] = gson.fromJson(ListJson, Transaction[].class);
        Transaction transaction = new Transaction();

        for (int i = 0; i < transactionArray.length; i++) {
            Transaction item = transactionArray[i];
            transaction.setUser_id(item.getUser_id());
            transaction.setItem_code(item.getItem_code());
            transaction.setQty(item.getQty());
            transaction.setPrice(item.getPrice());
            transaction.setTotal_price(item.getTotal_price());

            TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
            Call<ResponseTransaction> call = transactionService.Create(transaction);
            call.enqueue(new Callback<ResponseTransaction>() {

                @Override
                public void onResponse(Call<ResponseTransaction> call, Response<ResponseTransaction> response) {
                    if (response.isSuccessful()) {
                        //  mProgress.dismiss();
                        ResponseTransaction responseTransaction = response.body();
                        if (responseTransaction.getSuccess().equals("true")) {
                            //Toast.makeText(TransationActivity.this, responseTransaction.getMessage(), Toast.LENGTH_SHORT).show();
                            deleteTransaction();
                        } else {
                            Toast.makeText(TransationActivity.this, (CharSequence) responseTransaction.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(TransationActivity.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseTransaction> call, Throwable t) {
                    //Toast.makeText(TransationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(TransationActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }



    public List<Transaction> getData1() {
        List<Transaction> transList = new ArrayList<Transaction> ();
        for (int i = 0; i < transactionList.size();i++){
            Transaction transaction = new Transaction();
             int    user_id      = transactionList.get(i).getUser_id();
             String item_code    = String.valueOf(transactionList.get(i).getItem_code());
             int  price          = transactionList.get(i).getPrice();
             int qty             = transactionList.get(i).getQty();
             int total_price     = transactionList.get(i).getTotal_price();

            transaction.setUser_id(user_id);
            transaction.setItem_code(item_code);
            transaction.setPrice(price);
            transaction.setQty(qty);
            transaction.setTotal_price(total_price);
            transList.add(transaction);

        }
        adapter.setLoaddata(transList);
        Toast.makeText(TransationActivity.this, "Response"+ transList,Toast.LENGTH_SHORT).show();


        return transList;
    }



    private List<Transaction> ss() {
        List<Transaction> transactionList = new ArrayList<Transaction>();
     //   for (int i = 0; i < transactionList.size(); i++) {
            Transaction transaction = new Transaction();

           /* int user_id = transactionList.get(i).getUser_id();
            String item_code = String.valueOf(transactionList.get(i).getItem_code());
            int price = transactionList.get(i).getPrice();
            int qty = transactionList.get(i).getQty();
            int total_price = transactionList.get(i).getTotal_price();
*/
            transaction.setUser_id(47);
            transaction.setItem_code("B007");
            transaction.setPrice(20000);
            transaction.setQty(2);
            transaction.setTotal_price(40000);
            transactionList.add(transaction);

      //  }

        return transactionList;
    }


    private void loadDatax(){
       // Create SharedPreferences object.
       Context ctx = getApplicationContext();
       SharedPreferences sharedPreferences = ctx.getSharedPreferences(PREFS_NAME, ctx.MODE_PRIVATE);
       // Get saved string data in it.
       String ListJson = sharedPreferences.getString(PREFS_NAME, "");
       // Create Gson object and translate the json string to related java object array.
       Gson gson = new Gson();
       Transaction TransactionArray[] = gson.fromJson(ListJson, Transaction[].class);
       // Loop the Transactiondetail array and print each Transactiondetail data in android monitor as debug log.
       int length = TransactionArray.length;
       for(int i=0;i<length;i++)
       {
          // Get each user info in dto.
          Transaction transaction = TransactionArray[i];
          StringBuffer userInfoBuf = new StringBuffer();
          userInfoBuf.append("user_id : ");
          userInfoBuf.append(transaction.getUser_id());
          userInfoBuf.append(" , item_code : ");
          userInfoBuf.append(transaction.getItem_code());
          userInfoBuf.append(" , qty : ");
          userInfoBuf.append(transaction.getQty());
          userInfoBuf.append(" , price : ");
          userInfoBuf.append(transaction.getPrice());
          userInfoBuf.append(" , total_price : ");
          userInfoBuf.append(transaction.getTotal_price());

          // Print debug log in android monitor console.Debug info tag is USER_INFO_LIST_TAG.
          Log.d("transactionList", userInfoBuf.toString());
        }
    }

    private void inputUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 30, 30, 30);
        //layout.setScrollBarStyle(1);


        LayoutInflater inflater = this.getLayoutInflater();
        View view2 = inflater.inflate(R.layout.books_list,null, false);
        final RecyclerView mrecyclerID = new RecyclerView(this);
        adapter2 = new BooksAdapter(booksList, getApplicationContext());
        mManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mrecyclerID.setHasFixedSize(true);
        mrecyclerID.setLayoutManager(mManager2);
        mrecyclerID.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
       // mrecyclerID.setItemAnimator(new DefaultItemAnimator());


        final EditText editTextSearch = new EditText(this);
        editTextSearch.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editTextSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0);
        editTextSearch.setHint("Search");
        editTextSearch.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextSearch.setBackgroundColor(Color.WHITE);
        editTextSearch.setHintTextColor(Color.GRAY);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editTextSearch) {
                filter(editTextSearch.toString());
            }
        });

        layout.addView(editTextSearch);

        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(1, 0xFF000000); //black border with full opacity

        TextView title = new TextView(this);
        title.setBackgroundColor(Color.parseColor("#303F9F"));
        title.setText("PRODUCTS LIST");
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setPadding(30, 30, 30, 30);


        final ScrollView scrollView = new ScrollView(this);
/*
        for (Books item : booksList) {

            String id = String.valueOf(item.getId());
            String Item_name = item.getItem_name();
            Double Price = Double.valueOf(item.getPrice());
            String file = item.getFile();

            LinearLayout childLayout = new LinearLayout(
                    TransationActivity.this);

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                childLayout.setBackgroundDrawable(border);
            } else {
                childLayout.setBackground(border);
            }

            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT);
            childLayout.setLayoutParams(linearParams);

            */
/* img.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
*//*

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            params.weight = 1.0f;
            params.height = 124;
            params.width = 124;
            params.gravity = Gravity.CENTER_VERTICAL|Gravity.LEFT;
            params.setMargins(5,5,5,5);

            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params2.weight = 1.0f;
            params2.gravity = Gravity.CENTER_VERTICAL|Gravity.LEFT;

            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params3.weight = 1.0f;
            params3.gravity = Gravity.CENTER_VERTICAL|Gravity.RIGHT;
            params3.setMarginEnd(5);

            final ImageView img = new ImageView(this);
            final TextView txtItem_name = new TextView(this);
            final TextView txtPrice = new TextView(this);

            txtItem_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtItem_name.setTextColor(BLUE);
                    Toast.makeText(TransationActivity.this, "You Clicked is:"+txtItem_name.getText().toString()  , Toast.LENGTH_SHORT).show();

                }
            });


            txtItem_name.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            txtPrice.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            img.setLayoutParams(params);

            txtItem_name.setLayoutParams(params2);
            txtItem_name.setGravity(Gravity.BOTTOM | Gravity.LEFT);

          //  txtPrice.setTextSize(17);
            txtPrice.setLayoutParams(params3);
            txtPrice.setGravity(Gravity.BOTTOM | Gravity.RIGHT);

            Picasso.with(this).load(URI_SEGMENT_UPLOAD + file).into(img);
            txtItem_name.setText(Item_name);

            Locale local = new Locale("id", "id");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(local);
            txtPrice.setText(String.valueOf(fmt.format(Price)).replaceAll("[Rp\\s]", ""));

            childLayout.addView(img);
            childLayout.addView(txtItem_name);
            childLayout.addView(txtPrice);


            layout.addView(childLayout);
        }

*/

        layout.addView(mrecyclerID);
        scrollView.addView(layout);
        builder.setView(scrollView);

        BookService bookService = APIClient.getClient().create(BookService.class);
        Call<ResponseBook> call = bookService.getFindAll();
        call.enqueue(new Callback<ResponseBook>() {

            @Override
            public void onResponse(Call<ResponseBook> call, Response<ResponseBook> response) {
                booksList = response.body().getResult();
                adapter2 = new BooksAdapter(booksList, getApplicationContext() );
                mrecyclerID.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<ResponseBook> call, Throwable t) {
               // Toast.makeText(TransationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(TransationActivity.this,"Server Disconnected", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setCustomTitle(title);
        builder.setCancelable(false);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = builder.create();
       // int  Height=950;
        //int Width=700;

        dialog.show();
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

        //dialog.getWindow().setLayout(Width, Height );

       /* dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setBackgroundColor(Color.parseColor("#303F9F"));
       */
    }

   /* private void inputUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setView(R.layout.fraglayout);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(30, 30, 30, 30);

        View view3 = getLayoutInflater().inflate(R.layout.fraglayout, null);

        EditText editTextSearch = (EditText) view3.findViewById(R.id.editTextSearch);



        final RecyclerView  mrecyclerID = (RecyclerView) view3.findViewById(R.id.mrecyclerID);// ndViewById(R.id.mrecyclerID);
        mrecyclerID.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new BooksAdapter(booksList, getApplicationContext());
        mrecyclerID.setAdapter(adapter2);

        View view2 = getLayoutInflater().inflate(R.layout.books_list, null, false);
        final ScrollView scrollView = new ScrollView(this);

        //showing all data
        for (Books item : booksList) {
            String id = String.valueOf(item.getId());
            String Item_name = item.getItem_name();
            String Price = String.valueOf(item.getPrice());
            String file = item.getFile();

            final TextView txtId = (TextView) view2.findViewById(R.id.txtId);
            txtId.setText(id);
            if (txtId == null)
                layout.addView(txtId);

            final TextView txtItem_name = (TextView) view2.findViewById(R.id.txtItem_name);
            txtItem_name.setText(Item_name);
            if (Item_name == null)
                layout.addView(txtItem_name);


            final TextView txtPrice = (TextView) view2.findViewById(R.id.txtPrice);
            txtPrice.setText(Price);
            if (txtPrice == null)
                layout.addView(txtPrice);

            final ImageView img = (ImageView) view2.findViewById(R.id.file);
            Picasso.with(this).load(URI_SEGMENT_UPLOAD + file).into(img);
            if (img == null)
                layout.addView(img);
            //layout.addView(view2);

        }
       // layout.addView(layout);
        scrollView.addView(view2);
        builder.setView(scrollView);
        builder.setTitle("PRODUCTS LIST");

        builder.setCancelable(false);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });


        BookService bookService = APIClient.getClient().create(BookService.class);
        Call<ResponseBook> call = bookService.getFindAll();
        call.enqueue(new Callback<ResponseBook>() {

            @Override
            public void onResponse(Call<ResponseBook> call, Response<ResponseBook> response) {
                booksList = response.body().getResult();
                adapter2 = new BooksAdapter(booksList, getApplicationContext());
                mrecyclerID.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseBook> call, Throwable t) {
                Toast.makeText(TransationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        // return dialogView;

        final AlertDialog dialog = builder.create();
        //dialog.setContentView(dialogView);
        // dialog.setContentView(scrollView);
        dialog.show();



    }
*/


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

                if (direction == ItemTouchHelper.LEFT){

                    AlertDialog.Builder builder = new AlertDialog.Builder(TransationActivity.this);
                    final LinearLayout layout = new LinearLayout(TransationActivity.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setGravity(Gravity.CLIP_VERTICAL);
                    layout.setPadding(30, 30, 30, 30);

                    TextView title = new TextView(TransationActivity.this);
                    title.setBackgroundColor(Color.parseColor("#303F9F"));
                    title.setText("REMOVE MESSAGE");
                    title.setTextColor(Color.WHITE);
                    title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    title.setPadding(30, 30, 30, 30);
                    builder.setCustomTitle(title);

                    builder.setMessage("Are you sure to Remove this data :"+item_code+"?")
                            //.setMessage("----------------------------------------------------------")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,  int id) {

                                    TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
                                    Call<ResponseTransaction> call = transactionService.delete(item_code, session_id);
                                    call.enqueue(new Callback<ResponseTransaction>() {
                                        @Override
                                        public void onResponse(Call<ResponseTransaction> call, Response<ResponseTransaction> response) {
                                            ResponseTransaction responseTransaction = response.body();
                                            //Toast.makeText(TransationActivity.this, responseTransaction.getSuccess(), Toast.LENGTH_SHORT).show();
                                            adapter.removeItem(position);

                                            count      = adapter.grandItem();
                                            txtSub_Qty.setText(String.valueOf(count));

                                            mSubTotal = adapter.grandTotal();
                                            Locale local = new Locale("id", "id");
                                            NumberFormat fmt = NumberFormat.getCurrencyInstance(local);
                                            subTotal.setText(String.valueOf(fmt.format(mSubTotal)).replaceAll("[Rp\\s]", ""));
                                        }
                                        @Override
                                        public void onFailure(Call<ResponseTransaction> call, Throwable t) {
                                            Toast.makeText(TransationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                    //SwipeableItemViewHolder.setSwipeItemHorizontalSlideAmount()
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(TransationActivity.this);
                    final LinearLayout layout = new LinearLayout(TransationActivity.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setGravity(Gravity.CLIP_VERTICAL);
                    layout.setPadding(30, 30, 30, 30);

                    TextView title = new TextView(TransationActivity.this);
                    title.setBackgroundColor(Color.parseColor("#303F9F"));
                    title.setText("EDIT MESSAGE");
                    title.setTextColor(Color.WHITE);
                    title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    title.setPadding(30, 30, 30, 30);
                    builder.setCustomTitle(title);
                    builder.setMessage("Are you sure to Edit this data: "+item_code+"?")
                            //.setMessage("----------------------------------------------------------")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    Transaction item = transactionList.get(position);
                                    adapter.editItem(position);
                                    adapter.notifyItemChanged(position);

                                    final ProgressDialog pd = new ProgressDialog(TransationActivity.this);
                                    //pd.setTitle("Please Wait ....");
                                    pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    pd.setCancelable(false);
                                    pd.setIndeterminate(true);
                                    pd.getWindow().setGravity(Gravity.CENTER);
                                    pd.setProgressStyle(android.R.attr.progressBarStyleLarge);

                                    pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                                    AlertDialog.Builder builder = new AlertDialog.Builder(TransationActivity.this);

                                    LinearLayout layout = new LinearLayout(TransationActivity.this);
                                    layout.setOrientation(LinearLayout.VERTICAL);
                                    layout.setGravity(Gravity.CLIP_VERTICAL);
                                    layout.setPadding(30, 30, 30, 30);

                                    TextView title = new TextView(TransationActivity.this);
                                    title.setBackgroundColor(Color.parseColor("#303F9F"));
                                    title.setText("EDIT TRANSACTION");
                                    title.setTextColor(Color.WHITE);
                                    title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    title.setPadding(30, 30, 30, 30);

                                    final ScrollView scrollView = new ScrollView(TransationActivity.this);


                                    final TextView tvId = new TextView(TransationActivity.this);
                                    tvId.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    tvId.setText("ID");
                                    tvId.setHintTextColor(Color.GRAY);
                                    layout.addView(tvId);


                                    final EditText Id = new EditText(TransationActivity.this);
                                    Id.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    Id.setText(String.valueOf(item.getId()));
                                    Id.setHintTextColor(Color.GRAY);
                                    layout.addView(Id);


                                    final TextView tvItem_code = new TextView(TransationActivity.this);
                                    tvItem_code.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    tvItem_code.setText("ITEM CODE");
                                    tvItem_code.setHintTextColor(Color.GRAY);
                                    layout.addView(tvItem_code);


                                    final EditText Item_code = new EditText(TransationActivity.this);
                                    Item_code.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    Item_code.setText(item.getItem_code());
                                    Item_code.setHintTextColor(Color.GRAY);
                                    layout.addView(Item_code);

                                    final TextView tvPrice = new TextView(TransationActivity.this);
                                    tvPrice.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    tvPrice.setText("PRICE");
                                    tvPrice.setHintTextColor(Color.GRAY);
                                    layout.addView(tvPrice);

                                    final EditText Price = new EditText(TransationActivity.this);
                                    Price.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    Price.setText(String.valueOf(item.getPrice()));
                                    Price.setHintTextColor(Color.GRAY);
                                    layout.addView(Price);


                                    final TextView tvQty = new TextView(TransationActivity.this);
                                    tvQty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    tvQty.setText("QTY");
                                    tvQty.setHintTextColor(Color.GRAY);
                                    layout.addView(tvQty);

                                    final EditText Qty = new EditText(TransationActivity.this);
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

                                    dialogs.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Boolean wantToCloseDialog = false;
                                        }
                                    });
                                }


                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                    //SwipeableItemViewHolder.setSwipeItemHorizontalSlideAmount()
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                 //   alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
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
                        p.setColor(Color.parseColor("#303F9F"));
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


    public void loaddata() {

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


   /* public  void saveSharedPreferencesLogList(Context context, List<transactiondetail> transactionList) {
        SharedPreferences mPrefs = context.getSharedPreferences("PhotoCollage", context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(collageList);
        prefsEditor.putString("myJson", json);
        prefsEditor.commit();
    }*/

    /*

     public  void saveUserName(Context con,String username)
    {
        try
        {
            usernameSharedPreferences= PreferenceManager.getDefaultSharedPreferences(con);
            usernameEditor = usernameSharedPreferences.edit();
            usernameEditor.putInt(PREFS_KEY_SIZE,(USERNAME.size()+1));
            int size=USERNAME.size();//USERNAME is arrayList
            usernameEditor.putString(PREFS_KEY_USERNAME+size,username);
            usernameEditor.commit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    public void loadUserName(Context con)
    {
        try
        {
            usernameSharedPreferences= PreferenceManager.getDefaultSharedPreferences(con);
            size=usernameSharedPreferences.getInt(PREFS_KEY_SIZE,size);
            USERNAME.clear();
            for(int i=0;i<size;i++)
            {
                String username1="";
                username1=usernameSharedPreferences.getString(PREFS_KEY_USERNAME+i,username1);
                USERNAME.add(username1);
            }
            usernameArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, USERNAME);
            username.setAdapter(usernameArrayAdapter);
            username.setThreshold(0);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
     */

/*
    public static void saveSharedPreferencesLogList(Context context, List<String> collageList) {
            SharedPreferences mPrefs = context.getSharedPreferences("PhotoCollage", context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(collageList);
            prefsEditor.putString("myJson", json);
            prefsEditor.commit();
        }
*/

        /*

        public static List<String> loadSharedPreferencesLogList(Context context) {
        List<String> savedCollage = new ArrayList<String>();
        SharedPreferences mPrefs = context.getSharedPreferences("PhotoCollage", context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("myJson", "");
        if (json.isEmpty()) {
            savedCollage = new ArrayList<String>();
        } else {
            Type type = new TypeToken<List<String>>() {
            }.getType();
            savedCollage = gson.fromJson(json, type);
        }

        return savedCollage;
    }
     */

  /*  public void savetocart(Context context, SingleItem singleitems){
        List<SingleItem>  itemList = getItems(context);

        if (itemList == null) {
            itemList = new ArrayList<>();
        }
        itemList.add(singleitems);
        saveitems(context, itemList);

    }*/

    /*private String saveListAsAString(Context context, String key, List<String> value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MODE_SHARED, Context.MODE_PRIVATE);
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        final Gson gson = new Gson();
        String jsonString = gson.toJson(value);
        edit.putString(key,jsonString);
        edit.commit();
        return jsonString;
    }
    */


    /*

    // Create List of users that you want to save
ArrayList userList   = new ArrayList();
userList.add(new Users());
SharedPreferences prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
//save the user list to preference
Editor editor = prefs.edit();
try {
editor.putString("UserList", ObjectSerializer.serialize(userList));
} catch (IOException e) {
e.printStackTrace();
}
editor.commit();
     */


    /*

    ArrayList userList   = new ArrayList();
// Load user List from preferences
SharedPreferences prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
try {
          userList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("UserList", ObjectSerializer.serialize(new ArrayList())));
  } catch (IOException e) {
      e.printStackTrace();
  }
     */


    public void editTransaction(View v ){
        int position=0;

        final ProgressDialog pd = new ProgressDialog(getApplicationContext());
        //pd.setTitle("Please Wait ....");
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.setProgressStyle(android.R.attr.progressBarStyleLarge);

        pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        Transaction currentItem = transactionList.get(position);

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(30, 30, 30, 30);

        TextView title = new TextView(getApplicationContext());
        title.setBackgroundColor(Color.parseColor("#303F9F"));
        title.setText("EDIT TRANSACTION");
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setPadding(30, 30, 30, 30);

        final ScrollView scrollView = new ScrollView(getApplicationContext());


        final TextView tvitem_code = new TextView(getApplicationContext());
        tvitem_code.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tvitem_code.setText("ITEM CODE");
        tvitem_code.setHintTextColor(Color.GRAY);
        layout.addView(tvitem_code);



        final EditText Item_code = new EditText(getApplicationContext());
        Item_code.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Item_code.setText(currentItem.getItem_code());
        Item_code.setHintTextColor(Color.GRAY);
        layout.addView(Item_code);


        final TextView tvprice = new TextView(getApplicationContext());
        tvprice.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tvprice.setText("PRICE");
        tvprice.setHintTextColor(Color.GRAY);
        layout.addView(tvprice);


        final EditText Price = new EditText(getApplicationContext());
        Price.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Price.setText(String.valueOf(currentItem.getPrice()));
        Price.setHintTextColor(Color.GRAY);
        layout.addView(Price);


        final TextView tvqty = new TextView(getApplicationContext());
        tvqty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tvqty.setText("QTY");
        tvqty.setHintTextColor(Color.GRAY);
        layout.addView(tvqty);


        final EditText Qty = new EditText(getApplicationContext());
        Qty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Qty.setText(currentItem.getQty());
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

      /*  LayoutInflater inflater = LayoutInflater.from(this);
        View view2 = inflater.inflate(R.layout.books_list, null);*/
        //   LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view2 = inflater.inflate(R.layout.books_list, null, false);
   /* View view2 = getLayoutInflater().inflate(R.layout.books_list,null);

        ScrollView scrollView = new ScrollView(this);
      *//*  LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(layoutParams);

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(linearParams);*//*

        for (Books list : booksList) {
            String id = String.valueOf(list.getId());
            String Item_name = list.getItem_name();
            String Price = String.valueOf(list.getPrice());
            String file = list.getFile();

            final TextView txtId = (TextView) view2.findViewById(R.id.txtId);
            txtId.setText(id);


            final TextView txtItem_name = (TextView) view2.findViewById(R.id.txtItem_name);
            txtItem_name.setText(Item_name);


            final TextView txtPrice = (TextView) view2.findViewById(R.id.txtPrice);
            txtPrice.setText(Price);

            final ImageView img = (ImageView) view2.findViewById(R.id.file);
            Picasso.with(this).load(URI_SEGMENT_UPLOAD + file).into(img);

        }
        scrollView.addView(linearLayout);





       // sv.addView(linearLayout);
        *//**//*LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(lm);*/


        //  scroll.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.FILL_PARENT,
        // RecyclerView.LayoutParams.FILL_PARENT));

        //    scroll.removeAllViews();





/*   scrollView.addView(view2);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("ITEM LIST");

        alertDialog.setView(view2);

        alertDialog.setNegativeButton("CANCEL", null);
        AlertDialog alert = alertDialog.create();

        alert.show();*//*

        BookService bookService = APIClient.getClient().create(BookService.class);
        Call<ResponseBook> call = bookService.getFindAll();
        call.enqueue(new Callback<ResponseBook>() {

            @Override
            public void onResponse(Call<ResponseBook> call, Response<ResponseBook> response) {
                booksList = response.body().getResult();
            }
            @Override
            public void onFailure(Call<ResponseBook> call, Throwable t) {
                Toast.makeText(TransationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        new AlertDialog.Builder(TransationActivity.this).setView(view2)
                .setTitle("Scroll View")

                .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                   // @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }

                }).show();
*/

    }





