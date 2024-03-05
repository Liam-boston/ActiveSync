package edu.psu.sweng888.activesync.dataAccessLayer.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Represents a user of the application.
 */
@Entity
public class User {

    /**
     * The unique identifier for this user.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    public long userId;

    /**
     * The user's display name.
     */
    public String name; // TODO: Add a unique constraint to this column

    public User() { }

    public User(Long id, String name) {
        if (id != null) {
            this.userId = id;
        }
        if (name != null) {
            this.name = name;
        }
    }
}

