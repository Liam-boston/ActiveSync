package edu.psu.sweng888.activesync.dataAccessLayer.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

import edu.psu.sweng888.activesync.dataAccessLayer.models.WorkoutSet;

/**
 * Data access object interface that exposes operations on the "WorkoutSet" record type.
 */
@Dao
public interface WorkoutSetDao {

    @Insert
    void insert(WorkoutSet workoutSet);

    @Update
    void update(WorkoutSet workoutSet);

    @Upsert
    long upsert(WorkoutSet workoutSet);

    @Delete
    void delete(WorkoutSet workoutSet);

    @Query("SELECT * FROM workoutSet WHERE workout_id = :workoutId")
    List<WorkoutSet> getSetsForWorkout(long workoutId);

    @Query("DELETE FROM workoutset")
    void wipe();
}
