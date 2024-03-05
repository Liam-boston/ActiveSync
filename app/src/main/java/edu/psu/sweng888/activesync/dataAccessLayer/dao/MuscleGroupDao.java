package edu.psu.sweng888.activesync.dataAccessLayer.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;

@Dao
public interface MuscleGroupDao {

    @Insert
    long insert(MuscleGroup muscleGroup);

    /**
     * An insert that does nothing if a record already exists with the same primary key. This can
     * be used to seed the database with "default" muscle groups at startup. See the class
     * "DefaultMuscleGroups" as an example.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long ensureInserted(MuscleGroup muscleGroup);

    @Upsert
    long upsert(MuscleGroup muscleGroup);

    @Delete
    int delete(MuscleGroup muscleGroup);

    @Query("SELECT * FROM musclegroup")
    List<MuscleGroup> getAll();

    @Query("SELECT * FROM musclegroup WHERE muscle_group_id = :muscleGroupId")
    MuscleGroup getById(long muscleGroupId);

    @Query("DELETE FROM musclegroup")
    void wipe();
}
