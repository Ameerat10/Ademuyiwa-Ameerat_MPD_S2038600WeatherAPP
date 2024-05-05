package com.example.weatherapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.Models.Observation;
import com.example.weatherapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

// Ameerat Ademuyiwa - S2038600
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private Observation observation; // Observation data passed from WeatherActivity

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Retrieve the passed data
        Intent intent = getIntent();
        int cityID = intent.getIntExtra("id", 0); // Default to 0 if not found
        String cityName = intent.getStringExtra("name"); // Retrieve cityName
        observation = (Observation) intent.getSerializableExtra("observation"); // Retrieve observation data

        // Set up the toolbar with a go-back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(cityName);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Add markers for each city
        ArrayList<City> cities = new ArrayList<>();
        addCities(cities);

        for (City city : cities) {
            LatLng location = new LatLng(city.getLatitude(), city.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .title(city.getName());

            // Set up a custom InfoWindowAdapter
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null; // We'll handle the content ourselves
                }

                @Override
                public View getInfoContents(Marker marker) {
                    // Inflate the custom layout for the info window
                    View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                    // Find the TextViews in the custom layout
                    TextView temperatureTextView = infoWindow.findViewById(R.id.temperature);
                    TextView windDirectionTextView = infoWindow.findViewById(R.id.windDirection);
                    TextView windSpeedTextView = infoWindow.findViewById(R.id.windSpeed);
                    TextView pressureTextView = infoWindow.findViewById(R.id.pressure);
                    TextView humidityTextView = infoWindow.findViewById(R.id.humidity);

                    // Update TextViews with observation data for this city
                    temperatureTextView.setText("Temperature: " + city.getObservation().getTemperature());
                    windDirectionTextView.setText("Wind Direction: " + city.getObservation().getWindDirection());
                    windSpeedTextView.setText("Wind Speed: " + city.getObservation().getWindSpeed());
                    pressureTextView.setText("Pressure: " + city.getObservation().getPressure());
                    humidityTextView.setText("Humidity: " + city.getObservation().getHumidity());

                    return infoWindow;
                }
            });

            // Add the marker to the map
            googleMap.addMarker(markerOptions);
        }

        // Move the camera to the first city
        LatLng firstCityLocation = new LatLng(cities.get(0).getLatitude(), cities.get(0).getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstCityLocation, 10f));
    }

    private void addCities(ArrayList<City> cities) {
        // Example cities, replace with actual data
        cities.add(new City("Tunis", 36.8025, 10.1651, observation));
        cities.add(new City("Glasgow", 55.9531, -4.2431, observation));
        cities.add(new City("London", 51.5074, -0.1278, observation));
        cities.add(new City("New York", 40.7128, -74.0060, observation));
        cities.add(new City("Muscat", 23.5893, 58.3947, observation));
        cities.add(new City("Port Louis", -20.8947, 57.4979, observation));
        cities.add(new City("Dhaka", 23.8103, 90.4125, observation));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // Navigate back to the previous activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static class City {
        private String name;
        private double latitude;
        private double longitude;
        private Observation observation;

        public City(String name, double latitude, double longitude, Observation observation) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
            this.observation = observation;
        }

        // Getters
        public String getName() {
            return name;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public Observation getObservation() {
            return observation;
        }
    }
}