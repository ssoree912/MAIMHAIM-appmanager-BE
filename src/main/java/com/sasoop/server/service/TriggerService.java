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
import com.sasoop.server.domain.member.Member;
import com.sasoop.server.domain.member.MemberRepository;
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
    private final MemberRepository memberRepository;

    public APIResponse<TriggerResponse.AppTriggers> getTriggers(App app){
        AppResponse.AppInfo appInfo = new AppResponse.AppInfo(app);
        List<AppTrigger> appTriggers = appTriggerRepository.findByApp(app).orElse(Collections.emptyList());
        List<TriggerResponse.Trigger> triggers = new ArrayList<>();
        for(AppTrigger appTrigger : appTriggers){
            TriggerResponse.Trigger trigger = TriggerResponse.of(appTrigger,triggerTypeService.getSettingOptions(appTrigger.getTriggerType()));
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
        return APIResponse.of(SuccessCode.INSERT_SUCCESS, appTrigger);
    }

    public APIResponse<TriggerResponse.Trigger> activateTrigger(App app, Long triggerId, AppRequest.Activate activate){
        AppTrigger getTrigger = validateAppAndTrigger(app,triggerId);
        getTrigger.updateActivate(activate.isActivate());
        AppTrigger appTrigger = appTriggerRepository.save(getTrigger);
        return APIResponse.of(SuccessCode.UPDATE_SUCCESS, appTrigger);
    }

    public APIResponse<TriggerResponse.Trigger> updateTrigger(App app, Long triggerId, TriggerRequest.UpdateTrigger triggerRequest){
        try {
            AppTrigger getTrigger = validateAppAndTrigger(app,triggerId);
            if(getTrigger.getTriggerType().getSettingType().equals(SettingType.MOTION)) updateMotionApp(app);
            JsonNode triggerValue = objectMapper.readTree(triggerRequest.getTriggerValue());
            getTrigger.updateTriggerValue(triggerValue);
            AppTrigger appTrigger = appTriggerRepository.save(getTrigger);
            return APIResponse.of(SuccessCode.UPDATE_SUCCESS, appTrigger);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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

    private void updateMotionApp(App newApp){
        Member member = newApp.getMember();
        App presentApp = newApp.getMember().getShakerApp();
//        흔들기 업데이트
        member.updateShakerApp(newApp);
        memberRepository.save(member);
//        기존 앱중 모션 흔들기 false로 만들기 (시연용)
        List<AppTrigger> presentAppTriggers = presentApp.getAppTriggers();
        for(AppTrigger appTrigger : presentAppTriggers){
            if(appTrigger.getTriggerType().getSettingType().equals(SettingType.MOTION)){
                appTrigger.updateActivate(false);
                appTriggerRepository.save(appTrigger);
            }
        }
    }



}
