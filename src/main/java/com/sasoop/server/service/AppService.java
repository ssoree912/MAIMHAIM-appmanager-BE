package com.sasoop.server.service;

import com.sasoop.server.common.dto.APIResponse;
import com.sasoop.server.common.dto.enums.SuccessCode;
import com.sasoop.server.controller.dto.request.AppRequest;
import com.sasoop.server.controller.dto.response.AppResponse;
import com.sasoop.server.domain.app.App;
import com.sasoop.server.domain.app.AppRepository;
import com.sasoop.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppService {
    private final AppRepository appRepository;

    /**
     * 앱 리스트 저장
     * @param appRequest 앱 환경 정보
     * @param user 사용자
     * @return 저장된 앱 정보 리스트
     */
    public APIResponse<List<AppResponse.AppInfo>> createApp(AppRequest.CreateApp appRequest, User user) {
        List<App> apps = new ArrayList<>();
//        앱 정보 리스트 객체화 후 리스트에 담아 한번에 저장
        for(AppRequest.AppSetting appSetting : appRequest.getApps()) {
            App app = App.toEntity(appSetting,user);
            apps.add(app);
        }
        List<App> savedApps = appRepository.saveAll(apps);
        List<AppResponse.AppInfo> appInfos = savedApps.stream().map(AppResponse.AppInfo::new).collect(Collectors.toList()); //저장된 앱 리스트 dto 리스트 전환
        return APIResponse.of(SuccessCode.INSERT_SUCCESS, appInfos);
    }

}
