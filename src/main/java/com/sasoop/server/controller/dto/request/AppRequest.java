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
        private String name;
    }

    @Getter
    @Setter
    public static class CreateApp {
        @NotNull
        @Schema(description = "유저 기본키", example = "1")
        private Long userId;
        @Schema(description = "앱 설정 정보 리스트")
        private List<AppSetting> appSettings;

    }
}
