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
public interface UserDao {

    /**
     * Inserts a new user record into the database.
     * @param user The user record to insert.
     * @return The auto-generated value of inserted record's "userId" column.
     */
    @Insert
    long insert(User user);

    /**
     * Ensures the record associated with the given user exists in the database. Use this method
     * to seed the database with default and/or test data.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long ensureInserted(User user);

    /**
     * Upserts a user record into the database.
     * @param user The user record to upsert.
     * @return The value of the record's "userId" column after the operation is complete.
     */
    @Upsert
    long upsert(User user);

    /**
     * Inserts an arbitrary number of user records into the database.
     * @param users The user records to insert.
     */
    @Insert
    long[] insertAll(User... users);

    /**
     * Deletes the given user record from the database.
     * @param user The user record to delete.
     */
    @Delete
    void Delete(User user);

    /**
     * Updates the given user record in the database.
     * @param user The user record to update.
     */
    @Update
    void update(User user);

    /**
     * Queries the database for a list of all user records.
     * @return A list containing all existing user records.
     */
    @Query("SELECT * FROM user")
    List<User> getAll();

    /**
     * Queries the database for a user having a specific ID.
     * @param userId The ID of the user to select.
     * @return The user associated with the given user ID.
     */
    @Query("SELECT * FROM user WHERE user.user_id = :userId")
    User getById(long userId);
}
