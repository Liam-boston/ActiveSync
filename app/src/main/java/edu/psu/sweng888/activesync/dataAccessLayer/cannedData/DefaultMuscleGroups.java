package edu.psu.sweng888.activesync.dataAccessLayer.cannedData;

import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;

/**
 * A configuration-as-code list of all the default muscle groups available at app startup.
 */
public final class DefaultMuscleGroups {
    public static final MuscleGroup Biceps = new MuscleGroup(1l, "Biceps", 0.45);
    public static final MuscleGroup Chest = new MuscleGroup(2l, "Chest", 0.20);
    public static final MuscleGroup Back = new MuscleGroup(3l, "Back", 0.20);
    public static final MuscleGroup Shoulders = new MuscleGroup(4l, "Shoulders", 0.33);
    public static final MuscleGroup Triceps = new MuscleGroup(5l, "Triceps", 0.45);
    public static final MuscleGroup Quadriceps = new MuscleGroup(6l, "Quadriceps", 0.15);
    public static final MuscleGroup Hamstrings = new MuscleGroup(7l, "Hamstrings", 0.15);
    public static final MuscleGroup Glutes = new MuscleGroup(8l, "Glutes", 0.50);

    // Prevent instantiation
    private DefaultMuscleGroups() {
    }

    // NOTE: When defining a new default muscle group, be sure to add it to this list!
    public static MuscleGroup[] getAllDefaultMuscleGroups() {
        return new MuscleGroup[]{
                Biceps,
                Chest,
                Back,
                Shoulders,
                Triceps,
                Quadriceps,
                Hamstrings,
                Glutes
        };
    }
    
    // NOTE: This class does not have an "initialize" method like the "DefaultExerciseTypes" class
    //       because the aforementioned class's "initialize" method creates entries in the database
    //       for items from this class so long as they are linked to an exercise type.
}
