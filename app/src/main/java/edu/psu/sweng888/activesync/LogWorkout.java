package edu.psu.sweng888.activesync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;
import java.util.Optional;

import edu.psu.sweng888.activesync.adapters.WorkoutSetAdapter;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeWithMuscleGroups;
import edu.psu.sweng888.activesync.dataAccessLayer.viewModels.WorkoutEntryModel;
import edu.psu.sweng888.activesync.dialogs.DatePickerDialogFragment;
import edu.psu.sweng888.activesync.eventListeners.DateSetListener;
import edu.psu.sweng888.activesync.utils.TextChangeListener;

public class LogWorkout extends Fragment {
    private RecyclerView workoutSetView;
    private WorkoutSetAdapter workoutSetAdapter;
    private WorkoutEntryModel viewModel = new WorkoutEntryModel();
    private List<ExerciseTypeWithMuscleGroups> availableExerciseTypes;
    private EditText workoutDateInput;
    private EditText workoutDurationInput;
    private Spinner exerciseTypeDropdown;

    public LogWorkout() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log_workout, container, false);

        // TODO: For editing an existing entry, get the primary key (workout ID) of the entry to
        //       edit from intent data that started this activity.

        // Get the list of available exercise types from the database. These will be used to
        // populate the dropdown with choices.
        ActiveSyncDatabase db = ActiveSyncApplication.getDatabase();
        availableExerciseTypes = db.exerciseTypeDao()
            .getExerciseTypesWithMuscleGroups();

        // Get references to view elements and other supporting items
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        workoutDateInput = view.findViewById(R.id.log_workout_date_input);
        workoutDurationInput = view.findViewById(R.id.log_workout_duration_input);
        exerciseTypeDropdown = view.findViewById(R.id.log_workout_exercise_type_input);
        Button submitButton = view.findViewById(R.id.log_workout_submit_button);

        // Set up the exercise type dropdown with the options loaded from the database.
        ArrayAdapter<ExerciseTypeWithMuscleGroups> exerciseTypeDropdownAdapter = new ArrayAdapter<>(
            view.getContext(),
            android.R.layout.simple_spinner_item,
            availableExerciseTypes
        );
        exerciseTypeDropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseTypeDropdown.setAdapter(exerciseTypeDropdownAdapter);

        // Reset the view model
        resetModelState();

        // Set up RecyclerView with references to workout set items
        workoutSetAdapter = new WorkoutSetAdapter(
            viewModel.workout.workoutId,
            viewModel.sets,
            addedSet -> {
                viewModel.sets.add(addedSet);
            },
            (updatedPosition, updatedSet) -> {
                viewModel.sets.set(updatedPosition, updatedSet);
            },
            (deletedPosition, deletedSet) -> {
                viewModel.sets.remove(deletedPosition);
            }
        );
        workoutSetView = view.findViewById(R.id.log_workout_set_list);
        workoutSetView.setAdapter(workoutSetAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        workoutSetView.setLayoutManager(layoutManager);
        workoutSetView.scrollToPosition(0);

        // Register a click action on the "new set" button that adds an item to the recycler view
        // adapter and scrolls the new item into view.
        Button addSetButton = view.findViewById(R.id.log_workout_new_set_button);
        addSetButton.setOnClickListener(v -> {
            workoutSetAdapter.addBlankSet(viewModel.workout.workoutId);
            workoutSetView.scrollToPosition(viewModel.sets.size() - 1); // Scroll to end
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

        workoutDurationInput.addTextChangedListener(new TextChangeListener<EditText>(workoutDurationInput) {
            @Override
            public void handleTextChange(EditText target, Editable editable) {
                try {
                    int latestValue = Integer.parseInt(editable.toString());
                    if (viewModel.workout.durationMinutes != latestValue) {
                        viewModel.workout.durationMinutes = latestValue;
                    }
                }
                catch (NumberFormatException nfe) {
                    // Swallow number format exceptions, which can happen when the user clears the
                    // input via backspace or similar.
                }
            }
        });

        // Register an event listener that updates the view model with changes to the selected
        // exercise type.
        class ExerciseTypeDropdownSelectionListener implements AdapterView.OnItemSelectedListener {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (viewModel == null) return;
                ExerciseTypeWithMuscleGroups selectedItem = (ExerciseTypeWithMuscleGroups) parent.getItemAtPosition(position);
                // Update the view model's state only if the selection is different than the current
                // state held by the model.
                if (viewModel.exerciseType.exerciseType.exerciseTypeId != selectedItem.exerciseType.exerciseTypeId) {
                    viewModel.exerciseType = selectedItem;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Intentionally blank
            }
        }
        exerciseTypeDropdown.setOnItemSelectedListener(new ExerciseTypeDropdownSelectionListener());

        // Register an event listener that will cause the model to be saved to the database when
        // the "submit" button is clicked.
        submitButton.setOnClickListener(this::onSubmitClick);

        // Register an event listener that will reset the model state when the user clicks "discard"
        view.findViewById(R.id.log_workout_discard_button).setOnClickListener(
            v -> resetModelState()
        );

        return view;
    }

    /**
     * Event handler that is triggered when the user clicks the "submit" button.
     * @param __ The view that triggered this event handler. This is unused.
     */
    private void onSubmitClick(View __) {
        try {
            if (persistModelState()) {
                resetModelState();
            }
        }
        catch (Exception e) {
            showLongToast("❌ Failed to persist workout.");
        }
    }

    /**
     * Updates the selected item of the exercise type dropdown to match the model state if it
     * already has a type selected.
     */
    private void updateExerciseDropdownWithModelState(Spinner exerciseTypeDropdown) {
        // Short-circuit if the required data items are null/unset
        if (availableExerciseTypes == null) return;

        // Update to default value if model is not ready
        if (viewModel == null || viewModel.exerciseType == null)
        {
            exerciseTypeDropdown.setSelection(0);
        }

        // Attempt to find the item in the list of available exercise types that matches the
        // view model's exercise type. If found, the index of the item is retrieved and used
        // to set the exercise type dropdown's currently selected item.
        Optional<ExerciseTypeWithMuscleGroups> matching = availableExerciseTypes.stream()
            .filter(
                x -> x.exerciseType.exerciseTypeId == viewModel.exerciseType.exerciseType.exerciseTypeId
            )
            .findFirst();

        if (matching.isPresent()) {
            int index = availableExerciseTypes.indexOf(matching.get());
            exerciseTypeDropdown.setSelection(index);
        }
        else {
            exerciseTypeDropdown.setSelection(0);
        }
    }

    private void updateDurationInputText(EditText workoutDurationInput) {
        workoutDurationInput.setText(
            viewModel == null || viewModel.workout == null
                ? ""
                : String.valueOf(viewModel.workout.durationMinutes)
        );
    }

    private void updateDateInputText(EditText dateInput) {
        // Set the given date input's display text to the date in yyyy-MM-dd format.
        dateInput.setText(
            viewModel == null || viewModel.workout.date == null
                ? ""
                : ActiveSyncApplication.YearMonthDayDateFormat.format(
                    viewModel.workout.date
                )
        );
    }

    private void showLongToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private boolean persistModelState() {
        // Guard against conditions that make the model invalid
        if (viewModel == null) return false;

        // Show validation messages for invalid fields
        if (viewModel.workout.date == null) {
            showLongToast("⚠ Please set a date before saving.");
            return false;
        }
        if (viewModel.sets.size() < 1) {
            showLongToast("⚠ Please create at least one set before saving.");
            return false;
        }
        if (viewModel.currentUser == null) {
            showLongToast("⚠ You must be logged in to save workouts!");
            return false;
        }

        // Attempt to persist the model to the database. On success, show a toast alerting the user
        // that their data has been saved.
        WorkoutEntryModel persisted = viewModel.persistToDatabase(ActiveSyncApplication.getDatabase());
        showLongToast("Workout saved, way to go! 🎉");
        return true;
    }


    private void resetModelState() {
        viewModel = new WorkoutEntryModel();
        viewModel.currentUser = ActiveSyncApplication.getActiveUser();
        if (viewModel.currentUser == null) {
            // Redirect to login view
            Intent redirectToLogin = new Intent(getContext(), LoginActivity.class);
            startActivity(redirectToLogin);
            Activity currentActivity = this.getActivity();
            if (currentActivity != null) currentActivity.finish();
        }
        if (workoutSetAdapter != null) {
            workoutSetAdapter.reset();
            workoutSetAdapter.addBlankSet(viewModel.workout.workoutId);
        }
        if (workoutSetView != null) {
            workoutSetView.scrollToPosition(0);
        }
        // Set up any fields of the form with values that may already exist in the view model
        updateDateInputText(workoutDateInput);
        updateDurationInputText(workoutDurationInput);
        updateExerciseDropdownWithModelState(exerciseTypeDropdown);
    }

}