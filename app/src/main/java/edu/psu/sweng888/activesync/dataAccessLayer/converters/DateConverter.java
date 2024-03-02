package edu.psu.sweng888.activesync.dataAccessLayer.converters;

import java.util.Date;

import androidx.room.TypeConverter;

/**
 * A set of type converters that allow instances of Date to be stored in RoomDB entities as a
 * nullable long value.
 */
public class DateConverter {

    /**
     * Converts the given nullable long value into a Date object.
     * @param value The nullable long value to convert.
     * @return A Date representation of the given long value if non-null; otherwise, null.
     */
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * Converts the given Date object in a nullable long value.
     * @param date The Date object to convert.
     * @return The number of milliseconds since the Unix epoch represented by the date if non-null; otherwise, null.
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
