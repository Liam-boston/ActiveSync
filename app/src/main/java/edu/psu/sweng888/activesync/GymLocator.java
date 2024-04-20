package edu.psu.sweng888.activesync;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GymLocator extends Fragment implements OnMapReadyCallback {

    private GoogleMap gymMap;
    private SearchView mapSearching;
    SupportMapFragment mapFragment;

    public GymLocator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Initialize Gym Locator View
        View view = inflater.inflate(R.layout.fragment_gym_locator, container, false);


        //Initialize Map Fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        SearchView mapSearching = (SearchView) view.findViewById(R.id.mapSearching);


        //Initialize array of place types
        String[] placeTypes = {"gym", "workout", "weight lifting"};
        //Initialize array of place names
        String[] placeNames = {"Gym", "Workout", "Weight Lifting"};

        //Async Map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {


                //Map Markers
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

        mapSearching.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                String location = mapSearching.getQuery().toString();
                List<Address> addressList = null;

                if (location != null){
                    Geocoder geocoder = new Geocoder(GymLocator.this.getActivity());

                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    gymMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    gymMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


    }
}
