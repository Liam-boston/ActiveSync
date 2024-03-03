package edu.psu.sweng888.activesync.dataAccessLayer.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a particular type of exercise that can be done in a workout, e.g. "pull-ups".
 */
@Entity
public class ExerciseType {

    /**
     * The unique identifier of this exercise type.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_type_id")
    public long exerciseTypeId;

    /**
     * The name of this exercise, e.g. "pull-ups".
     */
    public String name;

    public ExerciseType() {}

    public ExerciseType(Long id, String name) {
        if (id != null) this.exerciseTypeId = id;
        if (name != null) this.name = name;
    }
}
