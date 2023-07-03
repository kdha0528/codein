package com.codein.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
public class DateTimeUtils {
    public static String toSimpleFormat(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Period diffDate = Period.between(createdAt.toLocalDate(), now.toLocalDate());
        Duration diffTime = Duration.between(createdAt.toLocalTime(), now.toLocalTime());

        if (diffDate.getYears() == 0) {
            if (diffDate.getMonths() == 0) {
                if (diffDate.getDays() == 0) {
                    if (diffTime.toHours() == 0L) {
                        if (diffTime.toMinutes() == 0L) {
                            return "0분 전";
                        } else {
                            return diffTime.toMinutes()+"분 전";
                        }
                    } else {
                        return diffTime.toHours()+"시간 전";
                    }
                } else {
                    return diffDate.getDays()+"일 전";
                }
            } else {
                return diffDate.getMonths()+"달 전";
            }
        } else {
            return diffDate.getYears()+"년 전";
        }
    }

    public static String toNormalFormat(LocalDateTime createdAt){
        return createdAt.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"));
    }
}
