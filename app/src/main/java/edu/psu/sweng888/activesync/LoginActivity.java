package edu.psu.sweng888.activesync;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.psu.sweng888.activesync.databinding.LoginActivityBinding;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private Button signupRedirectButton;
    private Button loginButton;

    private void setIsLoading(boolean isLoading) {
        if (isLoading) {
            // Disable form when an operation is in progress
            loginEmail.setEnabled(false);
            loginPassword.setEnabled(false);
            loginButton.setEnabled(false);
            signupRedirectButton.setEnabled(false);
        }
        else {
            // Enable form when we're not loading
            loginEmail.setEnabled(true);
            loginPassword.setEnabled(true);
            loginButton.setEnabled(true);
            signupRedirectButton.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectButton = findViewById(R.id.signup_redirect_button);

        // Set initial loading state (i.e. "not loading"), which updates UI elements accordingly
        setIsLoading(false);

        // Set up click handler for "not registered yet?" button that redirects to registration view
        signupRedirectButton.setOnClickListener(view -> {
            setIsLoading(true);
            startActivity(
                new Intent(
                    LoginActivity.this,
                    SignUpActivity.class
                )
            );
        });

        // Set up a DEBUGGING handler that responds to a click on the title of the login screen.
        // This should "log in" as the TestUser to make application testing quicker.
        // TODO: REMOVE THIS BEFORE DELIVERING THE APPLICATION
        findViewById(R.id.login_title).setOnClickListener(view -> {
            redirectAfterSuccessfulLogin(
                "TestUser",
                "test.user@example.com"
            );
        });


        loginButton.setOnClickListener(view -> {

            String email = loginEmail.getText().toString();
            String pass = loginPassword.getText().toString();

            if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (!pass.isEmpty()) {
                    setIsLoading(true);
                    auth.signInWithEmailAndPassword(email, pass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    setIsLoading(false);
                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = authResult.getUser();
                                    redirectAfterSuccessfulLogin(user.getDisplayName(), user.getEmail());
                                }
                            }).addOnFailureListener(e -> {
                                setIsLoading(false);
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    loginPassword.setError("Enter a password");
                }
            } else if (email.isEmpty()) {
                loginEmail.setError("Enter an email");
            } else {
                loginEmail.setError("Enter a password");
            }
        });
    }

    private void redirectAfterSuccessfulLogin(String displayName, String emailAddress) {
        // We will use these details from the logged-in user to create or find a user in our
        // local database.
        Intent mainViewIntent = new Intent(LoginActivity.this, MainActivity.class );
        mainViewIntent.putExtra(
            Constants.EXTRAS_KEY_USER_DISPLAY_NAME,
            displayName == null ? Constants.USER_INFO_PLACEHOLDER : displayName
        );
        mainViewIntent.putExtra(
            Constants.EXTRAS_KEY_USER_EMAIL_ADDRESS,
            emailAddress == null ? Constants.USER_INFO_PLACEHOLDER : emailAddress
        );
        startActivity(mainViewIntent);
        finish();
    }
}

