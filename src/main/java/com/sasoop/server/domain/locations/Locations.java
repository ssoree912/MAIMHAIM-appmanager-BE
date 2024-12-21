package com.sasoop.server.domain.locations;

import com.sasoop.server.common.BaseTimeEntity;
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
public class Locations extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @Column
    private String location;
    private String address;
    private double latitude;
    private double longitude;

    public static Locations toEntity(String location, String address, double latitude, double longitude) {
        return Locations.builder()
                .location(location)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

}
