package edu.psu.sweng888.activesync;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.Serializable;

import edu.psu.sweng888.activesync.dataAccessLayer.models.Workout;
import edu.psu.sweng888.activesync.dataAccessLayer.viewModels.WorkoutEntryModel;

public class MainActivity extends AppCompatActivity  {
    // declare nav-bar and fragments
    private BottomNavigationView navBar;
    private Homepage homepageFragment;
    private WorkoutCalendar workoutCalendarFragment;
    private RecoveryStatus recoveryStatusFragment;
    private TrackProgress trackProgressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Bottom Navigation Elements and Implementation **/
        // Initialize nav-bar element references
        navBar = findViewById(R.id.nav_bar);
        homepageFragment = new Homepage();
        workoutCalendarFragment = new WorkoutCalendar();
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
                    return swapToFragment(homepageFragment);
                } else if (id == R.id.calendar_item) {
                    return swapToFragment(workoutCalendarFragment);
                } else if (id == R.id.log_workout_item) {
                    return swapToFragment(new LogWorkout());
                } else if (id == R.id.recovery_item) {
                    return swapToFragment(recoveryStatusFragment);
                } else if (id == R.id.progress_item) {
                    return swapToFragment(trackProgressFragment);
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

    /**
     * Swaps the fragment displayed by the main activity to the given fragment. Returns `true` if
     * the fragment swap was successfully queued; otherwise, returns `false`.
     * @param fragment The fragment to display.
     * @return `true` if the fragment replacement transaction was started successfully; otherwise, `false`.
     */
    private boolean swapToFragment(Fragment fragment, Bundle bundle) {
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.nav_bar_container, fragment)
            .commit() >= 0;
    }

    private boolean swapToFragment(Fragment fragment) {
        return this.swapToFragment(fragment, null);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Check for a workout to edit
        WorkoutEntryModel viewModel = (WorkoutEntryModel) intent.getSerializableExtra(Constants.EXTRAS_KEY_WORKOUT_TO_EDIT);
        if (viewModel != null) {
            // Switch to the fragment and set the model state to the workout that should be edited
            Bundle data = new Bundle();
            data.putSerializable(Constants.EXTRAS_KEY_WORKOUT_TO_EDIT, viewModel);
            swapToFragment(new LogWorkout(), data);
        }
    }

}
