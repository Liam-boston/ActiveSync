package edu.psu.sweng888.activesync;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class Homepage extends Fragment {
    // declare UI element references
    TextView greetingTextView;
    Button logWorkoutButton;
    Button calendarViewButton;
    Button recoveryStatusButton;
    Button trackProgressButton;
    Button gymLocatorButton;

    public Homepage() {
        // Required empty public constructor
    }

    private enum TimeOfDay {
        Morning, Afternoon, Evening, Night
    }

    private TimeOfDay getTimeOfDay(int hourOfDay) {
        if (hourOfDay >= 5 && hourOfDay < 12) return TimeOfDay.Morning;
        if (hourOfDay >= 12 && hourOfDay < 18) return TimeOfDay.Afternoon;
        if (hourOfDay >= 18 && hourOfDay < 20) return TimeOfDay.Evening;
        return TimeOfDay.Night; // 10pm - 5am
    }

    private String generateGreetingText(String displayName) {
        if (displayName == null || displayName.length() < 1) {
            displayName = "friend";
        }
        switch (getTimeOfDay(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))) {
            case Morning:
                return "Rise and shine, " + displayName + "!";
            case Afternoon:
                return "Good afternoon, " + displayName + "!";
            case Evening:
                return "Good evening, " + displayName + ".";
            case Night:
                return "Quite the night owl, " + displayName + "!";
            default:
                return "Hi there, " + displayName + ".";
        }
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

        // Get user name and email address data from login intent.
        Intent triggeringIntent = getActivity().getIntent();
        String displayName = "";
        if (triggeringIntent != null) {
            displayName = triggeringIntent.getStringExtra(Constants.EXTRAS_KEY_USER_DISPLAY_NAME);
            if (displayName == null || displayName.length() < 1 || displayName.equals(Constants.USER_INFO_PLACEHOLDER)) {
                displayName = triggeringIntent.getStringExtra(Constants.EXTRAS_KEY_USER_EMAIL_ADDRESS);
            }
        }
        greetingTextView.setText(generateGreetingText(displayName));

        // Create a user for this display name in the database if there is not one already and set
        // the user as the active user.
        ActiveSyncApplication.setActiveUser(
            ActiveSyncApplication
                .getDatabase()
                .userDao()
                .createOrReturnForFirebaseDisplayName(displayName)
        );

        //Max 5 items in navBar
        gymLocatorButton = view.findViewById(R.id.gym_locator_button);

        // FIXME: DEBUGGING - when the user clicks on the greeting text on the homepage, they are
        //        redirected to the "debugging" view fragment. This view fragment contains developer
        //        information and actions (e.g., database nuke) and should be removed before
        //        delivering the app.
        greetingTextView.setOnClickListener(__ -> startNewFragment(new DebuggingFragment()));

        /** * onClick listener for logWorkoutButton */
        logWorkoutButton.setOnClickListener(__ -> startNewFragment(new LogWorkout()));

        /** * onClick listener for calendarViewButton */
        calendarViewButton.setOnClickListener(__ -> startNewFragment(new WorkoutCalendar()));

        /** * onClick listener for recoveryStatusButton */
        recoveryStatusButton.setOnClickListener(__ -> startNewFragment(new RecoveryStatus()));

        /** * onClick listener for trackProgressButton */
        trackProgressButton.setOnClickListener(__ -> startNewFragment(new TrackProgress()));

        /** * onClick listener for gymLocatorButton */
        gymLocatorButton.setOnClickListener(__ -> startNewFragment(new GymLocator()));


        return view;
    }

    private void startNewFragment(Fragment fragment) {
        getActivity()
            .getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.nav_bar_container, fragment)
            .commit();
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
