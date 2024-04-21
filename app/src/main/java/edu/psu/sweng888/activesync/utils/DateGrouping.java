package edu.psu.sweng888.activesync.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * Data structure that allows one or more items of a certain type to be stored with a `Date` as
 * their key. Only the date portion of the `Date` keys are used (i.e., components less significant
 * than "Day" are discarded in all operations and comparisons).
 * @param <TItem> The type of items to store in the grouping.
 */
public class DateGrouping<TItem> {
    /**
     * The item groupings stored by date, which is internally represented as the number of
     * milliseconds since the Unix epoch (retrieved via `Date.getTime()`).
     */
    private final HashMap<Long, ArrayList<TItem>> _groupingsByDateAsMillis = new HashMap<>();

    public void add(TItem item, Date date) {
        long key = dateToKey(date);
        ArrayList<TItem> grouping = _groupingsByDateAsMillis.getOrDefault(key, new ArrayList<>());
        assert grouping != null;
        grouping.add(item);
        _groupingsByDateAsMillis.put(key, grouping);
    }

    public DateGrouping<TItem> populateWithDatedItems(Collection<TItem> items, Function<TItem, Date> dateSelector) {
        for (TItem item : items) {
            Date itemDate = dateSelector.apply(item);
            if (itemDate == null) {
                throw new RuntimeException("Date selector for item returned null -- unable to group item by date.");
            }
            this.add(item, itemDate);
        }
        return this;
    }

    private static long dateToKey(Date date) {
        return DateUtilities.withoutTime(date).getTime();
    }

    public List<TItem> getItemsFor(Date date) {
        if (date == null) return null;
        return _groupingsByDateAsMillis.get(dateToKey(date));
    }

    public boolean hasItemsFor(Date date) {
        if (date == null) return false;
        return _groupingsByDateAsMillis.containsKey(dateToKey(date));
    }
}
