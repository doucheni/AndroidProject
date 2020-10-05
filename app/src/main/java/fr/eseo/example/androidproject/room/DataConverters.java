package fr.eseo.example.androidproject.room;

import androidx.room.TypeConverter;

public class DataConverters {
    private DataConverters(){}

    public static class Date {
        @TypeConverter
        public static java.util.Date toDate(Long timestamp) {
            if (timestamp == null) {
                return null;
            }
            return new java.util.Date(timestamp.longValue());
        }
        @TypeConverter
        public static Long toTimeStamp(java.util.Date date) {
            if (date == null) {
                return null;
            }
            return Long.valueOf(date.getTime());
        }
    }
}
