package com.sasoop.server.domain.triggerReport;

import com.sasoop.server.domain.appTrigger.AppTrigger;
import com.sasoop.server.handler.LongConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class TriggerReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long triggerReportId;

    @Column(nullable = false)
    private Date startDate;
    private Date endDate;
    private int totalCount;

    @Convert(converter = LongConverter.class)
    private List<Long> weeklyReport ;

    @ManyToOne
    @JoinColumn(name = "trigger_id")
    private AppTrigger appTrigger;

    public void addTotalCount() {
        this.totalCount +=1;
    }

    public void addWeeklyReport(int index) {

        this.weeklyReport.set(index, this.weeklyReport.get(index) + 1);
    }

    public static TriggerReport toEntity(Date startDate, Date endDate, AppTrigger appTrigger) {
        return TriggerReport.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalCount(0)
                .weeklyReport(new ArrayList<>(Collections.nCopies(7, 0L)))
                .appTrigger(appTrigger)
                .build();
    }
}
