package com.itvrach.www.itvrach;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
private TextView tvcopyright,tvsolution;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);


        tvcopyright = (TextView) findViewById(R.id.tvcopyright);
        tvcopyright.setText("@ 2018 copyright ITVrach indonesia ");

        tvsolution = (TextView) findViewById(R.id.tvsolution);
        tvsolution.setText("The best choice for your IT business");



        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashActivity.this,
                        MainActivity.class));
                finish();
            }
        }, secondsDelayed * 5000);


    }

}
