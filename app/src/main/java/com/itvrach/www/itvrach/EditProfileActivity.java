package com.itvrach.www.itvrach;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.itvrach.model.ResponseModel;
import com.itvrach.model.User;
import com.itvrach.services.APIClient;
import com.itvrach.services.UserService;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity  {
    private EditText editTextUser_id, editTextUsername, editTextType
            , editTextFirstname, editTextLastname, editTextFullname, editTextplace
            , editTextAge, editTextSalary, editTextEmail
            , editTextdateofbirth ;

    private String user_id, username, type, firstname, lastname, fullname, place, dateofbirth, gender, religions, marital_status, email;
    private RadioGroup radioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private Button buttonUpdate, buttonCancel, button2;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        this.setTitle("EDIT PROFILE");
        Loading =new ProgressDialog(this);
        String titleId="Processing...";
        Loading.setTitle(titleId);
        Loading.setMessage("Update data...");
        Loading.setCancelable(false);
        Loading.setIndeterminate(true);

        editTextUser_id       = (EditText) findViewById(R.id.editTextUser_id);
        editTextUser_id.setVisibility(View.GONE);


        editTextUsername      = (EditText) findViewById(R.id.editTextUsername);
        editTextType          = (EditText) findViewById(R.id.editTextType);
        editTextFirstname     = (EditText) findViewById(R.id.editTextFirstname);
        editTextLastname      = (EditText) findViewById(R.id.editTextLastname);
        editTextFullname      = (EditText) findViewById(R.id.editTextFullname);
        editTextplace         = (EditText) findViewById(R.id.editTextplace);
        editTextdateofbirth   = (EditText) findViewById(R.id.editTextdateofbirth);
        editTextAge           = (EditText) findViewById(R.id.editTextAge);
        editTextSalary        = (EditText) findViewById(R.id.editTextSalary);
        radioGroup            = (RadioGroup) findViewById(R.id.radio_gender);
        maleRadioButton       = (RadioButton) findViewById(R.id.radio_male);
        femaleRadioButton     = (RadioButton) findViewById(R.id.radio_female);
        editTextEmail         = (EditText) findViewById(R.id.editTextEmail);
        single                = (CheckBox) findViewById(R.id.single);
        married               = (CheckBox) findViewById(R.id.married);
        searchableSpinner     = (SearchableSpinner) findViewById(R.id.searchableSpinner);

        editTextUser_id.setEnabled(false);
       // editTextUsername.setEnabled(false);
        editTextdateofbirth.setEnabled(false);
        editTextFullname.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextplace.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextFirstname.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextLastname.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        final Intent data = getIntent();
        if (null != data) {
            int user_id = data.getExtras().getInt("user_id");
            String username = data.getExtras().getString("username");
            String type = data.getExtras().getString("type");
            String firstname = data.getExtras().getString("firstname");
            String lastname = data.getExtras().getString("lastname");
            String fullname = data.getExtras().getString("fullname");
            String place    = data.getExtras().getString("place");
            String dateofbirth = data.getExtras().getString("dateofbirth");
            String email = data.getExtras().getString("email");
            String gender = data.getExtras().getString("gender");
            String marital_status = data.getExtras().getString("marital_status");
            int age = data.getExtras().getInt("age");
            Double salary = data.getExtras().getDouble("salary");
            searchableSpinner.post(new Runnable() {
                @Override
                public void run() {
                    String religion = data.getStringExtra("religions");
                    if (religion.equals("Islam")) {
                        searchableSpinner.setSelection(0);
                    } else if (religion.equals("Katolik")) {
                        searchableSpinner.setSelection(1);
                    } else if (religion.equals("Protestan")) {
                        searchableSpinner.setSelection(2);
                    } else if (religion.equals("Budha")) {
                        searchableSpinner.setSelection(3);
                    } else if (religion.equals("Hindu")) {
                        searchableSpinner.setSelection(4);
                    }
                }
            });


            if (gender.equals("Male")){
                maleRadioButton.setChecked(true);
            } else if (gender.equals("Female")){
                femaleRadioButton.setChecked(true);
            }

            if(marital_status.equals("Single")){
                single.setChecked(true);
                married.setChecked(false);
            }else if(marital_status.equals("Married")){
                married.setChecked(true);
                single.setChecked(false);
            }
            editTextUser_id.setText(String.valueOf(user_id));
            editTextUsername.setText(username);
            editTextType.setText(type);
            editTextFirstname.setText(firstname);
            editTextLastname.setText(lastname);
            editTextFullname.setText(fullname);
            editTextplace.setText(place);
            editTextdateofbirth.setText(dateofbirth);
            editTextEmail.setText(email);
            editTextAge.setText(String.valueOf(age));
            Locale local = new Locale("id", "id");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(local);
            editTextSalary.setText(String.valueOf(fmt.format(salary)).replaceAll("[Rp\\s]", ""));
        }

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
                        religions = selectedItem;
                        break;
                    case "Katholik":
                        religions = selectedItem;
                        break;
                    case "Protestan":
                        religions = selectedItem;
                        break;
                    case "Budha":
                        religions = selectedItem;
                        break;
                    case "Hindu":
                        religions = selectedItem;
                        break;
                    }

                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        calendar = Calendar.getInstance();
        simpleDateFormat = new  SimpleDateFormat("yyyy-MM-dd");
        img_calendar = (ImageButton) findViewById(R.id.img_calendar);
        img_calendar.setBackgroundResource(R.drawable.ic_calendar_default);
        img_calendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year   = calendar.get(Calendar.YEAR);
                month  = calendar.get(Calendar.MONTH);
                dayOfMonth    = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog( EditProfileActivity.this,
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

        single.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(single.isChecked()) {
                    marital_status="Single";
                    married.setChecked(false);
                    Toast.makeText(EditProfileActivity.this, single.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        married.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(married.isChecked()){
                    marital_status="Married";
                    single.setChecked(false);
                    Toast.makeText(EditProfileActivity.this, married.getText(), Toast.LENGTH_SHORT).show();
                }}
        });

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
                    editTextSalary.setText(clean);
                    editTextSalary.setSelection(clean.length());
                    editTextSalary.addTextChangedListener(this);

                }
            }
        });


        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("is This valid data?")

                        .setCancelable(false)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               /* if(username == null || username.trim().length() ==0){
                                    Toast.makeText(EditProfileActivity.this, "Username is required", Toast.LENGTH_SHORT).show();
                                    editTextUsername.requestFocus();
                                    return;
                                }*/

                               /* if(validateRegister(type, firstname, lastname, fullname, place
                                        , dateofbirth, age, salary,  email, gender, marital_status)){*/
                                    Loading.show();
                                    doUpdateData();
                                }
                            //}
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Loading.dismiss();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }


    private void doUpdateData(){
        user_id        = editTextUser_id.getText().toString();
        username       = editTextUsername.getText().toString();
        type           = editTextType.getText().toString();
        firstname      = editTextFirstname.getText().toString();
        lastname       = editTextLastname.getText().toString();
        fullname       = editTextFullname.getText().toString();
        place          = editTextplace.getText().toString();
        dateofbirth    = editTextdateofbirth.getText().toString();
        age            = Integer.parseInt(editTextAge.getText().toString());
        salary         = Double.parseDouble(editTextSalary.getText().toString().replaceAll("[^0-9]", ""));
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

        final User user = new User();
        user.setUser_id(Integer.parseInt(user_id));
        user.setUsername(username);
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
        Call<ResponseModel> call = userService.update(Integer.parseInt(user_id), user);
        call.enqueue(new Callback<ResponseModel>() {

            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    Loading.dismiss();
                    ResponseModel responModel = response.body();
                    if (responModel.getStatus().equals("true")) {
                        Toast.makeText(EditProfileActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfileActivity.this, SearchActivity.class);
                        startActivity(intent);
                        img_calendar.setImageResource(R.drawable.ic_calendar_default);
                    } else {
                        Toast.makeText(EditProfileActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                    Loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                //Toast.makeText(EditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(EditProfileActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();
                Loading.dismiss();
            }
        });
    }


    private Boolean validateRegister(String type,String firstname
            , String lastname ,String fullname, String place, String dateofbirth, int age
            , double salary, String gender, String marital_status, String email){


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

        if(gender == null){
            Toast.makeText(this, "Gender is required", Toast.LENGTH_SHORT).show();
            maleRadioButton.requestFocus();
            return false;
        }

        if(marital_status == null){
            Toast.makeText(this, "Martial is required", Toast.LENGTH_SHORT).show();
            single.requestFocus();
            return false;
        }

        return true;
    }


   /* public void spinnerPosition() {
        Intent data = getIntent();
        String religion = data.getStringExtra("religions");
        if(religion.equals("Islam")){
            searchableSpinner.setSelection(1);
        }else{
            if(religion.equals("Katolik")){
                searchableSpinner.setSelection(2);
            }
        }

    }*/
}
