package edu.psu.sweng888.activesync;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    // declare nav-bar and fragments
    private BottomNavigationView navBar;
    private Homepage homepageFragment;
    private Calendar calendarFragment;
    private LogWorkout logWorkoutFragment;
    private RecoveryStatus recoveryStatusFragment;
    private TrackProgress trackProgressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize nav-bar element references
        navBar = findViewById(R.id.nav_bar);
        homepageFragment = new Homepage();
        calendarFragment = new Calendar();
        logWorkoutFragment = new LogWorkout();
        recoveryStatusFragment = new RecoveryStatus();
        trackProgressFragment = new TrackProgress();

        // this will display the Homepage on initial launch (instead of the nav-bar)
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_bar_container, homepageFragment).commit();

        // onItemSelectedListener() for nav-bar functionality
        navBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home_item) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_bar_container, homepageFragment).commit();
                    return true;
                } else if (id == R.id.calendar_item) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_bar_container, calendarFragment).commit();
                    return true;
                } else if (id == R.id.log_workout_item) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_bar_container, logWorkoutFragment).commit();
                    return true;
                } else if (id == R.id.recovery_item) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_bar_container, recoveryStatusFragment).commit();
                    return true;
                } else if (id == R.id.progress_item) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_bar_container, trackProgressFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }

}
