package com.sasoop.server.domain.LocationTrigger;

import com.sasoop.server.common.BaseTimeEntity;
import com.sasoop.server.domain.appTrigger.AppTrigger;
import com.sasoop.server.domain.locations.Locations;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationTriggerReport extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationTriggerId;

    @Column
    private int count;
    private Date startDate;
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Locations locations;

    @ManyToOne
    @JoinColumn(name = "trigger_id")
    private AppTrigger appTrigger;

    public void addCount() {
        this.count +=1;
    }

    public static LocationTriggerReport toEntity(Date startDate, Date endDate, Locations locations, AppTrigger appTrigger) {
        return LocationTriggerReport.builder()
                .startDate(startDate)
                .endDate(endDate)
                .count(0)
                .locations(locations)
                .appTrigger(appTrigger)
                .build();
    }
}
