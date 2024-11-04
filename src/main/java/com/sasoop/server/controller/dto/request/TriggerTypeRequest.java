package com.sasoop.server.controller.dto.request;

import com.sasoop.server.domain.triggerType.SettingType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class TriggerTypeRequest {
    private List<CreateTriggerType> createTriggerTypes;

    @Getter
    @Setter
    public static class CreateTriggerType{
        private SettingType settingType;
        String options;
    }
}
