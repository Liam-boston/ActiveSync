package edu.psu.sweng888.activesync.adapters;

import android.content.Context;
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
    private TextView muscleGroupName, recoveryStatus;
    private ProgressBar recoveryBar;

    public RecoveryStatusAdapter(Context context, List<MuscleGroup> muscleGroupList) {
        super(context, 0, muscleGroupList);
        inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.recovery_status_list_item, parent, false);
        }

        MuscleGroup muscleGroup =  getItem(position);

        muscleGroupName = view.findViewById(R.id.muscleGroupName);
        recoveryStatus = view.findViewById(R.id.recoveryStatus);
        recoveryBar = view.findViewById(R.id.recoveryBar);

        // display MuscleGroup values
        assert muscleGroup != null;
        muscleGroupName.setText(muscleGroup.getName());

        // calculate recovery status
        Integer status = calculateRecoveryStatus(muscleGroup.getRecoveryRate());

        // display recovery status
        String recoveryPercentage = status + "% recovered";
        recoveryStatus.setText(recoveryPercentage);

        /* TODO: Change progress bar color based on recovery status
            < 10% = red
            < 25% = orange
            < 50% = yellow
            < 75% = light green
            < 100% = green
        */
        recoveryBar.setProgress(status);

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
     * @param rate
     * @return Integer recoveryStatus
     */
    private Integer calculateRecoveryStatus(double rate) {
        // TODO: implement calculateRecoveryStatus()
        return 50;
    }
}
