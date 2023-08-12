package com.tripathiaayush.weathertoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class firebaselogin extends AppCompatActivity {
    EditText logemail,logpass;
    AppCompatButton FBlogin;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebaselogin);
        getSupportActionBar().hide();
        logemail= (EditText) findViewById(R.id.emailatlog);
        logpass= (EditText) findViewById(R.id.passwordatlog);
        FBlogin= (AppCompatButton) findViewById(R.id.loginbuttonfire);
        auth=FirebaseAuth.getInstance();
        FBlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= logemail.getText().toString();
                String pass=logpass.getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
                    Toast.makeText(firebaselogin.this, "One or More Empty Field", Toast.LENGTH_LONG).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(firebaselogin.this, "Invalid E-Mail Address", Toast.LENGTH_SHORT).show();

                }
                else{
                    loginuser(email,pass);
                }
            }
        });

    }
    private void loginuser(String Email,String pass){
        auth.signInWithEmailAndPassword(Email,pass).addOnCompleteListener(firebaselogin.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser user= auth.getCurrentUser();
                    if (user.isEmailVerified()){
                        Toast.makeText(firebaselogin.this, "Signed IN", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(firebaselogin.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        auth.signOut();
                        alert();
                    }


                }
                else{
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Toast.makeText(firebaselogin.this, "Invalid Credentials/Not Registered", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void alert(){
        AlertDialog.Builder builder= new AlertDialog.Builder(firebaselogin.this);
        builder.setTitle("E-Mail Not Verified");
        builder.setMessage("Please check your inbox and verify your email to sign in further");
        builder.setPositiveButton("Open E-Mail", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent= new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }

    @Override
    protected  void onStart() {
        super.onStart();
        if(auth.getCurrentUser()!=null) {
            if (auth.getCurrentUser().isEmailVerified()) {
                Intent intent = new Intent(firebaselogin.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                auth.signOut();
                alert();
            }
        }

    }
}