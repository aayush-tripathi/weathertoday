package com.tripathiaayush.weathertoday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class UserLocation extends AppCompatActivity  {
    FusedLocationProviderClient client;
    String key = "c683aa2be4426005c01cb3486aa4e39a";
    LottieAnimationView lottieAnimationView;
    FirebaseAuth auth;
    String currentURL;
    RequestQueue requestQueue;
    double latitude,longitude;
    TextView usercity,usercountry,temp,feelslike,rainpasthour,windspeed,weatherdes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);
        usercity = (TextView) findViewById(R.id.usercity);
        usercountry = (TextView) findViewById(R.id.usercountry);
        windspeed = (TextView) findViewById(R.id.WindUser);
        temp = (TextView) findViewById(R.id.usertemp);
        weatherdes = (TextView) findViewById(R.id.weatherdescuser);
        feelslike = (TextView) findViewById(R.id.Feelslikeuser);
        rainpasthour = (TextView) findViewById(R.id.rainpasthour);
        client= LocationServices.getFusedLocationProviderClient(this);
        getLocation();

    }
    private void changeAnimation(int id) {
        lottieAnimationView.cancelAnimation();
        if (id == 210 || id == 211 || id == 221 || id == 212) {
            lottieAnimationView.setAnimation(R.raw.purethunder);
        } else if (id - 200 >= 0 && id - 200 <= 50) {
            lottieAnimationView.setAnimation(R.raw.thunderrain);
        } else if (id >= 300 && id <= 550) {
            lottieAnimationView.setAnimation((R.raw.purerain));
        } else if (id - 600 >= 0 && id < 650) {
            lottieAnimationView.setAnimation(R.raw.puresnow);
        } else if (id == 701) {
            lottieAnimationView.setAnimation(R.raw.mist);
        } else if (id == 711) {
            lottieAnimationView.setAnimation(R.raw.smoke);
        } else if (id == 721) {
            lottieAnimationView.setAnimation(R.raw.smoke);
        } else if (id == 731) {
            lottieAnimationView.setAnimation(R.raw.smoke);
        } else if (id == 741) {
            lottieAnimationView.setAnimation(R.raw.foghaze);
        } else if (id == 751) {
            lottieAnimationView.setAnimation(R.raw.sand);
        } else if (id == 761 || id == 762) {
            lottieAnimationView.setAnimation(R.raw.sand);
        } else if (id == 771) {
            lottieAnimationView.setAnimation(R.raw.windysquall);
        } else if (id == 781) {
            lottieAnimationView.setAnimation(R.raw.tornadotwister);
        } else if (id == 800) {
            lottieAnimationView.setAnimation(R.raw.clearday);
        } else if (id > 800) {
            lottieAnimationView.setAnimation(R.raw.cloudsw);
        } else {
            lottieAnimationView.setAnimation(R.raw.clearday);
        }
        lottieAnimationView.playAnimation();
    }
    private void getLocation(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        latitude=location.getLatitude();
                        longitude=location.getLongitude();
                        currentURL = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + key;
                        display(currentURL);
                    }

                }
            });
        }
        else{
            ActivityCompat.requestPermissions(UserLocation.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},100 );
        }
    }
    private void display(String url){
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray weather= response.getJSONArray("weather");
                    JSONObject objwe = weather.getJSONObject(0);
                    JSONObject tempal=response.getJSONObject("main");
                    JSONObject sys=response.getJSONObject("sys");
                    String cn=sys.getString("country");
                    Locale locale= new Locale("",cn);
                    String Cname=locale.getDisplayName();
                    Double ppt;
                    try {
                        JSONObject rain= response.getJSONObject("rain");
                        ppt=rain.getDouble("1h");
                    } catch (JSONException e) {
                        ppt=0.0;
                    }
                    JSONObject wind= response.getJSONObject("wind");
                    Double windsp=wind.getDouble("speed")*3.6;
                    String windt=String.format("%.2f",windsp);
                    windspeed.setText(windt+"km/h");
                    String prpy=String.format("%.2f",ppt);
                    rainpasthour.setText(prpy+"mm");

                    Double currtemp=tempal.getDouble("temp")-273.15;
                    Double feelslie=tempal.getDouble("feels_like")-273.15;
                    Double mintemp=tempal.getDouble("temp_min")-273.15;
                    Double maxtemp=tempal.getDouble("temp_max")-273.15;
                    String curt=String.format("%.2f",currtemp);
                    String mint=String.format("%.2f",mintemp);
                    String maxt=String.format("%.2f",maxtemp);
                    String ct=String.format("%.2f",feelslie);
                    String cityn=response.getString("name");
                    usercity.setText(cityn);
                    feelslike.setText(ct+"°C");
                    usercity.setText(cityn);
                    usercountry.setText(Cname);
                    int MainCode = objwe.getInt("id");
                    changeAnimation(MainCode);
                    String desc = objwe.getString("description");
                    weatherdes.setText(desc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       
        if(requestCode==100){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLocation();
            }
            else{
                Toast.makeText(this, "Grant the required permissions", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
    }
}