package edu.psu.sweng888.activesync;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class LogWorkout extends Fragment {
    // declare UI element references
    EditText exerciseType, date, duration;
    ListView completedSets;
    Button submitWorkout, discardWorkout;
    private Integer[] numSets;

    public LogWorkout() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_workout, container, false);

        // initialize UI element references
        exerciseType = view.findViewById(R.id.exercise_type_edit_text);
        date = view.findViewById(R.id.date_edit_text);
        duration = view.findViewById(R.id.duration_edit_text);
        completedSets = view.findViewById(R.id.completed_sets_list_view);
        submitWorkout = view.findViewById(R.id.submit_workout);
        discardWorkout = view.findViewById(R.id.discard_workout);

        // TODO: Find a way to dynamically resize the ListView
        // populate ListView with 10 empty sets
        this.numSets = new Integer[10];

        // TODO: Implement adapter
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, this.numSets);
        completedSets.setAdapter(adapter);
        completedSets.setItemsCanFocus(true);

        // TODO: Logic for retrieving values from ListView

        return view;
    }
}