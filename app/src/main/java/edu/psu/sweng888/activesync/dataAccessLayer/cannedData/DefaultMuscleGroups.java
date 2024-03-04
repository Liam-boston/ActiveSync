package edu.psu.sweng888.activesync.dataAccessLayer.cannedData;

import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;

/**
 * A configuration-as-code list of all the default muscle groups available at app startup.
 */
public final class DefaultMuscleGroups {

    // Prevent instantiation
    private DefaultMuscleGroups() {}

    // NOTE: When defining a new default muscle group, be sure to add it to this list!
    public static MuscleGroup[] getAllDefaultMuscleGroups(){
        return new MuscleGroup[] {
            Biceps,
            Chest
        };
    }

    public static final MuscleGroup Biceps = new MuscleGroup(1l, "Biceps", 0.05);

    public static final MuscleGroup Chest = new MuscleGroup(2l, "Chest", 0.05);

    // NOTE: This class does not have an "initialize" method like the "DefaultExerciseTypes" class
    //       because the aforementioned class's "initialize" method creates entries in the database
    //       for items from this class so long as they are linked to an exercise type.
}
