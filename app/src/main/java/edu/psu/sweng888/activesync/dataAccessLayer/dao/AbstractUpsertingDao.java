package edu.psu.sweng888.activesync.dataAccessLayer.dao;

import android.database.sqlite.SQLiteConstraintException;

import androidx.room.Transaction;

/**
 * Defines an interface of a RoomDB DAO that supports insert and update actions.
 * @param <TEntity> The RoomDB entity type for which this DAO is implemented.
 */
public abstract class AbstractUpsertingDao<TEntity> {

    /**
     * Inserts the given entity into a SQLite table via RoomDB.
     * @param entity The entity to insert into the database.
     * @return The primary key of the inserted entity on success; otherwise, throws an exception.
     */
    public abstract long insert(TEntity entity);

    /**
     * Updates the already-existing entry for the given entity in a SQLite table via RoomDB.
     * @param entity The entity to be updated.
     * @return The number of rows affected by the operation.
     */
    public abstract int update(TEntity entity);

    /**
     * Returns the primary key of the given entity.
     * @param entity The entity for which the primary key should be returned.
     * @return The primary key value of the given entity object.
     */
    public abstract long getPrimaryKey(TEntity entity);

    /**
     * Attempts to insert the given entity into the database. If the insert fails due to a key
     * constraint issue, an attempt is made to update the entity's entry instead.
     * Note that this method has slightly different behavior from the "stock" RoomDB @Upsert-
     * annotated methods and is preferred for our uses over the built-in method.
     * @param entity The entity to insert or update.
     * @return The primary key value of the entity as a result of the operation.
     */
    public long upsert(TEntity entity) {
        try {
            // Attempt to insert the entry and get the ID that was created via the insertion.
            // If there is already an entry with this ID (i.e. we are trying to update), this will
            // fail with a SQLiteConstraintException.
            return insert(entity);
        }
        catch (SQLiteConstraintException ex) {
            // Ignore the constraint issue since this is expected. Try an update instead. If we
            // get a positive number of rows affected, consider this a success and return the ID
            // of the model that we used to update the DB.
            int rowsAffected = update(entity);
            if (rowsAffected > 0) {
                return getPrimaryKey(entity);
            }
            throw new RuntimeException("Upsert operation failed!");
        }
    }
}
