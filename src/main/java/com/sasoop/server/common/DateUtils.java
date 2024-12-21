package com.sasoop.server.common;

import com.sasoop.server.domain.triggerRaw.TriggerRaw;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
public class DateUtils {
    public static LocalDate getStartOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    }

    public static LocalDate getEndOfWeek(LocalDate date) {
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
    }
    public static Date[] getStartOfWeek(TriggerRaw triggerRaw) {
        Date[] newWeek = new Date[2];
        // `createDate`를 LocalDate로 변환
        LocalDate createDate = triggerRaw.getCreateDate().toLocalDate();

        // 주의 시작(일요일)과 끝(토요일) 계산
        LocalDate startOfWeek = getStartOfWeek(createDate);
        LocalDate endOfWeek = getEndOfWeek(createDate);

        // LocalDate -> Date 변환
        Date startDate = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        newWeek[0] = startDate;
        newWeek[1] = endDate;
        return newWeek;
    }
    public static int getDayOfWeekIndex(TriggerRaw triggerRaw) {
        // `createDate`를 LocalDate로 변환
        LocalDate createDate = triggerRaw.getCreateDate().toLocalDate();

        // 요일 계산 (일요일: 1, 월요일: 2, ..., 토요일: 7)
        DayOfWeek dayOfWeek = createDate.getDayOfWeek();
        int dayIndex = dayOfWeek.getValue() % 7 ; // 일요일을 0로 설정
        return dayIndex;
    }
}
