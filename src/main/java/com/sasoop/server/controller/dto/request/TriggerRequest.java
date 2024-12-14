package com.sasoop.server.controller.dto.request;

import com.sasoop.server.domain.triggerType.SettingType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class TriggerRequest {

    @Getter
    @Setter
    public static class CreateTrigger{
        private Long appId;
        private Long triggerTypeId;
//        private Long functionId;
//        @Schema(example = "{\"run\": false, \"popUp\": false}")
        private boolean foreGround;
        private String triggerValue;

        private boolean activate;
        private String name;
        private String url;
    }

    @Getter
    @Setter
    public static class UpdateTrigger{
        private boolean foreGround;
        private String triggerValue;
        private SettingType type;
    }

    @Getter
    @Setter
    public static class ValidateTrigger{
        private String locations;
    }
}
