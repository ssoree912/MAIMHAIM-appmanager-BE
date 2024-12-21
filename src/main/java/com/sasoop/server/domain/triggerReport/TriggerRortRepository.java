package com.sasoop.server.domain.triggerReport;

import com.sasoop.server.domain.appTrigger.AppTrigger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface TriggerRortRepository extends JpaRepository<TriggerReport, Long> {
    Optional<TriggerReport> findByStartDateAndEndDateAndAppTrigger(Date startDate, Date endDate, AppTrigger appTrigger);
}
