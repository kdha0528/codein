package com.codein.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
public class DateTimeUtils {
    public static String toSimpleFormat(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        if (now.getYear() == createdAt.getYear()) {
            if (now.getMonth() == createdAt.getMonth()) {
                if (now.getDayOfMonth() == createdAt.getDayOfMonth()) {
                    if (now.getHour() == createdAt.getHour()) {
                        if (now.getMinute() == createdAt.getMinute()) {
                            return "0분 전";
                        } else {
                            return ChronoUnit.MINUTES.between(createdAt, now)+"분 전";
                        }
                    } else {
                        return ChronoUnit.HOURS.between(createdAt, now)+"시간 전";
                    }
                } else {
                    return ChronoUnit.DAYS.between(createdAt, now)+"일 전";
                }
            } else {
                return ChronoUnit.MONTHS.between(createdAt, now)+"달 전";
            }
        } else {
            return ChronoUnit.YEARS.between(createdAt, now)+"년 전";
        }
    }

    public static String toNormalFormat(LocalDateTime createdAt){
        return createdAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"));
    }
}
