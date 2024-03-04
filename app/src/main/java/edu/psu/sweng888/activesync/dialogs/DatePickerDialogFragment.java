package edu.psu.sweng888.activesync.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.psu.sweng888.activesync.eventListeners.DateSetListener;

/**
 * Date picker dialog fragment that can be used to select a date using the built-in Android
 * date picker spinners/calendar view.
 */
public class DatePickerDialogFragment
    extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

    private final DateSetListener dateSetListener;

    public DatePickerDialogFragment(DateSetListener listener) {
        dateSetListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        return new DatePickerDialog(
            requireContext(),
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Guard against an unset listener, in which case we just short circuit
        if (dateSetListener == null) return;

        // Create a `Date` representation of the year/month/day
        dateSetListener.handleDateSet(
            new GregorianCalendar(year, month, day).getTime()
        );
    }
}
