package com.tripathiaayush.weathertoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    AppCompatButton manual,auto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manual= (AppCompatButton) findViewById(R.id.redirectmanual);
        auto= (AppCompatButton) findViewById(R.id.redirectcurr);
        auth=FirebaseAuth.getInstance();
        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,SearchLocation.class);
                startActivity(intent);
            }

        });
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,UserLocation.class);
                startActivity(intent);
            }
        });
        if (!auth.getCurrentUser().isEmailVerified()){
            auth.signOut();
            alert();
            Intent intent=new Intent(MainActivity.this,login.class);
            startActivity(intent);
            finish();
        }
        else{
            Dexter.withContext(this)
                    .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            if(isGPSon()){

                            }
                            else{
                                turnOnGPS();
                            }
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            Toast.makeText(MainActivity.this, "Enter the required Location", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MainActivity.this,SearchLocation.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();

        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id =item.getItemId();
        if(id==R.id.logoutmenu){
            auth.signOut();
            Intent intent= new Intent(MainActivity.this,login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        if(id==R.id.locationmenu){
            Intent intent=new Intent(MainActivity.this,SearchLocation.class);
            startActivity(intent);
        }
        return  super.onOptionsItemSelected(item);
    }
    private void alert(){
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
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

    private boolean isGPSon(){
        LocationManager locationManager=null;
        boolean on=false;
        if(locationManager==null){
            locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        }
        on=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return  on;
    }
    private void turnOnGPS(){
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Location Turned Off ");
        builder.setMessage("Please Turn On Location For Weather Data");
        builder.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent= new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        AlertDialog alertDialog= builder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isGPSon()){
            turnOnGPS();
        }
    }
    /*<a href="https://www.flaticon.com/free-icons/app" title="app icons">App icons created by Freepik - Flaticon</a>*/
}