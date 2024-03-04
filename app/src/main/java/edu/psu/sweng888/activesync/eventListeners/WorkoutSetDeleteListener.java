package edu.psu.sweng888.activesync.eventListeners;

import edu.psu.sweng888.activesync.dataAccessLayer.models.WorkoutSet;

/**
 * Defines a callback function that can be used by activities that need to listen to the result of
 * a deletion of a workout set from the WorkoutSetAdapter.
 */
public interface WorkoutSetDeleteListener {
    /**
     * Invoked by the WorkoutSetAdapter when a set item is removed (e.g., via the user clicking the
     * delete button). Information about the deleted item is exposed via event arguments.
     * @param deletedPosition The index of the item that was deleted w.r.t. the internal list of the triggering object.
     * @param deletedSet A reference to the WorkoutSet object affected by this operation.
     */
    void handleDelete(int deletedPosition, WorkoutSet deletedSet);
}
