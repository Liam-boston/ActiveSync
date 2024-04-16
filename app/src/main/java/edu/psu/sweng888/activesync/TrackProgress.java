package edu.psu.sweng888.activesync;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTrackProgressBinding.inflate(inflater, container, false);

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

        return binding.getRoot();
    }

}