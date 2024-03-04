package edu.psu.sweng888.activesync;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import edu.psu.sweng888.activesync.adapters.WorkoutSetAdapter;
import edu.psu.sweng888.activesync.dataAccessLayer.viewModels.WorkoutEntryModel;

public class LogWorkout extends Fragment {
    // declare UI element references
    EditText exerciseType, date, duration;
    ListView completedSets;
    Button submitWorkout, discardWorkout;
    private WorkoutEntryModel viewModel = new WorkoutEntryModel();

    public LogWorkout() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_workout_redo, container, false);

        // TODO: For editing an existing entry, get the primary key (workout ID) of the entry to
        //       edit from intent data that started this activity.

        // Set up RecyclerView with references to workout set items
        WorkoutSetAdapter workoutSetAdapter = new WorkoutSetAdapter(
            viewModel.workout.workoutId,
            viewModel.sets
        );
        RecyclerView recyclerView = view.findViewById(R.id.log_workout_set_list);
        recyclerView.setAdapter(workoutSetAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);

        // Initialize UI element references
        Button addSetButton = view.findViewById(R.id.log_workout_new_set_button);
        addSetButton.setOnClickListener(v -> {
            viewModel.sets.add(
                workoutSetAdapter.addBlankSet(
                    viewModel.workout.workoutId
                )
            );
            recyclerView.scrollToPosition(viewModel.sets.size()); // Scroll to end
        });


        return view;
    }
}