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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.psu.sweng888.activesync.ActiveSyncApplication;
import edu.psu.sweng888.activesync.R;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseType;
import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;
import edu.psu.sweng888.activesync.dataAccessLayer.models.Workout;
import edu.psu.sweng888.activesync.dataAccessLayer.viewModels.WorkoutEntryModel;

public class RecoveryStatusAdapter extends ArrayAdapter<MuscleGroup> {
    private final LayoutInflater inflater;
    private ProgressBar recoveryBar;
    private List<WorkoutEntryModel> userWorkouts;
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
        TextView recoveryStatus = view.findViewById(R.id.recoveryStatus);
        TextView estimatedRecovery = view.findViewById(R.id.estimatedRecovery);
        recoveryBar = view.findViewById(R.id.recoveryBar);

        // get the active user
        User activeUser = ActiveSyncApplication.getActiveUser();

        // pull workouts from the database
        ActiveSyncDatabase db = ActiveSyncApplication.getDatabase();
        try {
            userWorkouts = WorkoutEntryModel.allFromDatabaseByUser(db, activeUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Calculate fatigue status
        assert muscleGroup != null;
        MuscleGroupFatigueStatus muscleGroupFatigueStatus = calculateFatigueStatus(muscleGroup);

        // display recoveryStatus
        String percentRecovered = muscleGroup.getName() + " \t-\t " + (100 - muscleGroupFatigueStatus.getApproximateIntegerFatigue()) + "% recovered";
        recoveryStatus.setText(percentRecovered);

        // display estimatedRecovery
        String recoveryDaysText = "until full recovery";
        switch (muscleGroupFatigueStatus.daysUntilFullRecovery) {
            case 0:
                recoveryDaysText = "Fully recovered!";
                break;
            case 1:
                recoveryDaysText = "~1 day " + recoveryDaysText;
                break;
            default:
                recoveryDaysText = "~" + muscleGroupFatigueStatus.daysUntilFullRecovery + " days " + recoveryDaysText;
                break;
        }
        estimatedRecovery.setText(recoveryDaysText);

        // display recoveryBar
        recoveryBar.setProgress(100 - muscleGroupFatigueStatus.getApproximateIntegerFatigue());
        adjustProgressBarColors(100 - muscleGroupFatigueStatus.getApproximateIntegerFatigue());

        return view;
    }

    /**
     * Returns a date that is the given number of days before today's date.
     */
    private Date getDaysAgoDate(int daysAgo) {
        final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;
        return new Date(System.currentTimeMillis() - (MILLISECONDS_PER_DAY * daysAgo));
    }

    /**
     * Returns a date that represents tomorrow.
     */
    private Date tomorrow() {
        return getDaysAgoDate(-1);
    }

    private boolean dateIsInRange(Date dateToConsider, Date oldestDate, Date newestDate) {
        return dateToConsider.after(oldestDate) && dateToConsider.before(newestDate);
    }

    private Date withoutTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private boolean workoutTargetsMuscleGroup(WorkoutEntryModel workout, MuscleGroup muscleGroup) {
        return workout.exerciseType.muscleGroups.stream().anyMatch(x -> x.muscleGroupId == muscleGroup.muscleGroupId);
    }

    /**
     * The results of a fatigue calculation on the associated muscle group w.r.t. a set of workouts
     * targeting that muscle group.
     */
    private static class MuscleGroupFatigueStatus {
        /**
         * The muscle group associated with this set of results.
         */
        public final MuscleGroup muscleGroup;

        /**
         * The percentage of fatigue experienced by this muscle group expressed as a double in the
         * range [0, 1].  Multiply this by 100 to get the "human-readable" percentage.
         */
        public final double fatiguePercentage;

        /**
         * The approximate whole number of days until this muscle group will be fully recovered,
         * assuming no more workouts targeting this group are performed in the meantime.
         */
        public final int daysUntilFullRecovery;

        public MuscleGroupFatigueStatus(
            MuscleGroup muscleGroup,
            double fatiguePercentage,
            int daysUntilFullRecovery
        ) {
            this.muscleGroup = muscleGroup;
            this.fatiguePercentage = fatiguePercentage;
            this.daysUntilFullRecovery = daysUntilFullRecovery;
        }

        public int getApproximateIntegerFatigue() {
            return (int) Math.round(100.0 * this.fatiguePercentage);
        }
    }

    /**
     * Returns statistics surrounding the fatigue level of the given muscle group with respect to
     * the workouts targeting this muscle group logged by the current user.
     *
     * The fatigue of a muscle group is defined as:
     *   fatigue = numDaysWorked * recoveryRate * E
     *
     * Where "numDaysWorked" is the number of days that have a workout logged that targets the
     * muscle group in question, "recoveryRate" is the rate at which the muscle group recovers
     * per day unworked, and "E" is the "exhaustion coefficient", which is the rate at which a
     * muscle group becomes fatigued per day worked.
     *
     * The exhaustion coefficient is currently hard-coded to 3.0 and is a factor of the muscle
     * group's recovery rate. Ideally, this would be replaced by a "fatigueRate" property defined
     * on each muscle group. The "exhaustion coefficient" concept is only used to get real-ish
     * looking numbers while testing.
     *
     * The number of days until full recovery is simply defined as:
     *   daysUntilRecovered = fatigue / recoveryRate
     */
    private MuscleGroupFatigueStatus calculateFatigueStatus(MuscleGroup muscleGroup) {
        // Get all workouts for this user that are associated with the given muscle group and took
        // place within the amount of time it would take for the muscle group to fully recover.
        int maxDaysUntilFullyRecovered = (int) Math.ceil(1 / muscleGroup.recoveryRate);
        long numDaysWorkedOnMuscleGroupWithinRecoveryTimeline = userWorkouts
            .stream()
            .filter(model -> workoutTargetsMuscleGroup(model, muscleGroup))
            .map(model -> withoutTime(model.workout.date))
            .filter(date ->
                dateIsInRange(
                    date,
                    withoutTime(getDaysAgoDate(maxDaysUntilFullyRecovered)),
                    withoutTime(tomorrow())
                )
            )
            .map(ActiveSyncApplication.YearMonthDayDateFormat::format) // Make the objects more easiliy "distinct"-able. This is hacky, but oh well.
            .distinct()
            .count();

        // The muscle group is assumed "rested" if no workouts have occurred in the time period it
        // takes to fully recover. Start the fatigue percentage at zero, then "penalize" it by a
        // factor of the recovery rate for the given muscle group for each day we have an exercise.
        // TODO: We're making the (very hand-wave-y) assumption that each muscle group is exhausted by three times the recovery rate for each day it is used. This should probably be replaced with a "fatigue rate" property on each muscle group.
        final double EXHAUSTION_FACTOR = 3.0;
        double fatigue = muscleGroup.recoveryRate * EXHAUSTION_FACTOR * numDaysWorkedOnMuscleGroupWithinRecoveryTimeline;

        // Calculate the number of days until the muscle group will be fully recovered.
        int daysUntilFullyRecovered = (int) Math.round(fatigue / muscleGroup.recoveryRate);

        // Return the calculated statistics wrapped up in an envelope type
        return new MuscleGroupFatigueStatus(muscleGroup, fatigue, daysUntilFullyRecovered);
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