package com.tripathiaayush.weathertoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class firebasereg extends AppCompatActivity {
    EditText email,pass,confpass;
    ProgressBar progressBar;
    AppCompatButton regbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebasereg);
        email= (EditText) findViewById(R.id.email);
        pass= (EditText) findViewById(R.id.password);
        confpass= (EditText) findViewById(R.id.confirmpass);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        regbut= (AppCompatButton) findViewById(R.id.registerbuttonfire);
        regbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailid= email.getText().toString();
                String passu=pass.getText().toString();
                String repass=confpass.getText().toString();
                if(TextUtils.isEmpty(emailid) || TextUtils.isEmpty(passu) || TextUtils.isEmpty(repass)){
                    Toast.makeText(firebasereg.this, "One or More Fields is Empty", Toast.LENGTH_LONG).show();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(emailid).matches()){
                    Toast.makeText(firebasereg.this, "Invalid E-Mail ID", Toast.LENGTH_SHORT).show();
                }
                else if(passu.length()<6){
                    Toast.makeText(firebasereg.this, "Password must be greater than six characters", Toast.LENGTH_LONG).show();
                }
                else if(!passu.equals(repass)){
                    Toast.makeText(firebasereg.this, "Type in the same password as before", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    register(emailid,passu);
                }
            }
        });

    }
    private void register(String email,String pass){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(firebasereg.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(firebasereg.this, "REGISTERED", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser= auth.getCurrentUser();
                    firebaseUser.sendEmailVerification();
                    Intent intent =new Intent(firebasereg.this,login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else{
                    try{
                        throw task.getException();
                    } catch(Exception e){
                        e.printStackTrace();

                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }


}