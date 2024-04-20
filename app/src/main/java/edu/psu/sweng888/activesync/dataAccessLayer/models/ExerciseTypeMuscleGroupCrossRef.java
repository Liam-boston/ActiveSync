package edu.psu.sweng888.activesync.dataAccessLayer.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;

@Entity(primaryKeys = { "exercise_type_id", "muscle_group_id" })
public class ExerciseTypeMuscleGroupCrossRef implements Serializable {
    @ColumnInfo(name = "exercise_type_id")
    public long exerciseTypeId;
    @ColumnInfo(name = "muscle_group_id")
    public long muscleGroupId;

    public ExerciseTypeMuscleGroupCrossRef () {}


    public ExerciseTypeMuscleGroupCrossRef (ExerciseType exerciseType, MuscleGroup muscleGroup) {
        this.exerciseTypeId = exerciseType.exerciseTypeId;
        this.muscleGroupId = muscleGroup.muscleGroupId;
    }
}
