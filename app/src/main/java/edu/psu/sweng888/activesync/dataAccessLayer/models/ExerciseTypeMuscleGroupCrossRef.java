package edu.psu.sweng888.activesync.dataAccessLayer.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = { "exercise_type_id", "muscle_group_id" })
public class ExerciseTypeMuscleGroupCrossRef {
    @ColumnInfo(name = "exercise_type_id")
    public long exerciseTypeId;
    @ColumnInfo(name = "muscle_group_id")
    public long muscleGroupId;
}
