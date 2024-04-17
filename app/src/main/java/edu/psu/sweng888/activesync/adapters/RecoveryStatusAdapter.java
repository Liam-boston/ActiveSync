package edu.psu.sweng888.activesync.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import edu.psu.sweng888.activesync.R;
import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;

public class RecoveryStatusAdapter extends ArrayAdapter<MuscleGroup> {
    private LayoutInflater inflater;
    private TextView recoveryStatus, estimatedRecovery;
    private ProgressBar recoveryBar;
    private Integer recStatus;

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
     *
     * Example: If a muscle group is 90% fatigued after a workout 2 days ago with a recovery rate of 0.06,
     * the recovery status of that muscle group would be 22%. The muscle group recovers 6% each day and
     * after 2 days of rest, the muscle group would be 12% more recovered than after the last workout.
     *
     * @param muscleGroup
     * @return Integer recoveryStatus (whole percentage)
     */
    private Integer calculateRecoveryStatus(MuscleGroup muscleGroup) {
        // TODO: implement calculateRecoveryStatus()
        return (int) (Math.random() * 100) + 1;
    }

    /**
     * With a muscle groups recovery rate (the rate at which it recovers from fatigue when not worked)
     * and the time elapsed since the muscle group was last worked, this method returns the estimated
     * number of days until the muscle group is fully recovered
     *
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
}
