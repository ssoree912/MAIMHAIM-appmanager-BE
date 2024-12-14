package com.sasoop.server.controller;

import com.sasoop.server.common.dto.APIResponse;
import com.sasoop.server.common.dto.ErrorResponse;
import com.sasoop.server.controller.dto.request.AppRequest;
import com.sasoop.server.controller.dto.request.TriggerRequest;
import com.sasoop.server.controller.dto.response.TriggerResponse;
import com.sasoop.server.domain.app.App;
import com.sasoop.server.domain.triggerType.SettingType;
import com.sasoop.server.domain.triggerType.TriggerType;
import com.sasoop.server.service.AppService;
import com.sasoop.server.service.TriggerService;
import com.sasoop.server.service.TriggerTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/apps")
@Tag(name = "Triggers")
@RequiredArgsConstructor
public class TriggerController {
    private final TriggerService triggerService;
    private final AppService appService;
    private final TriggerTypeService triggerTypeService;

    @Operation(summary = "앱에 따른 트리거 값 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{appId}/triggers")
    public ResponseEntity<APIResponse<TriggerResponse.AppTriggers>> getTriggers(@PathVariable("appId") Long appId , @RequestParam(required = false)SettingType settingType){
        App getApp = appService.findById(appId);
        TriggerType triggerType = (settingType != null ) ? triggerTypeService.findByTriggerType(settingType) : null;
        APIResponse response = triggerService.getTriggers(getApp,triggerType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "트리거 활성화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping("/{appId}/triggers/{triggerId}/status")
    public ResponseEntity<APIResponse<TriggerResponse.AppTriggers>> activateTrigger(@PathVariable("appId") Long appId, @PathVariable("triggerId") Long triggerId,
                                                                                @RequestBody AppRequest.Activate activate){
        App getApp = appService.findById(appId);
        triggerService.activateTrigger(getApp, triggerId, activate);
        APIResponse response = triggerService.getTriggers(getApp,null);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @Operation(summary = "트리거 설정 값 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping("/{appId}/triggers/{triggerId}")
    public ResponseEntity<APIResponse<TriggerResponse.AppTriggers>> updateTrigger(@PathVariable("appId") Long appId, @PathVariable("triggerId") Long triggerId,
                                                                                @RequestBody TriggerRequest.UpdateTrigger triggerRequest){
        App getApp = appService.findById(appId);
        appService.updateType(getApp,triggerRequest.getType());
        triggerService.updateTrigger(getApp, triggerId, triggerRequest);
        APIResponse response = triggerService.getTriggers(getApp,null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
