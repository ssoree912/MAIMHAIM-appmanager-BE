package com.sasoop.server.controller.dto.response;

import com.sasoop.server.domain.LocationTrigger.LocationTriggerReport;
import com.sasoop.server.domain.triggerReport.TriggerReport;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportResponse {
    @Getter
    @Setter
    public static class ReportInfo {
        private String appName;
        private int count;
        private Long appId;
        private List<Long> weeklyReport;

        public ReportInfo(String appName, Long appId) {
            this.appName = appName;
            this.count = 0;
            this.appId = appId;

        }


        public ReportInfo(TriggerReport triggerReport) {
            this.appName = triggerReport.getAppTrigger().getApp().getName();
            this.count = triggerReport.getTotalCount();
            this.appId = triggerReport.getAppTrigger().getApp().getAppId();
            this.weeklyReport = triggerReport.getWeeklyReport();
        }
    }

    @Getter
    @Setter
    public static class LocationInfo {
        private String address;
        private int count;

        public LocationInfo(LocationTriggerReport locationTriggerReport) {
            this.address = locationTriggerReport.getLocations().getAddress();
            this.count = locationTriggerReport.getCount();
        }
    }

    @Getter
    @Setter
    public static class AppReportInfo{
        private ReportInfo reportInfo;
        private List<LocationInfo> locationInfos;

        public AppReportInfo(ReportInfo reportInfo, List<LocationInfo> locationInfos) {
            this.reportInfo = reportInfo;
            this.locationInfos = locationInfos;
        }
    }
}
