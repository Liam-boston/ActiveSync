package edu.psu.sweng888.activesync;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.psu.sweng888.activesync.adapters.DebuggingTextItemAdapter;
import edu.psu.sweng888.activesync.dataAccessLayer.cannedData.DefaultUsers;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;
import edu.psu.sweng888.activesync.dataAccessLayer.models.Workout;
import edu.psu.sweng888.activesync.dataAccessLayer.viewModels.WorkoutEntryModel;

public class TrackProgress extends Fragment {
    public TrackProgress() {
        // Required empty public constructor
    }

    private DebuggingTextItemAdapter<WorkoutEntryModel> debuggingListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_track_progress, container, false);

        // ====== DEBUGGING ========================================================================
        // The logic in this debugging block is just used for debugging another area of the app and
        // can be abandoned once this screen has an actual implementation.
        try {
            User activeUser = ActiveSyncApplication.getActiveUser();
            ArrayList<WorkoutEntryModel> workoutModels = WorkoutEntryModel.allFromDatabaseByUser(
                ActiveSyncApplication.getDatabase(),
                activeUser == null ? DefaultUsers.TestUser : activeUser
            );
            ListView debuggingList = view.findViewById(R.id.dbg_saved_workouts_list);
            debuggingListAdapter = new DebuggingTextItemAdapter<>(getContext(), workoutModels);
            debuggingList.setAdapter(debuggingListAdapter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Button nukeDbButton = view.findViewById(R.id.dbg_nuke_database_button);
        nukeDbButton.setOnClickListener(v -> nukeDatabase());
        // =========================================================================================

        return view;
    }

    /**
     * Deletes all records from the database.
     */
    private void nukeDatabase() {
        ActiveSyncApplication.reinitializeDatabase();
        debuggingListAdapter.clear();
        debuggingListAdapter.notifyDataSetChanged();
        Toast.makeText(
            getContext(), "Database defaults restored.", Toast.LENGTH_LONG
        ).show();
    }
}