package org.example.rgybackend.Utils;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

public class TimeUtil {
    public final static Long DAY = 24 * 60 * 60 * 1000L;
    public final static Long HOUR = 60 * 60 * 1000L;
    public final static Long MINUTE = 60 * 1000L;
    public final static Long SECOND = 1000L;

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

    public static LocalDateTime getLocalDateTime(LocalDate date, Long hour) {
        return date.atTime(hour.intValue(), 0);
    }

    public static Long getTimestamp(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        Long timestamp = localDateTime
                .atZone(zoneId)
                .toInstant()
                .toEpochMilli();
        return timestamp;
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

    public static LocalDate firstDayOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public static LocalDate firstDayOfMonth() {
        LocalDate today = LocalDate.now();
        return today.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate firstDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }
}
