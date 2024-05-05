package com.example.weatherapp.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.Handlers.XMLPullParser;
import com.example.weatherapp.Models.Day;
import com.example.weatherapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;


// Ameerat Ademuyiwa - S2038600


public class ForecastActivity extends AppCompatActivity {

    private int cityID;

    private String cityName;
    private List<Day> forecastData;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        cityID = getIntent().getIntExtra("id", 0);

        // Retrieve the city name from intent extras
        String cityName = getIntent().getStringExtra("name");

        // Log the city name
        Log.d("ForecastActivity", "City Name: " + cityName);

        // Display the city name in a TextView
        TextView cityNameTextView = findViewById(R.id.cityNameTextView);
        cityNameTextView.setText(cityName);

        fetchForecastData();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Get the position of the selected item
                int position = item.getOrder();

                // Update UI with data for the selected day
                updateUIForDay(position);

                // Highlight the selected item in the navigation menu
                item.setChecked(true);

                return true;
            }
        });
        updateNavigationMenu();

    }

    private void fetchForecastData() {
        new ForecastData().execute();
    }

    private void updateUIForDay(int position) {
        if (forecastData != null && position >= 0 && position < forecastData.size()) {
            Day day = forecastData.get(position);

            TextView dayTempTextView = findViewById(R.id.day);
            dayTempTextView.setText(day.getDay());

            TextView weatherConditionTextView = findViewById(R.id.weatherConditionTextView);
            weatherConditionTextView.setText(day.getWeatherCondition());

            TextView currentTempTextView = findViewById(R.id.currentTemperature);
            String temperature = day.getMinimumTemperature();
            // Removing Fahrenheit value if present
            temperature = temperature.replaceAll("\\(\\d+°F\\)", "");
            currentTempTextView.setText(temperature);

            TextView maxTempTextView = findViewById(R.id.maxTempTextView);
            maxTempTextView.setText(day.getMaximumTemperature());

            TextView minTempTextView = findViewById(R.id.minTempTextView);
            minTempTextView.setText(day.getMinimumTemperature());

            TextView windDirectionTextView = findViewById(R.id.windDirectionTextView);
            windDirectionTextView.setText(day.getWindDirection());

            TextView windSpeedTextView = findViewById(R.id.windSpeedTextView);
            windSpeedTextView.setText(day.getWindSpeed());

            TextView pressureTextView = findViewById(R.id.pressureTextView);
            pressureTextView.setText(day.getPressure());

            TextView humidityTextView = findViewById(R.id.humidityTextView);
            humidityTextView.setText(day.getHumidity());

            TextView pollutionTextView = findViewById(R.id.pollutionTextView);
            pollutionTextView.setText(day.getPollution());

            TextView visibilityTextView = findViewById(R.id.visibilityTextView);
            visibilityTextView.setText(day.getVisibility());

            TextView sunriseTextView = findViewById(R.id.sunriseTextView);
            sunriseTextView.setText(day.getSunrise());

            TextView sunsetTextView = findViewById(R.id.sunsetTextView);
            sunsetTextView.setText(day.getSunset());

            ImageView currentImageView = findViewById(R.id.weatherIcon);
            currentImageView.setImageResource(day.getImageResource());


        }
    }

    private void updateNavigationMenu() {
        if (forecastData != null) {
            bottomNavigationView.getMenu().clear();
            for (int i = 0; i < forecastData.size(); i++) {
                Day day = forecastData.get(i);
                MenuItem menuItem = bottomNavigationView.getMenu().add(0, i, i, day.getDay());
                menuItem.setIcon(day.getImageResource());
                menuItem.setCheckable(true);
            }

            bottomNavigationView.setItemBackgroundResource(R.drawable.selector_menu_item);
        }
    }

    private class ForecastData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                String forecastLink = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/" + cityID;
                InputStream inputStream = new URL(forecastLink).openStream();
                forecastData = XMLPullParser.parseXML(inputStream);
                return true;
            } catch (IOException | XmlPullParserException e) {
                Log.e("ForecastData", "Error fetching or parsing forecast data", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                updateNavigationMenu();
                updateUIForDay(0);
            } else {
                Toast.makeText(ForecastActivity.this, "Failed to fetch forecast data", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}