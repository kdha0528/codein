package com.rainbowflavor.hdcweb.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
public class ScheduleDto {

    private String userName;

    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startScheduleDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endScheduleDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date scheduleDay;

    private String scheduleDetail;

    private String year;
    private String month;
    private String date;

}
