package mirea.buryakov.mydownloadkson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=6054ee9437c0e3d0725a76619a1fc6c5&lang=ru&units=metric";

    private TextView textViewWeather;
    private EditText editTextCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewWeather = findViewById(R.id.textViewWeather);
        editTextCity = findViewById(R.id.editTextCity);
        Button buttonCheckTheWeather = findViewById(R.id.buttonCheckTheWeather);
        buttonCheckTheWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editTextCity.getText().toString().trim();
                if (!city.isEmpty()) {
                    String url = String.format(WEATHER_URL, city);
                    downloadWeather(url);
                }
            }
        });


    }

    private void downloadWeather(String url) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String json = DownloadJSONUtil.downloadWeatherAPI(url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            String city = jsonObject.getString("name");
                            String temp = jsonObject.getJSONObject("main").getString("temp");
                            String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                            String weather = String.format(" %s\n Температура: %s\n На улице: %s", city, temp, description);
                            textViewWeather.setText(weather);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

}