package edu.psu.sweng888.activesync;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.psu.sweng888.activesync.dataAccessLayer.cannedData.DefaultUsers;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseType;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeWithMuscleGroups;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;
import edu.psu.sweng888.activesync.dataAccessLayer.models.WeightUnit;
import edu.psu.sweng888.activesync.dataAccessLayer.models.Workout;
import edu.psu.sweng888.activesync.dataAccessLayer.models.WorkoutSet;
import edu.psu.sweng888.activesync.databinding.FragmentTrackProgressBinding;
import edu.psu.sweng888.activesync.dialogs.DatePickerDialogFragment;

public class TrackProgress extends Fragment {

    private FragmentTrackProgressBinding binding;

    private FragmentManager fragmentManager;

    private boolean isLoading = false;

    private ExerciseTypeWithMuscleGroups selectedExerciseType = null;

    private Date searchStartDate = null;

    private Date searchEndDate = null;

    public TrackProgress() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
        @NonNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        binding = FragmentTrackProgressBinding.inflate(inflater, container, false);
        fragmentManager = requireActivity().getSupportFragmentManager();

        // Set chart's X-Axis formatter to display human-readable dates
        binding.progressTrackerLineChart.getXAxis().setValueFormatter(
            new UnixTimestampAsHumanReadableDateAxisFormatter()
        );
        binding.progressTrackerLineChart.getXAxis().setLabelRotationAngle(-45f);
        //*
        final float MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;
        binding.progressTrackerLineChart.getXAxis().setGranularity(MILLISECONDS_PER_DAY);
        //*/

        // Set up click event handlers
        binding.trackProgressResetButton.setOnClickListener(this::onResetButtonClick);
        binding.trackProgressSearchButton.setOnClickListener(this::onSearchButtonClick);
        binding.trackProgressStartDateInput.setOnClickListener(this::onStartDateInputClick);
        binding.trackProgressEndDateInput.setOnClickListener(this::onEndDateInputClick);

        // Set up choices for exercise type dropdown
        ArrayAdapter<ExerciseTypeWithMuscleGroups> exerciseTypeDropdownAdapter = new ArrayAdapter<>(
            binding.getRoot().getContext(),
            android.R.layout.simple_spinner_item,
            ActiveSyncApplication.getDatabase().exerciseTypeDao().getExerciseTypesWithMuscleGroups()
        );
        exerciseTypeDropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.trackProgressExerciseTypeInput.setAdapter(exerciseTypeDropdownAdapter);

        // Set up event listeners for exercise type dropdown
        class ExerciseTypeDropdownSelectionListener implements AdapterView.OnItemSelectedListener {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedExerciseType = (ExerciseTypeWithMuscleGroups) parent.getItemAtPosition(position);
                stateHasChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedExerciseType = null;
                stateHasChanged();
            }
        }
        binding.trackProgressExerciseTypeInput.setOnItemSelectedListener(
            new ExerciseTypeDropdownSelectionListener()
        );

        // Trigger initial state read
        stateHasChanged();

        return binding.getRoot();
    }

    private static void setEnabledWithOpacity(View view, boolean enabled) {
        view.setEnabled(enabled);
        view.getBackground().setAlpha(enabled ? 255 : 64);
    }

    private void stateHasChanged() {
        // Disable/enable inputs based on loading flag
        if (isLoading) {
            this.binding.trackProgressExerciseTypeInput.setEnabled(false);
            this.binding.trackProgressStartDateInput.setEnabled(false);
            this.binding.trackProgressEndDateInput.setEnabled(false);
            setEnabledWithOpacity(this.binding.trackProgressSearchButton, false);
            setEnabledWithOpacity(this.binding.trackProgressResetButton, false);
        }
        else {
            this.binding.trackProgressExerciseTypeInput.setEnabled(true);
            this.binding.trackProgressStartDateInput.setEnabled(true);
            this.binding.trackProgressEndDateInput.setEnabled(true);
            setEnabledWithOpacity(this.binding.trackProgressSearchButton, true);
            setEnabledWithOpacity(this.binding.trackProgressResetButton, true);
        }
    }

    private void onStartDateInputClick(View __) {
        new DatePickerDialogFragment(this::onStartDateChosen)
            .show(fragmentManager, "progressTrackerStartDatePicker");
    }

    private void onEndDateInputClick(View __) {
        new DatePickerDialogFragment(this::onEndDateChosen)
            .show(fragmentManager, "progressTrackerEndDatePicker");
    }

    private void onStartDateChosen(Date chosenDate) {
        this.searchStartDate = chosenDate;
        this.binding.trackProgressStartDateInput.setText(
            ActiveSyncApplication.YearMonthDayDateFormat.format(this.searchStartDate)
        );
        stateHasChanged();
    }

    private void onEndDateChosen(Date chosenDate) {
        this.searchEndDate = chosenDate;
        this.binding.trackProgressEndDateInput.setText(
            ActiveSyncApplication.YearMonthDayDateFormat.format(this.searchEndDate)
        );
        stateHasChanged();
    }

    /**
     * Button click handler for "search" button that performs a search for the requested data and
     * populates the graph with it.
     * @param __ The view that triggered this event. This parameter is unused.
     */
    private void onSearchButtonClick(View __) {
        // Search the data set for workouts owned by the current user that have an exercise type
        // matching the one selected in the form. Filter this data down to only those sessions that
        // occurred between the given start and stop times. If either time constraint is unset, use
        // the minimum/maximum value instead.
        // TODO: As a shortcut, we retrieve all data from the database then filter it in code.
        //       This is bad practice, and should be replaced with a more precise SQL query.
        //User activeUser = ActiveSyncApplication.getActiveUser();
        //User targetUser = activeUser == null ? DefaultUsers.TestUser : activeUser;
        User targetUser = DefaultUsers.TestUser; // TODO: DEBUGGING! Replace this line with the two above.
        long minTime = (searchStartDate == null ? new Date(Long.MIN_VALUE) : searchStartDate).getTime();
        long maxTime = (searchEndDate == null ? new Date(Long.MAX_VALUE) : searchEndDate).getTime();
        List<Workout> targetWorkouts = ActiveSyncApplication.getDatabase()
            .workoutDao()
            .getWorkoutsForUser(targetUser.userId)
            .stream()
            .filter(workout -> workout.exerciseTypeId == selectedExerciseType.exerciseType.exerciseTypeId)
            .filter(workout -> workout.date.getTime() >= minTime && workout.date.getTime() <= maxTime)
            .sorted(Comparator.comparing(workout -> workout.date))
            .collect(Collectors.toList());

        // If there aren't any matching results, we want to stop here. Make sure we clear data so
        // the user does not interpret past results as the results for this query.
        binding.progressTrackerLineChart.clear();
        binding.progressTrackerLineChart.invalidate();
        if (targetWorkouts.size() < 1) return;


        // For each matched workout, compute the maximum weight lifted across all sets. This is the
        // value we'd like to plot.  Assume all plots are in pounds (lbs).  If we encounter a set
        // that is measured in kilograms, convert it to pounds.
        final double LBS_PER_KG = 2.204623;
        Map<Long, Double> maxWeightByDayAsUnixTimestamp = new HashMap<>();
        for (Workout workout : targetWorkouts) {
            double maxWeightLbs = ActiveSyncApplication.getDatabase()
                .workoutSetDao()
                .getSetsForWorkout(workout.workoutId)
                .stream()
                .map(set -> set.weight.unit == WeightUnit.Pounds ? set.weight.amount : set.weight.amount * LBS_PER_KG)
                .max(Comparator.naturalOrder())
                .orElse(0.0); // TODO: Provide a different default value?
            // Add or update the entry for this day in the map
            long dateAsUnixTimestamp = workout.date.getTime();
            double existingMax = maxWeightByDayAsUnixTimestamp.getOrDefault(dateAsUnixTimestamp, Double.MIN_VALUE);
            maxWeightByDayAsUnixTimestamp.put(dateAsUnixTimestamp, Math.max(existingMax, maxWeightLbs));
        }

        // Create a dataset from the map
        List<Entry> weightByDayData = maxWeightByDayAsUnixTimestamp
            .keySet()
            .stream()
            .map(timestamp -> new Entry(timestamp, maxWeightByDayAsUnixTimestamp.get(timestamp).floatValue()))
            .sorted(Comparator.comparing(Entry::getX))
            .collect(Collectors.toList());

        // Add the data to the chart and invalidate it to force a re-render
        LineDataSet dataSet = new LineDataSet(weightByDayData, "Highest Weight by Day (LBS)");
        dataSet.setColor(Color.BLUE);
        LineData lineData = new LineData(dataSet);
        binding.progressTrackerLineChart.setData(lineData);
        binding.progressTrackerLineChart.invalidate();
    }

    private class UnixTimestampAsHumanReadableDateAxisFormatter extends ValueFormatter {
        @Override
        public java.lang.String getAxisLabel(float value, AxisBase axis) {
            return ActiveSyncApplication.YearMonthDayDateFormat.format(new Date((long)value));
        }
    }

    /**
     * Button click handler for "reset" button that resets form fields.
     * @param __ The view that triggered this event. This parameter is unused.
     */
    private void onResetButtonClick(View __) {
        // Reset fields
        this.searchStartDate = null;
        this.searchEndDate = null;
        binding.trackProgressStartDateInput.setText("");
        binding.trackProgressEndDateInput.setText("");
        binding.progressTrackerLineChart.clear();
        binding.progressTrackerLineChart.invalidate();
        stateHasChanged();
    }

}