package com.example.syndisync;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Weather extends AppCompatActivity {

    // Declare your TextViews as instance variables
    private TextView weatherTempCurrent1;
    private TextView weatherFeelsLikeCurrent1;
    private TextView weatherMain1;
    private TextView weatherWindSpeed1;
    private TextView weatherTimeDt_txt1;
    private TextView weatherTempCurrent2;
    private TextView weatherFeelsLikeCurrent2;
    private TextView weatherMain2;
    private TextView weatherWindSpeed2;
    private TextView weatherTimeDt_txt2;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private TextView temperatureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherTempCurrent1 = findViewById(R.id.weatherTempCurrent1);
        weatherFeelsLikeCurrent1 = findViewById(R.id.weatherFeelsLikeCurrent1);
        weatherMain1 = findViewById(R.id.weatherMain1);
        weatherWindSpeed1 = findViewById(R.id.weatherWindSpeed1);
        weatherTimeDt_txt1 = findViewById(R.id.weatherTimeDt_txt1);
        weatherTempCurrent2 = findViewById(R.id.weatherTempCurrent2);
        weatherFeelsLikeCurrent2 = findViewById(R.id.weatherFeelsLikeCurrent2);
        weatherMain2 = findViewById(R.id.weatherMain2);
        weatherWindSpeed2 = findViewById(R.id.weatherWindSpeed2);
        weatherTimeDt_txt2 = findViewById(R.id.weatherTimeDt_txt2);



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationPermission();
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                Log.d("Location", "Location found");
                                if (location != null) {
                                    lastKnownLocation = location;
                                    new FetchWeatherTask().execute(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                }
                            }
                        });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private class FetchWeatherTask extends AsyncTask<Double, Void, String> {

        @Override
        protected String doInBackground(Double... params) {
            double latitude = params[0];
            double longitude = params[1];
            String apiKey = "1662c94985342fc5ac57e5060e3fd424";
            String urlString = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey + "&units=metric";

            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                int responsecode = conn.getResponseCode();

                if(responsecode != 200)
                    throw new RuntimeException("HttpResponseCode: " +responsecode);
                else
                {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer content = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    conn.disconnect();
                    return content.toString();
                }
            } catch (Exception e) {
                Log.e("FetchWeatherTask", "Error: ", e);
                return null;
            }
        }


//        @Override
//        protected void onPostExecute(String result) {
//            if (result != null) {
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONArray list = jsonObject.getJSONArray("list");
//
//                    // Get the weather data for the current day
//                    JSONObject firstItem = list.getJSONObject(0);
//                    JSONObject main = firstItem.getJSONObject("main");
//                    JSONObject wind = firstItem.getJSONObject("wind");
//                    JSONArray weather = firstItem.getJSONArray("weather");
//                    String dt_txt = firstItem.getString("dt_txt");
//                    double temp = main.getDouble("temp");
//                    double feels_like = main.getDouble("feels_like");
//                    String weatherMain = weather.getJSONObject(0).getString("main");
//                    double speed = wind.getDouble("speed");
//
//                    // Update the TextViews for the current day
//                    weatherTimeDt_txt1.setText(dt_txt);
//                    weatherTempCurrent1.setText(String.valueOf(temp));
//                    weatherFeelsLikeCurrent1.setText(String.valueOf(feels_like));
//                    weatherMain1.setText(weatherMain);
//                    weatherWindSpeed1.setText(String.valueOf(speed));
//
//                    // Get the weather data for the next day
//                    JSONObject secondItem = list.getJSONObject(1); // Change this index based on your data
//                    main = secondItem.getJSONObject("main");
//                    wind = secondItem.getJSONObject("wind");
//                    weather = secondItem.getJSONArray("weather");
//                    dt_txt = secondItem.getString("dt_txt");
//                    temp = main.getDouble("temp");
//                    feels_like = main.getDouble("feels_like");
//                    weatherMain = weather.getJSONObject(0).getString("main");
//                    speed = wind.getDouble("speed");
//
//                    // Update the TextViews for the next day
//                    weatherTimeDt_txt2.setText(dt_txt);
//                    weatherTempCurrent2.setText(String.valueOf(temp));
//                    weatherFeelsLikeCurrent2.setText(String.valueOf(feels_like));
//                    weatherMain2.setText(weatherMain);
//                    weatherWindSpeed2.setText(String.valueOf(speed));
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Toast.makeText(Weather.this, "Error fetching weather data", Toast.LENGTH_SHORT).show();
//            }
//        }
@Override
protected void onPostExecute(String result) {
    if (result != null) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray list = jsonObject.getJSONArray("list");

            // Get the weather data for the next five forecasts
            for (int i = 0; i < 9; i++) { // Change this to 7 to include the current and next day plus five more forecasts
                JSONObject forecastItem = list.getJSONObject(i);
                JSONObject main = forecastItem.getJSONObject("main");
                JSONObject wind = forecastItem.getJSONObject("wind");
                JSONArray weather = forecastItem.getJSONArray("weather");
                String dt_txt = forecastItem.getString("dt_txt");
                double temp = main.getDouble("temp");
                double feels_like = main.getDouble("feels_like");
                String weatherMain = weather.getJSONObject(0).getString("main");
                double speed = wind.getDouble("speed");

                // Update the TextViews for the forecast
                // Replace these IDs with the IDs of your actual TextViews
                TextView weatherTimeDt_txt = findViewById(getResources().getIdentifier("weatherTimeDt_txt" + (i + 1), "id", getPackageName()));
                TextView weatherTempCurrent = findViewById(getResources().getIdentifier("weatherTempCurrent" + (i + 1), "id", getPackageName()));
                TextView weatherFeelsLikeCurrent = findViewById(getResources().getIdentifier("weatherFeelsLikeCurrent" + (i + 1), "id", getPackageName()));
                TextView weatherMainTextView = findViewById(getResources().getIdentifier("weatherMain" + (i + 1), "id", getPackageName()));
                TextView weatherWindSpeed = findViewById(getResources().getIdentifier("weatherWindSpeed" + (i + 1), "id", getPackageName()));

                weatherTimeDt_txt.setText(dt_txt);
                weatherTempCurrent.setText(String.valueOf(temp));
                weatherFeelsLikeCurrent.setText(String.valueOf(feels_like));
                weatherMainTextView.setText(weatherMain);
                weatherWindSpeed.setText(String.valueOf(speed));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else {
        Toast.makeText(Weather.this, "Error fetching weather data", Toast.LENGTH_SHORT).show();
    }
}
    }
}