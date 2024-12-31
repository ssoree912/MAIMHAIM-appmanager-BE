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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class LocationService {
    private final LocationsRepository locationsRepository;
    private final TriggerRawRepository triggerRawRepository;
    private final LocationTriggerReportRepository locationTriggerReportRepository;
    private final TriggerRortRepository triggerRortRepository;
    private static Long[] locationIds = {2L, 6L,7L,8L};
    private static int[] dummyCount = {5, 2, 3, 1};
    public void saveLocation(String location, String address, double latitude, double longitude){
        locationsRepository.findByLatitudeAndLongitude(latitude, longitude)
                .orElseGet(() -> locationsRepository.save(Locations.toEntity(location, address, latitude, longitude)));
    }
    @Transactional
    public void saveTriggerRaw(TriggerRaw realRaw, AppTrigger appTrigger){
        int index = 0;
        List<Locations> locations = findByIds();
        log.info(validate(locations.get(0), realRaw, appTrigger) ? true + "true" : false + "false");
        if(validate(locations.get(0), realRaw, appTrigger)) return;
        for(Locations location : locations){
            for(int i = 0; i< dummyCount[index]; i++) {
                LocationTriggerReport getLocationTriggerReport = locationTriggerReportRepository.findByStartDateAndEndDateAndLocationsAndAppTrigger(
                                DateUtils.getStartOfWeek(realRaw)[0], DateUtils.getStartOfWeek(realRaw)[1], location, appTrigger)
                        .orElseGet(() -> locationTriggerReportRepository.save(LocationTriggerReport.toEntity(DateUtils.getStartOfWeek(realRaw)[0], DateUtils.getStartOfWeek(realRaw)[1], location, appTrigger)));
                getLocationTriggerReport.addCount();
                TriggerReport triggerReport = triggerRortRepository.findByStartDateAndEndDateAndAppTrigger(
                                DateUtils.getStartOfWeek(realRaw)[0], DateUtils.getStartOfWeek(realRaw)[1], appTrigger)
                        .orElseGet(() -> triggerRortRepository.save(TriggerReport.toEntity(DateUtils.getStartOfWeek(realRaw)[0], DateUtils.getStartOfWeek(realRaw)[1], appTrigger)));
                triggerReport.addTotalCount();
                locationTriggerReportRepository.save(getLocationTriggerReport);

                int dayIndex = DateUtils.getDayOfWeekIndex(realRaw);
                triggerReport.addWeeklyReport(dayIndex);
                triggerRortRepository.save(triggerReport);
//            raw 데이터 저장
                TriggerRaw triggerRaw = TriggerRaw.toEntity(location.getLocation(), location.getAddress(), location.getLatitude(), location.getLongitude(), appTrigger);
                triggerRawRepository.save(triggerRaw);
            }
            index++;
        }

    }
    private List<Locations> findByIds(){
        List<Locations> locations = new ArrayList<>();
        for(Long locationId : locationIds){
            locations.add(locationsRepository.findById(locationId).get());
        }
        return locations;
    }
    private boolean validate(Locations location,TriggerRaw realRaw, AppTrigger appTrigger){
        return locationTriggerReportRepository.findByStartDateAndEndDateAndLocationsAndAppTrigger(
                        DateUtils.getStartOfWeek(realRaw)[0], DateUtils.getStartOfWeek(realRaw)[1], location, appTrigger)
                .map(locationTrigger -> locationTrigger.getCount() > 2)
                .orElse(false);
    }
}
