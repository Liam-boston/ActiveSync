package edu.psu.sweng888.activesync.dataAccessLayer.viewModels;

import android.database.sqlite.SQLiteConstraintException;

import androidx.room.Transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.psu.sweng888.activesync.ActiveSyncApplication;
import edu.psu.sweng888.activesync.dataAccessLayer.dao.WorkoutDao;
import edu.psu.sweng888.activesync.dataAccessLayer.dao.WorkoutSetDao;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeWithMuscleGroups;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;
import edu.psu.sweng888.activesync.dataAccessLayer.models.Workout;
import edu.psu.sweng888.activesync.dataAccessLayer.models.WorkoutSet;

/**
 * View model for the "workout entry" page of the application. Contains the "edit" state of the
 * view, which ultimately culminates in a workout entity (and associated entities) being saved
 * into the database.
 */
public class WorkoutEntryModel implements Serializable {

    /**
     * The currently logged-in user.
     */
    public User currentUser = new User();

    /**
     * The workout object being edited.
     */
    public Workout workout = new Workout();

    /**
     * The exercise type associated with the current workout.
     */
    public ExerciseTypeWithMuscleGroups exerciseType = new ExerciseTypeWithMuscleGroups();

    /**
     * The sets associated with the current workout.
     */
    public List<WorkoutSet> sets = new ArrayList<>();

    /**
     * Creates and returns WorkoutEntryModel instances for all workouts belonging to the given user.
     * @param db The ActiveSync database reference to use for queries.
     * @param user The User record for which workout models should be retrieved.
     * @return A list of WorkoutEntryModel instances representing all information about the given user's workouts.
     * @throws Exception Thrown when database accesses fail.
     */
    public static ArrayList<WorkoutEntryModel> allFromDatabaseByUser(
        ActiveSyncDatabase db,
        User user
    ) throws Exception{
        ArrayList<WorkoutEntryModel> models = new ArrayList<WorkoutEntryModel>();
        for (long workoutId : db.workoutDao().getWorkoutIdsForUser(user.userId)) {
            models.add(
                WorkoutEntryModel.fromDatabaseByWorkoutId(db, workoutId)
            );
        }
        return models;
    }

    /**
     * Creates and returns an instance of WorkoutEntryModel by querying the database for items
     * related to the given workout ID.
     * @param db The database class from which data access objects can be obtained.
     * @param workoutId The ID (primary key) of the workout entry to retrieve.
     * @return A fully-populated WorkoutEntryModel representing the workout information associated with the given workout ID.
     * @throws Exception When database access fails or requested records do not exist.
     */
    public static WorkoutEntryModel fromDatabaseByWorkoutId(
        ActiveSyncDatabase db,
        long workoutId
    ) throws Exception {
        // Create a blank model to populate with DB entities
        WorkoutEntryModel model = new WorkoutEntryModel();

        // Get the workout entity associated with the given ID
        model.workout = db.workoutDao().getById(workoutId);
        if (model.workout == null) {
            // TODO: Narrow exception type
            throw new Exception("Failed to retrieve a workout record by ID \"" + workoutId + "\".");
        }

        // Get the user associated with the workout
        model.currentUser = db.userDao().getById(model.workout.userId);
        if (model.currentUser == null) {
            // TODO: Narrow exception type
            throw new Exception(
                "Failed to retrieve the user associated with workout \"" + workoutId + "\" by " +
                "user ID \"" + model.workout.userId + "\"."
            );
        }

        // Get the exercise type associated with this workout along with the muscle groups
        // associated with that exercise type.
        model.exerciseType = db.exerciseTypeDao().getExerciseTypeWithMuscleGroups(
            model.workout.exerciseTypeId
        );
        if (model.exerciseType == null) {
            // TODO: Narrow exception type
            throw new Exception(
                "Failed to retrieve exercise type associated with workout \"" + workoutId + "\" " +
                "by exercise type ID \"" + model.workout.exerciseTypeId + "\"."
            );
        }

        // Get the workout sets associated with this workout
        model.sets = db.workoutSetDao().getSetsForWorkout(model.workout.workoutId);
        if (model.sets == null) {
            // TODO: Narrow exception type
            throw new Exception(
                "Failed to retrieve workout sets associated with workout \"" + workoutId + "\"."
            );
        }

        // Return the hydrated model
        return model;
    }

    public WorkoutEntryModel persistToDatabase(ActiveSyncDatabase db) {
        // Persist the workout object first -- other items to be persisted need its ID to be
        // linked to this view model correctly.
        this.workout.exerciseTypeId = this.exerciseType.exerciseType.exerciseTypeId;
        this.workout.userId = this.currentUser.userId;
        WorkoutDao workoutDao = db.workoutDao();
        this.workout.workoutId = workoutDao.upsert(this.workout);

        // For each set included in this workout, update the reference to the workout ID and
        // persist to the database.
        WorkoutSetDao workoutSetDao = db.workoutSetDao();
        workoutSetDao.deleteSetsForWorkoutId(this.workout.workoutId); // TODO: Make this smarter and only delete the records that are no longer associated with this workout model.
        for (WorkoutSet set : this.sets) {
            set.workoutId = this.workout.workoutId;
            set.workoutSetId = workoutSetDao.upsert(set);
        }
        return this;
    }

    public boolean deleteFromDatabase(ActiveSyncDatabase db) {
        // Delete the sets associated with this workout, then delete the workout itself
        WorkoutSetDao workoutSetDao = db.workoutSetDao();
        workoutSetDao.deleteSetsForWorkoutId(this.workout.workoutId);
        WorkoutDao workoutDao = db.workoutDao();
        workoutDao.delete(this.workout);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
            .append("     Workout ID: " + this.workout.workoutId + "\n")
            .append("           User: " + this.currentUser.name + "\n")
            .append("  Exercise Type: " + this.exerciseType.exerciseType.name + "\n")
            .append("           Date: " + ActiveSyncApplication.YearMonthDayDateFormat.format(this.workout.date) + "\n")
            .append("Duration (Mins): " + this.workout.durationMinutes + "\n")
            .append("Muscle Group(s): " + this.exerciseType.muscleGroups.stream().map(x -> x.name).reduce((a, b) -> a + ", " + b).get() + "\n")
            .append("      Num. Sets: " + this.sets.size() + "\n");

        String spacer = "           ";
        for (WorkoutSet set : sets) {
            builder.append(spacer + "-> " + set.toString() + "\n");
        }

        return builder.toString();
    }
}
