package edu.psu.sweng888.activesync.dataAccessLayer.viewModels;

import java.util.ArrayList;
import java.util.List;

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
public class WorkoutEntryModel {

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
    public static List<WorkoutEntryModel> allFromDatabaseByUser(
        ActiveSyncDatabase db,
        User user
    ) throws Exception{
        List<WorkoutEntryModel> models = new ArrayList<WorkoutEntryModel>();
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
        // Persist the workout record first and foremost, since other persisted records will need
        // to reference its ID.
        this.workout.exerciseTypeId = this.exerciseType.exerciseType.exerciseTypeId;
        this.workout.userId = this.currentUser.userId;
        this.workout.workoutId = db.workoutDao().upsert(this.workout);

        // Persist the sets associated with this instance
        WorkoutSetDao setDao = db.workoutSetDao();
        for (WorkoutSet set : this.sets) {
            set.workoutId = this.workout.workoutId;
            set.workoutSetId = setDao.upsert(set);;
        }
        return this;
    }
}
