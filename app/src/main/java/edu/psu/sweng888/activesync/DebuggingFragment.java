package edu.psu.sweng888.activesync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

import edu.psu.sweng888.activesync.adapters.DebuggingTextItemAdapter;
import edu.psu.sweng888.activesync.dataAccessLayer.cannedData.DefaultUsers;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;
import edu.psu.sweng888.activesync.dataAccessLayer.viewModels.WorkoutEntryModel;

public class DebuggingFragment extends Fragment {
    public DebuggingFragment() {
        // Required empty public constructor
    }

    private DebuggingTextItemAdapter<WorkoutEntryModel> debuggingListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_debugging, container, false);

        try {
            User activeUser = ActiveSyncApplication.getActiveUser();
            ArrayList<WorkoutEntryModel> workoutModels = WorkoutEntryModel.allFromDatabaseByUser(
                ActiveSyncApplication.getDatabase(),
                activeUser == null ? DefaultUsers.TestUser : activeUser
            );
            ListView debuggingList = view.findViewById(R.id.dbg_saved_workouts_list);
            debuggingListAdapter = new DebuggingTextItemAdapter<>(
                getContext(),
                workoutModels,
                this::handleDebugItemClick
            );
            debuggingList.setAdapter(debuggingListAdapter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Button nukeDbButton = view.findViewById(R.id.dbg_nuke_database_button);
        nukeDbButton.setOnClickListener(v -> nukeDatabase());

        return view;
    }

    private void handleDebugItemClick(WorkoutEntryModel workout) {
        // Redirect to the "log workout" view to edit the clicked item
        Intent editWorkout = new Intent(this.getActivity(), MainActivity.class);
        editWorkout.putExtra(Constants.EXTRAS_KEY_WORKOUT_TO_EDIT, workout);
        editWorkout.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // This flag is necessary to reuse the existing MainActivity as opposed starting a new one
        startActivity(editWorkout);
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