package com.sasoop.server.controller.dto.response;

import com.sasoop.server.domain.app.App;
import com.sasoop.server.domain.category.Category;
import com.sasoop.server.domain.triggerType.SettingType;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class AppResponse {
    @Getter
    @Setter
    public static class AppInfoWithCategory{
        private Long categoryId;
        private String categoryName;
        private List<AppInfo> apps;

        public AppInfoWithCategory(Category category, List<AppInfo> apps) {
            this.categoryId = category.getCategoryId();
            this.categoryName = category.getName();
            this.apps = apps;
        }
    }


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
        private boolean add;
        @Schema(description = "앱 고급모드 활성화")
        private boolean advancedActivate;
        private String image;
        private String packageName;
        private String ssid;
        private SettingType type;


        public AppInfo(App app) {
            this.appId = app.getAppId();
            this.name = app.getName();
            this.activate = app.isActivate();
            this.add = app.isAdd();
            this.advancedActivate = app.isAdvancedActivate();
            this.image = app.getManagedApp().getImageUrl();
            this.packageName = app.getManagedApp().getPackageName();
            this.ssid = app.getManagedApp().getSSID();
            this.type = app.getSettingType();
        }
    }

}
