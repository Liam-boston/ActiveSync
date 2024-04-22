package edu.psu.sweng888.activesync.utils;

public class GroupingRemovalResult {

    public final boolean removedSuccessfully;
    public final int itemsRemainingInGrouping;

    public GroupingRemovalResult(
        boolean removedSuccessfully,
        int itemsRemainingInGrouping
    ) {
        this.removedSuccessfully = removedSuccessfully;
        this.itemsRemainingInGrouping = itemsRemainingInGrouping;
    }
}
