package edu.psu.sweng888.activesync.dataAccessLayer.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseType;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeMuscleGroupCrossRef;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseTypeWithMuscleGroups;

@Dao
public interface ExerciseTypeDao {

    @Insert
    long insert(ExerciseType exerciseType);

    @Upsert
    long upsert(ExerciseType exerciseType);

    @Delete
    int delete(ExerciseType exerciseType);

    @Query("SELECT * FROM exercisetype WHERE exercise_type_id = :exerciseTypeId")
    ExerciseType getById(long exerciseTypeId);

    @Transaction
    @Query("SELECT * FROM exercisetype")
    List<ExerciseTypeWithMuscleGroups> getExerciseTypesWithMuscleGroups();

    @Transaction
    @Query("SELECT * FROM exercisetype WHERE exercise_type_id = :exerciseTypeId LIMIT 1")
    ExerciseTypeWithMuscleGroups getExerciseTypeWithMuscleGroups(long exerciseTypeId);

    /**
     * Creates a linkage between an exercise type and a muscle group.
     * @param crossReference The cross reference between the exercise type and muscle group, which contains the primary keys of the items that should be linked to each other.
     */
    @Upsert
    void linkMuscleGroupToExerciseType(ExerciseTypeMuscleGroupCrossRef crossReference);
}
