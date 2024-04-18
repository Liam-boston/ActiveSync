package edu.psu.sweng888.activesync.dataAccessLayer.cannedData;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.psu.sweng888.activesync.dataAccessLayer.dao.WorkoutDao;
import edu.psu.sweng888.activesync.dataAccessLayer.dao.WorkoutSetDao;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.Weight;
import edu.psu.sweng888.activesync.dataAccessLayer.models.Workout;
import edu.psu.sweng888.activesync.dataAccessLayer.models.WorkoutSet;
import edu.psu.sweng888.activesync.dataAccessLayer.models.WorkoutWithSets;

public final class DefaultWorkouts {

    private DefaultWorkouts() {}

    private static Workout TestUserPullups(
        long id,
        Date date
    ) {
        return new Workout(
            id,
            DefaultUsers.TestUser.userId,
            DefaultExerciseTypes.PullUps.exerciseTypeId,
            date,
            15
        );
    }

    private static Date dateOf(int year, int month, int day) {
        return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.of("America/Montreal")).toInstant());
    }

    public static final Workout April18Workout = TestUserPullups(1l, dateOf(2024, 04, 18));
    public static final WorkoutSet April18WorkoutSet1 = new WorkoutSet(1l, April18Workout.workoutId, 5, Weight.Pounds(100));
    public static final WorkoutSet April18WorkoutSet2 = new WorkoutSet(2l, April18Workout.workoutId, 5, Weight.Pounds(105));
    public static final WorkoutWithSets April18 = new WorkoutWithSets(April18Workout, April18WorkoutSet1, April18WorkoutSet2);

    public static final Workout April19Workout = TestUserPullups(2l, dateOf(2024, 04, 19));
    public static final WorkoutSet April19WorkoutSet1 = new WorkoutSet(3l, April19Workout.workoutId, 5, Weight.Pounds(100));
    public static final WorkoutWithSets April19 = new WorkoutWithSets(April19Workout, April19WorkoutSet1);

    public static final Workout April20Workout = TestUserPullups(3l, dateOf(2024, 04, 20));
    public static final WorkoutSet April20WorkoutSet1 = new WorkoutSet(4l, April20Workout.workoutId, 5, Weight.Kilograms(60));
    public static final WorkoutWithSets April20 = new WorkoutWithSets(April20Workout, April20WorkoutSet1);

    public static final Workout April21Workout = TestUserPullups(4l, dateOf(2024, 04, 21));
    public static final WorkoutSet April21WorkoutSet1 = new WorkoutSet(5l, April21Workout.workoutId, 5, Weight.Pounds(130));
    public static final WorkoutWithSets April21 = new WorkoutWithSets(April21Workout, April21WorkoutSet1);

    private static final WorkoutWithSets[] getAllDefaultWorkoutsWithSets() {
        return new WorkoutWithSets[] { April18, April19, April20, April21 };
    }

    public static void initialize(ActiveSyncDatabase db) {
        WorkoutDao workoutDao = db.workoutDao();
        WorkoutSetDao workoutSetDao = db.workoutSetDao();
        for (WorkoutWithSets workoutWithSets : getAllDefaultWorkoutsWithSets()) {
            workoutDao.ensureInserted(workoutWithSets.workout);
            for (WorkoutSet set : workoutWithSets.sets) {
                workoutSetDao.ensureInserted(set);
            }
        }
    }

}
