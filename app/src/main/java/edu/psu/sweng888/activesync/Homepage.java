package edu.psu.sweng888.activesync;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Homepage extends AppCompatActivity {
    // declare UI element references
    private TextView greetingTextView;
    private Button logWorkoutButton;
    private Button recoveryStatusButton;
    private Button trackProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // initialize UI element references
        greetingTextView = findViewById(R.id.greeting_textview);
        logWorkoutButton = findViewById(R.id.log_workout_button);
        recoveryStatusButton = findViewById(R.id.recovery_status_button);
        trackProgressButton = findViewById(R.id.track_progress_button);

        /**
         * onClick listener for logWorkoutButton
         */
        logWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something
            }
        });

        /**
         * onClick listener for recoveryStatusButton
         */
        recoveryStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, RecoveryStatus.class);
                startActivity(intent);
            }
        });

        /**
         * onClick listener for trackProgressButton
         */
        trackProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something
            }
        });
    }
}
