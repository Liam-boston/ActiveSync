package edu.psu.sweng888.activesync.dataAccessLayer.dao;

import android.content.ContentProviderClient;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import java.util.List;

import edu.psu.sweng888.activesync.dataAccessLayer.models.User;

/**
 * Data access object for the "User" model.
 */
@Dao
public abstract class UserDao {

    /**
     * Inserts a new user record into the database.
     * @param user The user record to insert.
     * @return The auto-generated value of inserted record's "userId" column.
     */
    @Insert
    public abstract long insert(User user);

    /**
     * Ensures the record associated with the given user exists in the database. Use this method
     * to seed the database with default and/or test data.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long ensureInserted(User user);

    /**
     * Upserts a user record into the database.
     * @param user The user record to upsert.
     * @return The value of the record's "userId" column after the operation is complete.
     */
    @Upsert
    public abstract long upsert(User user);

    /**
     * Inserts an arbitrary number of user records into the database.
     * @param users The user records to insert.
     */
    @Insert
    public abstract long[] insertAll(User... users);

    /**
     * Deletes the given user record from the database.
     * @param user The user record to delete.
     */
    @Delete
    public abstract void delete(User user);

    /**
     * Updates the given user record in the database.
     * @param user The user record to update.
     */
    @Update
    public abstract void update(User user);

    /**
     * Queries the database for a list of all user records.
     * @return A list containing all existing user records.
     */
    @Query("SELECT * FROM user")
    public abstract List<User> getAll();

    /**
     * Queries the database for a user having a specific ID.
     * @param userId The ID of the user to select.
     * @return The user associated with the given user ID.
     */
    @Query("SELECT * FROM user WHERE user.user_id = :userId")
    public abstract User getById(long userId);

    @Query("DELETE FROM user")
    public abstract void wipe();

    @Query("SELECT * FROM user WHERE user.name = :name LIMIT 1") // TODO: Remove the LIMIT 1 once there is a UNIQUE constraint on the name column.
    public abstract User getByName(String name);

    public User createOrReturnForFirebaseDisplayName(String displayName) {
        // Return the existing user if found
        User existingUser = getByName(displayName);
        if (existingUser != null) return existingUser;

        // Otherwise, return the newly created user
        User createdUser = new User(null, displayName);
        createdUser.userId = insert(createdUser);
        return createdUser;
    }
}
