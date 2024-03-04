package edu.psu.sweng888.activesync.eventListeners;

import java.util.Date;

/**
 * Defines a callback function that can be used by activities that need to listen to the result of
 * the date being set by an instance of the `DatePickerDialogFragment`.
 */
public interface DateSetListener {
    void handleDateSet(Date date);
}
