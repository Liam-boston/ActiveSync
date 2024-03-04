package edu.psu.sweng888.activesync;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Pair;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import edu.psu.sweng888.activesync.dataAccessLayer.cannedData.DefaultExerciseTypes;
import edu.psu.sweng888.activesync.dataAccessLayer.dao.ExerciseTypeDao;
import edu.psu.sweng888.activesync.dataAccessLayer.dao.MuscleGroupDao;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseType;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeMuscleGroupCrossRef;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeWithMuscleGroups;
import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;
import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class DefaultDatabaseEntryTests {

    private ActiveSyncDatabase db;
    @Before
    public void initializeDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ActiveSyncDatabase.class).build();
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

    private static boolean equalByFields(ExerciseType a, ExerciseType b) {
        if (a == null || b == null) return false;
        return a.exerciseTypeId == b.exerciseTypeId
            && a.name.equals(b.name);
    }

    private static boolean equalByFields(MuscleGroup a, MuscleGroup b) {
        if (a == null || b == null) return false;
        return a.name.equals(b.name)
            && a.muscleGroupId == b.muscleGroupId
            && a.recoveryRate == b.recoveryRate;
    }

    /**
     * Tests that the code used to initialize the default exercise types (and implicitly, their
     * associated muscle groups) works as intended as is idempotent, meaning it can be executed
     * many times without changing the result.
     */
    @Test
    public void defaultExerciseTypeInitializationIsIdempotent() {

        // Perform the initialization logic several times in succession
        DefaultExerciseTypes.initialize(db);
        DefaultExerciseTypes.initialize(db);

        // Get the expected data formatted as ExerciseTypeWithMuscleGroup objects
        List<ExerciseTypeWithMuscleGroups> expectedData = new ArrayList<>();
        for (Pair<ExerciseType, MuscleGroup[]> typeWithGroupsPair : DefaultExerciseTypes.getDefaultExerciseTypesWithAssociatedMuscleGroups()) {
            expectedData.add(
                new ExerciseTypeWithMuscleGroups(
                    typeWithGroupsPair.first,
                    Arrays.asList(typeWithGroupsPair.second)
                )
            );
        }

        // Assert that there are only a single set of created objects that match the content
        // described by the default exercise types file.
        List<ExerciseTypeWithMuscleGroups> typesWithGroups = db.exerciseTypeDao()
            .getExerciseTypesWithMuscleGroups();

        assertEquals(expectedData.size(), typesWithGroups.size());
        for (ExerciseTypeWithMuscleGroups expectedDatum : expectedData) {
            assertTrue(typesWithGroups.stream().anyMatch(x -> {
                    boolean exerciseTypeMatch = equalByFields(expectedDatum.exerciseType, x.exerciseType);
                    boolean allMuscleGroupsMatch = true;
                    for (MuscleGroup mg : x.muscleGroups) {
                        allMuscleGroupsMatch = allMuscleGroupsMatch && expectedDatum.muscleGroups.stream().anyMatch(y -> equalByFields(y, mg));
                    }
                    return allMuscleGroupsMatch;
                })
            );
        }
    }

}
