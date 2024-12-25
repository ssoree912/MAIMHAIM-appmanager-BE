package com.sasoop.server.service;

import com.sasoop.server.common.DateUtils;
import com.sasoop.server.common.dto.APIResponse;
import com.sasoop.server.common.dto.enums.SuccessCode;
import com.sasoop.server.controller.dto.response.ReportResponse;
import com.sasoop.server.domain.LocationTrigger.LocationTriggerReport;
import com.sasoop.server.domain.LocationTrigger.LocationTriggerReportRepository;
import com.sasoop.server.domain.app.App;
import com.sasoop.server.domain.app.AppRepository;
import com.sasoop.server.domain.appTrigger.AppTrigger;
import com.sasoop.server.domain.locations.Locations;
import com.sasoop.server.domain.locations.LocationsRepository;
import com.sasoop.server.domain.member.Member;
import com.sasoop.server.domain.triggerRaw.TriggerRaw;
import com.sasoop.server.domain.triggerRaw.TriggerRawRepository;
import com.sasoop.server.domain.triggerReport.TriggerReport;
import com.sasoop.server.domain.triggerReport.TriggerRortRepository;
import com.sasoop.server.domain.triggerType.SettingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportService.class);
    private final TriggerRawRepository triggerRawRepository;
    private final LocationsRepository locationsRepository;
    private final LocationTriggerReportRepository locationTriggerReportRepository;
    private final TriggerRortRepository triggerRortRepository;
    private final AppRepository appRepository;

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


    public APIResponse<List<ReportResponse.ReportInfo>> getReports(Member getMember, String startDate) {
        List<App> apps = getMember.getApps();
        List<TriggerReport> triggerReports = new ArrayList<>();
        for(App app : apps){
            app.getAppTriggers().forEach((appTrigger -> {
                if(appTrigger.getTriggerType().getSettingType().equals(SettingType.LOCATION)){
                    triggerRortRepository.findByStartDateAndAppTrigger(DateUtils.getStringToDate(startDate), appTrigger)
                            .ifPresent(triggerReports::add);
                }
            }));
        }
        triggerReports.sort((report1, report2) -> Integer.compare(report2.getTotalCount(), report1.getTotalCount()));
        List<ReportResponse.ReportInfo> reportInfos = triggerReports.stream().map(ReportResponse.ReportInfo::new).collect(Collectors.toList());
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, reportInfos);

    }

    public APIResponse getAppReports(Member getMember, String startDate, Long appId) {
        App app = appRepository.findById(appId).orElseThrow(() -> new IllegalArgumentException("App not found"));
        AppTrigger appTrigger = app.getAppTriggers().stream().filter(trigger -> trigger.getTriggerType().getSettingType().equals(SettingType.LOCATION)).findFirst().orElseThrow(() -> new IllegalArgumentException("Trigger not found"));

        List<LocationTriggerReport> locationTriggerReport = locationTriggerReportRepository.findByStartDateAndAppTriggerOrderByCountDesc(DateUtils.getStringToDate(startDate), appTrigger).orElse(Collections.emptyList());
        List<ReportResponse.LocationInfo> locationInfos = locationTriggerReport.stream().map(ReportResponse.LocationInfo::new).collect(Collectors.toList());
        TriggerReport triggerReport = triggerRortRepository.findByStartDateAndAppTrigger(DateUtils.getStringToDate(startDate), appTrigger).orElse(null);
        if(triggerReport == null){
            ReportResponse.ReportInfo reportInfo = new ReportResponse.ReportInfo(app.getName(), app.getAppId());
            ReportResponse.AppReportInfo appReportInfo = new ReportResponse.AppReportInfo(reportInfo, Collections.emptyList());
            return APIResponse.of(SuccessCode.SELECT_SUCCESS, appReportInfo);
        }
        ReportResponse.ReportInfo reportInfo = new ReportResponse.ReportInfo(triggerReport);
        ReportResponse.AppReportInfo appReportInfo = new ReportResponse.AppReportInfo(reportInfo, locationInfos);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, appReportInfo);
    }
}
