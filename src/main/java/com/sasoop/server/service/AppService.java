package com.sasoop.server.service;

import com.sasoop.server.common.dto.APIResponse;
import com.sasoop.server.common.dto.enums.SuccessCode;
import com.sasoop.server.controller.dto.request.AppRequest;
import com.sasoop.server.controller.dto.response.AppResponse;
import com.sasoop.server.controller.dto.response.InnerSettingResponse;
import com.sasoop.server.domain.app.App;
import com.sasoop.server.domain.app.AppRepository;
import com.sasoop.server.domain.category.Category;
import com.sasoop.server.domain.category.CategoryRepository;
import com.sasoop.server.domain.managedApp.ManagedApp;
import com.sasoop.server.domain.managedApp.ManagedAppRepository;
import com.sasoop.server.domain.member.Member;
import com.sasoop.server.domain.member.MemberRepository;
import com.sasoop.server.domain.triggerType.SettingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppService {
    private final AppRepository appRepository;
    private final ManagedAppRepository managedAppRepository;
    private final CategoryRepository categoryRepository;
    private final TriggerService triggerService;
    private final MemberRepository memberRepository;

    /**
     * 내가 가진 앱 추가
     * @param request 추가하고 싶은 앱(체크)
     * @return 앱 리스트
     */
    public APIResponse<List<AppResponse.AppInfo>> addApps(Member member,List<AppRequest.Adding> request) {
        List<App> appsList = new ArrayList<>();
        for (AppRequest.Adding activateApp : request) {
            App app = validateMemberAndApp(member,activateApp.getAppId());
            app.updateAdd(activateApp.isAdd());
            app.updateActivate(activateApp.isAdd());
            appRepository.save(app);
            appsList.add(app);
        }
        List<AppResponse.AppInfo> appInfos = appsList.stream().map(AppResponse.AppInfo::new).collect(Collectors.toList()); //저장된 앱 리스트 dto 리스트 전환
        return APIResponse.of(SuccessCode.INSERT_SUCCESS, appInfos);
    }

    public APIResponse<AppResponse.AppInfo> updateActivate(Long appId, boolean activate, Member member) {
        App app = validateMemberAndApp(member,appId);

        if(!app.isAdd()) throw new IllegalArgumentException("App is not add");

        app.updateActivate(activate);
        App updatedApp = appRepository.save(app);
        AppResponse.AppInfo appInfo = new AppResponse.AppInfo(updatedApp);

        return APIResponse.of(SuccessCode.UPDATE_SUCCESS, appInfo);
    }

    public APIResponse<AppResponse.AppInfo> updateAdvancedActivate(Long appId, boolean activate, Member member) {
        App app = validateMemberAndApp(member,appId);

        if(!app.isAdd() && !app.isActivate()) throw new IllegalArgumentException("App did not activate and add yet");

        app.updateAdvancedActivate(activate);
        App updatedApp = appRepository.save(app);
        AppResponse.AppInfo appInfo = new AppResponse.AppInfo(updatedApp);

        return APIResponse.of(SuccessCode.UPDATE_SUCCESS, appInfo);
    }
    public APIResponse<List<AppResponse.AppInfo>> findByFilter( String keyword, Member member) {
        List<App> apps = appRepository.findByMemberAndKeywordk( keyword, member).orElse(Collections.emptyList());
        List<AppResponse.AppInfo> appInfos = apps.stream().map(AppResponse.AppInfo::new).collect(Collectors.toList()); //저장된 앱 리스트 dto 리스트 전환
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, appInfos);
    }

    public APIResponse<List<AppResponse.AppInfoWithCategory>> findByFilterAndCategory(boolean add, String keyword, Member member) {
        List<App> apps = appRepository.findByFilter(add, keyword, member).orElse(Collections.emptyList());
        List<Category> categories = categoryRepository.findAllByOrderByNameAsc().orElse(Collections.emptyList());
        List<AppResponse.AppInfoWithCategory> appInfoWithCategories = categories.stream()
                .map(category -> {
                    List<AppResponse.AppInfo> filteredApps = apps.stream()
                            .filter(app -> app.getManagedApp() != null
                                    && app.getManagedApp().getCategory() != null
                                    && app.getManagedApp().getCategory().getCategoryId().equals(category.getCategoryId()))
                            .map(AppResponse.AppInfo::new)
                            .collect(Collectors.toList());
                    return new AppResponse.AppInfoWithCategory(category, filteredApps);
                })
                .filter(appInfoWithCategory -> !appInfoWithCategory.getApps().isEmpty()) // Optional: to exclude categories with no apps
                .collect(Collectors.toList());

        return APIResponse.of(SuccessCode.SELECT_SUCCESS, appInfoWithCategories);
    }

    /**
     * 내 앱 리스트 저장
     * @param appRequest 앱 환경 정보
     * @param member 사용자
     * @return 저장된 앱 정보 리스트
     */
    public APIResponse<List<AppResponse.AppInfo>> createApp(AppRequest.CreateAppSetting appRequest, Member member) {
        List<App> savedApps = new ArrayList<>();
//        앱 정보 리스트 객체화 후 리스트에 담아 한번에 저장
        for(AppRequest.AppSetting appSetting : appRequest.getApps()) {
            if(!validateApp(appSetting,member)) continue;
            ManagedApp managedApp = findByPackageName(appSetting.getPackageName());
            App app = App.toEntity(appSetting, member,false, managedApp);
            App savedApp = appRepository.save(app);
            triggerService.createTrigger(SettingType.LOCATION, app);
            triggerService.createTrigger(SettingType.MOTION, app);
            savedApps.add(savedApp);
        }
        List<AppResponse.AppInfo> appInfos = savedApps.stream().map(AppResponse.AppInfo::new).collect(Collectors.toList()); //저장된 앱 리스트 dto 리스트 전환
        return APIResponse.of(SuccessCode.INSERT_SUCCESS, appInfos);
    }

    /**
     * 앱 중복 확인
     * @param appSetting
     * @param member
     * @return
     */
    private boolean validateApp(AppRequest.AppSetting appSetting, Member member) {
//      앱매니저가 관리하는 앱인지와 내가 갖고있지 않은 앱인지 확인
        if(managedAppRepository.existsByPackageName(appSetting.getPackageName()) && !appRepository.existsByMemberAndPackageName(member,appSetting.getPackageName()) ) return true;
        return false;
    }
    private ManagedApp findByPackageName(String packageName) {
        return managedAppRepository.findByPackageName(packageName).orElseThrow(() -> new IllegalArgumentException("ManagedApp not found"));
    }

    private App validateMemberAndApp(Member member, Long appId) {
        App app = appRepository.findById(appId).orElseThrow(() -> new IllegalArgumentException("App not found"));
        if(app.getMember() != member) throw new IllegalArgumentException("Member is not match");
        return app;
    }

    public App findById(Long appId) {
        return appRepository.findById(appId).orElseThrow(() -> new IllegalArgumentException("App not found"));
    }

    public InnerSettingResponse.PackageName getLocationPackageName(Member member, String location){
        String packageName = "";
        InnerSettingResponse.PackageName response = new InnerSettingResponse.PackageName(packageName);
        ManagedApp managedApp = managedAppRepository.findByApBSSIDContaining(location).orElse(null);
        if(managedApp != null){
            log.info(managedApp.getName());
            App app = appRepository.findByMemberAndManagedApp(member, managedApp).orElse(null);
            if(app != null){
                if(validateActivate(app)) packageName = app.getPackageName();
                member.addCount();
                memberRepository.save(member);
                response.setPackageName(packageName);
            }
        }
        return response;
    }

    public InnerSettingResponse.PackageName getMotionPackageName(Member member){
        String packageName = "";
        InnerSettingResponse.PackageName response = new InnerSettingResponse.PackageName(packageName);
        App shakerApp = member.getShakerApp();
        if(shakerApp != null){
            if(validateActivate(shakerApp)) packageName = shakerApp.getPackageName();
            member.addCount();
            memberRepository.save(member);
            response.setPackageName(packageName);
        }
        return response;
    }
    private boolean validateActivate(App app){
//        앱 활성화 반펼
        if(app.isActivate() && app.isAdd()) return true;
        return false;
    }


}
