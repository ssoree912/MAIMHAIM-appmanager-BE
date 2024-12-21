package com.sasoop.server.domain.triggerRaw;

import com.sasoop.server.common.BaseTimeEntity;
import com.sasoop.server.domain.appTrigger.AppTrigger;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TriggerRaw extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long triggerRawId;

    @Column
    private String location;
    private String address;
    private double latitude;
    private double longitude;

    @ManyToOne
    @JoinColumn(name = "trigger_id")
    private AppTrigger appTrigger;

    public static TriggerRaw toEntity(String location, String address, double latitude, double longitude, AppTrigger appTrigger) {
        return TriggerRaw.builder()
                .location(location)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .appTrigger(appTrigger)
                .build();
    }

}