package edu.psu.sweng888.activesync.dataAccessLayer.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Relational model that ties a workout to all of its associated sets.
 */
public class WorkoutWithSets implements Serializable {
    @Embedded
    public Workout workout;

    @Relation(
        parentColumn = "workout_id",
        entityColumn = "workout_id"
    )
    public List<WorkoutSet> sets;

    public WorkoutWithSets() { }

    public WorkoutWithSets(Workout workout, WorkoutSet... sets) {
        this.workout = workout;
        this.sets = Arrays.asList(sets);
    }
}
