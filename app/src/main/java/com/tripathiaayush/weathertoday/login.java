package com.tripathiaayush.weathertoday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class login extends AppCompatActivity {
    AppCompatButton loginbutton;
    TextView registerlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        loginbutton= (AppCompatButton) findViewById(R.id.loginbutton);
        registerlink= (TextView) findViewById(R.id.registerlink);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this,firebaselogin.class);
                startActivity(intent);
            }
        });

        registerlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,firebasereg.class);
                startActivity(intent);
            }
        });
    }
}