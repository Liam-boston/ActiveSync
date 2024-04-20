package edu.psu.sweng888.activesync.dataAccessLayer.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents the main data unit of the application -- a single workout session, which is comprised
 * of one or more sets (WorkoutSet) of a particular exercise (ExerciseType).
 */
@Entity
public class Workout implements Serializable {

    /**
     * The unique identifier for this workout.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_id")
    public long workoutId;

    /**
     * The ID (foreign key) of the user that created this workout.
     */
    @ColumnInfo(name = "user_id")
    public long userId;

    /**
     * The ID (foreign key) of the ExerciseType instance associated with this workout.
     */
    @ColumnInfo(name = "exercise_type_id")
    public long exerciseTypeId;

    /**
     * The date and time on which this workout took place.
     */
    public Date date;

    /**
     * The duration of this workout, expressed in minutes.
     */
    @ColumnInfo(name = "duration_minutes")
    public int durationMinutes;

    public Workout() {}

    public Workout(
            Long workoutId,
            Long userId,
            Long exerciseTypeId,
            Date date,
            Integer durationMinutes
    ) {
        if (workoutId != null) this.workoutId = workoutId;
        if (userId != null) this.userId = userId;
        if (exerciseTypeId != null) this.exerciseTypeId = exerciseTypeId;
        if (date != null) this.date = date;
        if (durationMinutes != null) this.durationMinutes = durationMinutes;
    }

}
