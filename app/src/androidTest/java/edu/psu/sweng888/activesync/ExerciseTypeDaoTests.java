package edu.psu.sweng888.activesync;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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
import java.util.Arrays;

import edu.psu.sweng888.activesync.dataAccessLayer.dao.ExerciseTypeDao;
import edu.psu.sweng888.activesync.dataAccessLayer.dao.MuscleGroupDao;
import edu.psu.sweng888.activesync.dataAccessLayer.dao.WorkoutDao;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseType;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeMuscleGroupCrossRef;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeWithMuscleGroups;
import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;

@RunWith(AndroidJUnit4.class)
public class ExerciseTypeDaoTests {

    private ActiveSyncDatabase db;
    private ExerciseTypeDao exerciseTypeDao;
    private MuscleGroupDao muscleGroupDao;

    @Before
    public void initializeDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ActiveSyncDatabase.class).build();
        exerciseTypeDao = db.exerciseTypeDao();
        muscleGroupDao = db.muscleGroupDao();
    }

    @After
    public void disposeDb() throws IOException {
        db.close();
    }

    private static void assertRetrievedMatchesLocal(MuscleGroup retrieved, MuscleGroup local) {
        assertNotNull(retrieved);
        assertNotNull(local);
        assertEquals(retrieved.muscleGroupId, local.muscleGroupId);
        assertEquals(retrieved.name, local.name);
        assertEquals(retrieved.recoveryRate, local.recoveryRate);
    }

    private static void assertRetrievedMatchesLocal(ExerciseType retrieved, ExerciseType local) {
        assertNotNull(retrieved);
        assertNotNull(local);
        assertEquals(retrieved.exerciseTypeId, local.exerciseTypeId);
        assertEquals(retrieved.name, local.name);
    }

    private static boolean equalByFields(MuscleGroup a, MuscleGroup b) {
        if (a == null || b == null) return false;
        return a.name.equals(b.name)
            && a.muscleGroupId == b.muscleGroupId
            && a.recoveryRate == b.recoveryRate;
    }

    /**
     * Tests that an exercise type and its related muscle groups (and the records relating the two
     * sets of data) can be created separated and then retrieved as a relational group.
     */
    @Test
    public void exerciseTypeWithMuscleGroupsRoundTripTest() {
        // Create an object model for an exercise type called "pull-ups" and associate it with the
        // "biceps" and "chest" muscle groups. None of these records exist in the database yet.
        MuscleGroup biceps = new MuscleGroup(null, "Biceps", 0.01);
        MuscleGroup chest = new MuscleGroup(null, "Chest", 0.01);
        ExerciseType pullUps = new ExerciseType(null, "Pull-ups");

        // Create a muscle group that is not related to pull-ups
        MuscleGroup unrelatedGroup = new MuscleGroup(null, "foo", 1.0);

        // Insert the muscle groups first, followed by the exercise type.
        biceps.muscleGroupId = muscleGroupDao.insert(biceps);
        chest.muscleGroupId = muscleGroupDao.insert(chest);
        unrelatedGroup.muscleGroupId = muscleGroupDao.insert(unrelatedGroup);
        pullUps.exerciseTypeId = exerciseTypeDao.insert(pullUps);

        // Assert that the records were inserted successfully
        assertRetrievedMatchesLocal(
            muscleGroupDao.getById(biceps.muscleGroupId),
            biceps
        );

        assertRetrievedMatchesLocal(
            muscleGroupDao.getById(chest.muscleGroupId),
            chest
        );

        assertRetrievedMatchesLocal(
            muscleGroupDao.getById(unrelatedGroup.muscleGroupId),
            unrelatedGroup
        );

        assertRetrievedMatchesLocal(
            exerciseTypeDao.getById(pullUps.exerciseTypeId),
            pullUps
        );

        // Link the two newly-persisted muscle groups to the "pull-ups" exercise type
        exerciseTypeDao.linkMuscleGroupToExerciseType(
            new ExerciseTypeMuscleGroupCrossRef(pullUps, biceps)
        );
        exerciseTypeDao.linkMuscleGroupToExerciseType(
            new ExerciseTypeMuscleGroupCrossRef(pullUps, chest)
        );

        // Retrieve a relation of the "pull-ups" exercise to its associated muscle groups.
        ExerciseTypeWithMuscleGroups pullUpsWithGroups = exerciseTypeDao
            .getExerciseTypeWithMuscleGroups(pullUps.exerciseTypeId);

        // Make sure the relation record exists and that it has only "biceps" and "chest" groups
        // in the list of associated muscle groups.
        assertNotNull(pullUpsWithGroups);
        assertRetrievedMatchesLocal(pullUpsWithGroups.exerciseType, pullUps);
        assertEquals(2, pullUpsWithGroups.muscleGroups.size());
        assertTrue(pullUpsWithGroups.muscleGroups.stream().anyMatch(mg -> equalByFields(biceps, mg)));
        assertTrue(pullUpsWithGroups.muscleGroups.stream().anyMatch(mg -> equalByFields(chest, mg)));
        assertFalse(pullUpsWithGroups.muscleGroups.stream().anyMatch(mg -> equalByFields(unrelatedGroup, mg)));
    }
}
