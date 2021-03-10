package com.itvrach.www.itvrach;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.itvrach.adapter.MoreAdapter;
import com.itvrach.model.Menu;
import com.itvrach.model.ResponseMenu;
import com.itvrach.services.APIClient;
import com.itvrach.services.MoreService;
import com.itvrach.sessions.SessionManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by engineer on 3/7/2019.
 */

public class More extends Fragment implements OnMapReadyCallback{

    RecyclerView recyclerView;
    MoreAdapter adapter;
    private List<Menu> moreList = new ArrayList<Menu>();
    private RecyclerView.LayoutManager mManager;
    private Context context;
    private SessionManagement session;
    private View myView;
    private TextView tvTitle;
    private ImageView imgFile;


    private MapView mapView;
    private GoogleMap googleMap;
    private LinearLayout ly_ContactUs, ly_RecylerView;
    private List<Marker> markerList = new ArrayList<>();
    private String TAG ="More";







    public static More newInstance() {
        More fragment = new More();
        return fragment;
    }

    public More() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.more, container, false);
        mapView = (MapView) view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment

        ly_RecylerView = (LinearLayout) view.findViewById(R.id.ly_RecylerView);
        ly_RecylerView.setVisibility(View.GONE);

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
        final String emailid        = user.get(SessionManagement.KEY_EMAIL);
        final String sessionid      = user.get(SessionManagement.KEY_SESSIONID);
        final String fileImg        = user.get(SessionManagement.KEY_FILE);
        final String hp             = user.get(SessionManagement.KEY_HP);
        final String address        = user.get(SessionManagement.KEY_ADDRESS);

        myView = view.findViewById(R.id.my_view);
        TextView tv_about = (TextView) view.findViewById(R.id.tv_about);



        final Toolbar toolbar =(Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);


        tvTitle = (TextView)  ((Welcome) getActivity()).findViewById(R.id.tvTitle);
        tvTitle.setTextSize(14);
        tvTitle.setText("More");
        tvTitle.setVisibility(View.VISIBLE);
        imgFile = (ImageView) ((Welcome) getActivity()).findViewById(R.id.imgFile);
        imgFile.setVisibility(View.GONE);




        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new MoreAdapter(moreList, getContext());
        mManager          = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        recyclerView.addOnItemTouchListener(new More.RecyclerTouchListener(context,
                recyclerView, new More.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final Menu item = moreList.get(position);
                adapter.editItem(position);
                adapter.notifyItemChanged(position);

                String itemname = item.getMenu_name();

                if(itemname.equals("About")){
                   /* textTitle.setVisibility(View.GONE);
                    imgfile.setVisibility(View.GONE);
*/
                    final Toolbar toolbar =(Toolbar) getActivity().findViewById(R.id.toolbar);
                    toolbar.setTitleTextColor(getResources().getColor(R.color.white));

                    ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().getThemedContext();
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("About");
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FragmentTransaction ft;
                            ft = getFragmentManager().beginTransaction();
                            More fragment = new More();
                            ft.replace(R.id.fragment_container, fragment);
                            ft.addToBackStack(null);
                            ft.commit();

                            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                            //textTitle.setVisibility(View.VISIBLE);
                            //imgfile.setVisibility(View.VISIBLE);
                        }
                    });


                    recyclerView.setVisibility(View.GONE);
                    slideUp(myView);

                }else

                    if(itemname.equals("Contact")){
                      //  mapView.setVisibility(View.VISIBLE);
                      //  mapView.onCreate(savedInstanceState);



                       // ly_RecylerView.setVisibility(View.GONE);
                        final Toolbar toolbar =(Toolbar) getActivity().findViewById(R.id.toolbar);
                        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

                        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
                        ((AppCompatActivity)getActivity()).getSupportActionBar().getThemedContext();
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Contact");

                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                FragmentTransaction ft;
                                ft = getFragmentManager().beginTransaction();
                                More fragment = new More();
                                ft.replace(R.id.fragment_container, fragment);
                                ft.addToBackStack(null);
                                ft.commit();

                                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
                                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                                //textTitle.setVisibility(View.VISIBLE);
                                //imgfile.setVisibility(View.VISIBLE);
                            }
                        });

                }else

                    if(itemname.equals("Change Password")){
            /*    textTitle.setVisibility(View.GONE);
                imgfile.setVisibility(View.GONE);
*/
                final Toolbar toolbar =(Toolbar) getActivity().findViewById(R.id.toolbar);
                toolbar.setTitleTextColor(getResources().getColor(R.color.white));

                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
                ((AppCompatActivity)getActivity()).getSupportActionBar().getThemedContext();
                ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Change Password");

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FragmentTransaction ft;
                        ft = getFragmentManager().beginTransaction();
                        More fragment = new More();
                        ft.replace(R.id.fragment_container, fragment);
                        ft.addToBackStack(null);
                        ft.commit();

                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                        //textTitle.setVisibility(View.VISIBLE);
                        //imgfile.setVisibility(View.VISIBLE);
                    }
                });

                recyclerView.setVisibility(View.GONE);

            }
        }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        MoreService moreService = APIClient.getClient().create(MoreService.class);
        Call<ResponseMenu> call = moreService.getFindAll();
        call.enqueue(new Callback<ResponseMenu>() {
            @Override
            public void onResponse(Call<ResponseMenu> call, Response<ResponseMenu> response) {
                if (response.isSuccessful()) {
                    ResponseMenu responseMenu = response.body();
                    if (responseMenu.getSuccess().equals("true")) {
                        moreList = response.body().getResult();
                        adapter = new MoreAdapter(moreList, getContext());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(getContext(), "opps", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseMenu> call, Throwable t) {


            }
        });




        return  view;
    }


   @Override
    public void onResume() {
       mapView.onResume();
       super.onResume();
       Log.d(TAG," is onResume");

   }

   @Override
    public void onPause() {
        super.onPause();
       mapView.onPause();
        }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

  /*  private void drawMarker(LatLng point, String text) {
private LatLngBounds bounds;
private LatLngBounds.Builder builder;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point).title(text).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));
        mMap.addMarker(markerOptions);
        builder.include(markerOptions.getPosition());

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        builder = new LatLngBounds.Builder();
    for (int i = 0; i < locationList.size(); i++) {

        drawMarker(new LatLng(Double.parseDouble(locationList.get(i).getLatitude()), Double.parseDouble(locationList.get(i).getLongitude())), locationList.get(i).getNo());

     }
     bounds = builder.build();
     CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
     mMap.animateCamera(cu);

  */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        /*final LatLng Center  = new LatLng(-31.952854,   115.857342);
        final LatLng Branch1 = new LatLng(-31.952894,   115.869399);
*/
        LatLng issaquah   = new LatLng(47.5301011, -122.0326191);
        LatLng seattle    = new LatLng(47.6062095, -122.3320708);
        LatLng bellevue   = new LatLng(47.6101497, -122.2015159);
        LatLng sammamish  = new LatLng(47.6162683, -122.0355736);
        LatLng redmond    = new LatLng(47.6739881, -122.121512);




        LatLngBounds bounds;
        LatLngBounds.Builder builder;
        //List<Marker> markerList = new ArrayList<>();


       // googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        googleMap.setMyLocationEnabled(true);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);

        googleMap.getUiSettings().setCompassEnabled(true);

        //googleMap.setMinZoomPreference(5);


        IconGenerator iconGenerator = new IconGenerator(getContext());
       // iconGenerator.setBackground(Color.parseColor("#f39c29"));
        iconGenerator.setStyle(IconGenerator.STYLE_PURPLE);


        IconGenerator iconGenerator1 = new IconGenerator(getContext());
        iconGenerator1.setStyle(IconGenerator.STYLE_ORANGE);

         Marker mIssaquah = googleMap.addMarker(new MarkerOptions()
              //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map))
                 .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
             //  .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator1.makeIcon("ITvrach PT. \n CENTER OFFICE")))

             //   .title("ITvrach PT")
               .visible(true)
                .snippet("TEST")
                .position(issaquah)
                .flat(true)
                .anchor(0.1f,0.1f)
                //.rotation(90.0f)
                .zIndex(1.0f)
                .draggable(true));
        markerList.add(mIssaquah);
        markerList.get(0).showInfoWindow();


        Marker mSeattle = googleMap.addMarker(new MarkerOptions()
              //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
               // .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon("ITVRACH PT. \n BRACNH 1 OFFICE")))

               // .title("ITvrach PT")
                .visible(true)
                .snippet("TEST")
                .position(seattle)
                .flat(true)
                .anchor(0.1f,0.1f)
               // .rotation(90.0f)
                .zIndex(2.0f)
                .draggable(true));
       // mBrach1.setTag(0);
       // mBrach1.showInfoWindow();
        markerList.add(mSeattle);
        markerList.get(1).showInfoWindow();


        Marker mBellevue = googleMap.addMarker(new MarkerOptions()
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                // .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon("ITVRACH PT. \n BRACNH 1 OFFICE")))

                // .title("ITvrach PT")
                .visible(true)
                .snippet("TEST")
                .position(bellevue)
                .flat(true)
                .anchor(0.1f,0.1f)
                // .rotation(90.0f)
                .zIndex(2.0f)
                .draggable(true));
        // mBrach1.setTag(0);
        // mBrach1.showInfoWindow();
        markerList.add(mBellevue);
        markerList.get(2).showInfoWindow();


        Marker mSammamish = googleMap.addMarker(new MarkerOptions()
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                // .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon("ITVRACH PT. \n BRACNH 1 OFFICE")))

                // .title("ITvrach PT")
                .visible(true)
                .snippet("TEST")
                .position(sammamish)
                .flat(true)
                .anchor(0.1f,0.1f)
                // .rotation(90.0f)
                .zIndex(2.0f)
                .draggable(true));
        // mBrach1.setTag(0);
        // mBrach1.showInfoWindow();
        markerList.add(mSammamish);
        markerList.get(3).showInfoWindow();


        Marker mRedmond = googleMap.addMarker(new MarkerOptions()
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                // .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon("ITVRACH PT. \n BRACNH 1 OFFICE")))

                // .title("ITvrach PT")
                .visible(true)
                .snippet("TEST")
                .position(redmond)
                .flat(true)
                .anchor(0.1f,0.1f)
                // .rotation(90.0f)
                .zIndex(2.0f)
                .draggable(true));
        // mBrach1.setTag(0);
        // mBrach1.showInfoWindow();
        markerList.add(mRedmond);
        markerList.get(4).showInfoWindow();







        builder = new LatLngBounds.Builder();

        for (Marker m : markerList) {
            builder.include(m.getPosition());

        }

        bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);

        // Zoom and animate the google map to show all markers

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(bellevue)
                .zoom(15)
                .bearing(0)
                .tilt(30).build();

        googleMap.setBuildingsEnabled(true);


        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        CameraUpdate cu1 = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.animateCamera(cu);
        googleMap.moveCamera(cu1);


        // Instantiates a new Polygon object and adds points to define a rectangle
       /* PolygonOptions rectOptions = new PolygonOptions()
                .add(new LatLng(37.35, -122.0),
                        new LatLng(37.45, -122.0),
                        new LatLng(37.45, -122.2),
                        new LatLng(37.35, -122.2),
                        new LatLng(37.35, -122.0));*/

        //or
      /* PolygonOptions rectOptions1 = new PolygonOptions()
                .geodesic(true)
                .add(issaquah, seattle, bellevue, sammamish, redmond)
                .strokeJointType(JointType.ROUND)
                .strokeWidth(10)
                .strokeColor(Color.BLUE)
                .fillColor(Color.CYAN);


// Get back the mutable Polygon
       googleMap.addPolygon(rectOptions1);
*/
        /*CircleOptions circleOptions = new CircleOptions()
                 .center(bellevue)
                 .radius(10000)
                 .fillColor(Color.TRANSPARENT)
                 .strokeColor(Color.RED)
                 .strokeWidth(2);
        googleMap.addCircle(circleOptions);
*/

/*
      PolylineOptions rectOptions = new PolylineOptions()
                .add(issaquah, seattle, bellevue, sammamish, redmond)
                .width(10)
                .color(Color.RED)
                .geodesic(true)
                .startCap(new RoundCap())
                .jointType(JointType.BEVEL);
        googleMap.addPolyline(rectOptions);

*/

       /*GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions()
                .position(bellevue, 100, 100 )
                .image( BitmapDescriptorFactory.fromResource(R.drawable.ic_map));
        googleMap.addGroundOverlay(groundOverlayOptions);
*/


     /*   String  cacheLayer = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(addCache()).zIndex(1));
        String  mapLayer = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(addMap()).zIndex(2));
        String  dataLayer = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(addData()).zIndex(3));


        googleMap.addPolygon(new PolygonOptions().add(points).strokeColor(Color.RED).strokeWidth(3f).zIndex(4));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(points[0], 18));
*/

        /*googleMap.addPolyline(new PolylineOptions()
                        .add(Center, Branch1)
                        .width(5)
                        .color(Color.BLUE)
                        .geodesic(true));
*/


        //googleMap.moveCamera(cu);
       //googleMap.moveCamera(CameraUpdateFactory.newLatLng(Center));


        /*LatLng latLng = new LatLng(latitude, longitude);

        TextView text = new TextView(context);
        text.setText("Your text here");
        IconGenerator generator = new IconGenerator(context);
        generator.setBackground(context.getDrawable(R.drawable.bubble_mask));
        generator.setContentView(text);
        Bitmap icon = generator.makeIcon();

        MarkerOptions tp = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(icon));
        googleMap.addMarker(tp);
*/        /**
         *
         *
         *
         *Bitmap.Config conf = Bitmap.Config.ARGB_8888;
         Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
         Canvas canvas1 = new Canvas(bmp);

         // paint defines the text color, stroke width and size
         Paint color = new Paint();
         color.setTextSize(35);
         color.setColor(Color.BLACK);

         // modify canvas
         canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
         R.drawable.user_picture_image), 0,0, color);
         canvas1.drawText("User Name!", 30, 40, color);

         // add marker to Map
         mMap.addMarker(new MarkerOptions()
         .position(USER_POSITION)
         .icon(BitmapDescriptorFactory.fromBitmap(bmp))
         // Specifies the anchor to be at a particular point in the marker image.
         .anchor(0.5f, 1));
         *
         */

    }





    private static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private More.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final More.ClickListener clicklistener){

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



    public void slideUp(View view){
        //view.setVisibility(View.VISIBLE);
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,view.getHeight(),0);
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        view.startAnimation(translateAnimation);
        view.setVisibility(View.VISIBLE);
    }




    public void onBackPressed() {
    }
    public void callParentMethod(){
        getActivity().onBackPressed();
    }



    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu, menu);

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
            case R.id.menu_settings:
                return false;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
