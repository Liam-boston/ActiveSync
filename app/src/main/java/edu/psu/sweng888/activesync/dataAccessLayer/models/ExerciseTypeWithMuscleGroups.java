package edu.psu.sweng888.activesync.dataAccessLayer.models;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExerciseTypeWithMuscleGroups implements Serializable {
    @Embedded
    public ExerciseType exerciseType;
    @Relation(
            parentColumn = "exercise_type_id",
            entityColumn = "muscle_group_id",
            associateBy = @Junction(ExerciseTypeMuscleGroupCrossRef.class)
    )
    public List<MuscleGroup> muscleGroups;

    public ExerciseTypeWithMuscleGroups() {
        exerciseType = new ExerciseType();
        muscleGroups = new ArrayList<>();
    }

    public ExerciseTypeWithMuscleGroups(ExerciseType type, List<MuscleGroup> muscleGroups) {
        this.exerciseType = type;
        this.muscleGroups = muscleGroups;
    }

    @Override
    public String toString() {
        return this.exerciseType.toString();
    }
}
