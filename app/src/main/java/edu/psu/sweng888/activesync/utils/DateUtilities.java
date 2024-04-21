package edu.psu.sweng888.activesync.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import edu.psu.sweng888.activesync.Constants;

public class DateUtilities {

    private DateUtilities() {}

    public static Date withoutTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean isSameDate(Date a, Date b) {
        if (a == null || b == null) return false;
        return withoutTime(a).getTime() == withoutTime(b).getTime();
    }

    public static Date dateFor(int year, int month, int day) {
        return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.of("America/Montreal")).toInstant());
    }

    public static int getCurrentMonth() {
        return monthOf(new Date());
    }

    public static int getCurrentYear() {
        return yearOf(new Date());
    }

    public static int monthOf(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int yearOf(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static Date firstDateOfYearMonth(int year, int month) {
        return DateUtilities.dateFor(year, month, 1);
    }

    public static Date dateOfPreviousMonth(int currentYear, int currentMonth) {
        Date firstDateOfCurrentMonth = withoutTime(firstDateOfYearMonth(currentYear, currentMonth + 1));
        return withoutTime(new Date(firstDateOfCurrentMonth.getTime() - Constants.MILLISECONDS_PER_DAY)); // last day of previous month
    }

    public static Date dateOfPreviousMonth(Date date) {
        return dateOfPreviousMonth(
            yearOf(date),
            monthOf(date)
        );
    }

    public static Date dateOfNextMonth(int currentYear, int currentMonth) {
        Date firstDateOfCurrentMonth = withoutTime(firstDateOfYearMonth(currentYear, currentMonth + 1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDateOfCurrentMonth);
        int daysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return withoutTime(new Date(firstDateOfCurrentMonth.getTime() + daysInCurrentMonth * Constants.MILLISECONDS_PER_DAY)); // first day of next month (unless of by one error?)
    }

    public static Date dateOfNextMonth(Date date) {
        return dateOfNextMonth(
            yearOf(date),
            monthOf(date)
        );
    }

}
