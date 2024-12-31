package com.sasoop.server.controller;

import com.sasoop.server.common.dto.ErrorResponse;
import com.sasoop.server.controller.dto.request.TriggerRequest;
import com.sasoop.server.domain.app.App;
import com.sasoop.server.domain.appTrigger.AppTrigger;
import com.sasoop.server.domain.member.Member;
import com.sasoop.server.domain.triggerRaw.TriggerRaw;
import com.sasoop.server.domain.triggerType.SettingType;
import com.sasoop.server.service.*;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Slf4j
public class InnerController {
    private final MemberService memberService;
    private final AppService appService;
    private final TriggerService triggerService;
    private final ReportService reportService;
    private final LocationService locationService;

//    @Tag(name = "App")
//    @Operation(summary = "안드로이드 트리거 값 요청시 관련 패키지 조회")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공"),
//            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
//            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
//    })
//    @GetMapping("/{memberId}/")
//    public ResponseEntity<APIResponse> validateTrigger(@PathVariable("memberId") Long memberId, @RequestParam SettingType settingType, @RequestParam(required = false) String value){
//        Member getMember = memberService.findByMemberId(memberId);
//        InnerSettingResponse.PackageName packageName = appService.getLocationPackageName(getMember, value);
//        APIResponse response = APIResponse.of(SuccessCode.SELECT_SUCCESS, packageName);
//        return ResponseEntity.ok(response);
//    }

    @Operation(summary = "트리거 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Tag(name = "Triggers")
    @PostMapping("/triggers")
    public void createTrigger(@RequestBody TriggerRequest.CreateTrigger triggerRequest){
        App getApp = appService.findById(triggerRequest.getAppId());
        triggerService.getCreatedTrigger(getApp, triggerRequest);
    }

//    앱안에 여러 위치값이 있을때 validate
    @Hidden
    @Tag(name = "Triggers")
    @PostMapping("/apps/{appId}/triggers")
    public void validateTrigger(@PathVariable("appId") Long appId, @RequestParam SettingType settingType, @RequestBody TriggerRequest.ValidateTrigger triggerRequest){
        App getApp = appService.findById(appId);
        triggerService.validateTriggers(getApp,triggerRequest);
    }

    @Operation(summary="count 값 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @Tag(name = "Triggers")
    @PostMapping("/apps/{packageName}/count")
    public void addCount(@PathVariable String packageName, @RequestBody TriggerRequest.UpdateTriggerCount triggerRequest){
        Member getMember = memberService.findByMemberId(triggerRequest.getMemberId());
        AppTrigger appTrigger=triggerService.addCount(getMember, packageName, triggerRequest);
        if(triggerRequest.getType().equals(SettingType.LOCATION)){
            TriggerRaw triggerRaw = reportService.saveTriggerRaw(appTrigger, triggerRequest.getRaw().getLocation(), triggerRequest.getRaw().getAddress(), triggerRequest.getRaw().getLatitude(), triggerRequest.getRaw().getLongitude());
            locationService.saveTriggerRaw(triggerRaw,appTrigger);
        };
    }

    @Tag(name = "location")
    @PostMapping("location")
    public void addLocation(@RequestBody TriggerRequest.Raw raw){
        locationService.saveLocation( raw.getLocation(), raw.getAddress(), raw.getLatitude(), raw.getLongitude());
    }

}
