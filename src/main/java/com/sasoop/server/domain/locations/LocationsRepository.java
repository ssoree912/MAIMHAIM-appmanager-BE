package com.sasoop.server.domain.locations;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationsRepository extends JpaRepository<Locations, Long> {
    Optional<Locations> findByLatitudeAndLongitude(double latitude, double longitude);
}
