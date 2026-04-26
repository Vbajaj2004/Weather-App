package com.example.websiteapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import org.json.JSONObject;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.example.websiteapp.BuildConfig;

public class MainActivity extends AppCompatActivity {

    EditText editCity;
    Button btnGetWeather;
    TextView txtResult;

    String API_KEY = "13bb3c4a0e7404f12fd91eeb1f475c50";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editCity = findViewById(R.id.editCity);
        btnGetWeather = findViewById(R.id.btnGetWeather);
        txtResult = findViewById(R.id.txtResult);

        btnGetWeather.setOnClickListener(v -> getWeather());
    }

    private void getWeather() {
        String city = editCity.getText().toString().trim();

        if (city.isEmpty()) {
            txtResult.setText("Enter a city name");
            return;
        }

        new Thread(() -> {
            try {
                String urlString =
                        "https://api.openweathermap.org/data/2.5/weather?q="
                                + city + "&appid=" + API_KEY + "&units=metric";

                URL url = new URL(urlString);
                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();

                InputStreamReader reader =
                        new InputStreamReader(connection.getInputStream());

                int data = reader.read();
                StringBuilder result = new StringBuilder();

                while (data != -1) {
                    result.append((char) data);
                    data = reader.read();
                }

                JSONObject jsonObject = new JSONObject(result.toString());

                if (jsonObject.has("main")) {

                    String temp = jsonObject.getJSONObject("main")
                            .getString("temp");

                    runOnUiThread(() ->
                            txtResult.setText("Temperature: " + temp + "°C"));

                } else {

                    String message = jsonObject.getString("message");

                    runOnUiThread(() ->
                            txtResult.setText("Error: " + message));
                }

            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(() ->
                        txtResult.setText("Error: " + e.getMessage()));
            }
        }).start();
    }
    }
