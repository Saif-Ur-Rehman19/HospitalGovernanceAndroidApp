package com.saif.hospitalgovernanceapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorRegistrationActivity extends AppCompatActivity {
    
    private EditText doctorName, doctorID, doctorPhoneNumber, doctorEmail, doctorPassword;
    private Spinner departmentsSpinner, specializationSpinner, availabilitySpinner;
    private Button doctorRegistrationButton, doctorBackToSignInButton;
    private CircleImageView circleImageViewDoctor;
    private boolean imageControl = false;
    private Uri imageUri;
    private String selectedDepartment, selectedTime, selectedSpecialization;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

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
                    signUp(dName, dID, dPhoneNumber, dEmail, dPassword,
                            selectedDepartment, selectedTime, selectedSpecialization);
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

    private void signUp(String dName, String dID, String dPhoneNumber, String dEmail,
                        String dPassword, String selectedDepartment, String selectedTime, String selectedSpecialization) {
        firebaseAuth.createUserWithEmailAndPassword(dEmail, dPassword).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            databaseReference.child("Doctors").child(firebaseAuth.getUid()).child("doctorName").
                                    setValue(dName);
                            databaseReference.child("Doctors").child(firebaseAuth.getUid()).child("doctorID").
                                    setValue(dID);
                            databaseReference.child("Doctors").child(firebaseAuth.getUid()).child("doctorPhoneNumber").
                                    setValue(dPhoneNumber);
                            databaseReference.child("Doctors").child(firebaseAuth.getUid()).child("doctorEmail").
                                    setValue(dEmail);
                            databaseReference.child("Doctors").child(firebaseAuth.getUid()).child("doctorPassword").
                                    setValue(dPassword);
                            databaseReference.child("Doctors").child(firebaseAuth.getUid()).child("doctorDepartment").
                                    setValue(selectedDepartment);
                            databaseReference.child("Doctors").child(firebaseAuth.getUid()).child("doctorSpecialization").
                                    setValue(selectedSpecialization);
                            databaseReference.child("Doctors").child(firebaseAuth.getUid()).child("doctorSelectedTime").
                                    setValue(selectedTime);
                            databaseReference.child("Doctors").child(firebaseAuth.getUid()).child("type").
                                    setValue("doctor");
                            if (imageControl){
                                UUID randomID = UUID.randomUUID();
                                String imageName = "images/" + randomID + ".jpg";
                                storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(
                                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                StorageReference imageReference = firebaseStorage.getReference(imageName);
                                                imageReference.getDownloadUrl().addOnSuccessListener(
                                                        new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                String filePath = uri.toString();
                                                                databaseReference.child("Doctors").
                                                                        child(firebaseAuth.getUid()).child("image").
                                                                        setValue(filePath).addOnSuccessListener(
                                                                        new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                Toast.makeText(DoctorRegistrationActivity.this,
                                                                                        "Write to database is successful",
                                                                                        Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                ).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(DoctorRegistrationActivity.this,
                                                                                "Write to database is not successful",
                                                                                Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        });
                                            }
                                        });
                            } else {
                                databaseReference.child("Doctors").child(firebaseAuth.getUid()).child("image").setValue("null");
                            }
                            Toast.makeText(DoctorRegistrationActivity.this,
                                    "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DoctorRegistrationActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(DoctorRegistrationActivity.this, "There's a problem " + error,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

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