package com.sasoop.server.domain.triggerReport;

import com.sasoop.server.domain.appTrigger.AppTrigger;
import com.sasoop.server.handler.LongConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TriggerReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long triggerReportId;

    @Column(nullable = false)
    private Date startDate;
    private Date endDate;
    private int totalCount;

    @Convert(converter = LongConverter.class)
    private List<Long> weeklyReport;

    @ManyToOne
    @JoinColumn(name = "trigger_id")
    private AppTrigger appTrigger;
}
