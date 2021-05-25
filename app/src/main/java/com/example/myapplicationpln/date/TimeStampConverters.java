package com.example.myapplicationpln.date;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeStampConverters {
    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @TypeConverter
    public static Date fromTimestamp(String value){
        if (value !=null) {
            try {
                TimeZone timeZone = TimeZone.getTimeZone("IST");
                df.setTimeZone(timeZone);
                return  df.parse(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }else {
            return null;
        }
    }

    @TypeConverter
    public static String dateToTimestamp(Date value) {
        TimeZone timeZone = TimeZone.getTimeZone("IST");
        df.setTimeZone(timeZone);
        return value == null ? null : df.format(value);
    }
}
