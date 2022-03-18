package com.saif.hospitalgovernanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectRegistrationActivity extends AppCompatActivity {

    private TextView alreadyRegisteredTextView;
    private Button patientRegistrationButton, doctorRegistrationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity_registration);

        alreadyRegisteredTextView = findViewById(R.id.alreadyRegisteredTextView);
        patientRegistrationButton = findViewById(R.id.patientRegistration);
        doctorRegistrationButton = findViewById(R.id.doctorRegistration);

        alreadyRegisteredTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectRegistrationActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        patientRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectRegistrationActivity.this, PatientRegistration.class);
                startActivity(intent);
                finish();
            }
        });

        doctorRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectRegistrationActivity.this, DoctorRegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}