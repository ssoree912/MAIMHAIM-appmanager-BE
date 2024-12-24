package com.sasoop.server.controller.dto.response;

import com.sasoop.server.domain.appTrigger.AppTrigger;
import com.sasoop.server.domain.triggerReport.TriggerReport;
import lombok.Getter;
import lombok.Setter;

public class ReportResponse {
    @Getter
    @Setter
    public static class ReportInfo {
        private String appName;
        private int count;
        private Long appId;

        public ReportInfo(TriggerReport triggerReport) {
            this.appName = triggerReport.getAppTrigger().getApp().getName();
            this.count = triggerReport.getTotalCount();
            this.appId = triggerReport.getAppTrigger().getApp().getAppId();
        }
    }
}
