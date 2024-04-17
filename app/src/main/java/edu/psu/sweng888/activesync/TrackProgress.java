package edu.psu.sweng888.activesync;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.psu.sweng888.activesync.adapters.DebuggingTextItemAdapter;
import edu.psu.sweng888.activesync.dataAccessLayer.cannedData.DefaultUsers;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseType;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeWithMuscleGroups;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;
import edu.psu.sweng888.activesync.dataAccessLayer.models.Workout;
import edu.psu.sweng888.activesync.dataAccessLayer.viewModels.WorkoutEntryModel;
import edu.psu.sweng888.activesync.databinding.FragmentTrackProgressBinding;

public class TrackProgress extends Fragment {

    private FragmentTrackProgressBinding binding;

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

        // Set up click event handlers
        binding.trackProgressResetButton.setOnClickListener(this::onResetButtonClick);
        binding.trackProgressSearchButton.setOnClickListener(this::onSearchButtonClick);

        // Set up choices for exercise type dropdown
        ArrayAdapter<ExerciseTypeWithMuscleGroups> exerciseTypeDropdownAdapter = new ArrayAdapter<>(
            binding.getRoot().getContext(),
            android.R.layout.simple_spinner_item,
            ActiveSyncApplication.getDatabase().exerciseTypeDao().getExerciseTypesWithMuscleGroups()
        );
        exerciseTypeDropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.trackProgressExerciseTypeInput.setAdapter(exerciseTypeDropdownAdapter);


        return binding.getRoot();
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
        binding.trackProgressStartDateInput.setText("");
        binding.trackProgressEndDateInput.setText("");
        // TODO: Should we really clear the chart data?
        binding.progressTrackerLineChart.clear();
        binding.progressTrackerLineChart.invalidate();
    }

}