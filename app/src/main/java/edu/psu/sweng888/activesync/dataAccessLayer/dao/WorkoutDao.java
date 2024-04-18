package edu.psu.sweng888.activesync.dataAccessLayer.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.Date;
import java.util.List;

import edu.psu.sweng888.activesync.dataAccessLayer.models.Workout;

/**
 * Data access object interface that exposes operations available for the "workout" record type.
 */
@Dao
public abstract class WorkoutDao extends AbstractUpsertingDao<Workout> {

    public long getPrimaryKey(Workout workout) {
        return workout.workoutId;
    }

    /**
     * Inserts the given workout record into the database.
     * @param workout The workout record to insert.
     */
    @Insert
    @Override
    public abstract long insert(Workout workout);

    @Update
    @Override
    public abstract int update(Workout workout);

    /**
     * Selects and returns a specific workout record by its ID.
     * @param workoutId The ID of the workout record to retrieve.
     * @return The workout record associated with the given ID.
     */
    @Query("SELECT * FROM workout WHERE workout_id = :workoutId")
    public abstract Workout getById(long workoutId);

    /**
     * Returns the workouts created by the user with the given user ID.
     * @param userId The user ID of the user for which workouts should be retrieved.
     * @return A list of workouts created by the given user.
     */
    @Query("SELECT * FROM workout WHERE user_id = :userId")
    public abstract List<Workout> getWorkoutsForUser(long userId);

    @Query("SELECT workout_id FROM workout WHERE user_id = :userId")
    public abstract long[] getWorkoutIdsForUser(long userId);
        //'2016-10-09'

    /*
        - tried writing query to return workouts within past three days, but everytime
         I compare and format the date from the record it comes back null
    */
    @Query("SELECT * FROM workout WHERE DATE(date) >= DATE('now','-3 days') and :userId = user_id")
    public abstract List<Workout> getWorkoutsFromRange(long userId);
    @Query("DELETE FROM workout")
    public abstract void wipe();
}
