package edu.psu.sweng888.activesync;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity  {
    // declare nav-bar and fragments
    private BottomNavigationView navBar;
    private Homepage homepageFragment;
    private WorkoutCalendar workoutCalendarFragment;
    private LogWorkout logWorkoutFragment;
    private RecoveryStatus recoveryStatusFragment;
    private TrackProgress trackProgressFragment;




    /** Don't need this here most likely
     * Map already integrated on GymLocator Fragment
     * Only 5 elements allowed in bottom navigation **/
    //private GymLocator gymLocatorFragment;
    //private GoogleMap gymMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







        /** Bottom Navigation Elements and Implementation **/
        // Initialize nav-bar element references
        navBar = findViewById(R.id.nav_bar);
        homepageFragment = new Homepage();
        workoutCalendarFragment = new WorkoutCalendar();
        logWorkoutFragment = new LogWorkout();
        recoveryStatusFragment = new RecoveryStatus();
        trackProgressFragment = new TrackProgress();

        // Only 5 buttons allowed in navBar
        /*gymLocatorFragment = new GymLocator();*/

        // Initialize Homepage
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_bar_container, homepageFragment).commit();

        // Bottom Navigation Logic
        navBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home_item) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_bar_container, homepageFragment).commit();
                    return true;
                } else if (id == R.id.calendar_item) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_bar_container, workoutCalendarFragment).commit();
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
                /** Only use if adding GymLocator into bottom Navigation **/
                //else if (id == R.id.gym_item) {
                //    getSupportFragmentManager().beginTransaction().replace(R.id.nav_bar_container, gymLocatorFragment).commit();
                //    return true;
                //}

                return false;
            }
        });

    } //end of onCreate

}
