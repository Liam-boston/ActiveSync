package edu.psu.sweng888.activesync;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.ColorRes;
import androidx.fragment.app.Fragment;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.airbnb.paris.Paris;

import java.sql.Array;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import edu.psu.sweng888.activesync.adapters.RecoveryStatusAdapter;
import edu.psu.sweng888.activesync.adapters.WorkoutSetAdapter;
import edu.psu.sweng888.activesync.dataAccessLayer.cannedData.DefaultMuscleGroups;
import edu.psu.sweng888.activesync.dataAccessLayer.db.ActiveSyncDatabase;
import edu.psu.sweng888.activesync.dataAccessLayer.models.ExerciseType;
import edu.psu.sweng888.activesync.dataAccessLayer.models.MuscleGroup;
import edu.psu.sweng888.activesync.dataAccessLayer.models.User;
import edu.psu.sweng888.activesync.dataAccessLayer.models.Workout;
import edu.psu.sweng888.activesync.dataAccessLayer.models.WorkoutSet;
import edu.psu.sweng888.activesync.dataAccessLayer.viewModels.WorkoutEntryModel;
import edu.psu.sweng888.activesync.databinding.FragmentCalendarBinding;
import edu.psu.sweng888.activesync.utils.DateUtilities;

public class WorkoutCalendar extends Fragment {

    private FragmentCalendarBinding binding;

    public WorkoutCalendar() { }

    private enum DayOfWeek {
        Sunday,
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday
    }

    private static interface CalendarDayItemClickHandler {
        void handleCalendarDayItemClick(CalendarDayItem clicked);
    }

    private static class CalendarDayItem {
        private final Button button;

        private Date date;

        private boolean isSelected = false;

        public CalendarDayItem(Button button, Date date, CalendarDayItemClickHandler handler) {
            this.button = button;
            this.date = date;
            this.button.setOnClickListener(v -> {
                this.toggleSelected();
                if (handler != null) {
                    handler.handleCalendarDayItemClick(this);
                }
            });
            this.show();
        }

        public void setText(String text) {
            this.button.setText(text);
        }

        public void show() {
            this.button.setVisibility(View.VISIBLE);
        }

        public void hide() {
            this.button.setVisibility(View.GONE);
        }

        public void setBlank() {
            this.button.setText("");
        }

        private int color(@ColorRes int id) {
            return this.button.getContext().getResources().getColor(id);
        }

        public void enable() {
            this.button.setEnabled(true);
            this.button.setTextColor(color(R.color.black));
        }

        public void disable() {
            this.button.setEnabled(false);
            this.button.setTextColor(color(R.color.halfTransparentBlack));
        }

        public void select() {
            this.isSelected = true;
            this.button.setTextColor(color(R.color.white));
            this.button.setBackgroundColor(color(R.color.black));
        }

        public void deselect() {
            this.isSelected = false;
            this.button.setTextColor(color(R.color.black));
            this.button.setBackgroundColor(color(R.color.white));
        }

        public boolean isSelected() { return this.isSelected; }

        public void toggleSelected() {
            if (this.isSelected()) {
                this.deselect();
            }
            else {
                this.select();
            }
        }

        @SuppressLint("SetTextI18n")
        public void setDate(Date date) {
            this.date = date;
            Calendar cal = Calendar.getInstance();
            cal.setTime(this.date);
            this.button.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
        }

        public boolean isForDate(Date date) {
            return DateUtilities.isSameDate(this.date, date);
        }

        public Date getDate() {
            return this.date;
        }
    }

    private int selectedYear;
    private int selectedMonth;

    private Date firstDateOfSelectedYearMonth() {
        return DateUtilities.firstDateOfYearMonth(this.selectedYear, this.selectedMonth + 1); // FIXME: Inconsistency b/t Calendar API (which uses zero-based months) and our custom DateUtilities implementations (which use one-based months) forces us to do this lil' hack.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(inflater, container, false);

        // By default, start the selected year/month as that of the current day.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        this.selectedYear = calendar.get(Calendar.YEAR);
        this.selectedMonth = calendar.get(Calendar.MONTH);

        // Get model representations of the grid items that represent actual days in the selected
        // year and month.
        List<CalendarDayItem> calendarDayItems = getItemsForSelectedYearMonth(null); // TODO: Attach handler!

        // Get the workout data for the current user
        User activeUser = ActiveSyncApplication.getActiveUserOrRedirectToLogin(getContext());
        if (activeUser == null) {
            this.getActivity().finish();
            return null;
        }
        List<WorkoutEntryModel> workoutData;
        try {
            workoutData = WorkoutEntryModel.allFromDatabaseByUser(
                ActiveSyncApplication.getDatabase(),
                activeUser
            );
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // Enable any calendar day item that has data
        List<Date> selectedYearMonthDatesWithData = workoutData.stream()
            .map(model -> model.workout.date)
            .filter(date -> DateUtilities.isSameMonthAndYear(date, firstDateOfSelectedYearMonth()))
            .collect(Collectors.toList());
        List<CalendarDayItem> itemsWithData = calendarDayItems.stream()
            .filter(item -> selectedYearMonthDatesWithData.stream().anyMatch(
                dateWithData -> item.isForDate(dateWithData)
            ))
            .collect(Collectors.toList());
        for (CalendarDayItem itemWithData : itemsWithData) {
            itemWithData.enable();
            // TODO: Attach a handler!
        }



        return binding.getRoot();
    }

    private List<CalendarDayItem> getItemsForSelectedYearMonth(CalendarDayItemClickHandler itemClickHandler) {
        List<CalendarDayItem> calendarDayItems = new ArrayList<>();
        Date firstDateOfCurrentMonth = firstDateOfSelectedYearMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDateOfCurrentMonth);
        int firstDayOfMonth = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int buttonIndex = 0;
        for (Button dayButton : getChildButtons(binding.calendarViewMonthlyGrid)) {
            // Create calendar day item instance
            int dayOfWeekAsInt = (buttonIndex % 7) + 1;
            CalendarDayItem item = new CalendarDayItem(dayButton, null, itemClickHandler);
            item.disable();

            // If we are in the cells before the first day of the given month, blank them out
            if (buttonIndex < 7 && dayOfWeekAsInt < firstDayOfWeek) {
                item.setBlank();
            }
            else {
                int dayOfMonth = buttonIndex+1 - firstDayOfMonth;
                item.setDate(new Date(firstDateOfCurrentMonth.getTime() + Constants.MILLISECONDS_PER_DAY * (dayOfMonth - 1)));
            }

            // If we are in the cells after the last day of the month, hide them entirely
            if (buttonIndex > lastDayOfMonth) {
                item.hide();
            }
            else {
                // This calendar day item is a valid day in the month in question
                calendarDayItems.add(item);
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