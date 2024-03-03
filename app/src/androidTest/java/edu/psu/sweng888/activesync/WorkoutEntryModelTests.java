package edu.psu.sweng888.activesync;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.psu.sweng888.activesync.dataAccessLayer.dao.ExerciseTypeDao;
import edu.psu.sweng888.activesync.dataAccessLayer.dao.MuscleGroupDao;
import edu.psu.sweng888.activesync.dataAccessLayer.dao.WorkoutDao;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseType;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeMuscleGroupCrossRef;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeWithMuscleGroups;
import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;
import edu.psu.sweng888.activesync.dataAccessLayer.models.Weight;
import edu.psu.sweng888.activesync.dataAccessLayer.models.WeightUnit;
import edu.psu.sweng888.activesync.dataAccessLayer.models.Workout;
import edu.psu.sweng888.activesync.dataAccessLayer.models.WorkoutSet;
import edu.psu.sweng888.activesync.dataAccessLayer.viewModels.WorkoutEntryModel;

@RunWith(AndroidJUnit4.class)
public class WorkoutEntryModelTests {

    private ActiveSyncDatabase db;

    private WorkoutDao workoutDao;

    private User testUser;

    private final List<MuscleGroup> availableMuscleGroups = new ArrayList<>();

    private final List<ExerciseType> availableExerciseTypes = new ArrayList<>();

    @Before
    public void initializeDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ActiveSyncDatabase.class).build();

        // Save DAO references
        workoutDao = db.workoutDao();

        // Seed the database with reference data that's available for use in creating a workout
        // entry model instance.
        MuscleGroupDao muscleGroupDao = db.muscleGroupDao();
        ExerciseTypeDao exerciseTypeDao = db.exerciseTypeDao();

        MuscleGroup biceps = new MuscleGroup(null, "Biceps", 0.01);
        MuscleGroup chest = new MuscleGroup(null, "Chest", 0.01);
        ExerciseType pullUps = new ExerciseType(null, "Pull-ups");

        // Insert the muscle groups first, followed by the exercise type. Link the muscle groups
        // to the exercise type.
        biceps.muscleGroupId = muscleGroupDao.insert(biceps);
        chest.muscleGroupId = muscleGroupDao.insert(chest);
        pullUps.exerciseTypeId = exerciseTypeDao.insert(pullUps);
        exerciseTypeDao.linkMuscleGroupToExerciseType(
            new ExerciseTypeMuscleGroupCrossRef(pullUps, biceps)
        );
        exerciseTypeDao.linkMuscleGroupToExerciseType(
            new ExerciseTypeMuscleGroupCrossRef(pullUps, chest)
        );

        // Add the persisted entries to the lists to make them available for unit tests
        availableMuscleGroups.add(biceps);
        availableMuscleGroups.add(chest);
        availableExerciseTypes.add(pullUps);

        // Create a user for use in tests
        testUser = new User(null, "test-user");
        testUser.userId = db.userDao().insert(testUser);

    }

    @After
    public void disposeDb() throws IOException {
        db.close();
    }

    private static boolean equalByFields(Workout a, Workout b) {
        if (a == null || b == null) return false;
        return a.exerciseTypeId == b.exerciseTypeId
                && a.durationMinutes == b.durationMinutes
                && a.workoutId == b.workoutId
                && a.date.equals(b.date)
                && a.userId == b.userId;
    }

    private static boolean equalByFields(WorkoutSet a, WorkoutSet b) {
        if (a == null || b == null) return false;
        return a.workoutSetId == b.workoutSetId
            && a.workoutId == b.workoutId
            && a.reps == b.reps
            && a.weight.amount == b.weight.amount
            && a.weight.unit == b.weight.unit;
    }

    private static WorkoutSet createTestSet() {
        // Three sets of five reps at 150lbs
        return new WorkoutSet(5, new Weight(WeightUnit.Pounds, 150));
    }

    /**
     * Tests that a workout entry model (the view model containing all relevant information about
     * a single workout) can be created in memory, as it would be as a result of user actions, then
     * persisted to the database and retrieved again.
     */
    @Test
    public void workoutEntryModelRoundtripTest() throws Exception {
        // Create a workout entry model in memory using a pre-defined exercise type and user.
        WorkoutEntryModel workoutEntryModel = new WorkoutEntryModel();
        workoutEntryModel.currentUser = testUser;
        workoutEntryModel.workout = new Workout(
            null,
            null,
            availableExerciseTypes.get(0).exerciseTypeId,
            Date.from(Instant.now()),
            30
        );
        workoutEntryModel.exerciseType = new ExerciseTypeWithMuscleGroups(
            availableExerciseTypes.get(0),
            availableMuscleGroups
        );
        workoutEntryModel.sets.add(createTestSet());
        workoutEntryModel.sets.add(createTestSet());
        workoutEntryModel.sets.add(createTestSet());

        // Persist the workout entry model, which should create any necessary associations.
        workoutEntryModel.persistToDatabase(db);

        // Assert that the model's workout ID has been updated to point to an actual workout record
        // in the database.
        Workout retrievedWorkout = workoutDao.getById(workoutEntryModel.workout.workoutId);
        assertNotNull(retrievedWorkout);
        assertTrue(equalByFields(retrievedWorkout, workoutEntryModel.workout));

        // Assert that we can retrieve the model in full given only a reference to the DB and the
        // workout ID associated with the model.
        WorkoutEntryModel retrievedModel = WorkoutEntryModel.fromDatabaseByWorkoutId(
            db,
            retrievedWorkout.workoutId
        );
        assertNotNull(retrievedModel);

        // Assert that the retrieved model is equivalent to the model we persisted. This proves that
        // our persistence can round-trip properly, as well as the fact that the persistence method
        // takes it upon itself to update the "ID" fields (primary keys) of object references it
        // holds, freeing us from having to manage that ourselves.
        assertTrue(equalByFields(retrievedModel.workout, workoutEntryModel.workout));
        assertEquals(3, retrievedModel.sets.size());
        for (WorkoutSet localSet : workoutEntryModel.sets) {
            assertTrue(retrievedModel.sets.stream().anyMatch(set -> equalByFields(set, localSet)));
        }
        assertTrue(
            equalByFields(
                workoutEntryModel.exerciseType.exerciseType,
                retrievedModel.exerciseType.exerciseType
            )
        );
        for (MuscleGroup localMuscleGroup : workoutEntryModel.exerciseType.muscleGroups) {
            assertTrue(
                retrievedModel.exerciseType.muscleGroups.stream().anyMatch(
                    mg -> equalByFields(mg, localMuscleGroup)
                )
            );
        }
    }

    private static boolean equalByFields(ExerciseType a, ExerciseType b) {
        if (a == null || b == null) return false;
        return a.exerciseTypeId == b.exerciseTypeId
            && a.name.equals(b.name);
    }

    private static boolean equalByFields(MuscleGroup a, MuscleGroup b) {
        if (a == null || b == null) return false;
        return a.name.equals(b.name)
            && a.recoveryRate == b.recoveryRate
            && a.muscleGroupId == b.muscleGroupId;
    }
}
