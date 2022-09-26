package com.rainbowflavor.hdcweb.repository;

import com.rainbowflavor.hdcweb.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface JpaScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByScheduleStartDateAfterOrderByScheduleStartDate(Date start);
}
