package com.rainbowflavor.hdcweb.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class DateData {

    String year = "";
    String month = "";
    String date = "";
    String value = "";
    String scheduleDetail = "";

    public Map<String, Integer> todayInfo(DateData dateData) {

        Map<String, Integer> todayData = new HashMap<String, Integer>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(dateData.getYear()), Integer.parseInt(dateData.getMonth()), 1);

        int startDay = calendar.getMinimum(Calendar.DATE);
        int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int start = calendar.get(Calendar.DAY_OF_WEEK);

        Calendar todayCalendar = Calendar.getInstance();
        SimpleDateFormat ySDF = new SimpleDateFormat("yyyy");
        SimpleDateFormat mSDF = new SimpleDateFormat("M");

        int currentYear = Integer.parseInt(ySDF.format(todayCalendar.getTime()));
        int currentMonth = Integer.parseInt(mSDF.format(todayCalendar.getTime()));

        int searchYear = Integer.parseInt(dateData.getYear());
        int searchMonth = Integer.parseInt(dateData.getMonth()) + 1;

        int today = -1;
        if (currentYear == searchYear && currentMonth == searchMonth) {
            SimpleDateFormat dSDF = new SimpleDateFormat("dd");
            today = Integer.parseInt(dSDF.format(todayCalendar.getTime()));
        }

        searchMonth = searchMonth - 1;

        Map<String, Integer> before_after_calendar = before_after_calendar(searchYear, searchMonth);

        todayData.put("start", start);
        todayData.put("startDay", startDay);
        todayData.put("endDay", endDay);
        todayData.put("today", today);
        todayData.put("searchYear", searchYear);
        todayData.put("searchMonth", searchMonth + 1);
        todayData.put("beforeYear", before_after_calendar.get("beforeYear"));
        todayData.put("beforeMonth", before_after_calendar.get("beforeMonth"));
        todayData.put("afterYear", before_after_calendar.get("afterYear"));
        todayData.put("afterMonth", before_after_calendar.get("afterMonth"));

        return todayData;

    }

    private Map<String, Integer> before_after_calendar(int searchYear, int searchMonth) {

        Map<String, Integer> before_after_data = new HashMap<String, Integer>();

        int beforeYear = searchYear;
        int beforeMonth = searchMonth - 1;
        int afterYear = searchYear;
        int afterMonth = searchMonth + 1;

        if (beforeMonth < 0) {
            beforeMonth = 11;
            beforeYear = searchYear - 1;
        }

        if (afterMonth > 11) {
            afterMonth = 0;
            afterYear = searchYear + 1;
        }

        before_after_data.put("beforeYear", beforeYear);
        before_after_data.put("beforeMonth", beforeMonth);
        before_after_data.put("afterYear", afterYear);
        before_after_data.put("afterMonth", afterMonth);

        return before_after_data;
    }

    public DateData(String year, String month, String date, String value, String scheduleDetail) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.value = value;
        this.scheduleDetail = scheduleDetail;
    }

    public DateData(String year, String month, String date, String value) {
        if ((month != null && month != "") && (date != null && date != "")) {
            this.year = year;
            this.month = month;
            this.date = date;
            this.value = value;
        }
    }

    public DateData() {

    }
}
