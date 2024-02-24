package edu.psu.sweng888.activesync;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Homepage extends Fragment {
    // declare UI element references
    private TextView greetingTextView;
    private Button logWorkoutButton;
    private Button recoveryStatusButton;
    private Button trackProgressButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homepage, container, false);

        // initialize UI element references
//        greetingTextView = findViewById(R.id.greeting_textview);
//        logWorkoutButton = findViewById(R.id.log_workout_button);
//        recoveryStatusButton = findViewById(R.id.recovery_status_button);
//        trackProgressButton = findViewById(R.id.track_progress_button);

//        /**
//         * onClick listener for logWorkoutButton
//         */
//        logWorkoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // do something
//            }
//        });
//
//        /**
//         * onClick listener for recoveryStatusButton
//         */
//        recoveryStatusButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        /**
//         * onClick listener for trackProgressButton
//         */
//        trackProgressButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // do something
//            }
//        });
    }
}
