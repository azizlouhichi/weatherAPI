package com.example.secondapp;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "6fe49650e98edcabbcea05edb4d5ec20";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private TextView pressureTextView;
    private TextView temperatureTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pressureTextView = findViewById(R.id.pressureValue);
        temperatureTextView = findViewById(R.id.temperatureValue);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiService apiService = retrofit.create(WeatherApiService.class);

        double latitude = 36.8065;
        double longitude = 10.1815;

        Call<WeatherResponse> call = apiService.getWeather(latitude, longitude, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {
                        float pressure = weatherResponse.main.pressure;
                        pressureTextView.setText("Pressure: " + pressure + " hPa");

                        float temperature = weatherResponse.main.temp;
                        temperatureTextView.setText("Temperature: " + temperature + "°C");

                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error fetching weather data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
