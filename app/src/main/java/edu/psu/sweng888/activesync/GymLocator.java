package edu.psu.sweng888.activesync;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GymLocator extends Fragment implements OnMapReadyCallback {

    public GymLocator() {
        // Required empty public constructor
    }

    private GoogleMap gymMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize Gym Locator View
        View view = inflater.inflate(R.layout.fragment_gym_locator, container, false);

        //Initialize Map Fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        //Async Map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                //Map Loaded
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        //Map Click, initialize marker
                        MarkerOptions markerOptions = new MarkerOptions();
                        //Set position of marker
                        markerOptions.position(latLng);
                        //Title of marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        //Remove markers
                        googleMap.clear();
                        //Zoom to marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom (
                                latLng, 10
                        ));

                        //Marker on map
                        googleMap.addMarker(markerOptions);

                        }


                });
            }
        }); // End of Async Map

        // Return Map View
        return view;


    } // End of onCreateView


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}
