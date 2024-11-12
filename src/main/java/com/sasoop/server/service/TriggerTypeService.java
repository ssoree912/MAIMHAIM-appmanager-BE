package com.sasoop.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sasoop.server.controller.dto.request.TriggerTypeRequest;
import com.sasoop.server.domain.appTrigger.AppTrigger;
import com.sasoop.server.domain.triggerType.SettingOption;
import com.sasoop.server.domain.triggerType.SettingType;
import com.sasoop.server.domain.triggerType.TriggerType;
import com.sasoop.server.domain.triggerType.TriggerTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TriggerTypeService {

    private final ObjectMapper objectMapper;
    private final TriggerTypeRepository triggerTypeRepository;

    public void getCreateType(TriggerTypeRequest triggerTypeRequest){
        for(TriggerTypeRequest.CreateTriggerType request : triggerTypeRequest.getCreateTriggerTypes() ){
            createTriggerType(request);
        }
    }


    @Transactional
    public TriggerType createTriggerType(TriggerTypeRequest.CreateTriggerType request) {
        try {
            // Convert options JSON string to JsonNode
            JsonNode settingOptions = objectMapper.readTree(request.getOptions());

            // Create TriggerType entity and save to DB
            TriggerType triggerType = TriggerType.builder()
                    .triggerTypeName("Sample Trigger Name")  // Add a name or get it from request if available
                    .settingType(request.getSettingType())
                    .settingOptions(settingOptions)
                    .build();

            return triggerTypeRepository.save(triggerType);

        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing options JSON", e);
        }
    }

    public <T> T getValueAsObject(AppTrigger appTrigger, Class<T> targetType) {
        try {
            return objectMapper.treeToValue(appTrigger.getTriggerValue(), targetType);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to " + targetType.getSimpleName(), e);
        }
    }
    public <T> T getSettingOptionsAsObject(TriggerType triggerType, Class<T> targetType) {
        try {
            return objectMapper.treeToValue(triggerType.getSettingOptions(), targetType);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to " + targetType.getSimpleName(), e);
        }
    }

    public Object getSettingOptions(TriggerType triggerType) {
        switch (triggerType.getSettingType()) {
            case LOCATION:
                return getSettingOptionsAsObject(triggerType, SettingOption.LocationSettings.class);
            case TIME:
                return getSettingOptionsAsObject(triggerType, SettingOption.TimeSettings.class);
            default:
                throw new IllegalArgumentException("Unsupported SettingType");
        }
    }

    public <T> T getSettingValuesObject(AppTrigger appTrigger, Class<T> targetType) {
        try {
            return objectMapper.treeToValue(appTrigger.getTriggerValue(), targetType);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to " + targetType.getSimpleName(), e);
        }
    }

    public Object getValue(AppTrigger appTrigger ,TriggerType triggerType) {
        switch (triggerType.getSettingType()) {
            case LOCATION:
                return getSettingValuesObject(appTrigger, SettingOption.LocationSettings.class);
            case TIME:
                return getSettingValuesObject(appTrigger, SettingOption.TimeSettings.class);
            case SCHEDULE:
                return getSettingValuesObject(appTrigger,SettingOption.TimeSettings.class);
            default:
                throw new IllegalArgumentException("Unsupported SettingType");
        }
    }

    public SettingOption findBySettingType(SettingType settingType) {
        TriggerType triggerType = triggerTypeRepository.findBySettingType(settingType).orElseThrow(() -> new IllegalArgumentException("없음"));
        Object setting = getSettingOptions(triggerType);
        if (setting instanceof SettingOption.LocationSettings) {
            SettingOption.LocationSettings locationSettings = (SettingOption.LocationSettings) setting;
            return SettingOption.of(locationSettings);
        }  else if (setting instanceof SettingOption.TimeSettings) {
            SettingOption.TimeSettings timeSettings = (SettingOption.TimeSettings) setting;
            return SettingOption.of(timeSettings);
            // Now you can access timeSettings.time
        }else {
            throw new IllegalArgumentException("Invalid SettingType");
        }
    }
    public TriggerType findByTriggerType(SettingType settingType) {
        return triggerTypeRepository.findBySettingType(settingType).orElseThrow(()-> new IllegalArgumentException("Invalid SettingType"));
    }

}
