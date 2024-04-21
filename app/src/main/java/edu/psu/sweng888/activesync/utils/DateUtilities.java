package edu.psu.sweng888.activesync.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.MONTH);
    }

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.YEAR);
    }

    public static Date firstDateOfYearMonth(int year, int month) {
        return DateUtilities.dateFor(year, month, 1);
    }

    public static Date lastDateOfYearMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        Date firstDate = firstDateOfYearMonth(year, month);
        calendar.setTime(firstDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Date firstDateOfCurrentMonth() {
        return firstDateOfYearMonth(getCurrentYear(), getCurrentMonth());
    }

    public static Date lastDateOfCurrentMonth() {
        return lastDateOfYearMonth(getCurrentYear(), getCurrentMonth());
    }

    public static boolean isSameMonthAndYear(Date a, Date b) {
        if (a == null || b == null) return false;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(a);
        int year_a = calendar.get(Calendar.YEAR);
        int month_a = calendar.get(Calendar.MONTH);
        calendar.setTime(b);
        int year_b = calendar.get(Calendar.YEAR);
        int month_b = calendar.get(Calendar.MONTH);
        return year_a == year_b && month_a == month_b;
    }
}
