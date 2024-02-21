package edu.psu.sweng888.activesync;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecoveryStatus extends AppCompatActivity {
    private TextView greetingTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_status);

        // initialize class variables
        this.greetingTextView = findViewById(R.id.recovery_status_greeting);
        this.backButton = findViewById(R.id.back_button);

        /**
         * onClick listener for recoveryStatusButton
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecoveryStatus.this, Homepage.class);
                startActivity(intent);
            }
        });
    }
}
