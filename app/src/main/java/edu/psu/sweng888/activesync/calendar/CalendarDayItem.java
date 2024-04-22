package edu.psu.sweng888.activesync.calendar;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;

import androidx.annotation.ColorRes;

import java.util.Calendar;
import java.util.Date;

import edu.psu.sweng888.activesync.R;
import edu.psu.sweng888.activesync.utils.DateUtilities;

public class CalendarDayItem {
    private final Button button;

    private Date date;

    private boolean isSelected = false;

    private CalendarDayItemClickHandler clickHandler;

    public CalendarDayItem(Button button, Date date, CalendarDayItemClickHandler handler) {
        this.button = button;
        this.date = date;
        this.clickHandler = handler;
        this.button.setOnClickListener(v -> {
            this.toggleSelected();
            if (this.clickHandler != null) {
                this.clickHandler.handleCalendarDayItemClick(this);
            }
        });
        this.show();
    }

    public void setText(String text) {
        this.button.setText(text);
    }

    public void setClickHandler(CalendarDayItemClickHandler handler) {
        this.clickHandler = handler;
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
        this.button.setBackgroundColor(color(R.color.white));
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
