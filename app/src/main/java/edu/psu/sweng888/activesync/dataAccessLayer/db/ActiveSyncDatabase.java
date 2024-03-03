package edu.psu.sweng888.activesync.dataAccessLayer.db;

import edu.psu.sweng888.activesync.dataAccessLayer.converters.DateConverter;
import edu.psu.sweng888.activesync.dataAccessLayer.models.*;
import edu.psu.sweng888.activesync.dataAccessLayer.dao.*;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
    version = 1,
    entities = {
        User.class,
        Workout.class,
        WorkoutSet.class,
        MuscleGroup.class,
        ExerciseType.class,
        ExerciseTypeMuscleGroupCrossRef.class
    }
)
@TypeConverters({
    DateConverter.class
})
public abstract class ActiveSyncDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract WorkoutDao workoutDao();
    public abstract WorkoutSetDao workoutSetDao();
    public abstract ExerciseTypeDao exerciseTypeDao();
    public abstract MuscleGroupDao muscleGroupDao();
}
