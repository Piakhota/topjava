package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return isBetweenHalfOpen(LocalDateTime.of(LocalDate.MIN, lt), LocalDateTime.of(LocalDate.MIN, startTime), LocalDateTime.of(LocalDate.MIN, endTime));
    }

    public static boolean isBetweenHalfOpen(LocalDate ld, LocalDate startDate, LocalDate endDate) {
        return isBetweenHalfOpen(LocalDateTime.of(ld, LocalTime.MIDNIGHT), LocalDateTime.of(startDate, LocalTime.MIDNIGHT), LocalDateTime.of(endDate, LocalTime.MIDNIGHT));
    }
    public static boolean isBetweenHalfOpen(LocalDateTime ldt, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ldt.compareTo(startDateTime) >= 0 && ldt.compareTo(endDateTime) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

