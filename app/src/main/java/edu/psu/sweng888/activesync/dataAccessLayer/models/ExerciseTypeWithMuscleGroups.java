package edu.psu.sweng888.activesync.dataAccessLayer.models;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class ExerciseTypeWithMuscleGroups {
    @Embedded
    public ExerciseType exerciseType;
    @Relation(
            parentColumn = "exercise_type_id",
            entityColumn = "muscle_group_id",
            associateBy = @Junction(ExerciseTypeMuscleGroupCrossRef.class)
    )
    public List<MuscleGroup> muscleGroups;

    public ExerciseTypeWithMuscleGroups() {}

    public ExerciseTypeWithMuscleGroups(ExerciseType type, List<MuscleGroup> muscleGroups) {
        this.exerciseType = type;
        this.muscleGroups = muscleGroups;
    }
}
