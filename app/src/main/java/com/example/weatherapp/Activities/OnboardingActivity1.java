package com.example.weatherapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.R;

// Ameerat Ademuyiwa - S2038600


public class OnboardingActivity1 extends AppCompatActivity {

    private Button nextButton;
    private TextView onboardingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_page_1);

        nextButton = findViewById(R.id.next_button);
        onboardingText = findViewById(R.id.onboarding_text);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnboardingActivity1.this, OnboardingActivity2.class);
                startActivity(intent);
            }
        });
    }
}
