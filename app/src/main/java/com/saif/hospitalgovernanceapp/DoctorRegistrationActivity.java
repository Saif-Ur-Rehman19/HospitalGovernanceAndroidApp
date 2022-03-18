package com.saif.hospitalgovernanceapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorRegistrationActivity extends AppCompatActivity {
    
    private EditText doctorName, doctorID, doctorPhoneNumber, doctorEmail, doctorPassword;
    private Spinner departmentsSpinner, specializationSpinner, availabilitySpinner;
    private Button doctorRegistrationButton, doctorBackToSignInButton;
    private CircleImageView circleImageViewDoctor;
    private boolean imageControl = false;
    private Uri imageUri;
    private String selectedDepartment, selectedTime, selectedSpecialization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        doctorName = findViewById(R.id.doctorName);
        doctorID = findViewById(R.id.doctorID);
        doctorPhoneNumber = findViewById(R.id.doctorPhoneNumber);
        departmentsSpinner = findViewById(R.id.departmentsSpinner);
        specializationSpinner = findViewById(R.id.specializationSpinner);
        availabilitySpinner = findViewById(R.id.availabilitySpinner);
        doctorRegistrationButton = findViewById(R.id.doctorRegistrationButton);
        doctorBackToSignInButton = findViewById(R.id.doctorBackToSignButton);
        circleImageViewDoctor = findViewById(R.id.circleImageViewDoctor);
        doctorEmail = findViewById(R.id.doctorEmail);
        doctorPassword = findViewById(R.id.doctorPassword);

        availabilitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTime = parent.getItemAtPosition(0).toString();
            }
        });

        departmentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDepartment = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDepartment = parent.getItemAtPosition(0).toString();
            }
        });

        specializationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSpecialization = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSpecialization = parent.getItemAtPosition(0).toString();
            }
        });

        doctorBackToSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorRegistrationActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        doctorRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dName = doctorName.getText().toString();
                String dID = doctorID.getText().toString();
                String dPhoneNumber = doctorPhoneNumber.getText().toString();
                String dEmail = doctorEmail.getText().toString();
                String dPassword = doctorPassword.getText().toString();
                if ((!dName.equals("") && !dID.equals("") && !dPhoneNumber.equals("") &&
                        !dEmail.equals("") && !dPassword.equals("")) &&
                        !selectedDepartment.startsWith("Select") &&
                                !selectedTime.startsWith("Select") &&
                        !selectedSpecialization.startsWith("Select")){
                    signUp();
                } else {
                    Toast.makeText(DoctorRegistrationActivity.this,
                            "Please enter all details correctly ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        circleImageViewDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

    }

    private void signUp() {

    }

    ActivityResultLauncher<Intent> chooseImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        imageUri = result.getData().getData();
                        if (!imageUri.equals("")) {
                            Picasso.get().load(imageUri).into(circleImageViewDoctor);
                            imageControl = true;
                        } else {
                            imageControl = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

    private void imageChooser(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        chooseImageLauncher.launch(intent);
    }


}