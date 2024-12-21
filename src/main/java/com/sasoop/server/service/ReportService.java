package com.sasoop.server.service;

import com.sasoop.server.common.DateUtils;
import com.sasoop.server.domain.LocationTrigger.LocationTriggerReport;
import com.sasoop.server.domain.LocationTrigger.LocationTriggerReportRepository;
import com.sasoop.server.domain.appTrigger.AppTrigger;
import com.sasoop.server.domain.locations.Locations;
import com.sasoop.server.domain.locations.LocationsRepository;
import com.sasoop.server.domain.triggerRaw.TriggerRaw;
import com.sasoop.server.domain.triggerRaw.TriggerRawRepository;
import com.sasoop.server.domain.triggerReport.TriggerReport;
import com.sasoop.server.domain.triggerReport.TriggerRortRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportService.class);
    private final TriggerRawRepository triggerRawRepository;
    private final LocationsRepository locationsRepository;
    private final LocationTriggerReportRepository locationTriggerReportRepository;
    private final TriggerRortRepository triggerRortRepository;

    @Transactional
    public void saveTriggerRaw(AppTrigger appTrigger, String location, String address, double latitude, double longitude){
        TriggerRaw triggerRaw = TriggerRaw.toEntity(location, address, latitude, longitude, appTrigger);
        TriggerRaw savedRaw = triggerRawRepository.save(triggerRaw);
        Locations locations = locationsRepository.findByLatitudeAndLongitude(latitude, longitude)
                .orElseGet(() -> locationsRepository.save(Locations.toEntity(location, address, latitude, longitude)));
        LocationTriggerReport getLocationTriggerReport = locationTriggerReportRepository.findByStartDateAndEndDateAndLocationsAndAppTrigger(
                DateUtils.getStartOfWeek(savedRaw)[0], DateUtils.getStartOfWeek(savedRaw)[1], locations, appTrigger)
                .orElseGet(() -> locationTriggerReportRepository.save(LocationTriggerReport.toEntity(DateUtils.getStartOfWeek(savedRaw)[0], DateUtils.getStartOfWeek(savedRaw)[1], locations, appTrigger)));
        TriggerReport triggerReport = triggerRortRepository.findByStartDateAndEndDateAndAppTrigger(
                        DateUtils.getStartOfWeek(savedRaw)[0], DateUtils.getStartOfWeek(savedRaw)[1], appTrigger)
                .orElseGet(() -> triggerRortRepository.save(TriggerReport.toEntity(DateUtils.getStartOfWeek(savedRaw)[0], DateUtils.getStartOfWeek(savedRaw)[1], appTrigger)));

        getLocationTriggerReport.addCount();
        locationTriggerReportRepository.save(getLocationTriggerReport);

        triggerReport.addTotalCount();
        int dayIndex = DateUtils.getDayOfWeekIndex(savedRaw);
        log.info(triggerReport.getWeeklyReport().toString());
        triggerReport.addWeeklyReport(dayIndex);
        triggerRortRepository.save(triggerReport);

    }


}
