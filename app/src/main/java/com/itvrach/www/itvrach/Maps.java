package com.itvrach.www.itvrach;


import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
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
import com.itvrach.adapter.ArrayAdapterNoFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static com.google.android.gms.internal.zzagz.runOnUiThread;
import static com.itvrach.services.RecievedCookiesInterceptor.context;


/**
 * A simple {@link Fragment} subclass.
 */
public class Maps extends Fragment implements
        OnMapReadyCallback, View.OnKeyListener, TextWatcher, View.OnTouchListener {

    private Maps.AddressListResultReceiver addressResultReceiver;

    private MapView mapView;
    private GoogleMap googleMap;
    private List<Marker> markerList = new ArrayList<>();
    private AutoCompleteTextView destination;
    private EditText edSearch;
    private SearchView searchView;
    private AutoCompleteTextView mAutoCompleteTextView;
    private TextView selection;


    private static final int MESSAGE_TEXT_CHANGED = 0;
    private static final int AUTOCOMPLETE_DELAY = 500;
    private static final int THRESHOLD = 3;

    private List<Address> autoCompleteSuggestionAddresses;
    private ArrayAdapter<String> adapterAutoComplete;


    private Location location;
    private Geocoder geocoder;


    private static final String[] items = {"lorem",
            "ligula",
            "liqua",
            "amet",
            "ante",
            "augue",
            "arcu",
            "aliquet",
            "adipiscing",
            "erat",
            "elit",
            "ipsum", "dolor",
            "sit", "sodales",
            "vel",
            "mollis", "morbi",
            "etiam",
            "porttitor", "pellentesque", "placerat", "purus"};


    private TextView tvTitle;
    private ImageView imgFile;
    private ListView addressListView;
    private TextInputEditText addressNameTv;


    private static final String IDENTIFIER = "AddressesByNameIS";




    public static Maps newInstance() {
        Maps fragment = new Maps();
        return fragment;
    }

    public Maps() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.maps, container, false);

        // mAutoCompleteTextView = initializeAutoCompleteTextView(view);
        // setupAutoCompleteTextView(mAutoCompleteTextView);


        initViews(view);
        setupViews(view);



        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);


        tvTitle = (TextView) ((Welcome) getActivity()).findViewById(R.id.tvTitle);
        tvTitle.setTextSize(14);
        tvTitle.setText("Maps");
        tvTitle.setVisibility(View.VISIBLE);
        imgFile = (ImageView) ((Welcome) getActivity()).findViewById(R.id.imgFile);
        imgFile.setVisibility(View.GONE);
        mapView.onCreate(savedInstanceState);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction ft;
                ft = getFragmentManager().beginTransaction();
                Profile fragment = new Profile();
                ft.replace(R.id.fragment_container, fragment);
                ft.addToBackStack(null);
                ft.commit();


                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            }
        });


        return view;
    }

    private class AddressListResultReceiver extends ResultReceiver {
        AddressListResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == 0) {
                Toast.makeText(getContext(),
                        "Enter address name, " ,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (resultCode == 1) {
                Toast.makeText(getContext(),
                        "Address not found, " ,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String[] addressList = resultData.getStringArray("addressList");
            showResults(addressList);
        }
    }

    public void getAddressesByName(View view){
        getAddresses(addressNameTv.getText().toString());
    }

    private void getAddresses(String addName) {
        if (!Geocoder.isPresent()) {
            Toast.makeText(getContext(),
                    "Can't find address, ",
                    Toast.LENGTH_SHORT).show();
            return;
        }

      /*  Intent intent = new Intent(this, AddressesByNameIntentService.class);
        intent.putExtra("address_receiver", addressResultReceiver);
        intent.putExtra("address_name", addName);
        startService(intent);
*/
    }






    /*private AutoCompleteTextView initializeAutoCompleteTextView( View view) {
        return (AutoCompleteTextView) view.findViewById(R.id.tv_autocomplete);
    }
*/


    private void initViews(View view) {
        mapView = (MapView) view.findViewById(R.id.map_view);
        mapView.getMapAsync(this);//when you already implement OnMapReadyCallback in your fragment
        edSearch = (EditText) view.findViewById(R.id.edSearch);
        edSearch.setOnKeyListener(this);
        mapView.setVisibility(GONE);

        //  selection=(TextView)view.findViewById(R.id.selection);


        // setupAutoCompleteTextView(mAutoCompleteTextView);


        adapterAutoComplete = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());

        mAutoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.tv_autocomplete);
       // addressListView = (ListView) view.findViewById(R.id.addresses_lst);


        mAutoCompleteTextView.addTextChangedListener(this);
        mAutoCompleteTextView.requestFocus();

        //  mAutoCompleteTextView.setOnItemSelectedListener(this);
        mAutoCompleteTextView.setThreshold(THRESHOLD);
        adapterAutoComplete.notifyDataSetChanged();
        mAutoCompleteTextView.setAdapter(adapterAutoComplete);
        mAutoCompleteTextView.setOnTouchListener(this);




     /*   ArrayAdapter<String>   adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, items);
      //  ArrayAdapter<String> adapter2  = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,items);


        mAutoCompleteTextView.setAdapter(adapter);
*/

    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

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


    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng issaquah = new LatLng(47.5301011, -122.0326191);
        LatLngBounds bounds;
        LatLngBounds.Builder builder;
        this.googleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException se) {

        }
        googleMap.setMyLocationEnabled(true);

        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        googleMap.getUiSettings().setCompassEnabled(true);


        Marker mIssaquah = googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                .position(issaquah)
                .flat(true)
                .anchor(0.1f, 0.1f)
                .zIndex(1.0f)
                .draggable(true));

        markerList.add(mIssaquah);
        markerList.get(0).showInfoWindow();

        builder = new LatLngBounds.Builder();

        for (Marker m : markerList) {
            builder.include(m.getPosition());

        }

        bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.30);

        googleMap.setBuildingsEnabled(true);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(issaquah)
                .bearing(0)
                .zoom(16f)
                .tilt(70).build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        CameraUpdate cu1 = CameraUpdateFactory.newCameraPosition(cameraPosition);

        googleMap.animateCamera(cu);
        googleMap.moveCamera(cu1);


        // googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);

        //  googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }


    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {


        String location = edSearch.getText().toString();
        List<Address> addressList = null;

        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);

        googleMap.setMyLocationEnabled(true);

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);


        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {


            if (location != null || !location.equals("")) {


                // Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    addressList = geocoder.getFromLocationName(location, 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);

                    String countryName = address.getCountryName();
                    String cityName = address.getPostalCode();
                    String addressLine = address.getAddressLine(0);
                    String streetName = address.getLocality();
                    String subLocality = address.getSubLocality();
                    String postalCode = address.getPostalCode();
                    String state = address.getAdminArea();


               /*     String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
*/


                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            .flat(true)
                            .position(latLng)
                            .snippet(countryName + ", " + cityName + ", " + addressLine + ", " + streetName + ", " + subLocality)
                            .anchor(0.1f, 0.1f)
                            .zIndex(1.0f)
                            .title(countryName + ", " + location))
                            .showInfoWindow();


                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
            return true;
        }
        return false;
    }


    //new list address



    private void setupViews(View view){
        Geocoder geocoder = new Geocoder(getContext());
        LinearLayout addressLayout = (LinearLayout) view.findViewById(R.id.addressLayout);

        final TextInputEditText edLocationName = (TextInputEditText) view.findViewById(R.id.edLocationName);
        Button btnFind       = (Button) view.findViewById(R.id.btnFind);
        final ListView listViewResult = (ListView) view.findViewById(R.id.listAddress);


        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String locationName = edLocationName.getText().toString();

                List<Address> geoResult = findGeocoder(locationName);
                if(geoResult !=null){
                    List<String> geoStringResult = new ArrayList<String>();
                    for(int i=0; i<geoResult.size(); i++){
                        Address thisAddress = geoResult.get(i);
                        String strThisAddress ="";

                        for(int a=0; a < thisAddress.getMaxAddressLineIndex(); a++){
                            strThisAddress +=thisAddress.getAddressLine(a);
                        }

                        strThisAddress+=
                                "Street         :" + thisAddress.getThoroughfare()+"\n"
                                + "Country Name :" + thisAddress.getCountryName()+"\n"
                                + "Country Code :" + thisAddress.getCountryCode()+"\n"
                                + "Admin Area   :" + thisAddress.getAdminArea()+"\n"
                                + "Postal Code :" + thisAddress.getPostalCode();

                        geoStringResult.add(strThisAddress);
                    }

                    ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1, geoStringResult);
                    listViewResult.setAdapter(adapterLocation);

                }
            }
        });

    }

    public List<Address> findGeocoder(String locationName){
        int maxResult =6;
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(getContext());
        try {
            addresses = geocoder.getFromLocationName(locationName, maxResult);

        }catch (IOException e){
            e.printStackTrace();
        }catch (IllegalArgumentException e){
          e.printStackTrace();
        }

        return addresses;
    }








    public List<Address> getAddress2(String value){
        String msg = "";
        String addressName = "";

        if (addressName == null) {
            msg = "No name found";
            //sendResultsToReceiver(0, msg, null);
            //return;
        }

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(addressName, 5);
        } catch (Exception ioException) {
            Log.e(IDENTIFIER, "Error in getting addresses for the given name");
        }

        if (addresses == null || addresses.size()  == 0) {
            msg = "No address found for the address name";
           // sendResultsToReceiver(1, msg, null);

        } else {
            Log.d(IDENTIFIER, "number of addresses received "+addresses.size());
            String[] addressList = new String[addresses.size()] ;
            int j =0;
            for(Address address : addresses){
                ArrayList<String> addressInfo = new ArrayList<>();
                for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressInfo.add(address.getAddressLine(i));
                }
                addressList[j] = TextUtils.join(System.getProperty("line.separator"),
                        addressInfo);
                Log.d(IDENTIFIER,addressList[j]);
                j++;
            }
            sendResultsToReceiver(2,"", addressList);
        }
        return addresses;
    }

    private void sendResultsToReceiver(int resultCode, String message, String[] addressList) {
        Bundle bundle = new Bundle();
        bundle.putString("msg", message);
        bundle.putStringArray("addressList", addressList);
        addressResultReceiver.send(resultCode, bundle);
    }


    private void showResults(String[] addressList){
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, addressList);
        addressListView.setAdapter(arrayAdapter);
    }



    public List<Address> getAddress(String value) {
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        int maxResult = 5;
        List<Address> autoCompleteSuggestionAddresses = null;

        try {
            adapterAutoComplete.clear();
            autoCompleteSuggestionAddresses = geocoder.getFromLocationName(value, maxResult);
        } catch (Exception ioException) {
            Log.e(IDENTIFIER, "Error in getting addresses for the given name");
        }

        if (autoCompleteSuggestionAddresses == null || autoCompleteSuggestionAddresses.size() == 0) {
            value = "No address found for the address name";
        } else {
            if (autoCompleteSuggestionAddresses != null && autoCompleteSuggestionAddresses.size() > 0) {
                for (int i = 0; i < autoCompleteSuggestionAddresses.size(); i++) {
                    Address address = autoCompleteSuggestionAddresses.get(i);
                    Log.v("Location ", autoCompleteSuggestionAddresses.toString());
                    String streetName = address.getAddressLine(0);
                    adapterAutoComplete.add(streetName.toString());

                }
            }
            Log.d(IDENTIFIER, "number of addresses received " + autoCompleteSuggestionAddresses.size());
        }
        return autoCompleteSuggestionAddresses;
    }




      /*  } catch (IOException ex) {
            Log.d("Maps", "Failed to get autocomplete suggestions", ex);
            ex.printStackTrace();
        }

        return autoCompleteSuggestionAddresses;
    }*/


    @Override
    public void beforeTextChanged(CharSequence location, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence locationName, int start, int before, int count) {
        final String value = locationName.toString();

        if (!value.equals("") && value.length() >= THRESHOLD) {

            Thread t = new Thread() {
                public void run() {
                    try {

                        runOnUiThread(new Runnable() {
                            public void run() {
                                getAddress(value);
                            }
                        });
                    } catch (Exception e) {
                    }
                }
            };
            t.start();


        } else {
            adapterAutoComplete.clear();
        }
    }

    @Override
    public void afterTextChanged(Editable locationName) {
       /* for (UnderlineSpan span : locationName.getSpans(0, locationName.length(), UnderlineSpan.class)) {
            locationName.removeSpan(span);
        }*/


    }







   /* private void getAddressInfo(String s){
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        ArrayAdapter<String>   adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, addressList2);


        try {
            List<Address> a = geocoder.getFromLocationName(s,1);

            for(int i=0;i<a.size();i++){
                String city = a.get(0).getLocality();
                String country = a.get(0).getCountryName();
                String address = a.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                addressList2.add(address+", "+city+", "+country);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }
*/

   /* public void setupAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView) {
     //   ArrayAdapter<String>   adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, addressList2);
        autoCompleteTextView.setThreshold(1);
       // autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                getAddressInfo(getContext(),location, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
*/


    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // menu.clear();
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


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int iconRight = R.drawable.ic_clear_black_litle;
        int iconLeft = R.drawable.ic_search_litle;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            final TextView textView =(TextView) v;
            if (event.getX() >= textView.getWidth()- textView.getCompoundPaddingEnd()) {
                textView.setText("");
                mAutoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(iconLeft, 0, iconRight, 0);
                return true;
            }else{
               // if(mAutoCompleteTextView.length()< 0)
                mAutoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(iconLeft, 0, iconRight, 0);
                return true;
            }
        }

        return false;
    }

}
