package edu.psu.sweng888.activesync;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Homepage extends Fragment {
    // declare UI element references
    TextView greetingTextView;
    Button logWorkoutButton;
    Button calendarViewButton;
    Button recoveryStatusButton;
    Button trackProgressButton;

    public Homepage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        // initialize UI element references
        greetingTextView = view.findViewById(R.id.greeting_textview);
        logWorkoutButton = view.findViewById(R.id.log_workout_button);
        calendarViewButton = view.findViewById(R.id.calendar_view_button);
        recoveryStatusButton = view.findViewById(R.id.recovery_status_button);
        trackProgressButton = view.findViewById(R.id.track_progress_button);

        /**
         * onClick listener for logWorkoutButton
         */
        logWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogWorkout logWorkoutFragment = new LogWorkout();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_bar_container, logWorkoutFragment).commit();
            }
        });

        /**
         * onClick listener for calendarViewButton
         */
        calendarViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarFragment = new Calendar();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_bar_container, calendarFragment).commit();
            }
        });

        /**
         * onClick listener for recoveryStatusButton
         */
        recoveryStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecoveryStatus recoveryStatusFragment = new RecoveryStatus();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_bar_container, recoveryStatusFragment).commit();
            }
        });

        /**
         * onClick listener for trackProgressButton
         */
        trackProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackProgress trackProgressFragment = new TrackProgress();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_bar_container, trackProgressFragment).commit();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
