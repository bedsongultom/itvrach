package com.itvrach.www.itvrach;

//import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
/*import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
*/
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.itvrach.model.ResponseModel;
import com.itvrach.model.User;
import com.itvrach.services.APIClient;
import com.itvrach.services.UserService;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity  {

    private EditText editTextUsername, editTextPassword, editTextType, editTextFirstname
            , editTextplace, editTextLastname, editTextFullname, editTextEmail,editTextFile
            , editTextdateofbirth,editTextAge, editTextSalary, editTextSalary2;

   // private EditText editTextCreatedDate, editTextCreatedTime;
    //private EditText editTextUPdateDates;
    private String username, password, type, firstname, lastname
            ,fullname,place, dateofbirth,  gender, email, religions, marital_status, file ;

    //private String Date;
    private String Time;
    private int year;
    private int month;
    private int dayOfMonth, selectedId;

    private int age;
    private double salary;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat;
   // private SimpleDateFormat simpleDateFormat1;
    //private SimpleDateFormat simpleDateFormat2;
    private Button buttonSave, buttonCancel;
    private ImageButton img_calendar;
   // private ImageButton ImgButton, ImgButtonTime ;
    private RadioGroup radioGroup;
    private RadioButton maleRadioButton,femaleRadioButton;
    //private RadioButton radioButton;
    private TextView tv_marital_status, tv_gender;


    private CheckBox checkBox, single, married;
    //private CheckBox[] checkBox;
    //private Integer[] checkBoxId = {R.id.single, R.id.married};
    //private Spinner apinner;
    private SearchableSpinner searchableSpinner;
    private ProgressDialog mProgress;
    private  EditText inputactivation;
    final Context context = this;


    String[] religion = new String[]{
            "Islam",
            "Katolik",
            "Protestan",
            "Budha",
            "Hindu"
    };

    //searchableSpinner = (SearchableSpinner) findViewById(R.id.searchableSpinner);

   /* private String[] spinnerItems  = {
            "Islam",
            "Khatolik",
            "Protestan",
            "Budha",
            "Hindu",
            "Kohongchu"
    };

*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("REGISTRATION");
        //searchableSpinner.setTitle("Select Item");
        //searchableSpinner.setHint("Please, select something"); // Hint

        mProgress =new ProgressDialog(this);
        String titleId="Processing...";
        mProgress.setTitle(titleId);
        mProgress.setMessage("Registration...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);



        editTextUsername      = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword      = (EditText) findViewById(R.id.editTextPassword);
        editTextType          = (EditText) findViewById(R.id.editTextType);
        editTextFirstname     = (EditText) findViewById(R.id.editTextFirstname);
        editTextLastname      = (EditText) findViewById(R.id.editTextLastname);
        editTextFullname      = (EditText) findViewById(R.id.editTextFullname);
        editTextplace         = (EditText) findViewById(R.id.editTextplace);

        editTextdateofbirth   = (EditText) findViewById(R.id.editTextdateofbirth);
        editTextAge           = (EditText) findViewById(R.id.editTextAge);
        editTextSalary        = (EditText) findViewById(R.id.editTextSalary);
        editTextEmail         = (EditText) findViewById(R.id.editTextEmail);
        radioGroup            = (RadioGroup) findViewById(R.id.radio_gender);
        maleRadioButton       = (RadioButton) findViewById(R.id.radio_male);
        femaleRadioButton     = (RadioButton) findViewById(R.id.radio_female);

        tv_gender             = (TextView) findViewById(R.id.tv_gender);
        tv_marital_status     = (TextView) findViewById(R.id.tv_marital_status);
       // editTextSalary2       = (EditText) findViewById(R.id.editTextSalary2);
        editTextdateofbirth.setEnabled(false);
        editTextdateofbirth.setBackgroundResource(R.drawable.underline);

        editTextFullname.setRawInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextplace.setRawInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextFirstname.setRawInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextLastname.setRawInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        //editTextdateofbirth.setFocusable(false);
        //editTextdateofbirth.setTextColor(Color.GRAY);
        //editTextdateofbirth.setTextColor(Color.parseColor("##D3D3D3"));
      //  editTextdateofbirth.setBackgroundColor(Color.TRANSPARENT);




        single              = (CheckBox)findViewById(R.id.single);
        married             = (CheckBox)findViewById(R.id.married);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.radio_male) {
                    gender="Male";
                } else if (checkedId == R.id.radio_female) {
                    gender="Female";
                }
            }
        });

       /* checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                private CheckBox
                if(checkBox.getId()==R.id.single){
                    marital_status="Single";
                    Toast.makeText(SignUpActivity.this, marital_status, Toast.LENGTH_SHORT).show();
                }else if(checkBox.getId()==R.id.married){
                    marital_status="Married";
                    Toast.makeText(SignUpActivity.this, marital_status, Toast.LENGTH_SHORT).show();
                }

            }
        });*/



        final SearchableSpinner searchableSpinner = (SearchableSpinner) findViewById(R.id.searchableSpinner);
        searchableSpinner.setTitle("Select Your Religion");


        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,religion);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchableSpinner.setAdapter(adapter);

        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String selectedItem = parent.getItemAtPosition(position).toString();
                switch (selectedItem) {

                    case "Islam":
                        religions=selectedItem;
                       // Toast.makeText(getApplicationContext(), religions, Toast.LENGTH_SHORT).show();
                        break;
                    case "Katholik":
                        religions=selectedItem;
                        //Toast.makeText(getApplicationContext(), religions, Toast.LENGTH_SHORT).show();
                        break;
                    case "Protestan":
                        religions=selectedItem;
                       // Toast.makeText(getApplicationContext(), religions, Toast.LENGTH_SHORT).show();
                        break;
                    case "Budha":
                        religions=selectedItem;
                        //Toast.makeText(getApplicationContext(), religions, Toast.LENGTH_SHORT).show();
                        break;
                    case "Hindu":
                        religions=selectedItem;
                        //Toast.makeText(getApplicationContext(), religions, Toast.LENGTH_SHORT).show();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        // inisialiasi Array Adapter dengan memasukkan string array di atas

       /* ategory =  (Spinner)findViewById(R.id.category_group);

        category_spinner= new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.category_value));
        category.setAdapter(category_spinner);

        category.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {

                sppiner_Text= category_spinner.getItem(arg2).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });*/

      //  editTextCreatedDate = (EditText) findViewById(R.id.editTextCreatedDate);
        //editTextCreatedDate.setVisibility(View.GONE);
        //editTextCreatedTime = (EditText) findViewById(R.id.editTextCreatedTime);
        //editTextUPdateDates = (EditText) findViewById(R.id.editTextUPdateDates);
       // male                = (CheckBox) findViewById(R.id.male);
        //female              = (CheckBox) findViewById(R.id.female);


        calendar = Calendar.getInstance();
        //simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat = new  SimpleDateFormat("yyyy-MM-dd");


       // simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");

       // Date = simpleDateFormat.format(calendar.getTime());
        //Tanggal = simpleDateFormat2.format(calendar.getTime());
      //  editTextCreatedDate.setText(Date);
      //  Time = simpleDateFormat1.format(calendar.getTime());
        //editTextCreatedTime.setText(Time);
       // */



  /*    single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(single.isChecked()){
                    married.setChecked(false);

                    Toast.makeText(SignUpActivity.this, "Male is Checked", Toast.LENGTH_SHORT).show();

                }else{
                    married.setChecked(false);
                    Toast.makeText(SignUpActivity.this, "Male is UnChecked", Toast.LENGTH_SHORT).show();

                }
            }
        });*/


      single.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(single.isChecked()) {
                        marital_status="Single";
                        married.setChecked(false);
                        Toast.makeText(SignUpActivity.this, single.getText(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        married.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(married.isChecked()){
                        marital_status="Married";
                single.setChecked(false);
                    Toast.makeText(SignUpActivity.this, married.getText(), Toast.LENGTH_SHORT).show();
            }}
        });






     /* married.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(married.isChecked()){
                    single.setChecked(false);

                    Toast.makeText(SignUpActivity.this, "Female is Checked", Toast.LENGTH_SHORT).show();
                }else{
                    single.setChecked(false);
                    Toast.makeText(SignUpActivity.this, "Female is UnChecked", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/




      /*  ImgButton = (ImageButton) findViewById(R.id.ImgButton);
        ImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextCreatedDate.setText(Date);
            }
        });

        ImgButtonTime  = (ImageButton) findViewById(R.id.ImgButtonTime);
        ImgButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextCreatedTime.setText(Time);
            }
        });
        */
       /* TextWatcher inputTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                editTextSalary2.setText(s.toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        editTextSalary.addTextChangedListener(inputTextWatcher);*/

        editTextSalary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                 String current = "";
                if (!s.toString().equals(current)) {
                    editTextSalary.removeTextChangedListener(this);

                    Locale local = new Locale("id", "id");
                    String replaceable = String.format("[Rp,.\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String cleanString = s.toString().replaceAll(replaceable,
                            "");

                    double parsed;
                    try {
                        parsed = Double.parseDouble(cleanString);
                    } catch (NumberFormatException e) {
                        parsed = 0.00;
                    }

                    NumberFormat formatter = NumberFormat
                            .getCurrencyInstance(local);
                    formatter.setMaximumFractionDigits(0);
                    formatter.setParseIntegerOnly(true);
                    String formatted = formatter.format((parsed));

                    String replace = String.format("[Rp\\s]",
                            NumberFormat.getCurrencyInstance().getCurrency()
                                    .getSymbol(local));
                    String clean = formatted.replaceAll(replace, "");

                    current = formatted;
                    editTextSalary.setText(clean);
                    editTextSalary.setSelection(clean.length());
                    editTextSalary.addTextChangedListener(this);
                    /*
                    tring phoneNumberstr = "Tel: 00971-55 7890 999";
                    String numberRefined = phoneNumberstr.replaceAll("[^0-9]", "");
                    result: 0097557890999
                     */
               }
            }
        });



        img_calendar = (ImageButton) findViewById(R.id.img_calendar);
        img_calendar.setBackgroundResource(R.drawable.ic_calendar_default);
        img_calendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                year   = calendar.get(Calendar.YEAR);
                month  = calendar.get(Calendar.MONTH);
                dayOfMonth    = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog( SignUpActivity.this,
                        new DatePickerDialog.OnDateSetListener(){
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String c = simpleDateFormat.format(calendar.getTime());

                                editTextdateofbirth.setText(c);
                            }
                        },year, month, dayOfMonth);
                datePickerDialog.show();
                img_calendar.setImageResource(R.drawable.ic_calendar_clicked);
                }

        });

        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                 username       = editTextUsername.getText().toString();
                 password       = editTextPassword.getText().toString();
                 type           = editTextType.getText().toString();
                 firstname      = editTextFirstname.getText().toString();
                 lastname       = editTextLastname.getText().toString();
                 fullname       = editTextFullname.getText().toString();
                 place          = editTextplace.getText().toString();
                 dateofbirth    = editTextdateofbirth.getText().toString();
                 age            = Integer.parseInt(editTextAge.getText().toString());

               /* double salePotential = 0; // Variable name should start with small letter
                try {
                    salePotential = Double.parseDouble(EtPotential.getText().toString().replace(",", ""));
                } catch (NumberFormatException e) {
                    // EditText EtPotential does not contain a valid double
                }*/
                 salary         = Double.parseDouble(editTextSalary.getText().toString().replaceAll("[^0-9]", ""));

               /*  selectedId     = radioGroup.getCheckedRadioButtonId();
                 radioButton    = (RadioButton) findViewById(selectedId);*/
               //  gender         = radioButton.getText().toString();
                 religions      = searchableSpinner.getSelectedItem().toString();
                 email          = editTextEmail.getText().toString();

                if(single.isChecked()) {
                      marital_status = single.getText().toString();
                }else{
                      marital_status= married.getText().toString();
                }

                if(maleRadioButton.isChecked()){
                    gender = maleRadioButton.getText().toString();
                }else{
                    gender= femaleRadioButton.getText().toString();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("is This valid data?")

                        .setCancelable(false)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(validateRegister(username,  password, type, firstname, lastname, fullname, place
                                        , dateofbirth, age, salary,  email, gender, marital_status)){
                                    mProgress.show();
                                    doRegister();

                                }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                                mProgress.dismiss();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }



            });


        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void doRegister(){
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setType(type);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setFullname(fullname);
        user.setPlace(place);
        user.setDateofbirth(dateofbirth);
        user.setAge(age);
        user.setSalary(salary);
        user.setGender(gender);
        user.setReligions(religions);
        user.setMarital_status(marital_status);
        user.setEmail(email);

        UserService userService = APIClient.getClient().create(UserService.class);
        Call<ResponseModel> call = userService.Register(user);
        call.enqueue(new Callback<ResponseModel>() {

            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                img_calendar.setImageResource(R.drawable.ic_calendar_default);
               if (response.isSuccessful()) {
                   mProgress.dismiss();
                   ResponseModel responModel = response.body();
                   if (responModel.getStatus().equals("true")) {
                       Toast.makeText(SignUpActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();

                       activationiskey();
                       /*Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                       startActivity(intent);*/



               } else {
                   Toast.makeText(SignUpActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();
               }
            } else {
                Toast.makeText(SignUpActivity.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
            }
        }


        @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
               // Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            Toast.makeText(SignUpActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();


                mProgress.dismiss();
            }
        });
    }




    private void activationiskey() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(30, 30, 30, 30);

    //     inputactivation.requestFocus();
        final EditText inputactivation = new EditText(this);
        inputactivation.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        inputactivation.setHint("Activation Key");
        inputactivation.setInputType(InputType.TYPE_CLASS_TEXT);
        inputactivation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email22, 0, 0, 0);
        inputactivation.setHintTextColor(Color.GRAY);
        layout.addView(inputactivation);
        layout.setPaddingRelative(20,20,20,20);


        final ProgressDialog pd = new ProgressDialog(this);
        //pd.setTitle("Please Wait ....");
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.setProgressStyle(android.R.attr.progressBarStyleLarge);
       // pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        builder.setView(layout);
        //builder.setTitle("RESET E-MAIL PASSWORD");
        TextView title = new TextView(context);
        title.setBackgroundColor(Color.parseColor("#303F9F"));
        title.setText(" activation key from : "+email );
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setPadding(30, 30, 30, 30);
        builder.setCustomTitle(title);

        builder.setCancelable(false);
        builder.setPositiveButton("ACTIVATION",
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

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        //dialog.getWindow().setTitleColor(Color.GREEN);
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
        negativeButton.setBackgroundColor(Color.parseColor("#303F9F"));
        negativeButton.setTextColor(Color.WHITE);
        negativeButton.invalidate();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;
                email = inputactivation.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "The activation can't be empty ", Toast.LENGTH_SHORT).show();
                    //pd.dismiss();
                }
                else
                {
                    pd.show();
                    pd.getWindow().setLayout(245,200);
                    pd.getWindow().setGravity(Gravity.CENTER);

                    User user = new User();
                    user.setEmail(email);
                    UserService userService = APIClient.getClient().create(UserService.class);
                    Call<ResponseModel> call = userService.forgotpassword(user);
                    call.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                            if (response.isSuccessful()) {
                                ResponseModel responModel = response.body();
                                if (responModel.getSuccess().equals("true")) {

                                    Toast.makeText(SignUpActivity.this, responModel.getMessage(), Toast.LENGTH_LONG).show();
                                    pd.dismiss();
                                    dialog.dismiss();
                                   /// changePassword();

                                } else {
                                    Toast.makeText(SignUpActivity.this, "The Activation is incorrect", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(SignUpActivity.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            //Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(SignUpActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();

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

    private Boolean validateRegister(String username, String password, String type,String firstname
            , String lastname ,String fullname, String place, String dateofbirth, int age
            , double salary, String gender, String marital_status, String email){

        if(username == null || username.trim().length() ==0){
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            editTextUsername.requestFocus();
            return false;
        }
        if(password == null || password.trim().length() ==0){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            editTextPassword.requestFocus();
            return false;
        }
        if(type == null || type.trim().length() ==0){
            Toast.makeText(this, "Type is required", Toast.LENGTH_SHORT).show();
            editTextType.requestFocus();
            return false;
        }
        if(firstname == null || firstname.trim().length() ==0){
            Toast.makeText(this, "Firstname is required", Toast.LENGTH_SHORT).show();
            editTextFirstname.requestFocus();
            return false;
        }
        if(lastname == null || lastname.trim().length() ==0){
            Toast.makeText(this, "Lastname is required", Toast.LENGTH_SHORT).show();
            editTextLastname.requestFocus();
            return false;
        }
        if(fullname == null || fullname.trim().length() ==0){
            Toast.makeText(this, "Fullname is required", Toast.LENGTH_SHORT).show();
            editTextFullname.requestFocus();
            return false;
        }

        if(place == null || place.trim().length() ==0){
            Toast.makeText(this, "Place is required", Toast.LENGTH_SHORT).show();
            editTextplace.requestFocus();
            return false;
        }

        if(dateofbirth == null || dateofbirth.trim().length()==0){
            Toast.makeText(this,"DateofBirth is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(age == 0){
            Toast.makeText(this, "Age is required", Toast.LENGTH_SHORT).show();
            editTextAge.requestFocus();
            return false;
        }

        if(salary < 0){
            Toast.makeText(this, "Salary is required", Toast.LENGTH_SHORT).show();
            editTextSalary.requestFocus();
            return false;
        }

        if(email == null || email.trim().length() ==0){
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            editTextEmail.requestFocus();
            return false;
        }

       /* selectedId     = radioGroup.getCheckedRadioButtonId();
        radioButton    = (RadioButton) findViewById(selectedId);
        gender         = radioButton.getText().toString();*/

       if(gender == null){
        Toast.makeText(this, "Gender is required", Toast.LENGTH_SHORT).show();
        tv_gender.requestFocus();
        return false;

        }

        if(marital_status == null){
            Toast.makeText(this, "Martial is required", Toast.LENGTH_SHORT).show();
            tv_marital_status.requestFocus();
            return false;
        }

          return true;
    }

}
