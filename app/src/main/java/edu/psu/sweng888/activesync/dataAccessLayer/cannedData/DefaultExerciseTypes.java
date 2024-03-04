package edu.psu.sweng888.activesync.dataAccessLayer.cannedData;

import android.util.Pair;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.psu.sweng888.activesync.dataAccessLayer.dao.ExerciseTypeDao;
import edu.psu.sweng888.activesync.dataAccessLayer.dao.MuscleGroupDao;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseType;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeMuscleGroupCrossRef;
import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;

/**
 * A configuration-as-code list of all the default exercise types available at app startup.
 */
public class DefaultExerciseTypes {

    // Prevent instantiation
    private DefaultExerciseTypes() {}

    // =============================================================================================
    // ====== EDIT THIS AREA ONLY TO ADD NEW EXERCISE TYPES ========================================
    // =============================================================================================
    public static final ExerciseType PullUps = new ExerciseType(1l, "Pull-ups");

    public static final ExerciseType PushUps = new ExerciseType(2l, "Push-ups");

    // NOTE: When adding a new default exercise type, register it in the method call below with the
    //       muscle groups that it targets!
    public static List<Pair<ExerciseType, MuscleGroup[]>> getDefaultExerciseTypesWithAssociatedMuscleGroups() {
        return registerDefaults(
            exerciseTypeWithGroups(
                PullUps,
                DefaultMuscleGroups.Biceps,
                DefaultMuscleGroups.Chest
            ),
            exerciseTypeWithGroups(
                PushUps,
                DefaultMuscleGroups.Chest,
                DefaultMuscleGroups.Back
            )
        );
    }
    // =============================================================================================
    // =============================================================================================
    // =============================================================================================


    private static List<Pair<ExerciseType, MuscleGroup[]>> registerDefaults(
        Pair<ExerciseType, MuscleGroup[]>... associations
    ) {
        List<Pair<ExerciseType, MuscleGroup[]>> defaults = new ArrayList<>();
        for (Pair<ExerciseType, MuscleGroup[]> association : associations) {
            defaults.add(association);
        }
        return defaults;
    }

    private static Pair<ExerciseType, MuscleGroup[]> exerciseTypeWithGroups(
        ExerciseType exerciseType,
        MuscleGroup... groups
    ) {
        return new Pair<>(exerciseType, groups);
    }


    public static void initialize(ActiveSyncDatabase db) {
        // TODO: Should we try to make sure this method is only called once per application run?
        //       That may be premature optimization since we're using an insert statement that
        //       ignores conflicts, but I'm not sure if there are other problems I'm unaware of.
        List<Pair<ExerciseType, MuscleGroup[]>>defaults =
            getDefaultExerciseTypesWithAssociatedMuscleGroups();

        HashSet<Long> registeredMuscleGroupIds = new HashSet<>(); // Keep track of what we've inserted so we don't double insert. Not a huge problem, but also easy to avoid.
        ExerciseTypeDao exerciseTypeDao = db.exerciseTypeDao();
        MuscleGroupDao muscleGroupDao = db.muscleGroupDao();
        for (Pair<ExerciseType, MuscleGroup[]> exerciseTypeAndGroupsPair : defaults) {
            // Pull items out of the pair
            ExerciseType exerciseType = exerciseTypeAndGroupsPair.first;
            MuscleGroup[] associatedMuscleGroups = exerciseTypeAndGroupsPair.second;

            // Ensure the exercise type exists in the database.
            exerciseTypeDao.ensureInserted(exerciseType);

            // Register entries for the muscle groups associated with the exercise first (if we
            // haven't already done so). Create a link between each muscle group and the exercise
            // type we added.
            for (MuscleGroup muscleGroup : associatedMuscleGroups) {
                muscleGroupDao.ensureInserted(muscleGroup);
                exerciseTypeDao.ensureMuscleGroupLinkedToExerciseType(
                    new ExerciseTypeMuscleGroupCrossRef(
                        exerciseType,
                        muscleGroup
                    )
                );
            }

        }
    }
}
