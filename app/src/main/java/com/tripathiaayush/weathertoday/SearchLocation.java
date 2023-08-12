package com.tripathiaayush.weathertoday;

import static java.lang.Math.round;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchLocation extends AppCompatActivity {
    String key = "c683aa2be4426005c01cb3486aa4e39a";
    LottieAnimationView lottieAnimationView;
    TextView cityname,weatherdesc,curr,min,max,countryn;
    AppCompatButton searchbutton;
    double lat = 0.0;
    int Flag = 0;
    double lon = 0.0;
    FirebaseAuth auth;
    EditText searchView;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        lottieAnimationView = (LottieAnimationView) findViewById(R.id.weatheranim);
        cityname = (TextView) findViewById(R.id.cityname);
        searchView = (EditText) findViewById(R.id.searchviewlocation);
        weatherdesc= (TextView) findViewById(R.id.WeatherDesc);
        auth=FirebaseAuth.getInstance();
        curr= (TextView) findViewById(R.id.currtemp);
        countryn= (TextView) findViewById(R.id.countryname);
        min= (TextView) findViewById(R.id.mintemp);
        max= (TextView) findViewById(R.id.maxtemp);
        searchbutton = (AppCompatButton) findViewById(R.id.searchlocationbuttton);

        requestQueue = Volley.newRequestQueue(this);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(searchView.getText().toString())) {
                    searchjsonparse();


                } else {
                    Toast.makeText(SearchLocation.this, "Enter a Location First", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchjsonparse() {
        String Location = searchView.getText().toString();

        String searchURL = "http://api.openweathermap.org/geo/1.0/direct?q=" + Location + "&appid=" + key;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, searchURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String NameLoc,State,Country,Cname;
                    State="";
                    JSONObject object = response.getJSONObject(0);
                    Double nlat = object.getDouble("lat");
                    Double nlon = object.getDouble("lon");
                    NameLoc=object.getString("name");
                    cityname.setText(NameLoc);
                    try{
                        State=object.getString("state");
                    }

                    catch (Exception e){
                        e.printStackTrace();
                    }
                    Country=object.getString("country");
                    Locale locale= new Locale("",Country);
                    Cname=locale.getDisplayName();
                    countryn.setText(Cname);
                    lat = nlat;
                    lon = nlon;
                    Flag = 1;
                    displayWeather();

                } catch (JSONException e) {
                    Toast.makeText(SearchLocation.this, "Invalid Location", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchLocation.this, "Something Went Wrong.Ensure correct name was entered", Toast.LENGTH_SHORT).show();
                int statusCode = error.networkResponse != null ? error.networkResponse.statusCode : -1;
                String message = "Unknown error occurred.";
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    message = new String(error.networkResponse.data);
                }
                Log.e("Volley Error", "Status Code: " + statusCode + ", Error Message: " + message);
            }
        });
        requestQueue.add(request);
    }

    private void displayWeather() {
        String currentURL = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, currentURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject objwe = weather.getJSONObject(0);
                    JSONObject tempal=response.getJSONObject("main");
                    Double currtemp=tempal.getDouble("temp")-273.15;
                    Double feelslike=tempal.getDouble("feels_like")-273.15;
                    Double mintemp=tempal.getDouble("temp_min")-273.15;
                    Double maxtemp=tempal.getDouble("temp_max")-273.15;
                    String curt=String.format("%.2f",currtemp);
                    String mint=String.format("%.2f",mintemp);
                    String maxt=String.format("%.2f",maxtemp);
                    curr.setText(curt+"°C");
                    min.setText(" Min: "+mint+"°C");
                    max.setText(" Max: " +maxt+"°C");
                    int MainCode = objwe.getInt("id");
                    changeAnimation(MainCode);
                    String desc = objwe.getString("description");
                    weatherdesc.setText(desc.toUpperCase());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchLocation.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }

    private void changeAnimation(int id){
        lottieAnimationView.cancelAnimation();
        if(id==210 ||id==211||id==221||id==212){
            lottieAnimationView.setAnimation(R.raw.purethunder);
        }
        else if(id-200>=0 && id-200<=50){
            lottieAnimationView.setAnimation(R.raw.thunderrain);
        }
        else if (id>=300 &&id<=550){
            lottieAnimationView.setAnimation((R.raw.purerain));
        }
        else if (id-600>=0 && id<650){
            lottieAnimationView.setAnimation(R.raw.puresnow);
        }
        else if(id==701){
            lottieAnimationView.setAnimation(R.raw.mist);
        }
        else if(id==711){
            lottieAnimationView.setAnimation(R.raw.smoke);
        }
        else if(id==721){
            lottieAnimationView.setAnimation(R.raw.smoke);
        }
        else if(id==731){
            lottieAnimationView.setAnimation(R.raw.smoke);
        }
        else if(id==741){
            lottieAnimationView.setAnimation(R.raw.foghaze);
        }
        else if(id==751){
            lottieAnimationView.setAnimation(R.raw.sand);
        }else if(id==761||id==762){
            lottieAnimationView.setAnimation(R.raw.sand);
        }
        else if(id==771){
            lottieAnimationView.setAnimation(R.raw.windysquall);
        }
        else if(id==781){
            lottieAnimationView.setAnimation(R.raw.tornadotwister);
        }
        else if(id==800){
            lottieAnimationView.setAnimation(R.raw.clearday);
        }
        else if(id>800){
            lottieAnimationView.setAnimation(R.raw.cloudsw);
        }
        else{
            lottieAnimationView.setAnimation(R.raw.clearday);
        }
        lottieAnimationView.playAnimation();



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
            Intent intent= new Intent(SearchLocation.this,login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        return  super.onOptionsItemSelected(item);
    }

}