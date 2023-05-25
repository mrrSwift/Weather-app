package com.swift.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class ViewWeather extends AppCompatActivity {
    private PrefManager prefManager;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "f5b3b155236aa8d578a01f09f3e38ff9";
    DecimalFormat df = new DecimalFormat("#.##");
    DecimalFormat dfm = new DecimalFormat("#");
    String city ,country ;
    String lat ,lon;
    ImageButton reset, refresh;
    TextView cityName,temp,realFeel,humi,windSpeed,windDeg,cloudi,press,desc;
    ImageView des;
    Boolean cod;
    ProgressDialog progress ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_weather);


        cityName = findViewById(R.id.cityName);
        temp = findViewById(R.id.temp);
        realFeel = findViewById(R.id.realFeel);
        humi = findViewById(R.id.humi);
        windSpeed = findViewById(R.id.windSpeed);
        windDeg = findViewById(R.id.windDeg);
        cloudi = findViewById(R.id.Cloudi);
        press = findViewById(R.id.press);
        desc = findViewById(R.id.desc);
        reset = findViewById(R.id.reset);
        refresh = findViewById(R.id.refresh);
        des = findViewById(R.id.des);

        progress = new ProgressDialog(this);
        progress.setTitle("Updating");
        progress.setMessage("Wait while Updating...");
        progress.setCancelable(false);
        progress.show();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prefManager.putCod(false);
                prefManager.delete();
                startActivity(new Intent(ViewWeather.this, Weather.class));
                finish();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                getWeatherDetails();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                }, 1500);

            }


        });


        prefManager = new PrefManager(this);
        city = prefManager.getCityName();

        country = prefManager.getcontry();
        cityName.setText(city);
        cod = prefManager.getCod();
        lat = prefManager.getLat();
        lon = prefManager.getLon();

        getWeatherDetails();
    }


    public void getWeatherDetails() {
        String tempUrl = "";

        if(cod){
            tempUrl = url + "?lat=" + lat +"&lon="+ lon + "&appid=" + appid;
        }else{
            if(city.equals("")){
                Toast.makeText(getApplicationContext(),"Empty city",Toast.LENGTH_SHORT).show();
            }else{
                if(country.equals("")){
                    tempUrl = url + "?q=" + city + "&appid=" + appid;
                }else{
                    tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
                }
            }
        }


            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    String output = "";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double tempp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        String deg = jsonObjectWind.getString("deg");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityNamee = jsonResponse.getString("name");
                        String icon = jsonObjectWeather.getString("icon");

                        Glide.with(ViewWeather.this).load("https://openweathermap.org/img/wn/"+icon+"@2x.png").into(des);

                        temp.setText(dfm.format(tempp));
                        desc.setText(description.toUpperCase());
                        realFeel.setText(df.format(feelsLike)+" °C" );
                        humi.setText(humidity+ "%");
                        windSpeed.setText(wind+"m/s");
                        windDeg.setText(deg+"deg");
                        cloudi.setText(clouds+"%");
                        press.setText(pressure+" hPa");
                        cityName.setText(cityNamee + " (" +countryName+")");

                        prefManager.putCityName(cityNamee);
                        progress.dismiss();


                        output += "Current weather of " + cityName + " (" + countryName + ")"
                                + "\n Temp: " + df.format(tempp) + " °C"
                                + "\n Feels Like: " + df.format(feelsLike) + " °C"
                                + "\n Humidity: " + humidity + "%"
                                + "\n Description: " + description
                                + "\n Wind Speed and deg: " + wind + "m/s "+ deg +" deg"
                                + "\n Cloudiness: " + clouds + "%"
                                + "\n Pressure: " + pressure + " hPa";


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e+"", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Can't found Or Network Error", Toast.LENGTH_LONG).show();
                    prefManager.putCod(false);
                    prefManager.delete();
                    startActivity(new Intent(ViewWeather.this, Weather.class));
                    finish();

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }


