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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
        MuscleGroupFatigueStatus muscleGroupFatigueStatus;
        try {
            muscleGroupFatigueStatus = calculateFatigueStatus(muscleGroup);
        }
        catch (ParseException pe) {
            throw new RuntimeException(pe);
        }

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

        public static MuscleGroupFatigueStatus fullyRecovered(MuscleGroup muscleGroup) {
            return new MuscleGroupFatigueStatus(muscleGroup, 0.0, 0);
        }

        public int getApproximateIntegerFatigue() {
            return (int) Math.round(100.0 * this.fatiguePercentage);
        }
    }

    /**
     * Returns statistics surrounding the fatigue level of the given muscle group with respect to
     * the workouts targeting this muscle group logged by the current user.
     */
    private MuscleGroupFatigueStatus calculateFatigueStatus(MuscleGroup muscleGroup) throws ParseException {
        // Determine the current fatigue rate by looking through all the workouts the user has logged
        // for the given muscle group and counting each day worked as "fatigue" and each day not worked
        // as "rest". Sum the fatigue and rest coefficients across this time range up to the current
        // day to get the total fatigue.
        // TODO: This has the unfortunately requirement of reading **all the data**, but since this
        //       is a "toy" app, this is okay for now. Future work would be finding a way to more
        //       efficiently calculate this time series data.
        Set<String> daysTargetingMuscleGroup = userWorkouts
            .stream()
            .filter(model -> workoutTargetsMuscleGroup(model, muscleGroup))
            .map(model -> ActiveSyncApplication.YearMonthDayDateFormat.format(model.workout.date)) // We use a formatted string because it is easier to "distinct()".
            .distinct()
            .collect(Collectors.toSet());

        // If we've never worked this muscle group, we can exit now with full recovery status
        if (daysTargetingMuscleGroup.size() < 1) {
            return MuscleGroupFatigueStatus.fullyRecovered(muscleGroup);
        }

        // Get the oldest date that we worked the muscle group. We then count the days between today
        // and the oldest date (inclusive) for days worked and days rested. The former build up
        // fatigue and the latter reduce it.
        Date earliestDateWorked;
        try {
            earliestDateWorked = withoutTime(ActiveSyncApplication.YearMonthDayDateFormat.parse(
                daysTargetingMuscleGroup
                    .stream()
                    .min(Comparator.naturalOrder())
                    .get())
            );
        }
        catch (ParseException pe) {
            throw new RuntimeException(pe);
        }

        // Loop from the earliest date to today's date, counting the worked VS rested days to come
        // up with a fatigue level.
        double fatiguePercentage = 0.0;
        Date today = withoutTime(new Date());
        Date dateBeingConsidered = earliestDateWorked;
        final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;
        while (!dateBeingConsidered.after(today))
        {
            String dateBeingConsideredAsString = ActiveSyncApplication.YearMonthDayDateFormat.format(dateBeingConsidered);
            if (daysTargetingMuscleGroup.contains(dateBeingConsideredAsString)) {
                // The user worked this muscle group on this day. Add fatigue equal to the muscle
                // group's fatigue rate.
                double FATIGUE_RATE = muscleGroup.recoveryRate * 2; // TODO: Define a fatigue rate per muscle group!
                fatiguePercentage = fatiguePercentage + FATIGUE_RATE;
            }
            else {
                // The user rested this muscle group on this day. Remove fatigue equal to the muscle
                // group's recovery rate.
                fatiguePercentage = fatiguePercentage - muscleGroup.recoveryRate;
            }
            // Clamp the fatigue percentage to the range [0, 1].
            fatiguePercentage = clamp(fatiguePercentage, 0, 1);
            /**
             * The number of milliseconds in a single day (24 hours).
             */
            dateBeingConsidered = withoutTime(new Date(dateBeingConsidered.getTime() + MILLISECONDS_PER_DAY));
        }

        // Calculate the days of pure rest until a full recovery.
        int approximateDaysUntilFullRecovery = (int) Math.round(fatiguePercentage / muscleGroup.recoveryRate);

        return new MuscleGroupFatigueStatus(muscleGroup, fatiguePercentage, approximateDaysUntilFullRecovery);

    }

    private static double clamp(double value, double minInclusive, double maxInclusive) {
        return Math.max(minInclusive, Math.min(maxInclusive, value));
    }

    private static boolean dateIsAfterOther(Date toConsider, Date referencePoint) {
        return ActiveSyncApplication.YearMonthDayDateFormat.format(toConsider)
            .compareTo(ActiveSyncApplication.YearMonthDayDateFormat.format(referencePoint)) > 0;
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