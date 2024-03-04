package edu.psu.sweng888.activesync.eventListeners;

import edu.psu.sweng888.activesync.dataAccessLayer.models.WorkoutSet;

/**
 * Defines a callback function that can be used by activities that need to listen to changes in
 * fields of a set rendered by the WorkoutSetAdapter.
 */
public interface WorkoutSetChangeListener {
    /**
     * Invoked by the WorkoutSetAdapter when a set item's properties are changed (e.g., via the
     * user entering information into the input fields). Information about the changed item is
     * exposed via event arguments.
     * @param updatedPosition The index of the item that was changed w.r.t. the internal list of the triggering object.
     * @param updatedSet A reference to the WorkoutSet object affected by this operation.
     */
    void handleChange(int updatedPosition, WorkoutSet updatedSet);
}
