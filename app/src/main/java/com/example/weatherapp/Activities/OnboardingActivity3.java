package com.example.weatherapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.Adapters.CitiesAdapter;
import com.example.weatherapp.Models.City;
import com.example.weatherapp.R;

import java.util.ArrayList;


// Ameerat Ademuyiwa - S2038600


public class OnboardingActivity3 extends AppCompatActivity {

    private Button backButton, getStartedButton;
    private ArrayList<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_page_3);

        backButton = findViewById(R.id.back_button);
        getStartedButton = findViewById(R.id.next_button);

        cities = new ArrayList<>();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the CitiesAdapter class
                Intent intent = new Intent(OnboardingActivity3.this, CitiesAdapter.class);
                startActivity(intent);
            }
        });
    }

}
