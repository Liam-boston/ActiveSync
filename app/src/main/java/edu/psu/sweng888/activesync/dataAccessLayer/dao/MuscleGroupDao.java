package edu.psu.sweng888.activesync.dataAccessLayer.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;

@Dao
public interface MuscleGroupDao {

    @Insert
    void insert(MuscleGroup muscleGroup);

    @Update
    void update(MuscleGroup muscleGroup);

    @Delete
    void delete(MuscleGroup muscleGroup);
}
