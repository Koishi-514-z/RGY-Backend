package org.example.rgybackend.Utils;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

public class TimeUtil {
    public static Long DAY = 24 * 60 * 60 * 1000L;

    public static boolean isSameDay(Long timestamp1, Long timestamp2) {
        ZoneId zone = ZoneId.systemDefault(); 
        LocalDate date1 = Instant.ofEpochMilli(timestamp1).atZone(zone).toLocalDate();
        LocalDate date2 = Instant.ofEpochMilli(timestamp2).atZone(zone).toLocalDate();
        return date1.equals(date2);
    }

    public static boolean isSameDay(LocalDate date, Long timestamp) {
        ZoneId zone = ZoneId.systemDefault(); 
        LocalDate date_plus = Instant.ofEpochMilli(timestamp).atZone(zone).toLocalDate();
        return date.equals(date_plus);
    }

    public static LocalDate getLocalDate(Long timestamp) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate date = Instant.ofEpochMilli(timestamp).atZone(zone).toLocalDate();
        return date;
    }

    public static Long getStartOfDayTimestamp(LocalDate date) {
        ZoneId zoneId = ZoneId.systemDefault(); 
        Long timestamp = date
                .atStartOfDay(zoneId)       
                .toInstant()              
                .toEpochMilli(); 
        return timestamp;         
    }

    public static Long getStartOfDayTimestamp(Long timestamp) {
        ZoneId zoneId = ZoneId.systemDefault(); 
        Long tsp = Instant.ofEpochMilli(timestamp)
                .atZone(zoneId)
                .toLocalDate()
                .atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli();
        return tsp;
    }

    public static Long now() {
        return System.currentTimeMillis();
    }

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static LocalDate firstDayOfWeek() {
        LocalDate today = LocalDate.now();
        return today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public static LocalDate firstDayOfMonth() {
        LocalDate today = LocalDate.now();
        return today.with(TemporalAdjusters.firstDayOfMonth());
    }
}
