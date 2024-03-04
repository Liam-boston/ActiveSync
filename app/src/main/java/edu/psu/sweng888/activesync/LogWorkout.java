package edu.psu.sweng888.activesync;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import edu.psu.sweng888.activesync.adapters.WorkoutSetAdapter;
import edu.psu.sweng888.activesync.dataAccessLayer.viewModels.WorkoutEntryModel;
import edu.psu.sweng888.activesync.dialogs.DatePickerDialogFragment;
import edu.psu.sweng888.activesync.eventListeners.DateSetListener;

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
        View view = inflater.inflate(R.layout.fragment_log_workout, container, false);

        // TODO: For editing an existing entry, get the primary key (workout ID) of the entry to
        //       edit from intent data that started this activity.

        // Get references to view elements and other supporting items
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        EditText workoutDateInput = view.findViewById(R.id.log_workout_date_input);
        EditText workoutDurationInput = view.findViewById(R.id.log_workout_duration_input);

        // Set up any fields of the form with values that may already exist in the view model
        updateDateInputText(workoutDateInput);
        updateDurationInputText(workoutDurationInput);

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

        // Register a click action on the "new set" button that adds an item to the recycler view
        // adapter and scrolls the new item into view.
        Button addSetButton = view.findViewById(R.id.log_workout_new_set_button);
        addSetButton.setOnClickListener(v -> {
            viewModel.sets.add(
                workoutSetAdapter.addBlankSet(
                    viewModel.workout.workoutId
                )
            );
            recyclerView.scrollToPosition(viewModel.sets.size()); // Scroll to end
        });

        // Register a click action on the date input that opens a date picker and sets the date
        // input with the result when the user selects a date with it.
        DateSetListener dateSetListener = emittedDate -> {
            viewModel.workout.date = emittedDate;
            updateDateInputText(workoutDateInput);
        };
        workoutDateInput.setOnClickListener(v -> {
            // Open the date picker with a callback that will set the model's date and use that to
            // update the date inputs display text.
            new DatePickerDialogFragment(dateSetListener)
                .show(
                    fragmentManager,
                    "workoutDatePicker"
                );
        });

        // Register an event that stores the value from the duration input in the model when the
        // input loses focus.
        workoutDurationInput.setOnFocusChangeListener((v, hasFocus) -> {
            // Ignore events that result in us gaining focus
            if (hasFocus) return;

            // When we lose focus, we should be safe to use the input value to update the model.
            viewModel.workout.durationMinutes = Integer.parseInt(
                ((EditText) v).getText().toString()
            );
        });


        return view;
    }

    private void updateDurationInputText(EditText workoutDurationInput) {
        if (viewModel == null || viewModel.workout == null) return;
        workoutDurationInput.setText(String.valueOf(viewModel.workout.durationMinutes));
    }

    private static final DateFormat displayDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private void updateDateInputText(EditText dateInput) {
        // Guard against unusable or unready date data
        if (viewModel == null || viewModel.workout.date == null) return;

        // Set the given date input's display text to the date in yyyy-MM-dd format.
        dateInput.setText(
            displayDateFormat.format(
                viewModel.workout.date
            )
        );
    }
}