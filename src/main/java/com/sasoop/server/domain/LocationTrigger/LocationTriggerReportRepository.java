package com.sasoop.server.domain.LocationTrigger;

import com.sasoop.server.domain.appTrigger.AppTrigger;
import com.sasoop.server.domain.locations.Locations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface LocationTriggerReportRepository extends JpaRepository<LocationTriggerReport, Long> {
    Optional<LocationTriggerReport> findByStartDateAndEndDateAndLocationsAndAppTrigger(Date startDate, Date endDate, Locations locations, AppTrigger appTrigger);
}
