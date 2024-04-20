package edu.psu.sweng888.activesync;

import android.app.Application;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import edu.psu.sweng888.activesync.dataAccessLayer.cannedData.DefaultExerciseTypes;
import edu.psu.sweng888.activesync.dataAccessLayer.cannedData.DefaultUsers;
import edu.psu.sweng888.activesync.dataAccessLayer.cannedData.DefaultWorkouts;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;

public class ActiveSyncApplication extends Application {

    // TODO: Figure out dependency injection in Android
    private static ActiveSyncDatabase database;

    public ActiveSyncApplication () {}

    public static ActiveSyncDatabase getDatabase() {
        return database;
    }

    private static User activeUser;

    public static final DateFormat YearMonthDayDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(
                getApplicationContext(),
                ActiveSyncDatabase.class,
                "activesync.db"
            )
            .allowMainThreadQueries() // TODO: Remove! Figure out how to make insertions happen on background thread.
            .build();
        initializeDatabase();
    }

    public static void initializeDatabase() {
        DefaultUsers.initialize(database);
        DefaultExerciseTypes.initialize(database);
        DefaultWorkouts.initialize(database);
    }

    public static void reinitializeDatabase() {
        ActiveSyncDatabase db = ActiveSyncApplication.getDatabase();
        db.workoutSetDao().wipe();
        db.workoutDao().wipe();
        db.exerciseTypeDao().wipeExerciseTypeMuscleGroupLinks();
        db.exerciseTypeDao().wipeExerciseTypes();
        db.muscleGroupDao().wipe();
        db.userDao().wipe();
        ActiveSyncApplication.initializeDatabase();
    }

    public static User getActiveUser() { return activeUser; }
    public static void setActiveUser(User activeUser) { ActiveSyncApplication.activeUser = activeUser;}
}
