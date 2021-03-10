package com.itvrach.www.itvrach;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itvrach.adapter.MenuAdapter;
import com.itvrach.model.Menu;
import com.itvrach.model.ResponseMenu;
import com.itvrach.services.APIClient;
import com.itvrach.services.MenuService;
import com.itvrach.sessions.SessionManagement;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.itvrach.services.APIClient.URI_SEGMENT_UPLOAD;

/**
 * Created by engineer on 1/23/2019.
 */

public class Home extends Fragment {

    RecyclerView recyclerView;
    MenuAdapter adapter;
    private List<Menu> listContentArr = new ArrayList<Menu>();
    private RecyclerView.LayoutManager mManager;
    private Context context;
    private SessionManagement session;

    private  View myView1;
    private Welcome welcome;
    private LinearLayout connectionLayout;


    private TextView tvTitle;
    private ImageView imgFile;
    private String TAG =" Home ";

    public static Home newInstance() {
        Home fragment = new Home();
        return fragment;
    }

    public Home() {
        // Required empty public constructor
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate Home Fragment");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.home, container, false);

        session = new SessionManagement(getContext());
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        final String userid         = user.get(SessionManagement.KEY_USERID);
        final String username       = user.get(SessionManagement.KEY_USERNAME);
        final String type           = user.get(SessionManagement.KEY_TYPE);
        final String firstname      = user.get(SessionManagement.KEY_FIRSTNAME);
        final String lastname       = user.get(SessionManagement.KEY_LASTNAME);
        final String fullname       = user.get(SessionManagement.KEY_FULLNAME);
        final String email          = user.get(SessionManagement.KEY_EMAIL);
        final String sessionid      = user.get(SessionManagement.KEY_SESSIONID);
        final String fileImg        = user.get(SessionManagement.KEY_FILE);
        final String hp             = user.get(SessionManagement.KEY_HP);
        final String address        = user.get(SessionManagement.KEY_ADDRESS);


        final Toolbar toolbar =(Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        tvTitle = (TextView)  ((Welcome) getActivity()).findViewById(R.id.tvTitle);
        imgFile = (ImageView) ((Welcome) getActivity()).findViewById(R.id.imgFile);

        imgFile.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setTextSize(14);
        tvTitle.setText(email);

        Picasso picasso = Picasso.with(getContext());
        picasso.load(URI_SEGMENT_UPLOAD + fileImg).resize(80, 80).into(imgFile);



        connectionLayout = (LinearLayout) rootView.findViewById(R.id.connectionLayout);
        connectionLayout.setVisibility(View.GONE);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        adapter = new MenuAdapter(listContentArr, getContext());
        mManager = new GridLayoutManager(context, 4);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

       final View myView = ((Welcome) getActivity()).findViewById(R.id.my_view);
       TextView tv_disconnected = (TextView) ((Welcome) getActivity()).findViewById(R.id.tv_disconnected);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context,
                recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                final Menu item = listContentArr.get(position);
                adapter.editItem(position);
                adapter.notifyItemChanged(position);

                String itemname = item.getMenu_name();
                if(itemname.equals("Logout")){
                    ((Welcome) getActivity()).logout();
                }

                else if(itemname.equals("Approved")){
                    Fragment selectedFragment = null;

                    selectedFragment = new Approved();
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,
                            selectedFragment)
                            .addToBackStack(null)
                            .commit();

                    ((Welcome)getActivity()).enableApprovedLayoutTabs(true);
                }

                else if(itemname.equals("Shipping")) {

                    Fragment selectedFragment = null;
                    selectedFragment = new ShippingFragment();
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,
                                    selectedFragment)
                            .addToBackStack(null)
                            .commit();


                    ((Welcome) getActivity()).enableShippingLayoutTabs(true);
                }

                else if(itemname.equals("Chart")) {

                    Fragment selectedFragment = null;

                    selectedFragment = new BarChartFragment();
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,
                                    selectedFragment)
                            .addToBackStack(null)
                            .commit();
                    ((Welcome)getActivity()).enableChartLayoutTabs(true);
                }



                else if(itemname.equals("Reports")) {

                    Fragment selectedFragment = null;

                    selectedFragment = new StockReport();
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,
                                    selectedFragment)
                            .addToBackStack(null)
                            .commit();

                    ((Welcome)getActivity()).enableReportsLayoutTabs(true);
                }

                else if(itemname.equals("Documents")) {

                    Fragment selectedFragment = null;

                    selectedFragment = new DocumentsFragment();
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container,
                                    selectedFragment)
                            .addToBackStack(null)
                            .commit();

                   // ((Welcome)getActivity()).enableReportsLayoutTabs(true);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));



        final ProgressDialog pdrcylerview;
        pdrcylerview = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        pdrcylerview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdrcylerview.setCancelable(false);
        pdrcylerview.setIndeterminate(true);
        pdrcylerview.getWindow().setGravity(Gravity.CENTER);
        pdrcylerview.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        pdrcylerview.show();
        pdrcylerview.getWindow().setLayout(245, 200);
        pdrcylerview.getWindow().setGravity(Gravity.CENTER);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pdrcylerview.dismiss();
            }
        }, 2000);


        MenuService menuService = APIClient.getClient().create(MenuService.class);
        Call<ResponseMenu> call = menuService.getFindAll();
        call.enqueue(new Callback<ResponseMenu>() {
            @Override
            public void onResponse(Call<ResponseMenu> call, Response<ResponseMenu> response) {
                if (response.isSuccessful()) {
                    ResponseMenu responseMenu = response.body();
                    if (responseMenu.getSuccess().equals("true")) {
                        listContentArr = response.body().getResult();
                        if (listContentArr.size() != 0) {
                            listContentArr = response.body().getResult();
                            adapter = new MenuAdapter(listContentArr, getContext());
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            pdrcylerview.dismiss();
                        }
                    }

                }
                else {
                    Log.d(TAG," : data is empty" );
                }
            }

            @Override
            public void onFailure(Call<ResponseMenu> call, Throwable t) {
                pdrcylerview.dismiss();
                Log.d("Search"," say : no Internet connection" );
                connectionLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

                 /* ((Welcome) getActivity()).slideUp(myView);

                 new Handler().postDelayed(new Runnable() {
                 public void run() {
                        ((Welcome) getActivity()).slideDown(myView);
                       }
                    },  3000);
*/
            }

        });

        return  rootView;
    }


   /* @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity.getClass()== Welcome.class) {

            welcome = (Welcome) activity;
        }
        }
    }
*/

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"is on Resume");
    }





    private static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
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






    public void onBackPressed() {
    }
    public void callParentMethod(){
        getActivity().onBackPressed();
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
        shareItem.setVisible(false);

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

