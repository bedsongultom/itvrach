package com.itvrach.www.itvrach;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.viewtooltip.ViewTooltip;
import com.itvrach.adapter.CostumGridUpdater;
import com.itvrach.adapter.PagerAdapter2;
import com.itvrach.adapter.PagerAdapter3;
import com.itvrach.adapter.PagerAdapter1;
import com.itvrach.adapter.PagerAdapter4;
import com.itvrach.adapter.PagerAdapter5;
import com.itvrach.adapter.PagerAdapter6;
import com.itvrach.adapter.PagerAdapter7;
import com.itvrach.model.Books;
import com.itvrach.model.ResponseModel;
import com.itvrach.model.User;
import com.itvrach.services.APIClient;
import com.itvrach.services.UserService;
import com.itvrach.sessions.SessionManagement;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static com.itvrach.services.APIClient.URI_SEGMENT_UPLOAD;
import static com.itvrach.services.APIClient.serverDisconnect;

/**
 * Created by engineer on 1/23/2019.
 */

public class Welcome extends AppCompatActivity {

    private SearchView searchView;

    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private static final String TAG = "BottomNavigation";
    private String TAG_WELCOME = "Welcome";
    @ColorInt
    public static final int BLUE = 0xFF0FB8B3;
    @ColorInt
    public static final int GREEN = 0xFF5BBD76;
    private ViewTooltip tooltipView = null;
    private SessionManagement session;
    private EditText editTextUser_id;
    private TextView textViewEmail, txtsession_id;
    private ProgressDialog mProgress;
    private String email;
    private TextView  txtCount;
    private String Mytitle;
    private Drawable iconX, iconY;
    private ImageView imgCarts;
    private FragmentTransaction ft;
    private String text;
    private Context context;
    private Bitmap bitmap;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    public TextView tvTitle, tvInbox;
    public ImageView imgFile;

    private EditText edSearch;

    private TabLayout tabLayout1, tabLayout2;

    private TabLayout tabShippingLayout, tabInboxLayout, tabApprovedLayout, tabChartLayout, tabReportsLayout;
    private SwipeDisabledViewPager viewPagerShipping, viewPagerInbox, viewPagerApproved, viewPagerChart, viewPagerReports;





    private SwipeDisabledViewPager viewPager1, viewPager2;
    private PagerAdapter2 pagerAdapter2;
    private PagerAdapter1 pagerAdapter1;

    private   PagerAdapter3 pagerAdapter3;
    private   PagerAdapter4 pagerAdapter4;
    private   PagerAdapter5 pagerAdapter5;
    private   PagerAdapter6  pagerAdapter6;
    private   PagerAdapter7 pagerAdapter7;


    private LinearLayout lltab1;
    private LinearLayout shippingLinearLayout, inboxLinearLayout, approvedLinearLayout, chartLinearLayout, reportsLinearLayout;


    private int[] tabIcons = {
            R.drawable.ic_product,
            R.drawable.ic_product,
            R.drawable.ic_product,
            R.drawable.ic_map,
            R.drawable.ic_find,
            R.drawable.ic_local_shipping,
            R.drawable.ic_inbox,
            R.drawable.ic_check_black,
            R.drawable.ic_report,
            R.drawable.ic_chart,
            R.drawable.ic_pie_chart,
            R.drawable.ic_multiline_chart

    };

    private MenuItem settingsItem;
    private MenuItem searchItem;
    private MenuItem cartItem;
    private Menu overflow;
    private String fullname;
    private String hp;
    private String address;
    private String username;
    private String type;
    private String firstname;
    private String lastname;
    private String emails;

    CostumGridUpdater adapter;
    private List<Books> listContentArr = new ArrayList<>();

    private static final int REQUEST_CAMERA =1;
    private ZXingScannerView scannerView;


    public View myView, badge, v;
    private boolean isUp;
    public TextView tv_disconnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        session = new SessionManagement(getApplicationContext());
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        final String userid         = user.get(SessionManagement.KEY_USERID);
        final String username       = user.get(SessionManagement.KEY_USERNAME);
        final String type           = user.get(SessionManagement.KEY_TYPE);
        final String firstname      = user.get(SessionManagement.KEY_FIRSTNAME);
        final String lastname       = user.get(SessionManagement.KEY_LASTNAME);
        final String fullname       = user.get(SessionManagement.KEY_FULLNAME);
        final String emailid        = user.get(SessionManagement.KEY_EMAIL);
        final String sessionid      = user.get(SessionManagement.KEY_SESSIONID);
        final String fileImg        = user.get(SessionManagement.KEY_FILE);
        final String hp             = user.get(SessionManagement.KEY_HP);
        final String address        = user.get(SessionManagement.KEY_ADDRESS);

        addFragment();


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);






        tvTitle  = (TextView)findViewById(R.id.tvTitle);
        imgFile =  (ImageView) findViewById(R.id.imgFile);
        Picasso picasso = Picasso.with(this);
        picasso.load(URI_SEGMENT_UPLOAD + fileImg).resize(80, 80).into(imgFile);
        tvTitle.setTextSize(14);
        tvTitle.setText(emailid);


        //noinspection deprecation
        iconX = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_white_m);
        //noinspection deprecation
        iconY = getApplicationContext().getResources().getDrawable(R.drawable.ic_close_white_m);
        myView = findViewById(R.id.my_view);
        tv_disconnected = (TextView) findViewById(R.id.tv_disconnected);
        tv_disconnected.setText(serverDisconnect);


      /*  edSearch = (EditText) findViewById(R.id.edSearch);
        edSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                for (UnderlineSpan span : s.getSpans(0, s.length(), UnderlineSpan.class)) {
                    s.removeSpan(span);

                }

                sendDataFromEdSearch();

            }
        });


            edSearch.setVisibility(View.GONE);
*/




       /* edSearch.addTextChangedListener(new TextWatcher() {

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



            }
        });
*/




        mProgress =new ProgressDialog(Welcome.this, R.style.AppCompatAlertDialogStyle);
        String titleId = "Processing...";
        mProgress.setTitle(titleId);
        mProgress.setMessage("Logging out...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

       /* FragmentTransaction ft;
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, new Home());
        ft.commit();
*/




        //frame_tab1 = (FrameLayout) findViewById(R.id.frame_tab1);
        lltab1 = (LinearLayout) findViewById(R.id.lltab1);
       // disableTabLayout1Off();
       // disableTabLayout2Off();

        tabLayout1 = (TabLayout) findViewById(R.id.tabLayout1);
        tabLayout1.setTabGravity(TabLayout.GRAVITY_FILL);

        shippingLinearLayout = (LinearLayout) findViewById(R.id.shippingLinearLayout);
        inboxLinearLayout    = (LinearLayout) findViewById(R.id.inboxLinearLayout);
        approvedLinearLayout = (LinearLayout) findViewById(R.id.approvedLinearLayout);
        chartLinearLayout    = (LinearLayout) findViewById(R.id.chartLinearLayout);
        reportsLinearLayout  = (LinearLayout) findViewById(R.id.reportsLinearLayout);





        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);


        pagerAdapter1 = new PagerAdapter1(getSupportFragmentManager(), this);
        pagerAdapter1.addFragment1(new Search(), "PHP", tabIcons[0]);
        pagerAdapter1.addFragment1(new Search(), " Android", tabIcons[1]);
        pagerAdapter1.addFragment1(new Search(), "Javascript", tabIcons[2]);

        viewPager1 = (SwipeDisabledViewPager) findViewById(R.id.pager1);
        viewPager1.setAdapter(pagerAdapter1);
        tabLayout1.setupWithViewPager(viewPager1);
        viewPager1.setVisibility(View.GONE);


        viewPager1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout1));
        tabLayout1.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab1) {
                viewPager1.setCurrentItem(tab1.getPosition());

                View view = tab1.getCustomView();

                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear1);
                TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
                tabTextView.setTextColor(getResources().getColor(R.color.tab_text_selected));
                ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
                //tabImageView.setColorFilter(getResources().getColor(R.color.tab_text_selected), PorterDuff.Mode.SRC_ATOP);
                tabImageView.setColorFilter(Color.parseColor("#F39C12"), PorterDuff.Mode.SRC_ATOP);
//                tab1.getIcon().setColorFilter(Color.parseColor("#FF684E"), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab1) {
                //tab.getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);

                View view = tab1.getCustomView();
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear1);
                TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
                tabTextView.setTextColor(getResources().getColor(R.color.tab_text_unselected));
                ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
                tabImageView.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab1) {

            }

        });



        tabReportsLayout  = (TabLayout) findViewById(R.id.tabReportsLayout);
        tabReportsLayout.setVisibility(View.GONE);

        pagerAdapter7 = new PagerAdapter7(getSupportFragmentManager(), this);
        pagerAdapter7.addFragment7(new StockReport(), R.string.stock, tabIcons[8]);
        pagerAdapter7.addFragment7(new SellingReport(), R.string.selling, tabIcons[8]);
       /* pagerAdapter7.addFragment7(new StockReport(), R.string.buying, tabIcons[8]);
        pagerAdapter7.addFragment7(new StockReport(), R.string.income,tabIcons[8]);*/




        viewPagerReports = (SwipeDisabledViewPager) findViewById(R.id.pagerReports);
        viewPagerReports.setPagingEnabled(false);
        viewPagerReports.setAdapter(pagerAdapter7);
        tabReportsLayout.setupWithViewPager(viewPagerReports);
        viewPagerReports.setVisibility(View.GONE);
        viewPagerReports.setCurrentItem(0);

        viewPagerReports.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabReportsLayout));
        tabReportsLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab7) {
                viewPagerReports.setCurrentItem(tab7.getPosition());


                View view = tab7.getCustomView();
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear1);
                TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
                tabTextView.setTextColor(getResources().getColor(R.color.tab_text_selected));
                ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
                tabImageView.setColorFilter(Color.parseColor("#F39C12"), PorterDuff.Mode.SRC_ATOP);
                tabImageView.setVisibility(View.GONE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab7) {
                View view = tab7.getCustomView();
                //   LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear1);
                TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
                tabTextView.setTextColor(getResources().getColor(R.color.tab_text_unselected));
                ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
                tabImageView.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                tabImageView.setVisibility(View.GONE);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab6) {

            }
        });










        tabChartLayout  = (TabLayout) findViewById(R.id.tabChartLayout);
        tabChartLayout.setVisibility(View.GONE);

        pagerAdapter6 = new PagerAdapter6(getSupportFragmentManager(), this);
        pagerAdapter6.addFragment6(new BarChartFragment(), "Bar Chart", tabIcons[9]);
        pagerAdapter6.addFragment6(new PieChartFragment(), "Pie Cart", tabIcons[10]);
        pagerAdapter6.addFragment6(new LineChartFragment(), "Line Cart", tabIcons[11]);


        viewPagerChart = (SwipeDisabledViewPager) findViewById(R.id.pagerChart);
        viewPagerChart.setPagingEnabled(false);
        viewPagerChart.setAdapter(pagerAdapter6);
        tabChartLayout.setupWithViewPager(viewPagerChart);
        viewPagerChart.setVisibility(View.GONE);
       // highLightCurrentApprovedTab(0);
        viewPagerChart.setCurrentItem(0);

        viewPagerChart.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabChartLayout));
        tabChartLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab6) {
                viewPagerChart.setCurrentItem(tab6.getPosition());
                View view = tab6.getCustomView();
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear1);
                TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
                tabTextView.setTextColor(getResources().getColor(R.color.tab_text_selected));
                ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
                tabImageView.setColorFilter(Color.parseColor("#F39C12"), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab6) {
                View view = tab6.getCustomView();
                //   LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear1);
                TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
                tabTextView.setTextColor(getResources().getColor(R.color.tab_text_unselected));
                ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
                tabImageView.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab6) {

            }
        });








        tabApprovedLayout  = (TabLayout) findViewById(R.id.tabApprovedLayout);
        tabApprovedLayout.setVisibility(View.GONE);
        pagerAdapter5 = new PagerAdapter5(getSupportFragmentManager(), this);
        pagerAdapter5.addFragment5(new Approved(), "Approved", tabIcons[7]);
        pagerAdapter5.addFragment5(new Approved(), "Approved History", tabIcons[8]);
        viewPagerApproved = (SwipeDisabledViewPager) findViewById(R.id.pager5);
        viewPagerApproved.setPagingEnabled(false);
        viewPagerApproved.setAdapter(pagerAdapter5);
        tabApprovedLayout.setupWithViewPager(viewPagerApproved);
        viewPagerApproved.setVisibility(View.GONE);
        highLightCurrentApprovedTab(0);
        viewPagerApproved.setCurrentItem(0);
        viewPagerApproved.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabApprovedLayout));
        tabApprovedLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab5) {
                viewPagerApproved.setCurrentItem(tab5.getPosition());
                View view = tab5.getCustomView();
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear1);
                TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
                tabTextView.setTextColor(getResources().getColor(R.color.tab_text_selected));
                ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
                tabImageView.setColorFilter(Color.parseColor("#F39C12"), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab5) {
                View view = tab5.getCustomView();
                //   LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear1);
                TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
                tabTextView.setTextColor(getResources().getColor(R.color.tab_text_unselected));
                ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
                tabImageView.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab5) {

            }
        });









        tabInboxLayout  = (TabLayout) findViewById(R.id.tabInboxLayout);
        tabInboxLayout.setVisibility(View.GONE);

        pagerAdapter4 = new PagerAdapter4(getSupportFragmentManager(), this);
        pagerAdapter4.addFragment4(new Inbox(), "Inbox", tabIcons[6]);
        pagerAdapter4.addFragment4(new More(), "Transaction History", tabIcons[8]);


        viewPagerInbox = (SwipeDisabledViewPager) findViewById(R.id.pager4);
        viewPagerInbox.setPagingEnabled(false);
        viewPagerInbox.setAdapter(pagerAdapter4);
        tabInboxLayout.setupWithViewPager(viewPagerInbox);
        viewPagerInbox.setVisibility(View.GONE);
        highLightCurrentInboxTab(0);
        viewPagerInbox.setCurrentItem(0);
        viewPagerInbox.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabInboxLayout));
        tabInboxLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab4) {
                viewPagerInbox.setCurrentItem(tab4.getPosition());


                View view = tab4.getCustomView();
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear1);
                TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
                tabTextView.setTextColor(getResources().getColor(R.color.tab_text_selected));
                ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
                tabImageView.setColorFilter(Color.parseColor("#F39C12"), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab4) {
                View view = tab4.getCustomView();
                //   LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear1);
                TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
                tabTextView.setTextColor(getResources().getColor(R.color.tab_text_unselected));
                ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
                tabImageView.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab4) {

            }
        });



        tabShippingLayout = (TabLayout) findViewById(R.id.tabShippingLayout);
        tabShippingLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabShippingLayout.setVisibility(View.GONE);

        pagerAdapter3 = new PagerAdapter3(getSupportFragmentManager(), this);
        pagerAdapter3.addFragment3(new ShippingFragment(), "Shipping", tabIcons[5]);
        pagerAdapter3.addFragment3(new More(), "Location", tabIcons[3]);
       // highLightCurrentShippingTab(0);
        viewPagerShipping = (SwipeDisabledViewPager) findViewById(R.id.pager3);
        viewPagerShipping.setCurrentItem(0);

        viewPagerShipping.setPagingEnabled(false);
        viewPagerShipping.setAdapter(pagerAdapter3);
        tabShippingLayout.setupWithViewPager(viewPagerShipping);
        viewPagerShipping.setVisibility(View.GONE);

        viewPagerShipping.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabShippingLayout));
        tabShippingLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab3) {
                viewPagerShipping.setCurrentItem(tab3.getPosition());


                View view = tab3.getCustomView();
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear1);
                TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
                tabTextView.setTextColor(getResources().getColor(R.color.tab_text_selected));
                ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
                tabImageView.setColorFilter(Color.parseColor("#F39C12"), PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab3) {
                View view = tab3.getCustomView();
             //   LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear1);
                TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
                tabTextView.setTextColor(getResources().getColor(R.color.tab_text_unselected));
                ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
                tabImageView.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab3) {

            }
        });




        tabLayout2 = (TabLayout) findViewById(R.id.tabLayout2);
        tabLayout2.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout2.setVisibility(View.GONE);

        pagerAdapter2 = new PagerAdapter2(getSupportFragmentManager(), this);
        pagerAdapter2.addFragment2(new Transactiondetail(), "Transaction", tabIcons[2]);
        pagerAdapter2.addFragment2(new Profile(), " Payment", tabIcons[3]);

        viewPager2 = (SwipeDisabledViewPager) findViewById(R.id.pager2);
        viewPager2.setPagingEnabled(false);
        viewPager2.setAdapter(pagerAdapter2);
        tabLayout2.setupWithViewPager(viewPager2);
        viewPager2.setVisibility(View.GONE);


        viewPager2.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout2));
        tabLayout2.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab2) {
                viewPager2.setCurrentItem(tab2.getPosition());

                View view = tab2.getCustomView();

                //    LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear1);
                TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);

                tabTextView.setTextColor(getResources().getColor(R.color.tab_text_selected));

                ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
                //tabImageView.setColorFilter(getResources().getColor(R.color.tab_text_selected), PorterDuff.Mode.SRC_ATOP);
                tabImageView.setColorFilter(Color.parseColor("#F39C12"), PorterDuff.Mode.SRC_ATOP);

                //  tab.getIcon().setColorFilter(Color.parseColor("#FF684E"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab2) {
                //tab.getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);

                View view = tab2.getCustomView();
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear1);
                TextView tabTextView = (TextView) view.findViewById(R.id.tabTextView);
                tabTextView.setTextColor(getResources().getColor(R.color.tab_text_unselected));
                ImageView tabImageView = (ImageView) view.findViewById(R.id.tabImageView);
                tabImageView.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab2) {

            }

        });





        /*Drawable dr = getResources().getDrawable(R.drawable.ic_account);

        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        Drawable d = new BitmapDrawable(getResources(),
                Bitmap.createScaledBitmap(bitmap, 40, 40, true));
        d.setAlpha(125);*/

        //   getSupportActionBar().setIcon(R.drawable.ic_menu_white);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);*/











/*
        imgCarts = (ImageView) findViewById(R.id.imgCart);
        imgCarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addFragment();
                FragmentTransaction ft2;
                ft2 = getSupportFragmentManager().beginTransaction();
                Transactiondetail fragment = new Transactiondetail();
                ft2.replace(R.id.fragment_container, fragment);
                ft2.commit();
                highLightCurrentTab2(0);
                viewPager2.setCurrentItem(0);//viewPager.getCurrentItem());

                tabLayout1.setVisibility(View.VISIBLE);
                viewPager1.setVisibility(View.VISIBLE);
                viewPager1.setPagingEnabled(true);
                ((ViewGroup)tabLayout1.getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
                ((ViewGroup)tabLayout1.getChildAt(0)).getChildAt(1).setVisibility(View.VISIBLE);
                tabLayout1.setTabGravity(TabLayout.GRAVITY_FILL);

            }

        });


*/





        editTextUser_id = (EditText) findViewById(R.id.editTextUser_id);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        txtsession_id = (TextView) findViewById(R.id.txtsession_id);

      
        // bottomNavigationView.getMenu().clear();




        editTextUser_id.setText(userid);
        txtsession_id.setText(sessionid);
        txtsession_id.setVisibility(View.GONE);
        textViewEmail.setVisibility(View.GONE);
        editTextUser_id.setVisibility(View.GONE);

       /* TextView tfile = new TextView(Welcome.this);
        tfile.setText(fileImg);
*/
        //  String file = tfile.getText().toString();


      /*  textViewEmail.setText(emailid);
        email = textViewEmail.getText().toString();


        tvTitle.setText("  " + emailid);
        Picasso.with(getApplicationContext()).load(URI_SEGMENT_UPLOAD + fileImg).resize(80, 80).into(imgFile);
*/

        setupBottomNavigationView();


        //   toolbar.setLogo();


        //  Drawable drawble =LoadImageFrom

//     Fimage.setImageURI(Uri.parse(URI_SEGMENT_UPLOAD+file));
//        Picasso.with(Welcome.this).load("http://192.168.43.132/web/api/assets/uploads/aa819b0ae38047cc09b9f218285b3ecf.jpg").resize(48, 48)
        //              .into(Fimage);

        //  URL newurl = null;
        /*try {
          String  newurl = URI_SEGMENT_UPLOAD + file;
            Bitmap micon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            Fimage.setImageBitmap(micon_val);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/


        // getSupportActionBar().setDisplayShowHomeEnabled(true);


        // this.setTitle(" user: "+emailid);
//toolbar.setTitle(Html.fromHtml("<small> user:</small>"+emailid));


      /*  findViewById(R.id.menu_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                          }
        });
*/

      //setupBottomNavigationView();





        // getSupportActionBar().setbundlDefaultDisplayHomeAsUpEnabled(true);
        //  getSupportActionBar().setHomeButtonEnabled(true);


        /*mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout) ;
        mToggle  = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mToggle.setDrawerIndicatorEnabled(true);
      //  mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();*/
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Search fragment = new Search();
        //

    }

    public void enableReportsLayoutTabs(boolean enable) {
        if(enable){
            reportsLinearLayout.setVisibility(View.VISIBLE);
            tabReportsLayout.setVisibility(View.VISIBLE);
            viewPagerReports.setVisibility(View.VISIBLE);

            highLightCurrentReportsTabs(0);
            viewPagerReports.setCurrentItem(0);
            viewPagerReports.setPagingEnabled(true);

            ((ViewGroup) tabReportsLayout.getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
            ((ViewGroup) tabReportsLayout.getChildAt(0)).getChildAt(1).setVisibility(View.VISIBLE);
            tabReportsLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        }else{

            reportsLinearLayout.setVisibility(View.GONE);
            tabReportsLayout.setVisibility(View.GONE);
            viewPagerReports.setVisibility(View.GONE);

            highLightCurrentReportsTabs(0);
            viewPagerReports.setCurrentItem(0);
            viewPagerReports.setPagingEnabled(false);

            ((ViewGroup) tabReportsLayout.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
            ((ViewGroup) tabReportsLayout.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
            tabReportsLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        }

    }


    public void enableInboxLayoutTabs(boolean enable) {
        if(enable){
            inboxLinearLayout.setVisibility(View.VISIBLE);
            tabInboxLayout.setVisibility(View.VISIBLE);
            viewPagerInbox.setVisibility(View.VISIBLE);

            highLightCurrentInboxTab(0);
            viewPagerInbox.setCurrentItem(0);
            viewPagerInbox.setPagingEnabled(true);

            ((ViewGroup) tabInboxLayout.getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
            ((ViewGroup) tabInboxLayout.getChildAt(0)).getChildAt(1).setVisibility(View.VISIBLE);
            tabInboxLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        }else{

            inboxLinearLayout.setVisibility(View.GONE);
            tabInboxLayout.setVisibility(View.GONE);
            viewPagerInbox.setVisibility(View.GONE);

            highLightCurrentInboxTab(0);
            viewPagerInbox.setCurrentItem(0);
            viewPagerInbox.setPagingEnabled(false);

            ((ViewGroup) tabInboxLayout.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
            ((ViewGroup) tabInboxLayout.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
            tabInboxLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        }

    }


    public void enableApprovedLayoutTabs(boolean enable){

        if(enable){
            approvedLinearLayout.setVisibility(View.VISIBLE);
            tabApprovedLayout.setVisibility(View.VISIBLE);
            viewPagerApproved.setVisibility(View.VISIBLE);

            highLightCurrentApprovedTab(0);
            viewPagerApproved.setCurrentItem(0);
            viewPagerApproved.setPagingEnabled(true);

            ((ViewGroup) tabApprovedLayout.getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
            ((ViewGroup) tabApprovedLayout.getChildAt(0)).getChildAt(1).setVisibility(View.VISIBLE);
            tabApprovedLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        }else{

            approvedLinearLayout.setVisibility(View.GONE);
            tabApprovedLayout.setVisibility(View.GONE);
            viewPagerApproved.setVisibility(View.GONE);

            highLightCurrentApprovedTab(0);
            viewPagerApproved.setCurrentItem(0);
            viewPagerApproved.setPagingEnabled(false);

            ((ViewGroup) tabApprovedLayout.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
            ((ViewGroup) tabApprovedLayout.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
            tabApprovedLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        }

    }


    public void enableChartLayoutTabs(boolean enable){

        if(enable){

            chartLinearLayout.setVisibility(View.VISIBLE);
            tabChartLayout.setVisibility(View.VISIBLE);
            viewPagerChart.setVisibility(View.VISIBLE);

            highLightCurrentChartTab(0);
            viewPagerChart.setCurrentItem(0);
            viewPagerChart.setPagingEnabled(true);

            ((ViewGroup) tabChartLayout.getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
            ((ViewGroup) tabChartLayout.getChildAt(0)).getChildAt(1).setVisibility(View.VISIBLE);
            ((ViewGroup) tabChartLayout.getChildAt(0)).getChildAt(2).setVisibility(View.VISIBLE);
            tabChartLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        }else {

            chartLinearLayout.setVisibility(View.GONE);
            tabChartLayout.setVisibility(View.GONE);
            viewPagerChart.setVisibility(View.GONE);
            highLightCurrentChartTab(0);
            viewPagerChart.setCurrentItem(0);
            viewPagerChart.setPagingEnabled(false);
            ((ViewGroup) tabChartLayout.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
            ((ViewGroup) tabChartLayout.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
            ((ViewGroup) tabChartLayout.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            tabChartLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }



    }



    public void enableShippingLayoutTabs(boolean enable){
        if(enable){

            shippingLinearLayout.setVisibility(View.VISIBLE);
            tabShippingLayout.setVisibility(View.VISIBLE);
            viewPagerShipping.setVisibility(View.VISIBLE);

            highLightCurrentShippingTab(0);
            viewPagerShipping.setCurrentItem(0);
            viewPagerShipping.setPagingEnabled(true);

            ((ViewGroup) tabShippingLayout.getChildAt(0)).getChildAt(0).setVisibility(View.VISIBLE);
            ((ViewGroup) tabShippingLayout.getChildAt(0)).getChildAt(1).setVisibility(View.VISIBLE);
            tabShippingLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        }else{

            shippingLinearLayout.setVisibility(View.GONE);
            tabShippingLayout.setVisibility(View.GONE);
            viewPagerShipping.setVisibility(View.GONE);

            highLightCurrentShippingTab(0);
            viewPagerShipping.setCurrentItem(0);
            viewPagerShipping.setPagingEnabled(false);

            ((ViewGroup) tabShippingLayout.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
            ((ViewGroup) tabShippingLayout.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
            tabShippingLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        }

    }


    public void titleId(){
        tvTitle.setVisibility(View.VISIBLE);

    }




    public void slideUp(View view){
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,view.getHeight(),0);
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        view.startAnimation(translateAnimation);
        view.setVisibility(View.GONE);
    }

    public void slideDown(View view){
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0, view.getHeight());
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        view.startAnimation(translateAnimation);
        view.setVisibility(View.GONE);

    }


    public void hideBottomNavigationView(BottomNavigationView view){

        view.animate().translationY(view.getHeight());
    }
    public void showBottomNavigationView(BottomNavigationView view){
        view.animate().translationY(0);
    }



    public void setupBottomNavigationView(){

        ColorStateList iconColorStates = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        },
                new int[]{
                        Color.parseColor("#F39C12"),
                        Color.parseColor("#ffffff")

                });

        ColorStateList textColorStates = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        },
                new int[]{

                        Color.parseColor("#ffffff"),
                       // Color.parseColor("#ffdd00")
                        Color.parseColor("#F39C12")

                });



        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);


/*
        Menu menu  = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(R.id.menu_inbox);

        String before = menuItem.getTitle().toString();
        String counter = Integer.toString(5);

        SpannableString sColored = new SpannableString(counter);
        menuItem.setTitle(sColored);
*/




        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        v = bottomNavigationMenuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        badge = LayoutInflater.from(this).inflate(R.layout.badge_layout_for_inbox, bottomNavigationView, false);
        tvInbox = (TextView) badge.findViewById(R.id.tvInbox);
        itemView.addView(badge);





        /*bottomNavigationView.getMenu().clear();
        bottomNavigationView.inflateMenu(R.menu.navigation);
        bottomNavigationView.getMaxItemCount();
        updateMenu();*/

        bottomNavigationView.setItemIconTintList(iconColorStates);
        bottomNavigationView.setItemTextColor(textColorStates);

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        Fragment selectedFragment = null;

                        switch (item.getItemId()) {

                            case R.id.menu_home:
                                item.setCheckable(true);
                                selectedFragment = new Home();

                                tabLayout1.setVisibility(View.GONE);
                                viewPager1.setVisibility(View.GONE);

                                ((ViewGroup) tabLayout1.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
                                ((ViewGroup) tabLayout1.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                                ((ViewGroup) tabLayout1.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                                tabLayout2.setVisibility(View.GONE);
                                viewPager2.setVisibility(View.GONE);



                                enableShippingLayoutTabs(false);
                                enableChartLayoutTabs(false);
                                enableApprovedLayoutTabs(false);
                                enableInboxLayoutTabs(false);
                                enableReportsLayoutTabs(false);



                                break;

                            case R.id.menu_inbox:
                                item.setCheckable(true);
                                selectedFragment = new Inbox();

                                enableShippingLayoutTabs(false);
                                enableApprovedLayoutTabs(false);
                                enableChartLayoutTabs(false);
                                enableReportsLayoutTabs(false);
                                enableInboxLayoutTabs(true);

                                break;


                            case R.id.menu_search:
                                item.setCheckable(true);
                                selectedFragment = new Search();

                                lltab1.setVisibility(View.GONE);
                                tabLayout1.setVisibility(View.GONE);
                                tabLayout1.setTabGravity(TabLayout.GRAVITY_FILL);
                                viewPager1.setVisibility(View.GONE);

                                highLightCurrentTab1(0);
                                viewPager1.setCurrentItem(0);//viewPager.getCurrentItem());
                                ((ViewGroup) tabLayout1.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
                                ((ViewGroup) tabLayout1.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                                ((ViewGroup) tabLayout1.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                                viewPager1.setPagingEnabled(false);
                                tabLayout2.setVisibility(View.GONE);
                                viewPager2.setVisibility(View.GONE);


                                enableShippingLayoutTabs(false);
                                enableApprovedLayoutTabs(false);
                                enableChartLayoutTabs(false);
                                enableInboxLayoutTabs(false);
                                enableReportsLayoutTabs(false);

                                break;

                            case R.id.menu_account:
                                item.setCheckable(true);
                                selectedFragment = Profile.newInstance();

                                enableShippingLayoutTabs(false);
                                enableApprovedLayoutTabs(false);
                                enableChartLayoutTabs(false);
                                enableInboxLayoutTabs(false);
                                enableReportsLayoutTabs(false);

                                break;


                            case R.id.menu_more:
                                item.setCheckable(true);
                                selectedFragment = new More();

                                tabLayout1.setVisibility(View.GONE);
                                viewPager1.setVisibility(View.GONE);
                                ((ViewGroup) tabLayout1.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
                                ((ViewGroup) tabLayout1.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                                ((ViewGroup) tabLayout1.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
                                tabLayout2.setVisibility(View.GONE);
                                viewPager2.setVisibility(View.GONE);

                                enableShippingLayoutTabs(false);
                                enableChartLayoutTabs(false);
                                enableApprovedLayoutTabs(false);
                                enableInboxLayoutTabs(false);
                                enableReportsLayoutTabs(false);

                                break;
                        }

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                        return true;
                    }
                });
    }



/*
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.isChecked();
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }
*/




    /*@Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                try{
                    Search search=new Search();
                    search.SearchQuery(newText);

                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try{
                    Search search=new Search();
                    search.SearchQuery(newText);

                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }        });
        return super.onCreateOptionsMenu(menu);

    }

*/


    private void addFragment() {
        FragmentTransaction ft2;
        ft2 = getSupportFragmentManager().beginTransaction();
        Home fragment = new Home();
        ft2.replace(R.id.fragment_container, fragment);
        ft2.commit();
    }


    //start for shipping tab
    public void highLightCurrentReportsTabs(int position) {
        for (int i = 0; i < tabReportsLayout.getTabCount(); i++) {
            TabLayout.Tab tab7 = tabReportsLayout.getTabAt(i);
            assert tab7 != null;
            tab7.setCustomView(null);
            tab7.setCustomView(pagerAdapter7.getTabView(i));
        }

        TabLayout.Tab tab7 = tabReportsLayout.getTabAt(position);
        assert tab7 != null;
        tab7.setCustomView(null);
        tab7.setCustomView(pagerAdapter7.getSelectedTabView(position));
    }
    //end for shipping tab



    //start for shipping tab
    public void highLightCurrentChartTab(int position) {
        for (int i = 0; i < tabChartLayout.getTabCount(); i++) {
            TabLayout.Tab tab6 = tabChartLayout.getTabAt(i);
            assert tab6 != null;
            tab6.setCustomView(null);
            tab6.setCustomView(pagerAdapter6.getTabView(i));
        }

        TabLayout.Tab tab6 = tabChartLayout.getTabAt(position);
        assert tab6 != null;
        tab6.setCustomView(null);
        tab6.setCustomView(pagerAdapter6.getSelectedTabView(position));
    }
    //end for shipping tab


    //start for shipping tab
    public void highLightCurrentApprovedTab(int position) {
        for (int i = 0; i < tabApprovedLayout.getTabCount(); i++) {
            TabLayout.Tab tab5 = tabApprovedLayout.getTabAt(i);
            assert tab5 != null;
            tab5.setCustomView(null);
            tab5.setCustomView(pagerAdapter5.getTabView(i));
        }

        TabLayout.Tab tab5 = tabApprovedLayout.getTabAt(position);
        assert tab5 != null;
        tab5.setCustomView(null);
        tab5.setCustomView(pagerAdapter5.getSelectedTabView(position));
    }
    //end for shipping tab


    //start for shipping tab
    public void highLightCurrentInboxTab(int position) {
        for (int i = 0; i < tabInboxLayout.getTabCount(); i++) {
            TabLayout.Tab tab4 = tabInboxLayout.getTabAt(i);
            assert tab4 != null;
            tab4.setCustomView(null);
            tab4.setCustomView(pagerAdapter4.getTabView(i));
        }

        TabLayout.Tab tab4 = tabInboxLayout.getTabAt(position);
        assert tab4 != null;
        tab4.setCustomView(null);
        tab4.setCustomView(pagerAdapter4.getSelectedTabView(position));
    }
    //end for shipping tab





    //start for shipping tab
    public void highLightCurrentShippingTab(int position) {
        for (int i = 0; i < tabShippingLayout.getTabCount(); i++) {
            TabLayout.Tab tab3 = tabShippingLayout.getTabAt(i);
            assert tab3 != null;
            tab3.setCustomView(null);
            tab3.setCustomView(pagerAdapter3.getTabView(i));
        }

        TabLayout.Tab tab3 = tabShippingLayout.getTabAt(position);
        assert tab3 != null;
        tab3.setCustomView(null);
        tab3.setCustomView(pagerAdapter3.getSelectedTabView(position));
    }
 //end for shipping tab




    public void highLightCurrentTab2(int position) {
        for (int i = 0; i < tabLayout2.getTabCount(); i++) {
            TabLayout.Tab tab2 = tabLayout2.getTabAt(i);
            assert tab2 != null;
            tab2.setCustomView(null);
            tab2.setCustomView(pagerAdapter2.getTabView(i));
        }

        TabLayout.Tab tab2 = tabLayout2.getTabAt(position);
        assert tab2 != null;
        tab2.setCustomView(null);
        tab2.setCustomView(pagerAdapter2.getSelectedTabView(position));
    }

    public void highLightCurrentTab1(int position) {
        for (int i = 0; i < tabLayout1.getTabCount(); i++) {
            TabLayout.Tab tab1 = tabLayout1.getTabAt(i);
            assert tab1 != null;
            tab1.setCustomView(null);
            tab1.setCustomView(pagerAdapter1.getTabView(i));
        }

        TabLayout.Tab tab1 = tabLayout1.getTabAt(position);
        assert tab1 != null;
        tab1.setCustomView(null);
        tab1.setCustomView(pagerAdapter1.getSelectedTabView(position));
    }




    public void clearTab1(){
        /*FragmentTransaction ft3;
        ft3 = getSupportFragmentManager().beginTransaction();
        ft3.replace(R.id.fragment_container, new Search());
        ft3.addToBackStack(null);
        ft3.commit();
*/
        lltab1.removeAllViews();
       // lltab1.setVisibility(View.GONE);
    }


    public void addTab1(){
        /*FragmentTransaction ft3;
        ft3 = getSupportFragmentManager().beginTransaction();
        ft3.replace(R.id.fragment_container, new Search());
        ft3.addToBackStack(null);
        ft3.commit();
*/


        // lltab1.setVisibility(View.GONE);
    }


    public void disableTabLayout2Off(){
       // frame_tab2.setVisibility(View.GONE);
    }






    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);
        final LinearLayout layout = new LinearLayout(Welcome.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(40, 30, 30, 30);

        TextView title = new TextView(Welcome.this);
        title.setBackgroundColor(Color.parseColor("#48367d"));
        title.setText("LOG-OUT");
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setPadding(40, 30, 30, 30);
        builder.setCustomTitle(title);

        // builder.setCustomTitle(title);
        builder.setMessage("Hi " + email + " Are you sure  to Logout from Itvrach ?");
        builder.setCancelable(false);
        builder.setPositiveButton("LOGOUT",
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

        Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        // positiveButton.setPadding(20,20,20,40);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
        params.weight = 14.0f;
        //params.gravity = Gravity.CENTER_HORIZONTAL; //this is layout_gravity

        positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null, null, null);
        positiveButton.setPadding(40, 0, 40, 0);

        positiveButton.setGravity(Gravity.CENTER);
        positiveButton.setLayoutParams(params);
        positiveButton.setBackgroundColor(Color.parseColor("#F39C12"));
        positiveButton.setTextColor(Color.WHITE);
        positiveButton.invalidate();


        Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        // negativeButton.setPadding(20,20,20,40);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
        params1.weight = 14.0f;
        //params1.gravity= Gravity.CENTER;
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
                mProgress.show();
                doLogout(email);


            }
        });
    }

    private void doLogout(final String email) {
        User user = new User();
        user.setEmail(email);
        UserService userService = APIClient.getClient().create(UserService.class);
        Call<ResponseModel> call = userService.Logout(user);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    mProgress.dismiss();
                    ResponseModel responModel = response.body();
                    if (responModel.getStatus().equals("true")) {
                        //login start main welcome activity
                        Toast.makeText(Welcome.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();
                        final String user_id = editTextUser_id.getText().toString();
                        final String session_id = txtsession_id.getText().toString();
                        TextView txtfile = new TextView(Welcome.this);
                        String file = txtfile.getText().toString();


                        session.createLoginSession(user_id,username ,type, firstname, lastname,
                                fullname, emails, session_id, file, hp,address);



                       /* Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);*/
                        session.logoutUser();
                        finish();

                    } else {
                        Toast.makeText(Welcome.this, "The Email or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(Welcome.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                slideUp(myView);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        slideDown(myView);
                    }
                },  3000);
                mProgress.dismiss();

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG_WELCOME,"on Resume Welcome Activity");
    }


    private Paint createPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new LinearGradient(0, 0, 0, 600, BLUE, GREEN, Shader.TileMode.CLAMP));
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    public void onBackPressed() {

    }



    /*public void onRequestPermissionResult(int requestCode, String permission[], int grantResults[]){
        switch (requestCode){
            case REQUEST_CAMERA:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        Toast.makeText(Welcome.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Welcome.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(String.valueOf(CAMERA))) {
                                displayAlertMessage("You need to allow access for both permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int i) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{String.valueOf(CAMERA)}, REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }
*/
  /*  @Override
    public void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                if(scannerView == null){
                    scannerView = new ZXingScannerView(this);

                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }else{
                requestPermissions();
            }
        }
    }

    public void onDestroy(){
        super.onDestroy();
        scannerView.stopCamera();
    }

*/

    /*public  void displayAlertMessage(String message,DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK",listener)
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }

    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,new String[]{String.valueOf(CAMERA)},REQUEST_CAMERA);
    }
*/


   /* @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                scannerView.resumeCameraPreview(Welcome.this);

            }
        });
        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scanResult));
                startActivity(intent);
            }
        });
        builder.setMessage(scanResult);
        AlertDialog alert = builder.create();
        alert.show();


    }*/



    private static class BottomNavigationViewHelper {
        public static void disableShiftMode(BottomNavigationView view) {

            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");

                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }

   /* @Override
    public boolean onSupportNavigationUp(){
        onBackPressed();
        return true;
    }*/


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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        boolean displayItem = true;

       /* final MenuItem qrcodeItem = menu.findItem(R.id.action_qrcode);
        qrcodeItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
*/
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        final MenuItem cartItem = menu.findItem(R.id.action_cart);
        cartItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);


        final MenuItem settingsItem = menu.findItem(R.id.menu_settings);


       /* if (displayItem) {
            qrcodeItem.setVisible(true);


            //settingsItem.setVisible(false);
        } else{
           // qrcodeItem.setVisible(false);
            searchItem.setVisible(false);
            cartItem.setVisible(false);
            settingsItem.setVisible(false);
        }*/
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId()) {



            case R.id.action_search:
                return false;
            case R.id.action_cart:
                return false;
          /*  case R.id.action_qrcode:

                return false;*/
            case R.id.menu_settings:
                return false;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void updateContTextView(int counts) {
        txtCount       = (TextView) findViewById(R.id.txtCount);
        txtCount.setText(String.valueOf(counts));
    }




    public void tvInboxTextView(int updateCount) {
        tvInbox.setText(String.valueOf(updateCount));
    }


}