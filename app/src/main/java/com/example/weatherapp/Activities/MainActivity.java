package com.example.weatherapp.Activities;


import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.Adapters.CitiesAdapter;
import com.example.weatherapp.Models.City;
import com.example.weatherapp.R;

import java.util.ArrayList;


// Ameerat Ademuyiwa - S2038600


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assuming you have a method to handle the splash screen logic
        // handleSplashScreen();

        // Start the OnboardingActivity
        Intent onboardingIntent = new Intent(MainActivity.this, CityListActivity.class);
        startActivity(onboardingIntent);
        finish(); // Prevents the user from going back to the MainActivity
    }
}
