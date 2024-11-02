package com.sasoop.server.service;

import com.sasoop.server.common.dto.APIResponse;
import com.sasoop.server.common.dto.enums.SuccessCode;
import com.sasoop.server.controller.dto.request.AppRequest;
import com.sasoop.server.controller.dto.response.AppResponse;
import com.sasoop.server.domain.managedApp.ManagedAppRepository;
import com.sasoop.server.domain.member.Member;
import com.sasoop.server.domain.app.App;
import com.sasoop.server.domain.app.AppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppService {
    private final AppRepository appRepository;
    private final ManagedAppRepository managedAppRepository;


    /**
     * 앱 리스트 저장
     * @param appRequest 앱 환경 정보
     * @param member 사용자
     * @return 저장된 앱 정보 리스트
     */
    public APIResponse<List<AppResponse.AppInfo>> createApp(AppRequest.CreateApp appRequest, Member member) {
        List<App> apps = new ArrayList<>();
//        앱 정보 리스트 객체화 후 리스트에 담아 한번에 저장
        for(AppRequest.AppSetting appSetting : appRequest.getApps()) {
            if(!validateApp(appSetting,member)) continue;
            App app = App.toEntity(appSetting, member);
            apps.add(app);
        }
        List<App> savedApps = appRepository.saveAll(apps);
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


}
