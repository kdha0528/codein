package com.rainbowflavor.hdcweb.service;

import com.rainbowflavor.hdcweb.domain.Schedule;
import com.rainbowflavor.hdcweb.dto.ScheduleDto;
import com.rainbowflavor.hdcweb.repository.JpaScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ScheduleService {
    private final JpaScheduleRepository scheduleRepository;

    public void createSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    public List<ScheduleDto> getScheduleInMonth(Integer year, Integer month){

        String startDateToString;
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        // 년도를 옮기지 않으면 현재 날짜가 나오지 않으므로 구해서 넣어줘야한다.
        Calendar cal = Calendar.getInstance();
        if (year == null || month == null) {
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
        } else if (month == 0) {
            month = 1;
        }

        startDateToString = year + "-" + month + "-1";
        Date startDate = Date.valueOf(startDateToString);

        List<Schedule> schedules = scheduleRepository.findAllByScheduleStartDateAfterOrderByScheduleStartDate(startDate);
        List<ScheduleDto> scheduleDtos = new ArrayList<>();

        schedules.listIterator().forEachRemaining(attr->{
            startCal.setTime(attr.getScheduleStartDate());
            endCal.setTime(attr.getScheduleEndDate());
            while (startCal.compareTo(endCal) != 1) {
                ScheduleDto scheduleDto = new ScheduleDto();
                Date date = new Date(startCal.getTimeInMillis());
                scheduleDto.setId(attr.getId());
                scheduleDto.setScheduleDay(date);
                scheduleDto.setYear(String.valueOf(startCal.get(Calendar.YEAR)));
                scheduleDto.setMonth(String.valueOf(startCal.get(Calendar.MONTH)));
                scheduleDto.setDate(String.valueOf(startCal.get(Calendar.DATE)));
                scheduleDto.setScheduleDetail(attr.getScheduleDetail());
                scheduleDtos.add(scheduleDto);
                startCal.add(Calendar.DATE,1);
            }
        });
        return scheduleDtos;
    }

}
