package com.sasoop.server.controller.dto.response;

import lombok.Getter;
import lombok.Setter;

public class InnerSettingResponse {
    @Getter
    @Setter
    public static class TriggerDTO{
        private Long triggerTypeId;
        private String triggerTypeName;
        private String settingOption;
    }
    @Getter
    @Setter
    public static class PackageName{
        String packageName;

        public PackageName(String packageName) {
            this.packageName = packageName;
        }
    }
}
