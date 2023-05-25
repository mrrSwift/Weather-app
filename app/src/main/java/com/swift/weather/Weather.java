package com.swift.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class Weather extends AppCompatActivity {

    private PrefManager prefManager;
    EditText etCity, etCountry, etLat, etLon;
    CheckBox cod;
    private FusedLocationProviderClient fusedLocationClient;
    ProgressDialog progress ;
    Boolean checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        etLat = findViewById(R.id.etLat);
        etLon = findViewById(R.id.etLon);
        cod = findViewById(R.id.cbCod);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        Button btngeet = findViewById(R.id.btnGet);
        Button btnGps = findViewById(R.id.btnGps);

        prefManager = new PrefManager(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        progress = new ProgressDialog(this);
        progress.setTitle("Searching");
        progress.setMessage("Wait while Searching...");
        progress.setCancelable(false);


        checked = cod.isChecked();


        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                getLoc();


            }
        });

        btngeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCity.getText().length() > 1) {
                    String city = etCity.getText().toString().trim();
                    prefManager.putCityName(city);
                    startActivity(new Intent(Weather.this, ViewWeather.class));
                    finish();
                } else if(!checked ) {
                    Toast.makeText(getApplicationContext(), "Empty city", Toast.LENGTH_SHORT).show();
                }

                if (etCountry.getText().length() > 1) {
                    String country = etCountry.getText().toString().trim();
                    prefManager.putcontry(country);
                    startActivity(new Intent(Weather.this, ViewWeather.class));
                    finish();
                }
                if (String.valueOf(etLat.getText()).length() > 1) {
                    prefManager.putLat(etLat.getText().toString());
                    prefManager.putLon(etLon.getText().toString());
                    startActivity(new Intent(Weather.this, ViewWeather.class));
                    finish();
                }else if(checked) {
                    Toast.makeText(getApplicationContext(), "Empty Coordinate", Toast.LENGTH_SHORT).show();
                }




            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checked = cod.isChecked();

                if (checked) {
                    etLat.setVisibility(View.VISIBLE);
                    etLon.setVisibility(View.VISIBLE);
                    etCity.setVisibility(View.INVISIBLE);
                    etCountry.setVisibility(View.INVISIBLE);
                    prefManager.putCod(true);


                } else {
                    etLat.setVisibility(View.INVISIBLE);
                    etLon.setVisibility(View.INVISIBLE);
                    etCity.setVisibility(View.VISIBLE);
                    etCountry.setVisibility(View.VISIBLE);
                    prefManager.putCod(false);

                }

            }
        });


        if (prefManager.getCityName().length() > 1) {
            startActivity(new Intent(Weather.this, ViewWeather.class));
            finish();
        }
    }


    public void getLoc() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        fusedLocationClient.getCurrentLocation(100,null)
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                progress.dismiss();
                if (location != null) {
                    prefManager.putLat(String.valueOf(location.getLatitude()));
                    prefManager.putLon(String.valueOf(location.getLongitude()));
                    prefManager.putCod(true);
                    Toast.makeText(getApplicationContext(), location.getLatitude()+"  "+location.getLongitude(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Weather.this, ViewWeather.class));
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Bad signal", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}