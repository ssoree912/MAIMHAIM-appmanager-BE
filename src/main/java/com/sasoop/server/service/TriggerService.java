package com.sasoop.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sasoop.server.common.dto.APIResponse;
import com.sasoop.server.common.dto.enums.SuccessCode;
import com.sasoop.server.controller.dto.request.AppRequest;
import com.sasoop.server.controller.dto.request.TriggerRequest;
import com.sasoop.server.controller.dto.response.AppResponse;
import com.sasoop.server.controller.dto.response.TriggerResponse;
import com.sasoop.server.domain.app.App;
import com.sasoop.server.domain.appTrigger.AppTrigger;
import com.sasoop.server.domain.appTrigger.AppTriggerRepository;
import com.sasoop.server.domain.detailFunction.DetailFunction;
import com.sasoop.server.domain.detailFunction.DetailFunctionRepository;
import com.sasoop.server.domain.triggerType.SettingType;
import com.sasoop.server.domain.triggerType.TriggerType;
import com.sasoop.server.domain.triggerType.TriggerTypeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TriggerService {
    private static final Logger log = LoggerFactory.getLogger(TriggerService.class);
    private final AppTriggerRepository appTriggerRepository;
    private final TriggerTypeService triggerTypeService;
    private final DetailFunctionRepository detailFunctionRepository;
    private final TriggerTypeRepository triggerTypeRepository;
    private final ObjectMapper objectMapper;

    public APIResponse<TriggerResponse.AppTriggers> getTriggers(App app,TriggerType triggerType){
        AppResponse.AppInfo appInfo = new AppResponse.AppInfo(app);
        List<AppTrigger> appTriggers = appTriggerRepository.findByAppAndOptionalTriggerType(app,triggerType).orElse(Collections.emptyList());
        List<TriggerResponse.Trigger> triggers = new ArrayList<>();
        for(AppTrigger appTrigger : appTriggers){
            TriggerResponse.Trigger trigger = TriggerResponse.of(appTrigger,triggerTypeService.getValue(appTrigger,appTrigger.getTriggerType()));
            triggers.add(trigger);
        }
        APIResponse response = APIResponse.of(SuccessCode.SELECT_SUCCESS, new TriggerResponse.AppTriggers(appInfo, triggers));
        return response;
    }

    public void validateTriggers(App app, TriggerRequest.ValidateTrigger triggerRequest){
        String packageName = "";
        if(validateActivate(app) && appTriggerRepository.existsByAppAndTriggerValueContaining(app,triggerRequest.getLocations())) packageName = app.getPackageName();

    }

    public APIResponse<TriggerResponse.Trigger> getCreatedTrigger(App app, TriggerRequest.CreateTrigger triggerRequest){
        AppTrigger appTrigger = createTrigger(app,triggerRequest);
        TriggerResponse.Trigger response = TriggerResponse.of(appTrigger,triggerTypeService.getSettingOptions(appTrigger.getTriggerType()));
        return APIResponse.of(SuccessCode.INSERT_SUCCESS, response);
    }


    public void activateTrigger(App app, Long triggerId, AppRequest.Activate activate){
        AppTrigger getTrigger = validateAppAndTrigger(app,triggerId);
//        모션 트리거의 경우 shakerapp변경,기존 모션 트리거 끄기
        getTrigger.updateActivate(activate.isActivate());
        AppTrigger appTrigger = appTriggerRepository.save(getTrigger);
        TriggerResponse.Trigger<?> triggerResponse = TriggerResponse.of(appTrigger, appTrigger.getTriggerValue()); // triggerValue가 필요하다면 전달

//        return app;
    }

    public void updateTrigger(App app, Long triggerId, TriggerRequest.UpdateTrigger triggerRequest){
        AppTrigger getTrigger = validateAppAndTrigger(app,triggerId);
        if(!triggerRequest.getType().equals(getTrigger.getTriggerType().getSettingType())) throw new IllegalArgumentException("바꿀 트리거타입과 트리거 id가 같지 않습니다");
        getTrigger.updateForeGround(triggerRequest.isForeGround());
        if(triggerRequest.getTriggerValue() != "" && triggerRequest.getTriggerValue() != null){
            try {
                JsonNode triggerValue = objectMapper.readTree(triggerRequest.getTriggerValue());
                getTrigger.updateTriggerValue(triggerValue);
                getTrigger.updateActivate(true);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        appTriggerRepository.save(getTrigger);
    }

    private AppTrigger validateAppAndTrigger(App app, Long triggerId){
        AppTrigger getTrigger = appTriggerRepository.findById(triggerId).orElseThrow(() -> new IllegalArgumentException("Invalid trigger id"));
        if(app != getTrigger.getApp()) throw new IllegalArgumentException("App id is not correct with trigger");
        return getTrigger;
    }

    @Transactional
    public AppTrigger createTrigger(App app, TriggerRequest.CreateTrigger triggerRequest)  {
        try {
            TriggerType getTriggerType = triggerTypeRepository.findById(triggerRequest.getTriggerTypeId()).orElseThrow(() -> new IllegalArgumentException("Invalid trigger type"));
            DetailFunction getFunction = detailFunctionRepository.findById(app.getManagedApp().getCategory().getCategoryId()).orElseThrow(() -> new IllegalArgumentException("Invalid function"));
            JsonNode triggerValue = objectMapper.readTree(triggerRequest.getTriggerValue());
            AppTrigger appTrigger = AppTrigger.toEntity(triggerRequest, triggerValue, app, getTriggerType, getFunction);
            return appTriggerRepository.save(appTrigger);

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error parsing options JSON", e);
        }
    }

    @Transactional
    public AppTrigger createTrigger(SettingType settingType ,App app)  {
        TriggerType getTriggerType = triggerTypeRepository.findBySettingType(settingType).orElseThrow(() -> new IllegalArgumentException("Invalid trigger type"));
        DetailFunction getFunction = detailFunctionRepository.findById(app.getManagedApp().getCategory().getCategoryId()).orElseThrow(() -> new IllegalArgumentException("Invalid function"));
        JsonNode triggerValue = getTriggerType.getSettingOptions();
        AppTrigger appTrigger = AppTrigger.toEntity( triggerValue, app, getTriggerType, getFunction);
        return appTriggerRepository.save(appTrigger);
    }

    private boolean validateActivate(App app){
//        앱 활성화 반펼
        if(app.isActivate()) return true;
        return false;
    }

}
