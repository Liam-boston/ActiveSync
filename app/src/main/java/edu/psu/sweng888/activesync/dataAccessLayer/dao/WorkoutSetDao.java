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
public abstract class WorkoutSetDao extends AbstractUpsertingDao<WorkoutSet> {

    public long getPrimaryKey(WorkoutSet workoutSet) {
        return workoutSet.workoutSetId;
    }

    @Insert
    @Override
    public abstract long insert(WorkoutSet workoutSet);

    @Update
    @Override
    public abstract int update(WorkoutSet workoutSet);

    @Delete
    public abstract void delete(WorkoutSet workoutSet);

    @Query("SELECT * FROM workoutSet WHERE workout_id = :workoutId")
    public abstract List<WorkoutSet> getSetsForWorkout(long workoutId);

    @Query("DELETE FROM workoutset")
    public abstract void wipe();


    /**
     * Deletes the sets associated with the given workout. Used to ensure that upserted
     * WorkoutEntryModel view models always have the most up-to-date set information since a user
     * can delete sets locally before syncing with the database.
     */
    @Query("DELETE FROM workoutset WHERE workout_id = :workoutId")
    public abstract int deleteSetsForWorkoutId(long workoutId);
}
