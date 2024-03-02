package edu.psu.sweng888.activesync.dataAccessLayer.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseType;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeWithMuscleGroups;

@Dao
public interface ExerciseTypeDao {

    @Insert
    void insert(ExerciseType exerciseType);

    @Update
    void update(ExerciseType exerciseType);

    @Delete
    void delete(ExerciseType exerciseType);

    @Transaction
    @Query("SELECT * FROM exercisetype")
    List<ExerciseTypeWithMuscleGroups> getExerciseTypesWithMuscleGroups();

    @Transaction
    @Query("SELECT * FROM exercisetype WHERE exercise_type_id = :exerciseTypeId LIMIT 1")
    ExerciseTypeWithMuscleGroups getExerciseTypeWithMuscleGroups(long exerciseTypeId);
}
