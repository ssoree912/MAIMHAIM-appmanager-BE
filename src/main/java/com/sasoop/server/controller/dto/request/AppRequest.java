package com.sasoop.server.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class AppRequest {


    @Getter
    @Setter
    public static class AppSetting{
        @NotBlank
        @Schema(description = "앱 패키지 명",example = "com.cj.twosome")
        private String packageName;
        @NotNull
        @Schema(description = "앱 uid")
        private String uid;
    }

    @Getter
    @Setter
    public static class CreateAppSetting {
        @NotNull
        @Schema(description = "유저 기본키", example = "1")
        private Long memberId;
        @Schema(description = "앱 설정 정보 리스트")
        private List<AppSetting> apps;
    }

    @Getter
    @Setter
    public static class AddApp{
        @NotNull
        @Schema(description = "유저 기본키", example = "1")
        private Long memberId;
        @NotNull
        @Schema(description = "추가할 앱 아이디리스트")
        private List<Adding> apps;
    }
    @Getter
    @Setter
    public static class Activate{
        private Long appId;
        @Schema(description = "활성화 여부")
        private boolean activate;
    }

    @Getter
    @Setter
    public static class Adding{
        private Long appId;
        @Schema(description = "추가 여부")
        private boolean add;
    }
}
