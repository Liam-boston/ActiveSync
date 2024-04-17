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

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseType;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeWithMuscleGroups;
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

        // Disable search button if we haven't selected any dates.
        boolean atLeastOneDateSet = this.searchStartDate != null || this.searchEndDate != null;
        setEnabledWithOpacity(
            this.binding.trackProgressSearchButton,
            this.selectedExerciseType != null && atLeastOneDateSet
        );
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

    private void DEBUGGING_setupDummyChartData() {
        // TODO: DEBUGGING! Add data into the line chart to test the library's functionality.
        // See this documentation page for quick-start tips:
        //   https://weeklycoding.com/mpandroidchart-documentation/getting-started/
        List<Entry> plotItems = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            plotItems.add(new Entry(i, i)); // y = x
        }
        LineDataSet dataSet = new LineDataSet(plotItems, "y = x");
        dataSet.setColor(Color.BLACK);
        LineData lineData = new LineData(dataSet);
        binding.progressTrackerLineChart.setData(lineData);
        binding.progressTrackerLineChart.invalidate();
    }

    /**
     * Button click handler for "search" button that performs a search for the requested data and
     * populates the graph with it.
     * @param __ The view that triggered this event. This parameter is unused.
     */
    private void onSearchButtonClick(View __) {
        this.DEBUGGING_setupDummyChartData(); // TODO: Replace w/ real impl.!
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