package com.sasoop.server.controller.dto.response;

import com.sasoop.server.domain.LocationTrigger.LocationTriggerReport;
import com.sasoop.server.domain.app.App;
import com.sasoop.server.domain.locations.Locations;
import com.sasoop.server.domain.triggerReport.TriggerReport;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ReportResponse {
    @Getter
    @Setter
    public static class ReportInfo {
        private String appName;
        private String image;
        private int count;
        private Long appId;
        private List<Long> weeklyReport;

        public ReportInfo(String appName, Long appId, String imageUrl) {
            this.appName = appName;
            this.count = 0;
            this.appId = appId;
            this.image = imageUrl;

        }


        public ReportInfo(TriggerReport triggerReport) {
            this.appName = triggerReport.getAppTrigger().getApp().getName();
            this.image = triggerReport.getAppTrigger().getApp().getManagedApp().getImageUrl();
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
    public static class Coordinate{
        private double latitude;
        private double longitude;

        public Coordinate(Locations locations) {
            this.latitude = locations.getLatitude();
            this.longitude = locations.getLongitude();
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

    @Getter
    @Setter
    public static class Map{
        private AppData app;
        private Coordinate coordinate;

        public Map(LocationTriggerReport locationTriggerReport) {
            this.app = new AppData(locationTriggerReport.getAppTrigger().getApp());
            this.coordinate = new Coordinate(locationTriggerReport.getLocations());
        }
    }
    @Getter
    @Setter
    public static class AppData {
        private Long appId;
        private String appName;
        private String image;

        public AppData(App app) {
            this.appId = app.getAppId();
            this.appName = app.getName();
            this.image = app.getManagedApp().getImageUrl();
        }
    }
    @Getter
    @Setter
    public static class MapByApp{
        private AppData app;
        private List<Coordinate> coordinates;

        public MapByApp(App app, List<Coordinate> coordinates) {
            this.app = new AppData(app);
            this.coordinates = coordinates;
        }
    }
    @Getter
    @Setter
    public static class MapReport{
        List<Map> maps;
        List<ReportInfo> appList;

        public MapReport(List<Map> maps, List<ReportInfo> reports) {
            this.maps = maps;
            this.appList = reports;
        }
    }
}
