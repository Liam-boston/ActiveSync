package edu.psu.sweng888.activesync.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.psu.sweng888.activesync.ActiveSyncApplication;
import edu.psu.sweng888.activesync.R;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;
import edu.psu.sweng888.activesync.dataAccessLayer.models.Workout;

public class RecoveryStatusAdapter extends ArrayAdapter<MuscleGroup> {
    private LayoutInflater inflater;
    private TextView recoveryStatus, estimatedRecovery;
    private ProgressBar recoveryBar;
    private Integer recStatus;
    private List<Workout> workoutsPerformed;
    public RecoveryStatusAdapter(Context context, List<MuscleGroup> muscleGroupList) {
        super(context, 0, muscleGroupList);
        inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.recovery_status_list_item, parent, false);
        }

        MuscleGroup muscleGroup = getItem(position);

        // initialize UI elements
        recoveryStatus = view.findViewById(R.id.recoveryStatus);
        estimatedRecovery = view.findViewById(R.id.estimatedRecovery);
        recoveryBar = view.findViewById(R.id.recoveryBar);

        // get the active user
        User activeUser = ActiveSyncApplication.getActiveUser();

        // pull workouts from the database
        ActiveSyncDatabase db = ActiveSyncApplication.getDatabase();
        workoutsPerformed = db.workoutDao().getWorkoutsForUser(activeUser.userId);

//        workoutsPerformed = db.workoutDao().getWorkoutsFromRange(activeUser.userId);
//        Log.d("TEST1", workoutsPerformed.get(0).date + " ");
//        Log.d("TEST1", workoutsPerformed.get(1).date + " ");

        // calculate recovery status
        assert muscleGroup != null;
        recStatus = calculateRecoveryStatus(muscleGroup);

        // display recoveryStatus
        String percentRecovered = muscleGroup.getName() + " \t-\t " + recStatus + "% recovered";
        recoveryStatus.setText(percentRecovered);

        // display estimatedRecovery
        String estimate = "~" + calculateRecoveryTime(muscleGroup) + " day(s) until full recovery";
        estimatedRecovery.setText(estimate);

        // display recoveryBar
        recoveryBar.setProgress(recStatus);
        adjustProgressBarColors(recStatus);

        return view;
    }

    /**
     * With a muscle groups recovery rate (the rate at which it recovers from fatigue when not worked)
     * and the time elapsed since the muscle group was last worked, this method returns the recovery status
     * as an Integer
     * <p>
     * Example: If a muscle group is 90% fatigued after a workout 2 days ago with a recovery rate of 0.06,
     * the recovery status of that muscle group would be 22%. The muscle group recovers 6% each day and
     * after 2 days of rest, the muscle group would be 12% more recovered than after the last workout.
     *
     * @param muscleGroup
     * @return Integer recoveryStatus (whole percentage)
     */
    private Integer calculateRecoveryStatus(MuscleGroup muscleGroup) {
        // Note: all muscle groups have a recovery rate of 33% which means we only
        // need to worry about workouts performed in the last 3 days, anything older
        // will have fully recovered already
        List<Workout> prevThreeDays = new ArrayList<>();

        // filter the workoutsPerformed list to workouts performed in the last 3 days AND
        // those that impact the selected MuscleGroup
        for (int i = 0; i < workoutsPerformed.size(); i++) {
            Workout k = workoutsPerformed.get(i);

            if (isWithinRange(k.date) && (k.exerciseTypeId == muscleGroup.muscleGroupId)) {
                prevThreeDays.add(workoutsPerformed.get(i));
            }
        }

        // TODO: filter workouts by MuscleGroup

        // temporary
        return (int) (Math.random() * 100) + 1;
    }

    /**
     * With a muscle groups recovery rate (the rate at which it recovers from fatigue when not worked)
     * and the time elapsed since the muscle group was last worked, this method returns the estimated
     * number of days until the muscle group is fully recovered
     * <p>
     * Example: If a muscle group is 90% fatigued after a workout 2 days ago with a recovery rate of 0.06,
     * the estimated recovery time would be ~13 days. The muscle group recovers 6% each day and after 2 days
     * of rest, the muscle group would be 22% recovered. With a recovery rate of 6%, it would take an additional
     * 13 days until that muscle group was fully recovered.
     *
     * @param muscleGroup
     * @return Integer recoveryTime (in days)
     */
    private Integer calculateRecoveryTime(MuscleGroup muscleGroup) {
        // TODO: implement calculateRecoveryTime()
        return 1;
    }

    /**
     * Adjust the color of each progress bar based on the calculated recovery status for that muscle group:
     * <10 = red
     * <30 = orange
     * <60 = yellow
     * <75 = light green
     * <100 = green
     *
     * @param recoveryStatus
     */
    private void adjustProgressBarColors(Integer recoveryStatus) {
        if (recoveryStatus <= 10) {
            // Red
            recoveryBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(204, 0, 0)));
        } else if (recoveryStatus <= 30) {
            // Orange
            recoveryBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(204, 102, 0)));
        } else if (recoveryStatus <= 60) {
            // Yellow
            recoveryBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(204, 204, 0)));
        } else if (recoveryStatus <= 75) {
            // Light green
            recoveryBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(102, 204, 0)));
        } else if (recoveryStatus <= 100) {
            // Green
            recoveryBar.setProgressTintList(ColorStateList.valueOf(Color.rgb(0, 204, 0)));
        }
    }

    /**
     * Helper method for calculateRecoveryStatus() - determines if the date
     * parameter falls within the previous 3 days
     *
     * @param date
     * @return boolean
     */
    private boolean isWithinRange(Date date) {
        // today
        Date today = new Date();

        // three days ago
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date threeDaysAgo = new Date(System.currentTimeMillis() - (3 * DAY_IN_MS));

        return !(date.before(today) || date.after(threeDaysAgo));
    }
}