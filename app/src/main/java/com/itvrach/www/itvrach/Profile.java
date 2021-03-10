package com.itvrach.www.itvrach;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;
import com.itvrach.model.ResponseModel;
import com.itvrach.services.APIClient;
import com.itvrach.services.UserService;
import com.itvrach.sessions.SessionManagement;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.itvrach.services.APIClient.URI_SEGMENT_UPLOAD;

/**
 * Created by engineer on 1/23/2019.
 */

@SuppressWarnings("ALL")
public class Profile extends Fragment implements
        View.OnClickListener {


    private EditText editTextUser_id, editTextHp, editTextAddress, editTextUsername, editTextType, editTextFirstname, editTextLastname, editTextplace, editTextAge, editTextSalary, editTextEmail, editTextdateofbirth;
    private ImageView img_file;
    private CircleImageView imgSelect;
    private TextView tvTextFullname;
    private RadioGroup radioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private Button btnUpdate, btnEdit;
    private CheckBox checkBox, single, married;
    private SearchableSpinner searchableSpinner;
    private ProgressDialog Loading;
    private int year;
    private int month;
    private int dayOfMonth, selectedId;
    private int age;
    private double salary;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat;
    private ImageButton img_calendar;
    private String[] religion = new String[]{
            "Islam",
            "Katolik",
            "Protestan",
            "Budha",
            "Hindu"
    };

    private Uri filePath;

    private SessionManagement session;
    private MapView mapView;
    private GoogleMap googleMap;
    private List<Marker> markerList = new ArrayList<>();
    private TextView tvTitle;
    private ImageView imgFile, imgProfile, imgMap;
    private String ORANGE = "#F39C12";
    private String GRAY   = "#BEBEBE";
    private String VIOLET = "#48367d";

    private int PICK_GALLERY_REQUEST=1, PICK_CAMERA_REQUEST=2;
    private Drawable icon;
    private String TAG="Profile";
    private TextView tvPath;


    /*public static Profile newInstance(String username,String fullname) {
        Profile fragment = new Profile();

        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("fullname", fullname);
        fragment.setArguments(args);
        return fragment;
    }*/


    public static Profile newInstance() {
        Profile fragment = new Profile();
        return fragment;
    }

   /* public String getShowIndex(){
        return getArguments().getString("username");

    }*/


    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG," onCreate Fragment");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile, null);
        initViews(view);

        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);

        tvTitle = (TextView) ((Welcome) getActivity()).findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setTextSize(14);
        tvTitle.setText("Profile");
        imgFile = (ImageView) ((Welcome) getActivity()).findViewById(R.id.imgFile);
        imgFile.setVisibility(View.GONE);

        return view;
    }

    private void getToolbar() {


    }


    private void initViews(View view) {


        session = new SessionManagement(getContext());

        session.checkLogin();

        // get user data from session
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

        icon = getContext().getResources().getDrawable(R.drawable.ic_close_white_m);

       // tvPath = (TextView) view.findViewById(R.id.tvPath);
        editTextUser_id = (EditText) view.findViewById(R.id.editTextUser_id);
        editTextUser_id.setVisibility(View.GONE);
        editTextUsername = (EditText) view.findViewById(R.id.editTextUsername);
        editTextType = (EditText) view.findViewById(R.id.editTextType);
        editTextFirstname = (EditText) view.findViewById(R.id.editTextFirstname);
        editTextLastname = (EditText) view.findViewById(R.id.editTextLastname);
        tvTextFullname = (TextView) view.findViewById(R.id.tvTextFullname);

        editTextdateofbirth = (EditText) view.findViewById(R.id.editTextdateofbirth);
        editTextAge = (EditText) view.findViewById(R.id.editTextAge);
        editTextSalary = (EditText) view.findViewById(R.id.editTextSalary);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_gender);
        maleRadioButton = (RadioButton) view.findViewById(R.id.radio_male);
        femaleRadioButton = (RadioButton) view.findViewById(R.id.radio_female);
        editTextHp = (EditText) view.findViewById(R.id.editTextHp);

        img_file  = (ImageView) view.findViewById(R.id.img_file);
        imgSelect = (CircleImageView) view.findViewById(R.id.img_select);


        imgMap = (ImageView) view.findViewById(R.id.imgMap);
        editTextAddress = (EditText) view.findViewById(R.id.editTextAddress);
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);
        single = (CheckBox) view.findViewById(R.id.single);
        married = (CheckBox) view.findViewById(R.id.married);
        searchableSpinner = (SearchableSpinner) view.findViewById(R.id.searchableSpinner);

        editTextUsername.setEnabled(false);

        editTextType.setEnabled(false);
        editTextFirstname.setEnabled(false);
        editTextLastname.setEnabled(false);
        tvTextFullname.setEnabled(false);
        editTextEmail.setEnabled(false);


        editTextUser_id.setText(userid);
        editTextUsername.setText(username);
        editTextType.setText(type);
        editTextFirstname.setText(firstname);
        editTextLastname.setText(lastname);
        tvTextFullname.setText(fullname);
        editTextEmail.setText(emailid);
        editTextHp.setText(hp);
        editTextAddress.setText(address);

        Picasso.with(getContext()).load(URI_SEGMENT_UPLOAD + fileImg).resize(200, 200).into(img_file);

        // Picasso picasso2 = Picasso.with(getContext());
        //   picasso2.load(URI_SEGMENT_UPLOAD + fileImg).resize(100, 100).into(imgProfile);

        tvTextFullname.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextFirstname.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextLastname.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);

        btnUpdate.setEnabled(false);
        btnUpdate.setBackgroundColor(Color.parseColor(GRAY));


        imgSelect.setOnClickListener(this);
        imgMap.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
    }


    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, ":onStop");
    }


    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,":onPause");
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,": onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, ":onResume");
    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        Log.d(TAG, ": onAttach");
    }


    @Override
    public void onDetach(){
        super.onDetach();
        Log.d(TAG, ": onDetach");
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d(TAG,":onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       /* mapView.onDestroy();*/
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //  mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEdit:
                //goToLogin();
                /*Snackbar snackbar = Snackbar.make(getView(),"No internet", Snackbar.LENGTH_LONG);
                snackbar.show();*/
                btnUpdate.setEnabled(true);
                btnUpdate.setBackgroundColor(Color.parseColor(ORANGE));
                editTextUsername.setEnabled(true);
                editTextType.setEnabled(true);
                editTextFirstname.setEnabled(true);
                editTextLastname.setEnabled(true);
                tvTextFullname.setEnabled(true);
                editTextEmail.setEnabled(true);
                btnEdit.setEnabled(false);
                btnEdit.setBackgroundColor(Color.parseColor(GRAY));

                break;

            case R.id.btnUpdate:
                Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                btnUpdate.setEnabled(false);
                btnUpdate.setBackgroundColor(Color.parseColor(GRAY));
                btnEdit.setEnabled(true);
                btnEdit.setBackgroundColor(Color.parseColor(VIOLET));

                break;

            case R.id.imgMap:
                Fragment maps = new Maps();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, maps)
                        .commit();
                break;


            case R.id.img_select:
                Log.d(TAG,": open dialog choose pictures");
                dialogChoosePicture();
                break;
        }
    }




    public void dialogChoosePicture(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(40, 30, 40, 30);

        TextView title = new TextView(getContext());
        title.setBackgroundColor(Color.parseColor("#48367d"));
        title.setText("Select Action");
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setPadding(40, 30, 30, 30);


        int [] [] states = new int[][]{
                new int[]{
                        android.R.attr.state_pressed},

                new int[]{}
                };

        int [] colors = new int[]{
                Color.parseColor(VIOLET),
                Color.parseColor(ORANGE)
        };

        ColorStateList myList = new ColorStateList(states, colors);



        final TextView tvFromFolder = new TextView(getContext());
        tvFromFolder.setId(10);
        tvFromFolder.setText(" Select From Gallery");
        tvFromFolder.setEnabled(true);
        tvFromFolder.setCompoundDrawablePadding(30);
        tvFromFolder.setPadding(80,20,20,20);
        tvFromFolder.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_folder_black_mini, 0, 0, 0);
        tvFromFolder.setTextColor(myList);
        layout.addView(tvFromFolder);


        tvFromFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, ": choose from gallery");
                chooseFromGallery();


            }
        });


        final TextView tvFromCamera = new TextView(getContext());
        tvFromCamera.setId(11);
        tvFromCamera.setText(" Capture From Camera");
        tvFromCamera.setTextColor(myList);
        tvFromCamera.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_from_camera_mini, 0, 0, 0);
        tvFromCamera.setCompoundDrawablePadding(30);
        tvFromCamera.setPadding(80,20,20,20);
        layout.addView(tvFromCamera);

        tvFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, ": capture from camera");
                takeFromCamera();
            }
        });


        /*String[] items = {"Select From Gallery",
                          "Capture From Camera"};

        builder.setItems(items,
            new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   switch (which){
                       case 0:
                           chooseFromGallery();
                           break;
                       case 1:
                           takeFromCamera();
                           break;
                   }
                }
            });
*/

        builder.setView(layout);
        builder.setCustomTitle(title);
        builder.setCancelable(false);

        builder.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        }); //End of alert.setNegativeButton

        final AlertDialog dialog = builder.create();

        dialog.show();


        Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.weight = 0.1f;
        negativeButton.setCompoundDrawablesWithIntrinsicBounds(icon, null,null,null);
        negativeButton.setPadding(50,0,50,0);
        negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
        negativeButton.setLayoutParams(params1);
        negativeButton.setBackgroundColor(Color.parseColor("#F39C12"));
        negativeButton.setTextColor(Color.WHITE);
        negativeButton.invalidate();
        dialog.getWindow().getDecorView().setPaddingRelative(8,32,8,24);
    }

    public void chooseFromGallery(){
       /* Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
*/
        Intent galleryIntent = new Intent();

        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
               // android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_GALLERY_REQUEST);
    }

    public void takeFromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, PICK_CAMERA_REQUEST);

    }



    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

             if(requestCode == PICK_GALLERY_REQUEST){
                 onSelectFromGalleryResult(data);
                 uploadMultipart();
             }

             else if(requestCode ==PICK_CAMERA_REQUEST){
                 onCaptureImageResult(data);
                 uploadMultipart();
             }
        }
    }



    private void onSelectFromGalleryResult(Intent data){
        Bitmap bm =null;
        if(data !=null){
            try{
                filePath = data.getData();
                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),filePath);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        img_file.setImageBitmap(bm);
    }


    /*private void onCaptureImageResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                String fileName = System.currentTimeMillis()+ ".jpg";
                String path = Environment.getExternalStorageDirectory() + "/" + fileName;
                File destination = new File(path);

                FileOutputStream fo;
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();

                filePath = Uri.fromFile(destination);
                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),filePath);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        img_file.setImageBitmap(bm);
    }
*/

    public Uri getImageUri(Context context, Bitmap bm) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bm, "Title", null);
        return Uri.parse(path);
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap bm = (Bitmap) data.getExtras().get("data");

        if(data !=null){
            filePath = getImageUri(getContext(), bm);
        }

        img_file.setImageBitmap(bm);
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
       // tvPath.setText(path);
        cursor.close();
        return path;
    }




    public void uploadMultipart() {
        String path = getPath(filePath);
        File file = new File(path);

        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        RequestBody user_idPart = RequestBody.create(MultipartBody.FORM, editTextUser_id.getText().toString());

        UserService userService = APIClient.getClient().create(UserService.class);
        Call<ResponseModel> call = userService.Upload(body, user_idPart);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {

                    ResponseModel responModel = response.body();
                    if (responModel.getStatus().equals("true")) {

                        Toast.makeText(getContext(), responModel.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(),responModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(getContext(), "Any Problem For this Picture \n Select another Picture.. !", Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                // Toast.makeText(WelcomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(),"Server Disconnected", Toast.LENGTH_SHORT).show();
            }
        });

    }











    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);


        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);

        MenuItem qrcodeItem = menu.findItem(R.id.action_qrcode);
        qrcodeItem.setVisible(false);

        MenuItem cartItem = menu.findItem(R.id.action_cart);
        cartItem.setVisible(false);

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

}

  /*  @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng issaquah   = new LatLng(47.5301011, -122.0326191);

        LatLngBounds bounds;
        LatLngBounds.Builder builder;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.setMinZoomPreference(5);


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

        builder = new LatLngBounds.Builder();

        for (Marker m : markerList) {
            builder.include(m.getPosition());

        }

        bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.animateCamera(cu);


    }
*/

