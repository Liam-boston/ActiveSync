package edu.psu.sweng888.activesync.dataAccessLayer.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;

@Dao
public interface MuscleGroupDao {

    @Insert
    long insert(MuscleGroup muscleGroup);

    @Upsert
    long upsert(MuscleGroup muscleGroup);

    @Delete
    int delete(MuscleGroup muscleGroup);

    @Query("SELECT * FROM musclegroup")
    List<MuscleGroup> getAll();

    @Query("SELECT * FROM musclegroup WHERE muscle_group_id = :muscleGroupId")
    MuscleGroup getById(long muscleGroupId);
}
