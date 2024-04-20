package edu.psu.sweng888.activesync;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import edu.psu.sweng888.activesync.adapters.RecoveryStatusAdapter;
import edu.psu.sweng888.activesync.adapters.WorkoutSetAdapter;
import edu.psu.sweng888.activesync.dataAccessLayer.cannedData.DefaultMuscleGroups;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseType;
import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;
import edu.psu.sweng888.activesync.dataAccessLayer.models.Workout;
import edu.psu.sweng888.activesync.dataAccessLayer.models.WorkoutSet;
import edu.psu.sweng888.activesync.dataAccessLayer.viewModels.WorkoutEntryModel;

public class WorkoutCalendar extends Fragment {

    private ListView listView;
    private WorkoutSetAdapter adapter;
    private List<Workout> workoutsPerformed;
    List<Workout> workout = new ArrayList<Workout>();

    private String Workout;
    // Required empty public constructor


    public WorkoutCalendar() {
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Configure the ListView
        listView = view.findViewById(R.id.calendar_list);

        // get the active user
        User activeUser = ActiveSyncApplication.getActiveUser();

        // pull workouts from the database
        ActiveSyncDatabase db = ActiveSyncApplication.getDatabase();

        List<WorkoutEntryModel> workouts;
        try {
            workouts = WorkoutEntryModel.allFromDatabaseByUser(db, activeUser);
            List<Date> workoutDates = workouts.stream().map(x -> x.workout.date).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }




        //List<WorkoutSet> exerciseTypes = new ArrayList<>(Arrays.asList(ExerciseType()));


        return view;
    }
}