package com.itvrach.www.itvrach;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.itvrach.adapter.SliderAdapter;
import com.itvrach.model.ResponseModel;
import com.itvrach.model.User;
import com.itvrach.services.APIClient;
import com.itvrach.services.UserService;
import com.itvrach.sessions.SessionManagement;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.itvrach.services.APIClient.serverDisconnect;


public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextType, editTextFirstname
            , editTextplace, editTextLastname, editTextFullname, editTextEmail,editTextEmails, editTextFile,editTextPasswords
            , editTextdateofbirth,editTextAge, editTextSalary, editTextSalary2;

    // private EditText editTextCreatedDate, editTextCreatedTime;
    //private EditText editTextUPdateDates;
    private String username, password, type, firstname, lastname
            ,fullname,place, dateofbirth,  gender, email,emails
            , religions, marital_status, file, hp
            , address, session_id, user_id;


    private String age;
    private RadioGroup radioGroup, radioGroup2;
    private RadioButton maleRadioButton, femaleRadioButton,singleRadioButton, marriedRadioButton ;


    //private RadioButton maleRadioButton,femaleRadioButton;
  //  private CheckBox checkBox, single, married;
    private int radio_male, radio_female;


    private Button buttonSignIn, buttonSignUp;
    private String  verify_password,reset_password_key ;
    private CheckBox chekForgotPass;
    private ProgressDialog mProgress;
    private ProgressDialog mProgress2;


    private Spannable spannable;
    private  TextView tv_Forgot;
    final Context context = this;
   // public static final String PREFS_NAME = "MyPrefs";
    private SessionManagement session;
    private  boolean doubleBackToExitPressedOnce = false;

    private int year;
    private int month;
    private int dayOfMonth, selectedId;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat;


    String[] religion = new String[]{
            "",
            "Islam",
            "Katolik",
            "Protestan",
            "Budha",
            "Hindu"
    };





    private SearchableSpinner searchableSpinner;

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private TextView[] mDots;
    private int mCurrentPage;

    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private static final String TAG = "BottomNavigation";
    @ColorInt
    public static final int BLUE = 0xFF0FB8B3;
    @ColorInt
    public static final int GREEN = 0xFF5BBD76;
    private ImageButton img_calendar;
    private Drawable iconX, iconY;


    private View myView;
    private boolean isUp;
    private TextView tv_disconnected;
    private String TAG2 ="ITvrach";

    private TextInputEditText edPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG2,"onCreate Activity");



       iconX = getApplicationContext().getResources().getDrawable(R.drawable.ic_check_white_m);
       iconY = getApplicationContext().getResources().getDrawable(R.drawable.ic_close_white_m);


        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout       = (LinearLayout) findViewById(R.id.dotsLayout);

        sliderAdapter    = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

       /* Bundle extras = getIntent().getExtras();
        boolean WelcomeActivity =Boolean.parseBoolean( extras.getString("WelcomeActivity"));*/
        session = new SessionManagement(getApplicationContext());

        mProgress2 = new ProgressDialog(this);
        String titleId2 = "Processing...";
        mProgress2.setTitle(titleId2);
        mProgress2.setMessage("Sign-up ...");
        mProgress2.setCancelable(false);
        mProgress2.setIndeterminate(true);

         simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
/*
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword.setVisibility(View.GONE);
*/
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextEmail.setVisibility(View.GONE);

        //editTextEmail.setHint(" your@email.com");

       /* spannable = new SpannableString("Forgoten Password? Click Here !!");
        setColorForPath(spannable, new String[] { "Forgoten Password?", "Click Here !!" }, Color.BLUE);
        tv_Forgot.setText(spannable);*/

      /* LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View parentView = inflater.inflate(R.layout.message_layout, null);
       View myView  = parentView.findViewById(R.id.my_view);
       TextView tv_disconnected = (TextView) parentView.findViewById(R.id.tv_d)

      */



       myView = findViewById(R.id.my_view);
       tv_disconnected = (TextView) findViewById(R.id.tv_disconnected);
       tv_disconnected.setText(serverDisconnect);


        TextView tv_Forgot = (TextView) findViewById(R.id.tv_Forgot);
        tv_Forgot.setTextSize(16);

        Spannable word = new SpannableString("Forgoten Your Password?");
        word.setSpan(new ForegroundColorSpan(Color.WHITE), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_Forgot.setText(word);
        Spannable wordTwo = new SpannableString(" Click Here !");

        wordTwo.setSpan(new ForegroundColorSpan(Color.parseColor("#48367d")), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_Forgot.append(wordTwo);
        tv_Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  showEmail();

                forgotPassword();
            }
        });



        Transactiondetail obj = new Transactiondetail();




        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
//       BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

       /* bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        Fragment selectedFragment = null;

                       *//* switch (item.getItemId()) {

                            case R.id.menu_home:
                                item.setCheckable(true);
                                selectedFragment = new Home();
                                break;

                            case R.id.menu_offers:
                                item.setCheckable(true);
                                selectedFragment = new Offers();
                                break;

                            case R.id.menu_search:
                                item.setCheckable(true);
                                selectedFragment = Search.newInstance();
                                break;

                            case R.id.menu_profile:
                                item.setCheckable(true);
                                selectedFragment = Profile.newInstance();
                                break;

                            case R.id.menu_report:
                                item.setCheckable(true);
                                break;
                        }
*//*
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();

                        return true;
                    }
                });
*/




        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   signin();
                  /* email = editTextEmail.getText().toString();
                   password = editTextPassword.getText().toString();

                   if(validateLogin(email, password)){
                       mProgress.show();
                       doLogin(email, password);
                   }*/
               }
           });


        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent (MainActivity.this, SignUpActivity.class);
                startActivity(intent);*/
                signup();
            }
        });
    }


    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG2,"onStart Activity");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG2,"onResume Activity");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d(TAG2,"onRestart Activity");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG2,"onStop Activity");
    }
    @Override

    protected void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        Log.d(TAG2,"onSaveInstanceState Activity");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG2,"onRestoreInstanceState Activity");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG2,"onDestroy Activity");
    }



    public static boolean checkEmail(String email){

        String PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher  = pattern.matcher(email);

        return matcher.matches();
    }


    public void slideUp(View view){
        buttonSignIn.setText("");
        buttonSignUp.setText("");
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,view.getHeight(),0);
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        view.startAnimation(translateAnimation);
        view.setVisibility(View.GONE);
    }

    public void slideDown(View view){
        buttonSignIn.setText("Sign-In");
        buttonSignUp.setText("Sign-Up");
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0, view.getHeight());
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        view.startAnimation(translateAnimation);
        view.setVisibility(View.GONE);

    }


    public static boolean checkDate(String dateofbirth){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setLenient(false);
        try {
            simpleDateFormat.parse(dateofbirth.trim());
        }catch (ParseException pe){
            return false;
        }
        return true;
    }





    private Paint createPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new LinearGradient(0, 0, 0, 600, BLUE, GREEN, Shader.TileMode.CLAMP));
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

   /* private static class BottomNavigationViewHelper {
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
*/


    public void addDotsIndicator(int position) {
       mDots = new TextView[3];
      //for remove redudant dots
       mDotLayout.removeAllViews();

       for (int i = 0; i < mDots.length; i++) {
           mDots[i] = new TextView(this);
           mDots[i].setText(Html.fromHtml("&#8226;"));
           mDots[i].setTextSize(35);
           mDots[i].setTextColor(Color.GRAY);
           mDotLayout.addView(mDots[i]);
       }

       if(mDots.length > 0){
           mDots[position].setTextColor(Color.WHITE);

       }

   }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrentPage =i;
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void showEmail(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        RelativeLayout mainlayout = new RelativeLayout(MainActivity.this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(0, 0, 0 , 0);

        TextView title = new TextView(MainActivity.this);
        title.setBackgroundColor(Color.parseColor("#48367d"));
        title.setText("RESET E-MAIL PASSWORD");
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setPadding(30, 30, 30, 30);

        final int left   = 25;
        final int top    = 20;
        final int right  = 20;
        final int bottom = 0;

        TextView Textemail = new TextView(MainActivity.this);
        LinearLayout.LayoutParams fm1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fm1.setMargins(left,top,right,bottom);
        Textemail.setText("E-mail");
        Textemail.setLayoutParams(fm1);
        Textemail.setTextColor(Color.GRAY);
        layout.addView(Textemail);


        RelativeLayout rl2 = new RelativeLayout(MainActivity.this);
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams  fm2 =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rl2.setLayoutParams(fm2);
        rl2.setPadding(20,50,20,20);
        input.setSingleLine();
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setLayoutParams(fm2);
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        input.setHintTextColor(Color.GRAY);
        rl2.addView(input);


        RelativeLayout relativelayout = new RelativeLayout(this);
        ViewGroup.LayoutParams  layoutparams =  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativelayout.setLayoutParams(layoutparams);

        LinearLayout linearlayout = new LinearLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        linearlayout.setBackgroundColor(Color.parseColor("#303F9F"));
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        linearlayout.setLayoutParams(params);
        linearlayout.setOrientation(LinearLayout.HORIZONTAL);
        relativelayout.addView(linearlayout);

        Button negativeButton = new Button (this);
        negativeButton.setBackgroundColor(Color.parseColor("#48367d"));
        negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null,null,null);
        negativeButton.setPadding(50,0,50,0);
        LinearLayout.LayoutParams negativeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        negativeParams.weight = 1.0f;
        negativeButton.setLayoutParams(negativeParams);
        negativeButton.setTextColor(Color.WHITE);
        negativeButton.setGravity(Gravity.CENTER);
        negativeButton.setText("CANCEL");
        negativeButton.invalidate();

        Button positiveButton = new Button (this);
        positiveButton.setBackgroundColor(Color.parseColor("#F39C12"));
        positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null,null,null);
        positiveButton.setPadding(50,0,50,0);
        LinearLayout.LayoutParams positiveParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        positiveParams.weight = 1.0f;
        positiveButton.setLayoutParams(positiveParams);
        positiveButton.setTextColor(Color.WHITE);
        positiveButton.setGravity(Gravity.CENTER);
        positiveButton.setText("SUBMIT");
        positiveButton.invalidate();


        linearlayout.addView(negativeButton);
        linearlayout.addView(positiveButton);

        mainlayout.addView(rl2);
        mainlayout.addView(layout);
        mainlayout.addView(relativelayout);

        builder.setView(mainlayout);
        builder.setCustomTitle(title);
        builder.setCancelable(false);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, 500);

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            dialog.dismiss();
            }
        });


}



    private void showmessage(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final TextView input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString().trim();
                Toast.makeText(getApplicationContext(), value,
                        Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
    }

/*
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    SharedPreferences.Editor editor = preferences.edit();
 editor.putBoolean("isLogin",true);
 editor.commit();


 and than start next activity. Now on all other activities except LoginActivity in onResume method add:

SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
if(!preferences .getBoolean("isLogin"))
{
   //TODO go to loginActivity
}


or

If user login then save his user id into SharedPreferences


    SharedPreferences preferences = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor PrefsEditor = preferences.edit();

    int user_id=101;

    //set value
    PrefsEditor.putInt("USERID", "user_id");


    In splash screen check:

    //get value
    int user_id=preferences.getInt("USERID", -1);

     if (user_id == -1) {
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        finish();
    } else {
        Intent intent = new Intent(mContext, HomeActivity.class);
        startActivity(intent);
        finish();
    }
  */

    private void changePassword() {
        final ProgressDialog pd = new ProgressDialog(this);
        //pd.setTitle("Please Wait ....");
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.setProgressStyle(android.R.attr.progressBarStyleLarge);

        pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(30, 30, 30, 30);

        TextView title = new TextView(context);
        title.setBackgroundColor(Color.parseColor("#303F9F"));
        title.setText("CHANGE E-MAIL PASSWORD");
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setPadding(30, 30, 30, 30);

        final EditText password_text = new EditText(this);
        password_text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        password_text.setHint("New Password");
        password_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, 0, 0);
        password_text.setHintTextColor(Color.GRAY);
        layout.addView(password_text);

        final EditText verify_password_text = new EditText(this);
        verify_password_text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        verify_password_text.setHint("Re-Enter Password");
        verify_password_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        verify_password_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_verify_password, 0, 0, 0);
        verify_password_text.setHintTextColor(Color.GRAY);
        layout.addView(verify_password_text);

        final EditText reset_password_key_text = new EditText(this);
        reset_password_key_text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        reset_password_key_text.setHint("Enter Reset Key");
        reset_password_key_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        reset_password_key_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_key, 0, 0, 0);
        reset_password_key_text.setHintTextColor(Color.GRAY);
        layout.addView(reset_password_key_text);



        builder.setView(layout);
        //builder.setTitle("CHANGE E-MAIL PASSWORD");

        builder.setCustomTitle(title);

        builder.setCancelable(false);
        builder.setPositiveButton("Change",
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
        layout.setPaddingRelative(20,20,20,10);

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;
                password           = password_text.getText().toString();
                verify_password    = verify_password_text.getText().toString();
                reset_password_key = reset_password_key_text.getText().toString();

                if(password == null || password.trim().length() ==0){
                    Toast.makeText(MainActivity.this, "Password can't be Empty", Toast.LENGTH_SHORT).show();
                    password_text.requestFocus();
                    return;
                }

                if(verify_password == null || verify_password.trim().length() ==0){
                    Toast.makeText(MainActivity.this, "Verify Password can't be Empty", Toast.LENGTH_SHORT).show();
                    verify_password_text.requestFocus();
                    return;
                }

                if(! verify_password.equals(password)){
                    Toast.makeText(MainActivity.this, "Verify Password not the same to password", Toast.LENGTH_SHORT).show();
                    verify_password_text.requestFocus();
                    return;
             }

                if(reset_password_key == null || reset_password_key.trim().length() ==0){
                    Toast.makeText(MainActivity.this, "Reset Key can't be Empty", Toast.LENGTH_SHORT).show();
                    reset_password_key_text.requestFocus();
                    return;


                }

                else{

                    pd.show();
                    pd.getWindow().setLayout(245,200);
                    pd.getWindow().setGravity(Gravity.CENTER);
                    User user = new User();
                    user.setPassword(password);
                    user.setReset_password_key(reset_password_key);

                    UserService userService = APIClient.getClient().create(UserService.class);
                    Call<ResponseModel> call = userService.changepassword(reset_password_key, user);
                    call.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful()) {
                        ResponseModel responModel = response.body();
                        if(responModel.getSuccess().equals("true")) {
                            Toast.makeText(MainActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                                dialog.dismiss();
                        }else {
                            Toast.makeText(MainActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }


                    } else {
                        Toast.makeText(MainActivity.this, "Invalid Key", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                  //  Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();

                    pd.dismiss();
                }
            });



                   /* Call<User> call = userService.changepassword(user.getReset_password_key(), user);
                    call.enqueue(new Callback<User>() {
                                     @Override
                                     public void onResponse(Call<User> call, Response<User> response) {
                                         if (response.isSuccessful()) {
                                             User changepassword = response.body();
                                             password_text.setText(changepassword.getPassword());
                                             reset_password_key_text.setText(changepassword.getReset_password_key());
                                             Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_LONG).show();
                                             pd.dismiss();
                                             dialog.dismiss();

                                         }else{
                                             Toast.makeText(MainActivity.this, "INVALID KEY", Toast.LENGTH_LONG).show();
                                             pd.dismiss();

                                         }
                                     }

                                     @Override
                                     public void onFailure(Call<User> call, Throwable t) {
                                         Log.e("Response :", "Unsuccessfull");
                                         Log.e("Update :", "Exception: " + t.getMessage());
                                         Toast.makeText(getBaseContext(), "Check it Again", Toast.LENGTH_SHORT).show();

                                     }
                                 });

*/


                           /* try {
                                ResponseModel responModel = response.body();
                                if (response.isSuccessful() && responModel.getSuccess().equals("true")) {
                                    Toast.makeText(MainActivity.this, responModel.getMessage(), Toast.LENGTH_LONG).show();
                                    pd.dismiss();
                                    dialog.dismiss();

                                } else {
                                    Toast.makeText(MainActivity.this, responModel.getMessage(), Toast.LENGTH_LONG).show();
                                    pd.dismiss();
                                }
                           *//* }else {
                                        Toast.makeText(MainActivity.this, responModel.getMessage()+" brother", Toast.LENGTH_LONG).show();
                                        pd.dismiss();
                                    }*//*
                                }
                                catch (Exception e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                                   *//* Log.d("onResponse", "There is an error");
                                    e.printStackTrace();*//*
                                }
                            }


                            *//*if (response.isSuccessful()) {
                                ResponseModel responModel = response.body();
                                if(responModel.getKey().equals("true")) {
                                    Toast.makeText(MainActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();
                                  //  pd.dismiss();
                                    if (responModel.getSuccess().equals("true")) {
                                        Toast.makeText(MainActivity.this, responModel.getMessage(), Toast.LENGTH_LONG).show();
                                        pd.dismiss();
                                        dialog.dismiss();

                                    }
                                }else {
                                    Toast.makeText(MainActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    pd.dismiss();

                                    }


                            } else {
                                Toast.makeText(MainActivity.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        }*//*

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });*/
                }

                    if (wantToCloseDialog) {
                        dialog.dismiss();
                    }
                }
            });
        }


    private void forgotPassword() {
        //AlertDialog.Builder builder = new AlertDialog.Builder(this, R.color.colorGrey);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final ProgressDialog pd = new ProgressDialog(this);
        //pd.setTitle("Please Wait ....");
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.setProgressStyle(android.R.attr.progressBarStyleLarge);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(40, 30, 40, 30);

        TextView title = new TextView(context);
        title.setBackgroundColor(Color.parseColor("#48367d"));
        title.setText("RESET E-MAIL PASSWORD");
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setPadding(40, 30, 30, 30);

       // String lines = "RESET E-MAIL PASSWORD ";
        //lines +="--------------------------------------------------";

      /*  final TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setText("RESET E-MAIL PASSWORD").;
        //textView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
      //  textView.setHintTextColor(Color.RED);
        layout.addView(textView);*/

       /* final TextView textView1 = new TextView(this);
        textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        textView1.setText("------------------------------------------------------------------------");
       *//* textView1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        textView1.setHintTextColor(Color.GRAY);*//*
        layout.addView(textView1);*/

      /*  final EditText input1 = new EditText(this);
        input1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        input1.setHint("RESET E-MAIL PASSWORD");
        input1.setInputType(InputType.TYPE_NULL);
        input1.setFocusable(false);
        input1.setFocusableInTouchMode(false);
        input1.setHintTextColor(Color.BLACK);
        layout.addView(input1);
*/



        TextInputLayout TextEmail = new TextInputLayout(MainActivity.this);
        TextEmail.setId(1);
        TextEmail.setLabelFor(2);
        layout.addView(TextEmail);


        final EditText input = new EditText(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        input.setHint("E-mail address");
        input.setId(2);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setSingleLine(true);
        input.setTextSize(15);
        input.setHintTextColor(Color.GRAY);
        TextEmail.addView(input);





       /* LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(30, 30, 30, 30);*/
       // layout.setPaddingRelative(20,20,20,20);



        builder.setView(layout);
        //builder.setTitle("RESET E-MAIL PASSWORD");

        builder.setCustomTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton("Sumbit",
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

       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));
        //dialog.getWindow().setTitleColor(Color.GREEN);



        dialog.getWindow().getDecorView().setPaddingRelative(8,32,8,24);

        Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
       // positiveButton.setPadding(20,20,20,40);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
        params.weight = 14.0f;
        //params.gravity = Gravity.CENTER_HORIZONTAL; //this is layout_gravity

        positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null,null,null);
        positiveButton.setPadding(40,0,40,0);

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

        negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null,null,null);
        negativeButton.setPadding(40,0,40,0);

        negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
        negativeButton.setLayoutParams(params1);
        negativeButton.setBackgroundColor(Color.parseColor("#48367d"));
        negativeButton.setTextColor(Color.WHITE);
        negativeButton.invalidate();

       /*    Button neutralButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
       // neutralButton.setGravity(Gravity.CENTER_HORIZONTAL);
        params2.weight = 3.0f;
        params2.gravity = Gravity.CENTER; //this is layout_gravity
        neutralButton.setLayoutParams(params2);
        neutralButton.setBackgroundColor(Color.parseColor("#F39C12"));
        neutralButton.setTextColor(Color.WHITE);
        neutralButton.setVisibility(View.INVISIBLE);
        //neutralButton.invalidate();*/









     /*   Button neutralButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
       // neutralButton.setGravity(Gravity.CENTER_HORIZONTAL);
        params1.weight = 1.0f;
        params1.gravity = Gravity.CENTER; //this is layout_gravity
        neutralButton.setLayoutParams(params1);
        neutralButton.setBackgroundColor(Color.parseColor("#F39C12"));
        neutralButton.setTextColor(Color.WHITE);
        neutralButton.setVisibility(View.INVISIBLE);
        //neutralButton.invalidate();*/


        /*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); //create a new one
        layoutParams.weight = 1.0f;
        layoutParams.gravity = Gravity.CENTER; //this is layout_gravity
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setLayoutParams(layoutParams);
*/









        /*Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        LinearLayout.LayoutParams posParams = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
       // posParams.weight = 3f;
        //posParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        positiveButton.setBackgroundColor(Color.parseColor("#303F9F"));
        posParams.gravity = Gravity.CENTER;


        Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negParams = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
       // negParams.weight = 2f;
        //negParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        negParams.gravity = Gravity.CENTER;
        negativeButton.setBackgroundColor(Color.parseColor("#F39C12"));

        negativeButton.setLayoutParams(posParams);
        positiveButton.setLayoutParams(negParams);*//*

        Button negativebtn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativebtn.setBackgroundColor(Color.parseColor("#303F9F"));
        LinearLayout.LayoutParams negativebtnPosition = (LinearLayout.LayoutParams) negativebtn.getLayoutParams();
        negativebtnPosition.gravity = Gravity.CENTER;
        negativebtnPosition.weight = 2f;
        negativebtnPosition.width = ViewGroup.LayoutParams.FILL_PARENT;
        negativebtnPosition.height = ViewGroup.LayoutParams.FILL_PARENT;
        negativebtn.setLayoutParams(negativebtnPosition);


        Button positivebtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positivebtn.setBackgroundColor(Color.parseColor("#F39C12"));
        LinearLayout.LayoutParams positivebtnPosition = (LinearLayout.LayoutParams) positivebtn.getLayoutParams();
        positivebtnPosition.gravity = Gravity.CENTER;
        negativebtnPosition.weight = 2f;
        positivebtnPosition.width = ViewGroup.LayoutParams.FILL_PARENT;
        positivebtnPosition.height = ViewGroup.LayoutParams.FILL_PARENT;
        positivebtn.setLayoutParams(positivebtnPosition);
*/



        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;
                 email = input.getText().toString();
                /*if (email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "The email can't be empty ", Toast.LENGTH_SHORT).show();
                    //pd.dismiss();
                  }
*/
                if (email == null || email.trim().length() == 0 ){
                    Toast.makeText(MainActivity.this, "E-mail is required", Toast.LENGTH_SHORT).show();
                    input.requestFocus();
                    return;
                }


                if(!checkEmail(email)){
                    Toast.makeText(MainActivity.this, "InValid mail Address", Toast.LENGTH_SHORT).show();
                    input.requestFocus();
                    return;
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
                                   /* Toast toast = Toast.makeText(MainActivity.this,responModel.getMessage(), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.LEFT, 0, 0);
                                    toast.show();*/


                                 Toast.makeText(MainActivity.this, responModel.getMessage(), Toast.LENGTH_LONG).show();
                                    pd.dismiss();
                                    dialog.dismiss();
                                    changePassword();

                                } else {
                                    Toast.makeText(MainActivity.this, "The Email or password is incorrect", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(MainActivity.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                           // Toast.makeText(MainActivity.this, "Server Disconnected", Toast.LENGTH_SHORT).show();
                            slideUp(myView);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    slideDown(myView);
                                }
                            },  3000);
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



    public static void setColorForPath(Spannable spannable, String[] paths, int color) {
        for (int i = 0; i < paths.length; i++) {
            int indexOfPath = spannable.toString().indexOf(paths[i]);
            if (indexOfPath == -1) {
                return;
            }
            spannable.setSpan(new ForegroundColorSpan(color), indexOfPath,
                    indexOfPath + paths[i].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void termsandprivacy(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(40, 30, 40, 30);

        TextView title = new TextView(context);
        title.setBackgroundColor(Color.parseColor("#48367d"));
        title.setText("TERMS AND CONDITION");
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setPadding(40, 30, 30, 30);

        TextView TextEmails = new TextView(MainActivity.this);
        TextEmails.setText("SOFTWARE");
        TextEmails.setTextColor(Color.GRAY);
        layout.addView(TextEmails);



        TextView textDescription = new TextView(MainActivity.this);
        LinearLayout.LayoutParams lDescription = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textDescription.setLayoutParams(lDescription);
        textDescription.setText("Description");
        textDescription.setTextColor(Color.GRAY);
        layout.addView(textDescription);

        // Create span
        String span =("ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss"
                +"sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss"
                +"sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
               /* span.setSpan(new ForegroundColorSpan(Color.BLUE),
                        0, span.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
*/

        DocumentView documentView = new DocumentView(MainActivity.this, DocumentView.FORMATTED_TEXT);
        // Support spanned text
        // Set the fallback alignment if an alignment is not specified for a line
        documentView.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
        documentView.setText(span); // Set to `true` to enable justification

        ReadMoreTextView readMoreTextView = new ReadMoreTextView (MainActivity.this);
        LinearLayout lnrm = new LinearLayout(this);
        lnrm.setBackgroundResource(R.drawable.edittext_border);
        LinearLayout.LayoutParams lexpTv1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lnrm.setBackgroundColor(Color.parseColor("#FFDCDCDE"));

        readMoreTextView.setTrimExpandedText("\n Show Less");
        readMoreTextView.setPadding(0,20,0,20);
        readMoreTextView.setTrimCollapsedText("\n Show More");

        readMoreTextView.setTrimMode(1);
        readMoreTextView.setTrimLength(0);
        readMoreTextView.setLayoutParams(lexpTv1);
        readMoreTextView.setColorClickableText(Color.parseColor("#48367d"));
        String ss = documentView.getText().toString();
        readMoreTextView.setText(ss);
        lnrm.addView(readMoreTextView);
        layout.addView(lnrm);



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
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));

        Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
        params1.weight = 0.1f;
        //params1.gravity= Gravity.CENTER;

        negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null,null,null);
        negativeButton.setPadding(80,0,80,0);

        negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
        negativeButton.setLayoutParams(params1);
        negativeButton.setBackgroundColor(Color.parseColor("#48367d"));
        negativeButton.setTextColor(Color.WHITE);
        negativeButton.invalidate();
        // layout.setPaddingRelative(22,0,22,0);
        dialog.getWindow().getDecorView().setPaddingRelative(8,32,8,24);

    }




    private void signin(){

        mProgress =new ProgressDialog(context,R.style.AppCompatAlertDialogStyle);
        String titleId="Processing...";
        mProgress.setTitle(titleId);
        mProgress.setMessage("Logging in...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        ColorStateList colorStateListEditText = new ColorStateList(new int[][]{
                new int[]{android.R.attr.state_enabled}},
                new int[]{
                        getResources().getColor(R.color.colorPrimaryDark)}
        );
       // pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(40, 30, 40, 30);

        TextView title = new TextView(context);
        title.setBackgroundColor(Color.parseColor("#48367d"));
        title.setText("SIGN-IN");
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setPadding(40, 30, 30, 30);

        /*TextView TextEmails = new TextView(MainActivity.this);
        TextEmails.setText("E-mail");
        TextEmails.setTextColor(Color.GRAY);
        layout.addView(TextEmails);
*/

        TextInputLayout TextEmails = new TextInputLayout(MainActivity.this);
        TextEmails.setId(12);
        TextEmails.setLabelFor(13);
        layout.addView(TextEmails);


        editTextEmails = new EditText(this);
        editTextEmails.setHint("E-mail address");
        editTextEmails.setId(13);
        editTextEmails.setTextSize(15);
        editTextEmails.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //editTextEmails.setHint("your@email.com");
        editTextEmails.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        editTextEmails.setSingleLine(true);
        editTextEmails.setBackgroundTintList(colorStateListEditText);
      //  editTextEmails.setBackgroundTintList(ColorStateList.valueOf(0x48367d));



       // editTextEmails.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email2, 0, 0, 0);
      //  editTextEmails.setHintTextColor(Color.GRAY);
        //layout.addView(editTextEmails);
        TextEmails.addView(editTextEmails);





        editTextEmail=editTextEmails;

/*

        TextView TextPasswords = new TextView(MainActivity.this);
        TextPasswords.setText("Password");
        TextPasswords.setTextColor(Color.GRAY);
        layout.addView(TextPasswords);
*/

        //noinspection deprecation
        Drawable selector = getResources().getDrawable(R.drawable.show_password_selector);
        final TextInputLayout tvPasswords = new TextInputLayout(MainActivity.this);
        tvPasswords.setId(10);
        tvPasswords.setLabelFor(11);
        tvPasswords.setPasswordVisibilityToggleEnabled(true);
        tvPasswords.setPasswordVisibilityToggleTintList(colorStateListEditText);
        tvPasswords.setPasswordVisibilityToggleDrawable(selector);

        layout.addView(tvPasswords);


        /*final TextInputEditText edPass = new TextInputEditText(this);
        edPass.setId(11);
        edPass.setEms(10);
        edPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edPass.setHint("Password");
        edPass.setTextSize(15);
        edPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (view.getId()) {
                    case 10: //R.id.ivPasswordToggle;

                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                Toast toastShow = Toast.makeText(MainActivity.this, "Show", Toast.LENGTH_SHORT);
                                toastShow.show();

                                edPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                edPass.setSelection(edPass.getText().length());
                                break;

                            case MotionEvent.ACTION_UP:
                                edPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                edPass.setSelection(edPass.getText().length());

                                Toast toastHide = Toast.makeText(MainActivity.this, "Hide", Toast.LENGTH_SHORT);
                                toastHide.show();
                                break;
                        }
                        break;

                }
                return false;
            }
        });

        tvPass.addView(edPass);
*/
















        //
        //
        // String val ="";

        editTextPasswords = new EditText(this);
        editTextPasswords.setId(11);
        editTextPasswords.setHint("Password");
        editTextPasswords.setTextSize(15);
        editTextPasswords.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
       // editTextPasswords.setHint("Enter Password");
       // editTextPasswords.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextPasswords.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextPasswords.setBackgroundTintList(colorStateListEditText);
        editTextPasswords.setLongClickable(false);

        editTextPasswords.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (view.getId()) {
                    case 10: //R.id.ivPasswordToggle;

                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                Toast toastShow = Toast.makeText(MainActivity.this, "Show", Toast.LENGTH_SHORT);
                                toastShow.show();

                                editTextPasswords.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                editTextPasswords.setSelection(editTextPasswords.getText().length());
                                break;

                            case MotionEvent.ACTION_UP:
                                editTextPasswords.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                editTextPasswords.setSelection(editTextPasswords.getText().length());

                                Toast toastHide = Toast.makeText(MainActivity.this, "Hide", Toast.LENGTH_SHORT);
                                toastHide.show();
                                break;
                        }
                        break;

                }
                return false;
            }
        });

        tvPasswords.addView(editTextPasswords);







        /*editTextPasswords.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_visibility, 0);
        //  editTextPasswords.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, 0, 0);
        //editTextPasswords.setHintTextColor(Color.GRAY);




    //    String val="";
     //   editTextPasswords.setText(val);
       final int plan = R.drawable.ic_visibility;
       final int enkripsi = R.drawable.ic_visibility_off;

        *//*final Drawable x = getResources().getDrawable(plan);//your x image, this one from standard android images looks pretty good actually
        x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
        editTextPasswords.setCompoundDrawables(null, null, x, null);
        editTextPasswords.setCompoundDrawablesWithIntrinsicBounds(0, 0,plan, 0);
*//*
        editTextPasswords.setOnTouchListener(new View.OnTouchListener() {
             public boolean onTouch(View v, MotionEvent event) {
                 if (event.getRawX() >= (editTextPasswords.getRight() - editTextPasswords.getCompoundDrawables()[2]
                         .getBounds().width())) {

                     if (event.getAction() == MotionEvent.ACTION_UP) {

                         editTextPasswords.setTransformationMethod(new PasswordTransformationMethod());
                         editTextPasswords.setCompoundDrawablesWithIntrinsicBounds(0, 0, plan, 0);
                         //editTextPasswords.setSelection(editTextPasswords.getText().length());
                         return true;
                     }
                     if (event.getAction() == MotionEvent.ACTION_DOWN) {

                         editTextPasswords.setCompoundDrawablesWithIntrinsicBounds(0, 0, enkripsi, 0);
                         editTextPasswords.setTransformationMethod(null);

                         return true;
                     }
                     editTextPasswords.setSelection(editTextPasswords.getText().length());
                 }
                 return false;
             }
         });*/


        /*editTextPasswords.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (editTextPasswords.getCompoundDrawables()[2] == null) {
                    return false;
                }

                if (event.getAction() != MotionEvent.ACTION_UP) {

                    editTextPasswords.setTransformationMethod(new PasswordTransformationMethod());
                    editTextPasswords.setCompoundDrawablesWithIntrinsicBounds(0, 0, plan, 0);
                    editTextPasswords.setSelection(editTextPasswords.getText().length());
                    return false;

                }

                if (event.getRawX() >= editTextPasswords.getWidth() - editTextPasswords.getPaddingRight() - x.getIntrinsicWidth()) {
                    editTextPasswords.setCompoundDrawablesWithIntrinsicBounds(0, 0, enkripsi, 0);
                    editTextPasswords.setTransformationMethod(null);
                   // editTextPasswords.setSelection(editTextPasswords.getText().length());
                    return false;
                }

                return false;
            }
        });
*/

       /* editTextPasswords.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (event.getRawX() >= editTextPasswords.getRight() - editTextPasswords.getCompoundDrawables()[2].getBounds().width()) {
                            editTextPasswords.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                            editTextPasswords.setTransformationMethod(null);
                            editTextPasswords.setSelection(editTextPasswords.getText().length());
                       }
                        break;
                    case MotionEvent.ACTION_UP:
                       // if (event.getRawX() >= editTextPasswords.getRight() - editTextPasswords.getCompoundDrawables()[2].getBounds().width()) {
                            editTextPasswords.setTransformationMethod(new PasswordTransformationMethod());
                            editTextPasswords.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
                            editTextPasswords.setSelection(editTextPasswords.getText().length());
                        //}
                        break;

                }
                    return false;

                }

        });
*/

     //
        //
        //  layout.addView(editTextPasswords);


        builder.setView(layout);
        builder.setCustomTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton("Submit",
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
       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#bebebe")));


        Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
        params.weight = 14.0f;
        //params.gravity = Gravity.CENTER_HORIZONTAL; //this is layout_gravity
        positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null,null,null);
        positiveButton.setPadding(40,0,40,0);

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

        negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null,null,null);
        negativeButton.setPadding(40,0,40,0);

        negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
        negativeButton.setLayoutParams(params1);
        negativeButton.setBackgroundColor(Color.parseColor("#48367d"));
        negativeButton.setTextColor(Color.WHITE);
        negativeButton.invalidate();
       // layout.setPaddingRelative(22,0,22,0);

        dialog.getWindow().getDecorView().setPaddingRelative(8,32,8,24);

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;

                email = editTextEmails.getText().toString();
                password = editTextPasswords.getText().toString();

                if (email == null || email.trim().length() == 0 ){
                    Toast.makeText(MainActivity.this, "E-mail is required", Toast.LENGTH_SHORT).show();
                    editTextEmails.requestFocus();
                    return;
                }


                if(!checkEmail(email)){
                    Toast.makeText(MainActivity.this, "InValid mail Address", Toast.LENGTH_SHORT).show();
                    editTextEmails.requestFocus();
                    return;
                }

                if(password == null || password.trim().length() ==0){
                    Toast.makeText(MainActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                    editTextPasswords.requestFocus();
                    return;
                }

                else{
                    mProgress.show();
                    doLogin(email, password);
                }


                if (wantToCloseDialog) {
                    dialog.dismiss();
                }
            }
        });

    }


    private SpannableStringBuilder setClickablePart(String str){
        SpannableStringBuilder mspannableStringBuilder = new SpannableStringBuilder(str);
        int m_index = str.indexOf("terms & conditions");


        final  String clickString = str.substring(m_index, str.length());
        mspannableStringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                termsandprivacy();
             }
            }, m_index, str.length(),0);
        return mspannableStringBuilder;
    }

    private void signup(){
        final ProgressDialog pd = new ProgressDialog(this);

        //pd.setTitle("Please Wait ....");



        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.getWindow().setGravity(Gravity.CENTER);
        pd.setProgressStyle(android.R.attr.progressBarStyleLarge);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        calendar = Calendar.getInstance();
        //simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(40, 30, 40, 30);

        TextView title = new TextView(context);
        title.setBackgroundColor(Color.parseColor("#48367d"));
        title.setText("SIGN-UP");
        title.setTextColor(Color.WHITE);
        title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title.setPadding(45, 30, 30, 30);

        final ScrollView scrollView = new ScrollView(MainActivity.this);


        TextView TextUsername = new TextView(MainActivity.this);
        TextUsername.setText("Username");
        TextUsername.setTextColor(Color.GRAY);
        layout.addView(TextUsername);
        final EditText editTextUsername = new EditText(MainActivity.this);
        editTextUsername.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editTextUsername.setTextColor(Color.BLACK);
        editTextUsername.setSingleLine(true);
        editTextUsername.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(editTextUsername);


        TextView TextPassword = new TextView(MainActivity.this);
        TextPassword.setText("Password");
        TextPassword.setTextColor(Color.GRAY);
        layout.addView(TextPassword);



        final EditText editTextPassword = new EditText(MainActivity.this);
        editTextPassword.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        // editTextPasswords.setHint("Enter Password");
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //  editTextPasswords.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, 0, 0);
        //editTextPasswords.setHintTextColor(Color.GRAY);

        editTextPassword.setTextColor(Color.BLACK);
        //  editTextPasswords.setText(val);
        final Drawable z = getResources().getDrawable(R.drawable.ic_visibility);//your x image, this one from standard android images looks pretty good actually
        z.setBounds(0, 0, z.getIntrinsicWidth(), z.getIntrinsicHeight());
        editTextPassword.setCompoundDrawables(null, null, z, null);
        editTextPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
        layout.addView(editTextPassword);

        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (editTextPassword.getCompoundDrawables()[2] == null) {

                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                    editTextPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
                    editTextPassword.setSelection(editTextPassword.getText().length());
                    return false;

                }

                if (event.getX() > editTextPassword.getWidth() - editTextPassword.getPaddingRight() - z.getIntrinsicWidth()) {
                    editTextPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                    editTextPassword.setTransformationMethod(null);
                    editTextPassword.setSelection(editTextPassword.getText().length());
                    return false;
                }

                return false;
            }
        });






        TextView TextType = new TextView(MainActivity.this);
        TextType.setText("Type");
        TextType.setTextColor(Color.GRAY);
        layout.addView(TextType);

        final EditText editTextType = new EditText(MainActivity.this);
        editTextType.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editTextType.setTextColor(Color.BLACK);
        editTextType.setSingleLine(true);
        editTextType.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(editTextType);

        TextView TextFirstname = new TextView(MainActivity.this);
        TextFirstname.setText("Firstname");
        TextFirstname.setTextColor(Color.GRAY);
        layout.addView(TextFirstname);

        final EditText editTextFirstname = new EditText(MainActivity.this);
        editTextFirstname.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editTextFirstname.setTextColor(Color.BLACK);
        editTextFirstname.setRawInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS|InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextFirstname.setSingleLine(true);
        layout.addView(editTextFirstname);

        TextView TextLastname = new TextView(MainActivity.this);
        TextLastname.setText("Lastname");
        TextLastname.setTextColor(Color.GRAY);
        layout.addView(TextLastname);

        final EditText editTextLastname = new EditText(MainActivity.this);
        editTextLastname.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editTextLastname.setTextColor(Color.BLACK);
        editTextLastname.setRawInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS|InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextLastname.setSingleLine(true);
        layout.addView(editTextLastname);

        TextView TextFullname = new TextView(MainActivity.this);
        TextFullname.setText("Fullname");
        TextFullname.setTextColor(Color.GRAY);
        layout.addView(TextFullname);

        final EditText editTextFullname = new EditText(MainActivity.this);
        editTextFullname.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editTextFullname.setTextColor(Color.BLACK);
        editTextFullname.setSingleLine(true);
        layout.addView(editTextFullname);
        editTextFullname.setRawInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS|InputType.TYPE_TEXT_FLAG_CAP_WORDS);



        TextView Textreligions = new TextView(MainActivity.this);
        Textreligions.setText("Religions");
        Textreligions.setTextColor(Color.GRAY);
        layout.addView(Textreligions);


        final SearchableSpinner searchableSpinner = new SearchableSpinner(MainActivity.this);
        searchableSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        searchableSpinner.setBackgroundResource(R.drawable.spinner_bg);
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
                        religions =selectedItem;
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
        layout.addView(searchableSpinner);

        TextView Textplace = new TextView(MainActivity.this);
        Textplace.setText("Place of birth");
        Textplace.setTextColor(Color.GRAY);
        layout.addView(Textplace);

        final EditText editTextplace = new EditText(MainActivity.this);
        editTextplace.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editTextplace.setTextColor(Color.BLACK);
        editTextplace.setRawInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS|InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editTextplace.setSingleLine(true);
        layout.addView(editTextplace);



        TextView Textdateofbirth = new TextView(MainActivity.this);
        Textdateofbirth.setText("Date of birth");
        Textdateofbirth.setTextColor(Color.GRAY);
        layout.addView(Textdateofbirth);






       /* final EditText editTextdateofbirth = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);
        editTextdateofbirth.setLayoutParams(lp);
        editTextdateofbirth.setGravity(android.view.Gravity.TOP|android.view.Gravity.LEFT);
        editTextdateofbirth.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editTextdateofbirth.setLines(1);
        editTextdateofbirth.setMaxLines(1);
        editTextdateofbirth.setTextColor(Color.BLACK);
        editTextdateofbirth.setEnabled(false);
        editTextdateofbirth.setBackgroundResource(R.drawable.underline);
        // editTextdateofbirth.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_calendar_default, 0);

        editTextdateofbirth.setCompoundDrawablesWithIntrinsicBounds( 0, 0, img, 0);
        layout.addView(editTextdateofbirth);
*/


        String value = "";//any text you are pre-filling in the EditText

        final EditText editTextdateofbirth = new EditText(MainActivity.this);

        editTextdateofbirth.setSingleLine(true);
        editTextdateofbirth.setRawInputType(InputType.TYPE_CLASS_DATETIME|InputType.TYPE_DATETIME_VARIATION_DATE);
        editTextdateofbirth.setFocusable(false);

        editTextdateofbirth.setEnabled(true);
      //  editTextdateofbirth.setBackgroundResource(R.drawable.underline);
        editTextdateofbirth.setText(value);
        final Drawable y = getResources().getDrawable(R.drawable.ic_calendar);//your x image, this one from standard android images looks pretty good actually
        y.setBounds(0, 0, y.getIntrinsicWidth(), y.getIntrinsicHeight());

        editTextdateofbirth.setCompoundDrawables(null, null, value.equals("") ? null : y, null);
        editTextdateofbirth.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_calendar, 0);
        layout.addView(editTextdateofbirth);
        editTextdateofbirth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editTextdateofbirth.setFocusable(false);
                editTextdateofbirth.setEnabled(true);
                if (editTextdateofbirth.getCompoundDrawables()[2] == null) {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() > editTextdateofbirth.getWidth() - editTextdateofbirth.getPaddingRight() - y.getIntrinsicWidth()) {
                    // editTextdateofbirth.setText("aa");
                    //editTextdateofbirth.setCompoundDrawables(null, null, null, null);

                    calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                    datePickerDialog = new DatePickerDialog(MainActivity.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            String c = simpleDateFormat.format(calendar.getTime());

                            editTextdateofbirth.setText(c);
                        }
                    }, year, month, dayOfMonth);



                    datePickerDialog.setCancelable(false);
                    datePickerDialog.show();
                    datePickerDialog.getWindow().getDecorView().setPaddingRelative(0, 0, 0, 0);
                    //  datePickerDialog.getWindow().setLayout(525,900);
                    datePickerDialog.getWindow().setLayout(518, 930);

                }



                 /*   datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,   "Cancel" , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which==DialogInterface.BUTTON_NEGATIVE){
                                        datePickerDialog.cancel();
                                    }

                                }
                            });
                    datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (whichButton==DialogInterface.BUTTON_POSITIVE){
                               // datePickerDialog.cancel();
                          //  }

                        }

                        }
                    }); //End of alert.setNegativeButton


                    final Button positiveButton = ((DatePickerDialog) datePickerDialog).getButton(DialogInterface.BUTTON_POSITIVE);
                   // positiveButton.setEnabled(false);
                    // positiveButton.setPadding(20,20,20,40);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    //positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                    params.weight = 14.0f;

                    positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null,null,null);
                    positiveButton.setPadding(40,0,40,0);

                    //params.gravity = Gravity.CENTER_HORIZONTAL; //this is layout_gravity
                    positiveButton.setGravity(Gravity.CENTER);
                    positiveButton.setLayoutParams(params);
                    positiveButton.setBackgroundColor(Color.parseColor("#F39C12"));
                    positiveButton.setTextColor(Color.WHITE);
                    positiveButton.invalidate();

                    Button negativeButton = ((DatePickerDialog) datePickerDialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                    // negativeButton.setPadding(20,20,20,40);
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    // positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
                    params1.weight = 14.0f;
                    //params1.gravity= Gravity.CENTER;

                    negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null,null,null);
                    negativeButton.setPadding(40,0,40,0);

                    negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
                    negativeButton.setLayoutParams(params1);
                    negativeButton.setBackgroundColor(Color.parseColor("#48367d"));
                    negativeButton.setTextColor(Color.WHITE);
                    negativeButton.invalidate();

                    datePickerDialog.getWindow().getDecorView().setPaddingRelative(0,0,0,0);
                  //  datePickerDialog.getWindow().setLayout(525,900);
                    datePickerDialog.getWindow().setLayout(518,900);


                }*/
                return false;
            }
        });




        /*editTextdateofbirth.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editTextdateofbirth.setTextColor(Color.BLACK);
        editTextdateofbirth.setEnabled(false);
        editTextdateofbirth.setBackgroundResource(R.drawable.underline);

        editTextdateofbirth.setX(40);
        editTextdateofbirth.setCompoundDrawablesWithIntrinsicBounds( 0, 0, img, 0);
*/


/*

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_calendar_default);
        //drawable.setBounds(0, 0, 0, 0); // that is the trick!
        editTextdateofbirth.setCompoundDrawables(null, null, drawable, null);
*/

    //    editTextdateofbirth.setPadding(left, top, right, bottom);
       // editTextdateofbirth.setCompoundDrawablePadding(5);
      //  layout.addView(editTextdateofbirth);




        /*editTextdateofbirth.setOnTouchListener(new View.OnTouchListener() {
                @Override

                        public boolean onTouch(View v, MotionEvent event) {
                            if(event.getAction() == MotionEvent.ACTION_UP) {
                                if(event.getRawX() >= editTextdateofbirth.getRight() - editTextdateofbirth.getTotalPaddingRight()) {
                                    // your action for drawable click event

                                    calendar = Calendar.getInstance();
                        year   = calendar.get(Calendar.YEAR);
                        month  = calendar.get(Calendar.MONTH);
                        dayOfMonth    = calendar.get(Calendar.DAY_OF_MONTH);
                        datePickerDialog = new DatePickerDialog( MainActivity.this,
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
                        return true;

                    }
                }
                return true;
            }
        });
*/




        TextView TextAge = new TextView(MainActivity.this);
        TextAge.setText("Age");
        TextAge.setTextColor(Color.GRAY);
        layout.addView(TextAge);

        final EditText editTextAge = new EditText(MainActivity.this);
        int maxLength =3;
        editTextAge.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editTextAge.setTextColor(Color.BLACK);
        editTextAge.setInputType(InputType.TYPE_CLASS_NUMBER);
        editTextAge.setRawInputType(Configuration.KEYBOARD_12KEY);
        editTextAge.setSingleLine(true);
        editTextAge.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        layout.addView(editTextAge);

        TextView TextGender = new TextView(MainActivity.this);
        TextGender.setText("Gender");
        TextGender.setTextColor(Color.GRAY);
        layout.addView(TextGender);


        ColorStateList colorStateList = new ColorStateList(new int[][]{
                new int[]{android.R.attr.state_enabled}},
                new int[]{
                getResources().getColor(R.color.colorPrimaryDark)}
        );




        final RadioGroup radioGroup= new RadioGroup(this);
        radioGroup.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        final RadioButton maleRadioButton = new RadioButton(this);
        maleRadioButton.setButtonTintList(colorStateList);


       // maleRadioButton.ilter(Color.parseColor("#F39C12"), PorterDuff.Mode.SRC_ATOP);

        maleRadioButton.setText("Male");
        //   maleradioButton.setId(1);
        radioGroup.addView(maleRadioButton);

        final RadioButton femaleRadioButton = new RadioButton(this);
        femaleRadioButton.setButtonTintList(colorStateList);


        femaleRadioButton.setText("Female");
        //   femaleradioButton.setId(2);
        radioGroup.addView(femaleRadioButton);
        layout.addView(radioGroup);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int radioGroup) {
                // find which radio button is selected
                if (radioGroup==1) {
                    gender="Male";
                    Toast.makeText(MainActivity.this, gender,Toast.LENGTH_SHORT).show();
                } else if (radioGroup == 2) {
                    gender="Female";
                    Toast.makeText(MainActivity.this, gender,Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView TextAddress = new TextView(MainActivity.this);
        TextAddress.setText("Address");
        TextAddress.setTextColor(Color.GRAY);
        layout.addView(TextAddress);

        final EditText editAddress = new EditText(MainActivity.this);
        editAddress.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editAddress.setTextColor(Color.BLACK);

        editAddress.setPadding(20,20,0,20);
        editAddress.setSingleLine(false);
        editAddress.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        editAddress.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editAddress.setLines(1);
        editAddress.setMaxLines(5);
        editAddress.setBackgroundResource(R.drawable.edittext_border);
        editAddress.setVerticalScrollBarEnabled(true);
        editAddress.setMovementMethod(ScrollingMovementMethod.getInstance());
        editAddress.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        layout.addView(editAddress);







        TextView TextEmail = new TextView(MainActivity.this);
        TextEmail.setText("E-mail Address");
        TextEmail.setTextColor(Color.GRAY);
        layout.addView(TextEmail);

        final EditText editTextEmail = new EditText(MainActivity.this);
        editTextEmail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editTextEmail.setTextColor(Color.BLACK);
        editTextEmail.setSingleLine(true);
        layout.addView(editTextEmail);
        editTextEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);





        TextView TextMarital_Status = new TextView(MainActivity.this);
        TextMarital_Status.setText("Marital Status");
        TextMarital_Status.setTextColor(Color.GRAY);
        layout.addView(TextMarital_Status);

        final RadioGroup radioGroup2= new RadioGroup(this);
        radioGroup2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        final RadioButton singleRadioButton = new RadioButton(this);
        singleRadioButton.setButtonTintList(colorStateList);
        singleRadioButton.setText("Single");
        //   maleradioButton.setId(1);
        radioGroup2.addView(singleRadioButton);

        final RadioButton marriedRadioButton = new RadioButton(this);
        marriedRadioButton.setButtonTintList(colorStateList);
        marriedRadioButton.setText("Married");
        //   femaleradioButton.setId(2);
        radioGroup2.addView(marriedRadioButton);
        layout.addView(radioGroup2);



        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int radioGroup2) {
                // find which radio button is selected
                if (radioGroup2==1) {
                    marital_status="Single";
                   // Toast.makeText(MainActivity.this, gender,Toast.LENGTH_SHORT).show();
                } else if (radioGroup2 == 2) {
                    marital_status="Married";
                    //Toast.makeText(MainActivity.this, gender,Toast.LENGTH_SHORT).show();
                }
            }
        });



/*

        final CheckBox single = new CheckBox(MainActivity.this);
        single.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        single.setText("Single");
        layout.addView(single);

        final CheckBox married = new CheckBox(MainActivity.this);
        married.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        married.setText("Married");
        layout.addView(married);

*/

  /*      single.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(single.isChecked()) {
                    marital_status="Single";
                    married.setChecked(false);
                    Toast.makeText(MainActivity.this, single.getText(), Toast.LENGTH_SHORT).show();
                }else{
                    married.setChecked(true);
                }
            }
        });

        married.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(married.isChecked()){
                    marital_status="Married";
                    single.setChecked(false);
                    Toast.makeText(MainActivity.this, married.getText(), Toast.LENGTH_SHORT).show();
                }else{
                    single.setChecked(true);
                }
            }
        });
*/








        /*Spannable wordspan = new SpannableString("Terms and Conditions");
        wordspan.setSpan(new ForegroundColorSpan(Color.BLUE), 0, wordspan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        final TextView TextTerms_conditions = new TextView(MainActivity.this);
        TextTerms_conditions.setPadding(0,10,0,0);
        TextTerms_conditions.setText(wordspan);
        layout.addView(TextTerms_conditions);*/


       // FrameLayout frameLayout = new FrameLayout(this);

        LinearLayout linearlayout = new LinearLayout(this);
        RelativeLayout.LayoutParams paramsx = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                100);
        linearlayout.setBackgroundColor(Color.parseColor("#FFE4E4E4"));
        paramsx.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        linearlayout.setLayoutParams(paramsx);
        linearlayout.setOrientation(LinearLayout.HORIZONTAL);


        final CheckBox terms = new CheckBox(MainActivity.this);
        terms.setButtonTintList(colorStateList);
        LinearLayout.LayoutParams chkParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chkParams.weight =3.5f;
        terms.setLayoutParams(chkParams);
        terms.setText("Yes,");
        terms.setTextColor(Color.BLACK);
        terms.setPadding(1,0,0,0);
        linearlayout.addView(terms);

        final String str ="I have read and agree with the above terms & conditions";
        TextView textTerm = new TextView (MainActivity.this);
        LinearLayout.LayoutParams positiveParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        positiveParams.weight = 1.0f;
        textTerm.setLayoutParams(positiveParams);
        textTerm.setTextColor(Color.BLACK);
        textTerm.setGravity(Gravity.LEFT);
        textTerm.setMovementMethod(LinkMovementMethod.getInstance());
        textTerm.setText(setClickablePart(str), TextView.BufferType.SPANNABLE);
        textTerm.invalidate();
        linearlayout.addView(textTerm);

        layout.addView(linearlayout);


        scrollView.addView(layout);
        builder.setView(scrollView);
        builder.setCustomTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton("Submit",
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

        dialog.getWindow().getDecorView().setPaddingRelative(8,32,8,24);
      //  dialog.getWindow().getDecorView().setPaddingRelative(0,0,0,0);
        //  datePickerDialog.getWindow().setLayout(525,900);
        dialog.getWindow().setLayout(685,1050);



        final Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setEnabled(false);
        // positiveButton.setPadding(20,20,20,40);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
        params.weight = 14.0f;

        positiveButton.setCompoundDrawablesWithIntrinsicBounds(iconX, null,null,null);
        positiveButton.setPadding(40,0,40,0);

        //params.gravity = Gravity.CENTER_HORIZONTAL; //this is layout_gravity
        positiveButton.setGravity(Gravity.CENTER);
        positiveButton.setLayoutParams(params);
        positiveButton.setBackgroundColor(Color.parseColor("#F39C12"));
        positiveButton.setTextColor(Color.WHITE);
        positiveButton.invalidate();

        terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(terms.isChecked()) {
                    positiveButton.setEnabled(true);
                }else{
                    positiveButton.setEnabled(false);
                }
            }
        });


        Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        // negativeButton.setPadding(20,20,20,40);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // positiveButton.setGravity(Gravity.CENTER_HORIZONTAL);
        params1.weight = 14.0f;
        //params1.gravity= Gravity.CENTER;

        negativeButton.setCompoundDrawablesWithIntrinsicBounds(iconY, null,null,null);
        negativeButton.setPadding(40,0,40,0);

        negativeButton.setGravity(Gravity.CENTER); //this is layout_gravity
        negativeButton.setLayoutParams(params1);
        negativeButton.setBackgroundColor(Color.parseColor("#48367d"));
        negativeButton.setTextColor(Color.WHITE);
        negativeButton.invalidate();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;
                username    = editTextUsername.getText().toString();
                password    = editTextPassword.getText().toString();
                type        = editTextType.getText().toString();
                firstname   = editTextFirstname.getText().toString();
                lastname    = editTextLastname.getText().toString();
                fullname    = editTextFullname.getText().toString();
                place       = editTextplace.getText().toString();
                dateofbirth = editTextdateofbirth.getText().toString();
                age         = editTextAge.getText().toString();
                email       = editTextEmail.getText().toString();
                religions      = searchableSpinner.getSelectedItem().toString();


/*
                if(single.isChecked()) {
                    marital_status = single.getText().toString();
                 //   Toast.makeText(MainActivity.this, "Please checked them first", Toast.LENGTH_SHORT).show();
                }else{
                    marital_status= married.getText().toString();
                }
*/

                if(singleRadioButton.isChecked()){
                    marital_status = singleRadioButton.getText().toString();
                }else{
                    marital_status= marriedRadioButton.getText().toString();
                }

                if(maleRadioButton.isChecked()){
                    gender = maleRadioButton.getText().toString();
                }else{
                    gender= femaleRadioButton.getText().toString();
                }


                if (username == null || username.trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Username is required", Toast.LENGTH_SHORT).show();
                    editTextUsername.requestFocus();
                    return;
                }

                if (password == null || password.trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                    editTextPassword.requestFocus();
                    return;
                }

                if (type == null || type.trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Type is required", Toast.LENGTH_SHORT).show();
                    editTextType.requestFocus();
                    return;
                }

                if (firstname == null || firstname.trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Firstname is required", Toast.LENGTH_SHORT).show();
                    editTextFirstname.requestFocus();
                    return;
                }

                if (lastname == null || lastname.trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Lastname is required", Toast.LENGTH_SHORT).show();
                    editTextLastname.requestFocus();
                    return;
                }

                if (fullname == null || fullname.trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Fullname is required", Toast.LENGTH_SHORT).show();
                    editTextFullname.requestFocus();
                    return;
                }

                if (place == null || place.trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Place is required", Toast.LENGTH_SHORT).show();
                    editTextplace.requestFocus();
                    return;
                }

                if (dateofbirth == null || dateofbirth.trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Dateofbirth is required", Toast.LENGTH_SHORT).show();
                    editTextdateofbirth.requestFocus();
                    return;
                }
                if(!checkDate(dateofbirth)){
                    Toast.makeText(MainActivity.this, "InValid Date Format", Toast.LENGTH_SHORT).show();
                    editTextdateofbirth.requestFocus();
                    return;
                }


                if ( age == null  ||  age.trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Age is required", Toast.LENGTH_SHORT).show();
                    editTextAge.requestFocus();
                    return;
                }


                if (email == null || email.trim().length() == 0 ){
                    Toast.makeText(MainActivity.this, "E-mail is required", Toast.LENGTH_SHORT).show();
                    editTextEmail.requestFocus();
                    return;
                }


                      if(!checkEmail(email)){
                    Toast.makeText(MainActivity.this, "InValid mail Address", Toast.LENGTH_SHORT).show();
                    editTextEmail.requestFocus();
                    return;
                }
                if (radioGroup2.getCheckedRadioButtonId() == -1){
                    Toast.makeText(MainActivity.this, "Marital status  is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (religions == null || religions.trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Religions  is required", Toast.LENGTH_SHORT).show();
                    searchableSpinner.performClick();
                    return;
                }
                

                if (radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(MainActivity.this, "Gender  is required", Toast.LENGTH_SHORT).show();
                    return;
                }



                else {
                    mProgress2.show();
                    doRegister();
                }

                if (wantToCloseDialog) {
                    dialog.dismiss();
                }
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
        user.setAge(Integer.parseInt(age));
        user.setGender(gender);
        user.setReligions(religions);
        user.setMarital_status(marital_status);
        user.setEmail(email);

        UserService userService = APIClient.getClient().create(UserService.class);
        Call<ResponseModel> call = userService.Register(user);
        call.enqueue(new Callback<ResponseModel>() {

            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    mProgress2.dismiss();
                    ResponseModel responModel = response.body();
                    if (responModel.getStatus().equals("true")) {
                        Toast.makeText(MainActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();

                      //  activationiskey();
                      // Intent intent = new Intent(MainActivity.this, MainActivity.class);
                       //startActivity(intent);

                    } else {
                        Toast.makeText(MainActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                    mProgress2.dismiss();
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

                mProgress2.dismiss();
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





    /* Start void doLogin */
    private void doLogin(final String email,  final String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        UserService userService = APIClient.getClient().create(UserService.class);
        // Call<ResponseModel> call = userService.Login(username, password);
        Call<ResponseModel> call = userService.Login(user);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                if (response.isSuccessful()) {
                    mProgress.dismiss();
                    ResponseModel responModel = response.body();
                    if (responModel.getStatus().equals("true")) {

                        //login start main welcome activity
                        Toast.makeText(MainActivity.this, responModel.getMessage(), Toast.LENGTH_SHORT).show();

                    //    String nameStr = name.getText().toString();


                        TextView txtsession_id = new TextView(MainActivity.this);
                        TextView txtfile = new TextView(MainActivity.this);


                        user_id     = String.valueOf(responModel.getUser().getUser_id());
                        username    = responModel.getUser().getUsername();
                        type        = responModel.getUser().getType();
                        firstname   = responModel.getUser().getFirstname();
                        lastname    = responModel.getUser().getLastname();
                        fullname    = responModel.getUser().getFullname();
                        emails      = responModel.getUser().getEmail();   //editTextEmail.getText().toString();
                        session_id  = responModel.getUser().getSession_id();//.toString();
                        file        = responModel.getUser().getFile();
                        hp          = responModel.getUser().getHp();
                        address     = responModel.getUser().getAddress();

                        /*SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("user_id", user_id);
                        editor.putString("email", email);

                        editor.commit();
*/


                        session.createLoginSession(user_id,username ,type, firstname, lastname,
                                fullname, emails, session_id, file, hp,address);

                        Intent intent = new Intent(getApplicationContext(), Welcome.class);
                      /*  User user = new  User();
                        intent.putExtra("username", username);
                       intent.putExtra("user_id", user_id);
*/
                        startActivity(intent);
                        response.headers().get("Set-Cookie");

                        /*Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        String user_id = responModel.getUser_id().toString();
                        intent.putExtra("email", editTextEmail.getText().toString());
                        intent.putExtra("user_id", user_id);
                        startActivity(intent);*/




                    } else {
                        Toast.makeText(MainActivity.this, "The Email or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(MainActivity.this, "Error! Please try again!", Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
               // Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();





               /* final  Spannable word3 = new SpannableString("Server : Disconnected");
                word3.setSpan(
                        new ForegroundColorSpan(Color.BLUE),
                        0,
                        word3.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                final  Spannable word2 = new SpannableString("Server :");
                word2.setSpan(
                        new ForegroundColorSpan(Color.WHITE),
                        0,
                        word2.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
*/

                  slideUp(myView);
                  new Handler().postDelayed(new Runnable() {
                      public void run() {
                          slideDown(myView);
                      }
                  },  3000);

                //  isUp=!isUp;

              //  Toast.makeText(MainActivity.this, serverDisconnect, Toast.LENGTH_SHORT).show();
             //   Toast.makeText(MainActivity.this, "Server Disconnected", Toast.LENGTH_LONG).show();
                mProgress.dismiss();

            }
        });


    }



   /* public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case 11: //R.id.ivPasswordToggle;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Toast toastShow = Toast.makeText(this, "Show", Toast.LENGTH_SHORT);
                        toastShow.show();

                        edPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        edPass.setSelection(edPass.getText().length());
                        break;

                    case MotionEvent.ACTION_UP:
                        edPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        edPass.setSelection(edPass.getText().length());

                        Toast toastHide = Toast.makeText(this, "Hide", Toast.LENGTH_SHORT);
                        toastHide.show();

                        break;

                }
                break;

        }
        return true;
    }

*/
       /* private Boolean validateLogin(String email, String password){
        if(email == null || email.trim().length() ==0){
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            editTextEmails.requestFocus();
            return false;
        }
        if(password == null || password.trim().length() ==0){
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            editTextPasswords.requestFocus();
            return false;
        }
        return true;
    }*/

}
