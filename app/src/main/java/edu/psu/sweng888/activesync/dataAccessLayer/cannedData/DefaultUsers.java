package edu.psu.sweng888.activesync.dataAccessLayer.cannedData;

import edu.psu.sweng888.activesync.dataAccessLayer.dao.UserDao;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;

/**
 * A configuration-as-code list of default test users.
 */
public final class DefaultUsers {

    // Prevent instantiation
    private DefaultUsers() {}

    // NOTE: When defining a new default muscle group, be sure to add it to this list!
    public static User[] getAllDefaultUsers(){
        return new User[] { TestUser };
    }

    public static final User TestUser = new User(1l, "TestUser");

    public static void initialize(ActiveSyncDatabase db) {
        UserDao userDao = db.userDao();
        for (User user : getAllDefaultUsers()) {
            userDao.ensureInserted(user);
        }
    }
}
