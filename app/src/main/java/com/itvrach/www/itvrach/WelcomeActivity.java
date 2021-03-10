package com.itvrach.www.itvrach;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itvrach.model.ResponseModel;
import com.itvrach.model.User;
import com.itvrach.services.APIClient;
import com.itvrach.services.UserService;
import com.itvrach.sessions.SessionManagement;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import static com.itvrach.www.itvrach.MainActivity.PREFS_NAME;


public class WelcomeActivity extends AppCompatActivity {
    private Button Logout, btnPickImage, btnUpload, btnProfil, btnList, btnSearch, btnTransaction, btnTransaction2;
    private EditText editTextUser_id;
    private TextView textViewEmail, txtsession_id;
    //private ImageView imgHolder;
    //private String imagePath;
    // private  static final int PICK_IMAGE =1;
    private ImageView imgView;
    private CircleImageView circleImageView;

    private String hp,address;

    //private String mediaPath;
    private TextView str1, filename1;
    private int PICK_IMAGE_REQUEST = 1;
    //storage permission code
    private Uri filePath;
    private Bitmap bitmap;
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    private ProgressDialog mProgress;
    private String fullname, type, firstname, lastname, username, emails;

    private SessionManagement session;
    private  boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_bak);
        this.setTitle("DASHBOARD");
// Session class instance
        session = new SessionManagement(getApplicationContext());

        mProgress = new ProgressDialog(this);
        String titleId = "Processing...";
        mProgress.setTitle(titleId);
        mProgress.setMessage("Logging out...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        askPermissions();



        editTextUser_id = (EditText) findViewById(R.id.editTextUser_id);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        txtsession_id = (TextView) findViewById(R.id.txtsession_id);
        Logout = (Button) findViewById(R.id.btnLogout);

        // t = (TextView)findViewById(R.id.textView3);
      /*  Typeface font_b = Typeface.createFromAsset(getAssets(),"fonts/MarkerFelt.ttf");
        editTextUser_id.setTypeface(font_b);*/
/**
 * Call this function whenever you want to check user login
 * This will redirect user to LoginActivity is he is not
 * logged in
 * */
        session.checkLogin();


        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        final String userid         = user.get(SessionManagement.KEY_USERID);
        final String username       = user.get(SessionManagement.KEY_USERNAME);
        final String type           = user.get(SessionManagement.KEY_TYPE);
        final String firstname      = user.get(SessionManagement.KEY_FIRSTNAME);
        final String lastname       = user.get(SessionManagement.KEY_LASTNAME);
        final String emailid        = user.get(SessionManagement.KEY_EMAIL);
        final String sessionid      = user.get(SessionManagement.KEY_SESSIONID);
        final String fileImg        = user.get(SessionManagement.KEY_FILE);
        final String hp             = user.get(SessionManagement.KEY_HP);
        final String address        = user.get(SessionManagement.KEY_ADDRESS);


        editTextUser_id.setText(userid);
        txtsession_id.setText(sessionid);
        textViewEmail.setText("WELCOME : " + emailid);

        final String email = textViewEmail.getText().toString();


        //SharedPreferences settings = getS_iharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
       /* String user_id = settings.getString("user_id","user_id");
        editTextUser_id.setText(user_id);

        String email = settings.getString("email","email");
        textViewEmail.setText("WELCOME : "+ email);
*/




        /*final Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        final String user_id  = intent.getStringExtra("user_id");
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewEmail.setText("WELCOME : "+ email);

        editTextUser_id.setText(user_id);*/


        //for hidden textview on screen
        editTextUser_id.setVisibility(View.GONE);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnUpload.setEnabled(false);
        btnUpload.setBackgroundColor(Color.GRAY);
        btnPickImage = (Button) findViewById(R.id.pick_img);

        imgView = (ImageView) findViewById(R.id.preview);



        str1 = (TextView) findViewById(R.id.filename1);
        //for hidden textview on screen
        str1.setVisibility(View.GONE);


/*
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(WelcomeActivity.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });
*/


        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();


            }
        });

        /*btnTransaction = (Button) findViewById(R.id.btnTransaction);
        btnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent usersListIntent = new Intent(WelcomeActivity.this, UsersActivity.class);
                startActivity(usersListIntent);
            }
        });*/

        /*btnTransaction2 = (Button) findViewById(R.id.btnTransaction2);
        btnTransaction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TransIntent = new Intent(WelcomeActivity.this, TransationActivity.class);
                startActivity(TransIntent);

            }
        });*/

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                builder.setTitle("Are you sure to Upload this picture?")
                        //.setMessage("----------------------------------------------------------")
                        .setCancelable(false)
                        .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                uploadMultipart();
                                //     WelcomeActivity.this.finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });

        /*btnProfil = (Button) findViewById(R.id.btnProfil);
        btnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        btnList = (Button) findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(WelcomeActivity.this, ListProfileActivity.class);
                startActivity(getIntent);
            }
        });


        Logout = (Button) findViewById(R.id.btnLogout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                final LinearLayout layout = new LinearLayout(WelcomeActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CLIP_VERTICAL);
                layout.setPadding(30, 30, 30, 30);

                TextView title = new TextView(WelcomeActivity.this);
                title.setBackgroundColor(Color.parseColor("#48367d"));
                title.setText("LOG-OUT FROM ITVRACH ?");
                title.setTextColor(Color.WHITE);
                title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                title.setPadding(30, 30, 30, 30);
                builder.setCustomTitle(title);

               // builder.setCustomTitle(title);
               // builder.setMessage("Log out of Itvrach ?");
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

                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                // positiveButton.setPadding(20,20,20,40);
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
                // negativeButton.setPadding(20,20,20,40);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                // positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                params1.weight = 14.0f;
                //params1.gravity= Gravity.CENTER;
                negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
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
        });*/

    }



                /*builder.setMessage("Log out of Itvrach ?")
                        .setCancelable(false)
                        .setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // mProgress.show();
                                mProgress.show();
                                doLogout(email);
                                // Clear the session data
                                // This will clear all session data and
                                // redirect user to LoginActivity



                               *//* Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                startActivity(intent);*//*

                                // mProgress.dismiss();

                                //     WelcomeActivity.this.finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }*/

    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);
        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
        }

        return hasImage;
    }


    private void showFileChooser() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void askPermissions() {

        int permissionCheckStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // we already asked for permisson & Permission granted, call camera intent
        if (permissionCheckStorage == PackageManager.PERMISSION_GRANTED) {

            //do what you want

        } else {

            // if storage request is denied
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You need to give permission to access storage in order to work this feature.");
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        // Show permission request popup
                        ActivityCompat.requestPermissions(WelcomeActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                STORAGE_PERMISSION_REQUEST_CODE);
                    }
                });
                builder.show();

            } //asking permission for first time
            else {
                // Show permission request popup for the first time
                ActivityCompat.requestPermissions(WelcomeActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_REQUEST_CODE);

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // check whether storage permission granted or not.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //do what you want;
                    }
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                imgView.setImageBitmap(bitmap);
                btnUpload.setEnabled(true);
                btnUpload.setBackgroundColor(Color.BLUE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);

        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        //str1.setText(path);
        cursor.close();

        return path;
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                str1.setText(mediaPath);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.preview);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                // Set the Image in ImageView after decoding the String
               // imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                imgView.setImageBitmap(bitmap);

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }
*/



  /*  private void loadImagefromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(WelcomeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }*/


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
                        Toast.makeText(WelcomeActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();

                        final String user_id    = editTextUser_id.getText().toString();
                        final String session_id = txtsession_id.getText().toString();
                        TextView txtfile        = new TextView(WelcomeActivity.this);
                        String file             = txtfile.getText().toString();


                       // session.createLoginSession(user_id, session_id, email, file, fullname, hp, address);


                        session.createLoginSession(user_id,username ,type, firstname, lastname,
                                fullname, emails, session_id, file, hp,address);

                       /* Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);*/
                        session.logoutUser();
                        finish();

                    } else {
                        Toast.makeText(WelcomeActivity.this, "The Email or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(WelcomeActivity.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
               // Toast.makeText(WelcomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                Toast.makeText(WelcomeActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();
                mProgress.dismiss();

            }
        });
    }


    public void uploadMultipart() {
        String path = getPath(filePath);
        File file = new File(path);
        // String name = str1.getText().toString().trim();
        EditText user_id = (EditText) findViewById(R.id.editTextUser_id);
        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        RequestBody user_idPart = RequestBody.create(MultipartBody.FORM, user_id.getText().toString());

        UserService userService = APIClient.getClient().create(UserService.class);
        Call<ResponseModel> call = userService.Upload(body, user_idPart);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {

                    ResponseModel responModel = response.body();
                    if (responModel.getStatus().equals("true")) {

                        btnUpload.setEnabled(false);
                        btnUpload.setBackgroundColor(Color.GRAY);
                        Toast.makeText(WelcomeActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(WelcomeActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {

                    btnUpload.setEnabled(true);
                    btnUpload.setBackgroundColor(Color.BLUE);
                    Toast.makeText(WelcomeActivity.this, "Any Problem For this Picture \n Select another Picture.. !", Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
               // Toast.makeText(WelcomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(WelcomeActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
