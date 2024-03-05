package edu.psu.sweng888.activesync.dataAccessLayer.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Upsert;

import java.util.List;
import java.util.Map;

import edu.psu.sweng888.activesync.dataAccessLayer.models.Workout;
import edu.psu.sweng888.activesync.dataAccessLayer.models.WorkoutSet;

/**
 * Data access object interface that exposes operations available for the "workout" record type.
 */
@Dao
public interface WorkoutDao {

    /**
     * Inserts the given workout record into the database.
     * @param workout The workout record to insert.
     */
    @Insert
    void insert(Workout workout);

    @Upsert
    long upsert(Workout workout);

    /**
     * Selects and returns a specific workout record by its ID.
     * @param workoutId The ID of the workout record to retrieve.
     * @return The workout record associated with the given ID.
     */
    @Query("SELECT * FROM workout WHERE workout_id = :workoutId")
    Workout getById(long workoutId);

    /**
     * Returns the workouts created by the user with the given user ID.
     * @param userId The user ID of the user for which workouts should be retrieved.
     * @return A list of workouts created by the given user.
     */
    @Query("SELECT * FROM workout WHERE user_id = :userId")
    List<Workout> getWorkoutsForUser(long userId);

    @Query("SELECT workout_id FROM workout WHERE user_id = :userId")
    long[] getWorkoutIdsForUser(long userId);

    @Query("DELETE FROM workout")
    void wipe();
}
