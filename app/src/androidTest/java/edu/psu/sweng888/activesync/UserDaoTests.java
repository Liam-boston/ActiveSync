package edu.psu.sweng888.activesync;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import edu.psu.sweng888.activesync.dataAccessLayer.dao.UserDao;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;

@RunWith(AndroidJUnit4.class)
public class UserDaoTests {

    private ActiveSyncDatabase db;
    private UserDao userDao;

    @Before
    public void initializeDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ActiveSyncDatabase.class).build();
        userDao = db.userDao();
    }

    @After
    public void disposeDb() throws IOException {
        db.close();
    }

    /**
     * Asserts that a user record retrieved from the DB matches the fields of a local copy.
     * @param retrieved The user record as retrieved from the database.
     * @param local The local copy of the user record.
     */
    private void assertRetrievedMatchesLocal(User retrieved, User local) {
        assertNotNull(retrieved);
        assertNotNull(local);
        assertEquals(retrieved.userId, local.userId);
        assertEquals(retrieved.name, local.name);
    }

    /**
     * Tests the ability to create a user from an in-memory model and retrieve that same user.
     */
    @Test
    public void createAndRetrieveUsers() {
        // Create a few user records to persist
        User[] usersToPersist = {
            new User(null, "foo"),
            new User(null, "bar"),
            new User(null, "baz"),
        };

        // Persist the records via the user DAO
        for (User user : usersToPersist) {
            user.userId = userDao.insert(user);
        }

        // Retrieve the user records and ensure there are 1:1 matches with the users we inserted
        for (User persistedUser : usersToPersist) {
            User retrievedUser = userDao.getById(persistedUser.userId);
            assertRetrievedMatchesLocal(retrievedUser, persistedUser);
        }
    }

    /**
     * Tests that a user record that does not already exist and has no ID set can be upserted
     * into the database, resulting in the record being persisted.
     */
    @Test
    public void upsertUser_nonexistentUser_noId_isSuccessful() {
        // Create a user to upsert and do not set an ID
        User toUpsert = new User(null, "foo");

        // Upsert the record and store the returned ID in the local copy of the record
        toUpsert.userId = userDao.upsert(toUpsert);

        // Assert that an associated record has been created in the database and the retrieved
        // record's fields match those of the local copy.
        User retrievedUser = userDao.getById(toUpsert.userId);
        assertRetrievedMatchesLocal(retrievedUser, toUpsert);
    }

    /**
     * Tests that a user record that does not already exist and has no ID set can be upserted
     * into the database, resulting in the record being persisted.
     */
    @Test
    public void upsertUser_existentUser_withId_isSuccessful() {
        // Create a user to insert, whose name will be "foo". Also create a record that will be
        // upserted on the same ID, whose name is "bar" (represents a name change).
        User toInsert = new User(null, "foo");
        User toUpsert = new User(null, "bar");

        // Insert the "original" record and store the auto-generated ID.
        toInsert.userId = userDao.insert(toInsert);

        // Assert that the record has been inserted with the proper fields.
        User afterInsert = userDao.getById(toInsert.userId);
        assertRetrievedMatchesLocal(afterInsert, toInsert);

        // Upsert the record that represents a field change.
        toUpsert.userId = toInsert.userId;
        userDao.upsert(toUpsert);

        // Assert that the record having the same ID as the initial inserted record now has the
        // properties of the upserted record.
        User afterUpsert = userDao.getById(toInsert.userId);
        assertRetrievedMatchesLocal(afterUpsert, toUpsert);
    }

    /**
     * Tests that the upsert operation will use the user ID field of a local user record as the
     * primary key if set and no record exists with that ID.
     */
    @Test
    public void upsertUser_nonexistentUser_withId_isSuccessful() {
        // Create a local user record to upsert
        User localUser = new User(999l, "foo");

        // Assert that there are no records with the local user record's ID
        User shouldNotExist = userDao.getById(localUser.userId);
        assertNull(shouldNotExist);

        // Upsert the local record
        userDao.upsert(localUser);

        // Assert that the upserted record, which did not exist before, now exists in the DB
        // under the ID that we set manually.
        User retrievedUser = userDao.getById(localUser.userId);
        assertRetrievedMatchesLocal(retrievedUser, localUser);
    }
}
