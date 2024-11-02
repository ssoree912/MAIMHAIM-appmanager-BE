package com.sasoop.server.controller.dto.response;

import com.sasoop.server.domain.app.App;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class AppResponse {

    @Getter
    @Setter
    @Schema(description = "앱 정보")
    public static class AppInfo{
        @Schema(description = "=앱 기본키",example = "1")
        private Long appId;
        @Schema(description = "앱 이름",example = "starbucks")
        private String name;
        @Schema(description = "앱 기본모드 활성화")
        private boolean activate;
        @Schema(description = "앱 고급모드 활성화")
        private boolean advancedActivate;
        private String image;

        public AppInfo(App app) {
            this.appId = app.getAppId();
            this.name = app.getName();
            this.activate = app.isActivate();
            this.advancedActivate = app.isAdvancedActivate();
            this.image = app.getManagedApp().getImageUrl();
        }
    }

}
