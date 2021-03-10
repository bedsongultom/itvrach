package com.itvrach.www.itvrach;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.itvrach.adapter.CostumGridUpdater_bak;
import com.itvrach.model.Books;
import com.itvrach.model.ResponseBook;
import com.itvrach.model.ResponseTransaction;
import com.itvrach.model.Transaction;
import com.itvrach.services.APIClient;
import com.itvrach.services.BookService;
import com.itvrach.services.TransactionService;
import com.itvrach.sessions.SessionManagement;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.itvrach.services.APIClient.URI_SEGMENT_UPLOAD;

/**
 * Created by engineer on 8/2/2018.
 */

public class ListProfileActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CostumGridUpdater_bak adapter;
    private List<Books> listContentArr= new ArrayList<>();
    private RecyclerView.LayoutManager mManager;
    private DialogInterface dialog;
    private Toolbar toolbar;

    private TextView tv_item_code;
    private TextView tv_price;
    private TextView txt_sessionid;
    private EditText tv_total_price;
    private EditText tv_total_pricex;


    private EditText tv_qty;
    private EditText tv_user_id;

    private int mCartItemCount = 10;
    private EditText edSearch;
    private ImageView imgSearch2, imgBack;
    private Button btn_Add;
    private Context context = this;
    private int amnt,amntp,sub, total_pricex;

    private TextWatcher tw;
    private String total_p;
    private String qty;
    private String price;
    private String subs;
    private String item_code;
    private String total_price;
    private String session_id;

    private String user_id;
    private SessionManagement session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_user_list);
        session = new SessionManagement(getApplicationContext());


        session.checkLogin();



        this.setTitle("");
        final Intent intent = getIntent();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        adapter = new CostumGridUpdater_bak(listContentArr, getApplicationContext());
        mManager = new GridLayoutManager(this, 3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setAdapter(adapter);

        final EditText tv_sessionid = new EditText(ListProfileActivity.this);
        final EditText tv_total_price = new EditText(ListProfileActivity.this);
        final EditText tv_total_pricex = new EditText(ListProfileActivity.this);

     //   txt_sessionid = (TextView) findViewById(R.id.txt_sessionid);

        tv_total_pricex.setEnabled(false);
        tv_total_pricex.setBackgroundResource(R.drawable.underline);




        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, final int position) {

                final ProgressDialog pd = new ProgressDialog(context);
                pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                pd.getWindow().setGravity(Gravity.CENTER);
                pd.setProgressStyle(android.R.attr.progressBarStyleLarge);

                pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                final Books item = listContentArr.get(position);
                adapter.editItem(position);
                adapter.notifyItemChanged(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(ListProfileActivity.this);

                final LinearLayout layout = new LinearLayout(ListProfileActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CLIP_VERTICAL);
                layout.setPadding(30, 30, 30, 30);

                TextView title = new TextView(ListProfileActivity.this);
                title.setBackgroundColor(Color.parseColor("#303F9F"));
                title.setText("SAVE TO CARTS");
                title.setTextColor(Color.WHITE);
                title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                title.setPadding(30, 30, 30, 30);

                final ScrollView scrollView = new ScrollView(ListProfileActivity.this);

                final ImageView image_file = new ImageView(ListProfileActivity.this);
                image_file.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Picasso.with(view.getContext()).load(URI_SEGMENT_UPLOAD + item.getFile()).into(image_file);
                layout.addView(image_file);

                //visible view of user_id
                TextView user_idtxt = new TextView(ListProfileActivity.this);
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
                final EditText tv_user_id = new EditText(ListProfileActivity.this);
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


                TextView session_idtxt = new TextView(ListProfileActivity.this);
                session_idtxt.setText("Session ID");
                session_idtxt.setTextColor(Color.GRAY);
                layout.addView(session_idtxt);
                session_idtxt.setVisibility(View.GONE);

                //  tv_sessionid.setEnabled(false);
                tv_sessionid.setBackgroundResource(R.drawable.underline);
                tv_sessionid.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tv_sessionid.setText(sessionid);
                tv_sessionid.setTextColor(Color.BLACK);
                if (tv_sessionid.getParent() != null) {
                    ((ViewGroup) tv_sessionid.getParent()).removeView(tv_sessionid); // <- fix
                }
                layout.addView(tv_sessionid);
                tv_sessionid.setVisibility(View.GONE);


                TextView item_producttxt = new TextView(ListProfileActivity.this);
                item_producttxt.setText("Item Product");
                item_producttxt.setTextColor(Color.GRAY);
                layout.addView(item_producttxt);


                final EditText tv_item_code = new EditText(ListProfileActivity.this);
                tv_item_code.setEnabled(false);
                tv_item_code.setBackgroundResource(R.drawable.underline);

                tv_item_code.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tv_item_code.setText(String.valueOf(item.getItem_code()));
                tv_item_code.setTextColor(Color.BLACK);
                // tv_item_code.setHintTextColor(Color.BLACK);
                layout.addView(tv_item_code);


                TextView item_pricetxt = new TextView(ListProfileActivity.this);
                item_pricetxt.setText("Price");
                item_pricetxt.setTextColor(Color.GRAY);
                layout.addView(item_pricetxt);

                final EditText tv_price = new EditText(ListProfileActivity.this);
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


                TextView item_qtytxt = new TextView(ListProfileActivity.this);
                item_qtytxt.setText("Qty");
                item_qtytxt.setTextColor(Color.GRAY);
                layout.addView(item_qtytxt);

                final EditText tv_qty = new EditText(ListProfileActivity.this);


                tv_qty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tv_qty.setHintTextColor(Color.BLACK);
                layout.addView(tv_qty);
                tv_qty.requestFocus();


                TextView item_totaltxt = new TextView(ListProfileActivity.this);
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
                        sub = 0;
                        total_pricex = 0;


                        try {
                            amnt = Integer.parseInt((qty));
                            amntp = Integer.parseInt((price));
                            sub = amnt * amntp;
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

               /*
                This below is solusution for handler error of
                The specified child already has a parent.
                You must call removeView() on the child's parent first (Android)

                */
                if (tv_total_pricex.getParent() != null) {
                    ((ViewGroup) tv_total_pricex.getParent()).removeView(tv_total_pricex); // <- fix
                }
                layout.addView(tv_total_pricex);




               /* tv_qty.addTextChangedListener(new TextWatcher(){
                boolean flag;
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
                            try {
                                amnt = Integer.parseInt(qty);
                                amntp = Integer.parseInt(price);
                                sub = amnt * amntp;
                            } catch (NumberFormatException e) {
                                s.clear();
                            }
                            total = String.valueOf(sub);

                                final EditText tv_total = new EditText(ListProfileActivity.this);
                                tv_total.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                tv_total.setHintTextColor(Color.GRAY);
                               // tv_total.requestFocus();

                                tv_qty.removeTextChangedListener(this);
                                if(!flag) {
                                  flag = true;
                                    tv_total.setText(total);
                                    flag = false;
                                tv_qty.addTextChangedListener(this);
                               }
                        layout.addView(tv_total);




                    }
                });*/


                scrollView.addView(layout);
                builder.setView(scrollView);
                builder.setCustomTitle(title);
                builder.setCancelable(false);
                builder.setPositiveButton("SAVE",
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

                //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));



                /*builder.setPositiveButton("SAVE",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                Boolean wantToCloseDialog = false;

                                session_id         = tv_sessionid.getText().toString();
                                user_id            = tv_user_id.getText().toString();
                                item_code          = tv_item_code.getText().toString();
                                qty                = tv_qty.getText().toString();
                                price              = tv_price.getText().toString().replaceAll("[^0-9]", "");
                                total_price        = tv_total_price.getText().toString().replaceAll("[^0-9]", "");//v_total_price.setText(total_price)

                                if(qty == null || qty.trim().length() ==0 || qty.isEmpty()){
                                    Toast.makeText(ListProfileActivity.this, "Password can't be Empty", Toast.LENGTH_SHORT).show();
                                    tv_qty.requestFocus();
                                    return;


                                }else{

                                pd.show();
                                pd.getWindow().setLayout(245,200);
                                pd.getWindow().setGravity(Gravity.CENTER);


                                final Transactiondetail transactiondetail = new Transactiondetail();
                                transactiondetail.setSession_id(String.valueOf(session_id));
                                transactiondetail.setUser_id(Integer.parseInt(user_id));
                                transactiondetail.setItem_code(item_code);
                                transactiondetail.setQty(Integer.parseInt(qty));
                                transactiondetail.setPrice(Integer.parseInt(price));
                                transactiondetail.setTotal_price(Integer.parseInt(total_price));

                                TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
                                Call<ResponseTransaction> call = transactionService.Create(transactiondetail);
                                call.enqueue(new Callback<ResponseTransaction>() {

                                    @Override
                                    public void onResponse(Call<ResponseTransaction> call, Response<ResponseTransaction> response) {
                                        if (response.isSuccessful()) {
                                            //  mProgress.dismiss();
                                            ResponseTransaction responseTransaction = response.body();
                                            if (responseTransaction.getSuccess().equals("true")) {
                                                Toast.makeText(ListProfileActivity.this, responseTransaction.getMessage(), Toast.LENGTH_SHORT).show();
                                                pd.dismiss();
                                                dialog.dismiss();

                                            } else {
                                                Toast.makeText(ListProfileActivity.this, (CharSequence) responseTransaction.getMessage(), Toast.LENGTH_SHORT).show();
                                                pd.dismiss();
                                            }
                                        } else {
                                            Toast.makeText(ListProfileActivity.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                                            pd.dismiss();
                                        }
                                    }


                                    @Override
                                    public void onFailure(Call<ResponseTransaction> call, Throwable t) {
                                        Toast.makeText(ListProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        // mProgress.dismiss();
                                        pd.dismiss();
                                    }

                                });

                                }

                                if (wantToCloseDialog) {
                                    dialog.dismiss();
                                }
                            }
                        });



                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton*/


                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                // positiveButton.setPadding(20,20,20,40);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                params.weight = 14.0f;
                //params.gravity = Gravity.CENTER_HORIZONTAL; //this is layout_gravity
                positiveButton.setGravity(Gravity.CENTER);
                positiveButton.setLayoutParams(params);
                positiveButton.setBackgroundColor(Color.WHITE);//.parseColor("#F39C12"));
                positiveButton.setTextColor(Color.GRAY);
                positiveButton.invalidate();


                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                // negativeButton.setPadding(20,20,20,40);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                // positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                params1.weight = 14.0f;
                //params1.gravity= Gravity.CENTER;
                negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
                negativeButton.setLayoutParams(params1);
                negativeButton.setBackgroundColor(Color.WHITE);//parseColor("#FFFFFF"));
                negativeButton.setTextColor(Color.GRAY);
                negativeButton.invalidate();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean wantToCloseDialog = false;

                        session_id = tv_sessionid.getText().toString();
                        user_id = tv_user_id.getText().toString();
                        item_code = tv_item_code.getText().toString();
                        qty = tv_qty.getText().toString();
                        price = tv_price.getText().toString().replaceAll("[^0-9]", "");
                        total_price = tv_total_price.getText().toString().replaceAll("[^0-9]", "");//v_total_price.setText(total_price)

                        if (qty == null || qty.trim().length() == 0 || qty.isEmpty() || qty.equals("0")) {
                            Toast.makeText(ListProfileActivity.this, "Qty can't be Empty or Zero ", Toast.LENGTH_SHORT).show();
                            tv_qty.requestFocus();
                            return;
                        }
                         else {

                            pd.show();
                            pd.getWindow().setLayout(245, 200);
                            pd.getWindow().setGravity(Gravity.CENTER);

                            final Transaction transaction = new Transaction();
                            transaction.setSession_id(String.valueOf(session_id));
                            transaction.setUser_id(Integer.parseInt(user_id));
                            transaction.setItem_code(item_code);
                            transaction.setQty(Integer.parseInt(qty));
                            transaction.setPrice(Integer.parseInt(price));
                            transaction.setTotal_price(Integer.parseInt(total_price));

                            TransactionService transactionService = APIClient.getClient().create(TransactionService.class);
                            Call<ResponseTransaction> call = transactionService.Create(transaction);
                            call.enqueue(new Callback<ResponseTransaction>() {

                                @Override
                                public void onResponse(Call<ResponseTransaction> call, Response<ResponseTransaction> response) {
                                    if (response.isSuccessful()) {
                                        //  mProgress.dismiss();
                                        ResponseTransaction responseTransaction = response.body();
                                        if (responseTransaction.getSuccess().equals("true")) {
                                            Toast.makeText(ListProfileActivity.this, responseTransaction.getMessage(), Toast.LENGTH_SHORT).show();
                                            pd.dismiss();
                                            dialog.dismiss();

                                        } else {
                                            Toast.makeText(ListProfileActivity.this, (CharSequence) responseTransaction.getMessage(), Toast.LENGTH_SHORT).show();
                                            pd.dismiss();
                                        }
                                    } else {
                                        Toast.makeText(ListProfileActivity.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }
                                }


                                @Override
                                public void onFailure(Call<ResponseTransaction> call, Throwable t) {
                                    //Toast.makeText(ListProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(ListProfileActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();
                                    // mProgress.dismiss();
                                    pd.dismiss();
                                }

                            });

                        }

                        if (wantToCloseDialog) {
                            dialog.dismiss();
                        }
                    }
                });



            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));

            ImageView picture=(ImageView)view.findViewById(R.id.img_file);
                picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* Toast.makeText(ListProfileActivity.this, "Single Click on Image :"+position,
                                Toast.LENGTH_SHORT).show();*/
                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {
              /*  Toast.makeText(ListProfileActivity.this, "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();*/
            }
        }));



        BookService bookService = APIClient.getClient().create(BookService.class);
        Call<ResponseBook> call = bookService.getFindAll();
        call.enqueue(new Callback<ResponseBook>() {
            @Override
            public void onResponse(Call<ResponseBook> call, Response<ResponseBook> response) {
                listContentArr = response.body().getResult();
                adapter = new CostumGridUpdater_bak(listContentArr, getApplicationContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseBook> call, Throwable t) {
                //Toast.makeText(ListProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(ListProfileActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();
            }
        });




        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        // toolbar.setNavigationIcon(R.drawable.back);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        edSearch = (EditText) findViewById(R.id.edSearch);


        final Drawable dw = getApplicationContext().getResources().getDrawable(R.drawable.ic_search);
        edSearch.setCompoundDrawablesWithIntrinsicBounds(dw, null, null, null);
        edSearch.setVisibility(View.VISIBLE);
        imgBack.setVisibility(View.GONE);

        edSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edSearch.setEnabled(true);
                    //toolbar.setNavigationIcon(R.drawable.back);
                    imgBack.setVisibility(v.VISIBLE);
                    Drawable dw = getApplicationContext().getResources().getDrawable(R.drawable.ic_search);
                    edSearch.setCompoundDrawables(null, null, null, null);
                } else {

                    Drawable dw = getApplicationContext().getResources().getDrawable(R.drawable.ic_search);
                    edSearch.setCompoundDrawables(dw, null, null, null);
                    imgBack.setVisibility(v.GONE);
                    //    toolbar.setNavigationIcon(null);
                }

            }

        });

        ImageView img_file = new ImageView (this);//findViewById(R.id.img_file) ;
        img_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListProfileActivity.this, "show", Toast.LENGTH_SHORT).show();
            }
        });



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* edSearch.setVisibility(v.GONE);
                Drawable dw = getApplicationContext().getResources().getDrawable(R.drawable.ic_search);
                edSearch.setCompoundDrawablesWithIntrinsicBounds(dw, null, null, null);
                edSearch.setVisibility(v.VISIBLE);
                imgBack.setVisibility(v.GONE);*/

                Intent intent = new Intent(ListProfileActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

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

        /*imgSearch2 = (ImageView) findViewById(R.id.imgSearch2);
        imgSearch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/
        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 imgSearch2.setOnClickListener(this);

                     }
                 });
*/

        /*Products product;
        List<Products> listdata=new ArrayList<>();
        for(int i=0;i<10;i++)
        {
            product = new Products(productNames[i],productImages[i]);
            listdata.add(product);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecyclerAdapter(this,listdata);
        mRecyclerView.setAdapter(mAdapter);*/
    }
/*
public void addbutton(){
    btn_Add
        @Override
        public void onClick(View view, int position) {
            final City city = cities.get(position);
            Intent i = new Intent(this, CityviewActivity.class);

            final int position = viewHolder.getAdapterPosition();
            Books item = booksList.get(position);
            adapter.editItem(position);
            adapter.notifyItemChanged(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            LinearLayout layout = new LinearLayout(view.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CLIP_VERTICAL);
            layout.setPadding(30, 30, 30, 30);

            TextView title = new TextView(context);
            title.setBackgroundColor(Color.parseColor("#303F9F"));
            title.setText("SAVE TO CARTS");
            title.setTextColor(Color.WHITE);
            title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            title.setPadding(30, 30, 30, 30);

            final ScrollView scrollView = new ScrollView(view.getContext());

            final EditText tv_email = new EditText(view.getContext());
            tv_email.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tv_email.setText(String.valueOf(item.getItem_name()));
            tv_email.setHintTextColor(Color.GRAY);
            layout.addView(tv_email);


            final ImageView image_file = new ImageView(view.getContext());
            image_file.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            Picasso.with(view.getContext()).load(URI_SEGMENT_UPLOAD + item.getFile()).into(image_file);
            layout.addView(image_file);


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
            dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));

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


    });
}
*/
    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
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




    /*  private void calculate()
    {
//remove the Editable thing declaration...
        heightInt = Double.parseDouble(height.getText().toString());
        wightInt = Double.parseDouble(weight.getText().toString());
        result = weightInt / (heightInt * heightInt);
        String textResult = "Your BMI is " + result;
        tvResult.setText(textResult);

    }*/
 /*   private void calculateAndShow(String price, String qty, double newRate){
        final EditText tv_qty = new EditText(ListProfileActivity.this);
        final EditText tv_price = new EditText(ListProfileActivity.this);
        double qty     = Double.parseDouble(tv_qty.getText().toString());
        double price   = Double.parseDouble((tv_price.getText().toString()));
        double Total = qty * price;
        tv_total.setText(String.valueOf(Total));

    }*/




    public void filter(String newText) {
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

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
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


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(MainActivity.this,cart_count,R.drawable.ic_shopping_cart_white_24dp));
        MenuItem menuItem2 = menu.findItem(R.id.notification_action);
        menuItem2.setIcon(Converter.convertLayoutToImage(MainActivity.this,2,R.drawable.ic_notifications_white_24dp));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddProduct() {
        cart_count++;
        invalidateOptionsMenu();
        Snackbar.make((CoordinatorLayout)findViewById(R.id.parentlayout), "Added to cart !!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();


    }

    @Override
    public void onRemoveProduct() {
        cart_count--;
        invalidateOptionsMenu();
        Snackbar.make((CoordinatorLayout)findViewById(R.id.parentlayout), "Removed from cart !!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
}*/


   /*private void tedts(){
       Drawable dw = getApplicationContext().getResources().getDrawable(R.drawable.ic_search);
       edSearch.setCompoundDrawables( dw, null,null , null );


   }*/


 /*  private void loadDatafromServer() {

        RecyclerView.LayoutManager mManager = new  GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mManager);
        mManager = new GridLayoutManager(this, 3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setAdapter(adapter);

        BookService bookService = APIClient.getClient().create(BookService.class);
        Call<ResponseBook> call = bookService.getFindAll();
        call.enqueue(new Callback<ResponseBook>() {
            @Override
            public void onResponse(Call<ResponseBook> call, Response<ResponseBook> response) {
                booksList = response.body().getResult();
                adapter = new CostumGridUpdater_bak(getApplicationContext(), booksList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }


            @Override
            public void onFailure(Call<ResponseBook> call, Throwable t) {
                Toast.makeText(ListProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
*/
   /* @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgSearch2) {
            Drawable dw = getApplicationContext().getResources().getDrawable(R.drawable.ic_search);
            edSearch.setCompoundDrawablesWithIntrinsicBounds(dw, null, null, null);
            edSearch.setVisibility(v.VISIBLE);
        }
        if (v.getId() == R.id.imgBack) {
            edSearch.setVisibility(v.GONE);
            Drawable dw = getApplicationContext().getResources().getDrawable(R.drawable.ic_search);
            edSearch.setCompoundDrawablesWithIntrinsicBounds(dw, null, null, null);
            edSearch.setVisibility(v.VISIBLE);
            imgBack.setVisibility(v.GONE);

        }
        if(v.getId()==R.id.btnAdd){
            addbutton();
        }

    }*/


}

