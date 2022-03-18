package com.saif.hospitalgovernanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText forgotPasswordEmailEditText;
    private Button resetButton;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotPasswordEmailEditText = findViewById(R.id.forgotPasswordEmail);
        resetButton = findViewById(R.id.resetButton);
        firebaseAuth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!forgotPasswordEmailEditText.equals("")){
                    resetPassword(forgotPasswordEmailEditText.getText().toString());
                    forgotPasswordEmailEditText.setText("");
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter your email address",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void resetPassword(String email){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "A password reset email has been sent to your email address",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this,
                                    "There is a problem", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}