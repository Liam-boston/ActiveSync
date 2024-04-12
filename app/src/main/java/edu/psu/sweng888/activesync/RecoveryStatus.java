package edu.psu.sweng888.activesync;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.psu.sweng888.activesync.adapters.RecoveryStatusAdapter;
import edu.psu.sweng888.activesync.dataAccessLayer.cannedData.DefaultMuscleGroups;
import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;

public class RecoveryStatus extends Fragment {
    private ListView listView;
    private RecoveryStatusAdapter adapter;

    public RecoveryStatus() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery_status, container, false);

        // This is a temporary setup just to finish the UI, will need to actually
        // use the MuscleGroupDao to pull values from the db
        // TODO: Pull MuscleGroup values from the db
        List<MuscleGroup> muscleGroups = new ArrayList<>(Arrays.asList(DefaultMuscleGroups.getAllDefaultMuscleGroups()));

        // Configure the ListView
        listView = view.findViewById(R.id.recovery_status_list);
        adapter = new RecoveryStatusAdapter(getContext(), muscleGroups);
        listView.setAdapter(adapter);

        return view;
    }
}