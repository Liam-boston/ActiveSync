package edu.psu.sweng888.activesync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    // Declaration of necessary variables.
    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;
    private Button signupButton;
    private Button loginRedirectButton;

    private void setIsLoading(boolean isLoading) {
        if (isLoading) {
            // Disable form when an operation is in progress
            signupEmail.setEnabled(false);
            signupPassword.setEnabled(false);
            signupButton.setEnabled(false);
            loginRedirectButton.setEnabled(false);
        }
        else {
            // Enable form when we're not loading
            signupEmail.setEnabled(true);
            signupPassword.setEnabled(true);
            signupButton.setEnabled(true);
            loginRedirectButton.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        //Initialize the FirebaseAuth instance in the onCreate()
        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectButton = findViewById(R.id.login_redirect_button);

        setIsLoading(false);

        loginRedirectButton.setOnClickListener(view -> {
            setIsLoading(true);
            startActivity(
                new Intent(
                    SignUpActivity.this, LoginActivity.class
                )
            );
        });


        signupButton.setOnClickListener(view -> {
            String user = signupEmail.getText().toString().trim();
            String pass = signupPassword.getText().toString().trim();

            if (user.isEmpty()){
                signupEmail.setError("Enter an email");
            }
            if(pass.isEmpty()){
                signupPassword.setError("Enter a password");
            } else{
                setIsLoading(true);
                auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(task -> {
                    setIsLoading(false);
                    if(task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    } else{
                        Toast.makeText(SignUpActivity.this, "Signup Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}

