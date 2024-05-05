package com.example.weatherapp.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.weatherapp.Handlers.XMLPullParser;
import com.example.weatherapp.Models.Day;
import com.example.weatherapp.Models.Observation;
import com.example.weatherapp.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;


// Ameerat Ademuyiwa - S2038600


public class WeatherActivity extends AppCompatActivity {

    private int cityID;
    private String cityName;
    private Observation observation;
    private List<Day> forecastData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation); // Set the observation layout initially

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Retrieve cityID and cityName from intent extras
        cityID = getIntent().getIntExtra("id", 0); // Default to 0 if not found
        String cityName = getIntent().getStringExtra("name"); // Retrieve cityName

//        // Display the city name
        TextView cityNameTextView = findViewById(R.id.cityNameTextView);
        cityNameTextView.setText(cityName);

        // Find the button by its ID
        Button mapButton = findViewById(R.id.mapButton);

        // Set up the OnClickListener
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the activity_maps layout
                Intent intent = new Intent(WeatherActivity.this, MapsActivity.class);
                intent.putExtra("id", cityID); // Pass cityID
                intent.putExtra("name", cityName); // Pass cityName
                intent.putExtra("observation", (Serializable) observation); // Pass observation data
                startActivity(intent);

            }
        });

        fetchObservationData();
    }


    private void fetchObservationData() {
        new ObservationData().execute();
    }

    private void updateObservationUI(Observation observation, List<Day> forecastData) {
        // Update UI elements with observation data
        // Find the views in the observation layout
        TextView temperatureTextView = findViewById(R.id.temperature);
        TextView windDirectionTextView = findViewById(R.id.windDirection);
        TextView windSpeedTextView = findViewById(R.id.windSpeed);
        TextView pressureTextView = findViewById(R.id.pressure);
        TextView humidityTextView = findViewById(R.id.humidity);
        TextView visibilityTextView = findViewById(R.id.visibility);
        TextView currentTempTextView = findViewById(R.id.currentTemp);
        TextView dayTempTextView = findViewById(R.id.day);
        TextView dateTempTextView = findViewById(R.id.date);
        TextView weatherConditionTextView = findViewById(R.id.weatherConditionTextView);

        // Update the views with the observation data
        temperatureTextView.setText(observation.getTemperature());
        windDirectionTextView.setText(observation.getWindDirection());
        windSpeedTextView.setText(observation.getWindSpeed());
        pressureTextView.setText(observation.getPressure());
        humidityTextView.setText(observation.getHumidity());
        visibilityTextView.setText(observation.getVisibility());
        currentTempTextView.setText(observation.getTemperature());
        dayTempTextView.setText(observation.getDay());
        dateTempTextView.setText(observation.getDate());
        weatherConditionTextView.setText(observation.getWeatherCondition());

        ImageView currentImageView = findViewById(R.id.weatherIcon);
        currentImageView.setImageResource(observation.getImageResource());

        Button forecastButton = findViewById(R.id.forecastButton);
        forecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to forecast activity
                Intent intent = new Intent(WeatherActivity.this, ForecastActivity.class);
                intent.putExtra("id", cityID); // Pass cityID to forecast activity
                intent.putExtra("name", cityName); // Pass cityName to forecast activity
                startActivity(intent);
            }
        });

    }

    private class ObservationData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                // Fetch observation data
                String observationLink = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/" + cityID;
                InputStream observationInputStream = new URL(observationLink).openStream();
                observation = XMLPullParser.parseObservation(observationInputStream);

                // Fetch forecast data
                String forecastLink = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/" + cityID;
                InputStream forecastInputStream = new URL(forecastLink).openStream();
                forecastData = XMLPullParser.parseXML(forecastInputStream);

                return true;
            } catch (IOException | XmlPullParserException e) {
                Log.e("ObservationData", "Error fetching or parsing observation or forecast data", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                updateObservationUI(observation, forecastData);
            } else {
                Toast.makeText(WeatherActivity.this, "Failed to fetch observation and forecast data", Toast.LENGTH_SHORT).show();
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

