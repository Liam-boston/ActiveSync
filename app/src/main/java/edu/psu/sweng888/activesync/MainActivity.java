package edu.psu.sweng888.activesync;

import static edu.psu.sweng888.activesync.R.id.mapSearch;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import edu.psu.sweng888.activesync.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity  {
    // declare nav-bar and fragments
    private BottomNavigationView navBar;
    private Homepage homepageFragment;
    private Calendar calendarFragment;
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
        calendarFragment = new Calendar();
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
