package com.sasoop.server.controller;

import com.sasoop.server.common.dto.ErrorResponse;
import com.sasoop.server.controller.dto.request.TriggerTypeRequest;
import com.sasoop.server.service.TriggerTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/trigger-type")
@Tag(name = "TriggerType")
@RequiredArgsConstructor
public class TriggerTypeController {
    private final TriggerTypeService triggerTypeService;
    @Operation(summary = "트리거 타입 추가)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping
    public ResponseEntity<?> createTriggerType(@RequestBody TriggerTypeRequest triggerTypeRequest) {
        triggerTypeService.getCreateType(triggerTypeRequest);
        return ResponseEntity.ok().build();
    }

//    @GetMapping()
//    public ResponseEntity<SettingOption> getTriggerType(){
//        return ResponseEntity.ok(triggerTypeService.findBySettingType(SettingType.LOCATION));
//    }

}