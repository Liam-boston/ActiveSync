package edu.psu.sweng888.activesync;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import edu.psu.sweng888.activesync.calendar.CalendarDayItem;
import edu.psu.sweng888.activesync.calendar.CalendarDayItemClickHandler;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;
import edu.psu.sweng888.activesync.dataAccessLayer.viewModels.WorkoutEntryModel;
import edu.psu.sweng888.activesync.databinding.FragmentCalendarBinding;
import edu.psu.sweng888.activesync.utils.DateGrouping;
import edu.psu.sweng888.activesync.utils.DateUtilities;

public class WorkoutCalendar extends Fragment implements CalendarDayItemClickHandler {

    private FragmentCalendarBinding binding;

    public WorkoutCalendar() { }

    private Date selectedMonthDate;
    private DateGrouping<WorkoutEntryModel> workoutsByDate;
    private List<CalendarDayItem> calendarDayItems;
    private Date selectedCalendarDate;

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat monthlyCalendarHeaderFormat = new SimpleDateFormat("MMMM yyyy");


    private Date firstDateOfSelectedYearMonth() {
        return DateUtilities.firstDateOfYearMonth(
            DateUtilities.yearOf(this.selectedMonthDate),
            DateUtilities.monthOf(this.selectedMonthDate) + 1 // FIXME: Inconsistency b/t Calendar API (which uses zero-based months) and our custom DateUtilities implementations (which use one-based months) forces us to do this lil' hack.
        );
    }

    private void updateMonthlyCalendarLabelForSelectedYearMonth() {
        binding.calendarViewMonthlyMonthYearLabel.setText(
            monthlyCalendarHeaderFormat.format(
                firstDateOfSelectedYearMonth()
            )
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(inflater, container, false);

        // By default, start the selected year/month as that of the current day.
        this.selectedMonthDate = new Date();

        // Update the label for the calendar to display the human-readable month and year
        updateMonthlyCalendarLabelForSelectedYearMonth();

        // Get the user's workout data
        User activeUser = ActiveSyncApplication.getActiveUserOrRedirectToLogin(getContext());
        if (activeUser == null) {
            this.getActivity().finish();
            return null;
        }

        try {
            workoutsByDate = new DateGrouping<WorkoutEntryModel>().populateWithDatedItems(
                WorkoutEntryModel.allFromDatabaseByUser(
                    ActiveSyncApplication.getDatabase(),
                    activeUser
                ),
                model -> model.workout.date
            );
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // Set up the fragment's state w.r.t. the calendar UI elements
        setupCalendar();

        // Add button handlers to the prev/next month buttons to re-setup the calendar.
        binding.calendarViewMonthlyButtonPrevious.setOnClickListener(__ -> {
            this.selectedMonthDate = DateUtilities.dateOfPreviousMonth(this.selectedMonthDate);
            updateMonthlyCalendarLabelForSelectedYearMonth();
            setupCalendar();
        });
        binding.calendarViewMonthlyButtonNext.setOnClickListener(__ -> {
            this.selectedMonthDate = DateUtilities.dateOfNextMonth(this.selectedMonthDate);
            updateMonthlyCalendarLabelForSelectedYearMonth();
            setupCalendar();
        });

        return binding.getRoot();
    }

    private void setupCalendar() {
        // Get model representations of the grid items that represent actual days in the selected
        // year and month.
        calendarDayItems = getItemsForSelectedYearMonth(this);

        // Enable any calendar day item that has data
        List<CalendarDayItem> itemsWithData = calendarDayItems.stream()
            .filter(item -> workoutsByDate.hasItemsFor(item.getDate()))
            .collect(Collectors.toList());

        for (CalendarDayItem itemWithData : itemsWithData) {
            itemWithData.enable();
        }
    }

    @Override
    public void handleCalendarDayItemClick(CalendarDayItem clicked) {
        if (!clicked.isSelected()) return;
        // If we just toggled this item to be "selected", store the item's date, then visually
        // deselect everything else.
        selectedCalendarDate = clicked.getDate();
        calendarDayItems.stream()
            .filter(item -> item != clicked)
            .filter(CalendarDayItem::isSelected)
            .forEach(CalendarDayItem::deselect);
    }

    private List<CalendarDayItem> getItemsForSelectedYearMonth(CalendarDayItemClickHandler itemClickHandler) {
        List<CalendarDayItem> calendarDayItems = new ArrayList<>();
        Date firstDateOfSelectedMonth = firstDateOfSelectedYearMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDateOfSelectedMonth);
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDayOfFirstWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int buttonIndex = 0;
        for (Button dayButton : getChildButtons(binding.calendarViewMonthlyGrid)) {
            // Create calendar day item instance
            int dayOfWeekAsInt = (buttonIndex % 7) + 1;
            CalendarDayItem item = new CalendarDayItem(dayButton, null, itemClickHandler);
            item.disable();

            // If we are in the cells before the first day of the given month, blank them out
            int dayOfMonth = (buttonIndex + 1) - firstDayOfFirstWeek;
            if (dayOfMonth < 0) {
                item.setBlank();
            }
            // If we are in range of the month's days, set the date and add the item to the return list
            else if (dayOfMonth < lastDayOfMonth){
                item.setDate(new Date(firstDateOfSelectedMonth.getTime() + Constants.MILLISECONDS_PER_DAY * dayOfMonth));
                calendarDayItems.add(item);
            }
            // If we are past the last day of the month, hide the item
            else {
                item.hide();
            }

            buttonIndex++;
        }
        return calendarDayItems;
    }

    private static List<Button> getChildButtons(ViewGroup viewGroup) {
        List<Button> buttons = new ArrayList<>();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof Button) {
                buttons.add((Button) child);
            }
        }
        return buttons;
    }
}