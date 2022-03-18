package com.saif.hospitalgovernanceapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientRegistration extends AppCompatActivity {

    private Button patientRegisterButton, patientBackToLogin;
    private EditText patientName, patientID, patientPhoneNumber, patientEmail, patientPassword;
    private CircleImageView circleImageViewPatient;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private boolean imageControl = false;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        patientBackToLogin = findViewById(R.id.patientBackToSignButton);
        patientName = findViewById(R.id.patientName);
        patientID = findViewById(R.id.patientID);
        patientPhoneNumber = findViewById(R.id.patientPhoneNumber);
        patientEmail = findViewById(R.id.patientEmail);
        patientPassword = findViewById(R.id.patientPassword);
        patientRegisterButton = findViewById(R.id.patientRegistrationButton);
        circleImageViewPatient = findViewById(R.id.circleImageViewPatient);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = firebaseDatabase.getReference();

        circleImageViewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        patientBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientRegistration.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        patientRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = patientName.getText().toString();
                String id = patientID.getText().toString();
                String phoneNumber = patientPhoneNumber.getText().toString();
                String email = patientEmail.getText().toString();
                String password = patientPassword.getText().toString();
                if (!name.equals("") && !id.equals("") && !phoneNumber.equals("")
                        && !email.equals("") && !password.equals("")) {
                    signUp(name, id, phoneNumber, email, password);

                } else {
                    Toast.makeText(PatientRegistration.this, "please enter all field correctly",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    ActivityResultLauncher<Intent> chooseImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        imageUri = result.getData().getData();
                        if (!imageUri.equals("")) {
                            Picasso.get().load(imageUri).into(circleImageViewPatient);
                            imageControl = true;
                        } else {
                            imageControl = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

    public void imageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        chooseImageLauncher.launch(intent);
    }

    public void signUp(String name, String id, String phoneNumber, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            databaseReference.child("Users").child(firebaseAuth.getUid()).child("userName").setValue(name);
                            databaseReference.child("Users").child(firebaseAuth.getUid()).child("id").setValue(id);
                            databaseReference.child("Users").child(firebaseAuth.getUid()).child("phoneNumber").setValue(phoneNumber);
                            databaseReference.child("Users").child(firebaseAuth.getUid()).child("email").setValue(email);
                            databaseReference.child("Users").child(firebaseAuth.getUid()).child("password").setValue(password);
                            databaseReference.child("Users").child(firebaseAuth.getUid()).child("type").setValue("patient");
                            if (imageControl){
                                UUID randomID = UUID.randomUUID();
                                String imageName = "images/" + randomID + ".jpg";
                                storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(
                                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                StorageReference myStorageRef = firebaseStorage.getReference(imageName);
                                                myStorageRef.getDownloadUrl().addOnSuccessListener(
                                                        new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                String filePath = uri.toString();
                                                                databaseReference.child("Users").
                                                                        child(firebaseAuth.getUid()).child("image").
                                                                        setValue(filePath).addOnSuccessListener(
                                                                        new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                Toast.makeText(
                                                                                        PatientRegistration.this,
                                                                                        "Write to database is successful",
                                                                                        Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                ).addOnFailureListener(
                                                                        new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(
                                                                                        PatientRegistration.this,
                                                                                        "Write to database is not successful",
                                                                                        Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                );
                                                            }
                                                        }
                                                );
                                            }
                                        }
                                );
                            } else {
                                databaseReference.child("Users").child(firebaseAuth.getUid()).child("image").setValue("null");

                            }
                            Toast.makeText(PatientRegistration.this,
                                    "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PatientRegistration.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(PatientRegistration.this, "There's a problem " + error,
                                    Toast.LENGTH_SHORT).show();
                    }
                }
    });
}
}