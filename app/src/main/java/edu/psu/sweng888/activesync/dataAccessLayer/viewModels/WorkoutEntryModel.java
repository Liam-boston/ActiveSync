package edu.psu.sweng888.activesync.dataAccessLayer.viewModels;

import java.util.List;

import edu.psu.sweng888.activesync.dataAccessLayer.dao.WorkoutSetDao;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseType;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeWithMuscleGroups;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;
import edu.psu.sweng888.activesync.dataAccessLayer.models.Workout;
import edu.psu.sweng888.activesync.dataAccessLayer.models.WorkoutSet;

/**
 * View model for the "workout entry" page of the application. Contains the "edit" state of the
 * view, which ultimately culminates in a workout entity (and associated entities) being saved
 * into the database.
 */
public class WorkoutEntryModel {

    /**
     * The currently logged-in user.
     */
    public User currentUser;

    /**
     * The workout object being edited.
     */
    public Workout workout;

    /**
     * The exercise type associated with the current workout.
     */
    public ExerciseTypeWithMuscleGroups exerciseType;

    /**
     * The sets associated with the current workout.
     */
    public List<WorkoutSet> sets;

    /**
     * Creates and returns an instance of WorkoutEntryModel by querying the database for items
     * related to the given workout ID.
     * @param db The database class from which data access objects can be obtained.
     * @param workoutId The ID (primary key) of the workout entry to retrieve.
     * @return A fully-populated WorkoutEntryModel representing the workout information associated with the given workout ID.
     * @throws Exception When database access fails or requested records do not exist.
     */
    public static WorkoutEntryModel fromDatabase(
        ActiveSyncDatabase db,
        int workoutId
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

    public void persistToDatabase(ActiveSyncDatabase db) {
        // Persist the workout record first and foremost, since other persisted records will need
        // to reference its ID.
        this.workout.exerciseTypeId = this.exerciseType.exerciseType.exerciseTypeId;
        this.workout.userId = this.currentUser.userId;
        long workoutId = db.workoutDao().upsert(this.workout);

        // Persist the sets associated with this instance
        WorkoutSetDao setDao = db.workoutSetDao();
        for (WorkoutSet set : this.sets) {
            set.workoutId = workoutId;
            set.workoutSetId = setDao.upsert(set);
        }
    }
}
