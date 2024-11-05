package com.sasoop.server.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

public class TriggerRequest {

    @Getter
    @Setter
    public static class CreateTrigger{
        private Long appId;
        private Long triggerTypeId;
        private Long functionId;
        @Schema(description = "{\"run\": false, \"popUp\": false}")
        private String triggerValue;
        private boolean activate;
        private String name;
        private String url;
    }

    @Getter
    @Setter
    public static class UpdateTrigger{
        private String triggerValue;
    }
}
