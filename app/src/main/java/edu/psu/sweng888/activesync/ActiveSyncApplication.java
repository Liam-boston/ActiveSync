package edu.psu.sweng888.activesync;

import android.app.Application;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import edu.psu.sweng888.activesync.dataAccessLayer.cannedData.DefaultExerciseTypes;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;

public class ActiveSyncApplication extends Application {

    // TODO: Figure out dependency injection in Android
    private static ActiveSyncDatabase database;

    public ActiveSyncApplication () {}

    public static ActiveSyncDatabase getDatabase() {
        return database;
    }

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
        DefaultExerciseTypes.initialize(database);
    }
}
