package edu.psu.sweng888.activesync.dataAccessLayer.models;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Represents a single set in a workout.
 */
@Entity
public class WorkoutSet {

    /**
     * The unique identifier of this workout set.
     */
    @PrimaryKey
    @ColumnInfo(name = "workout_set_id")
    public long workoutSetId;

    /**
     * The ID (foreign key) of the workout entity with which this set is associated.
     */
    @ColumnInfo(name = "workout_id")
    public long workoutId;

    /**
     * The number of repetitions of the exercise associated with this set.
     */
    public int reps;

    /**
     * The weight associated with this set (e.g., 40 kg).
     */
    @Embedded
    public Weight weight;
}
