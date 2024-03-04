package edu.psu.sweng888.activesync.eventListeners;

import edu.psu.sweng888.activesync.dataAccessLayer.models.WorkoutSet;

/**
 * Defines a callback function that can be used by activities that need to respond to set additions
 * that are triggered by the WorkoutSetAdapter.
 */
public interface WorkoutSetAddListener {
    /**
     * Invoked by the WorkoutSetAdapter when a new set is added to the list. Information about the
     * new item is exposed via event arguments.
     * @param addedSet A reference to the newly added WorkoutSet object.
     */
    void handleAdd(WorkoutSet addedSet);
}
