package edu.psu.sweng888.activesync.dataAccessLayer.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Represents a single muscle group that can be targetted by an exercise.
 */
@Entity
public class MuscleGroup implements Serializable {

    /**
     * The unique identifier for this muscle group.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "muscle_group_id")
    public long muscleGroupId;

    /**
     * The name of the muscle group, e.g. "biceps".
     */
    public String name;

    /**
     * The rate at which this muscle group recovers from fatigue when not worked. Expressed in the
     * percentage recovery per day of rest. For example, a recovery rate of 0.06 means that this
     * muscle group recovers 6% fatigue for each day of full rest.
     */
    @ColumnInfo(name = "recovery_rate")
    public double recoveryRate;

    public MuscleGroup() {}

    public MuscleGroup(Long id, String name, Double recoveryRate) {
        if (id != null) this.muscleGroupId = id;
        if (name != null) this.name = name;
        if (recoveryRate != null) this.recoveryRate = recoveryRate;
    }
    
    public long getMuscleGroupId() {
        return muscleGroupId;
    }

    public void setMuscleGroupId(long muscleGroupId) {
        this.muscleGroupId = muscleGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRecoveryRate() {
        return recoveryRate;
    }

    public void setRecoveryRate(double recoveryRate) {
        this.recoveryRate = recoveryRate;
    }
}
