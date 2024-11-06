package com.sasoop.server.controller;

import com.sasoop.server.common.dto.APIResponse;
import com.sasoop.server.common.dto.ErrorResponse;
import com.sasoop.server.controller.dto.request.AppRequest;
import com.sasoop.server.controller.dto.response.AppResponse;
import com.sasoop.server.domain.member.Member;
import com.sasoop.server.service.AppService;
import com.sasoop.server.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/managed-apps")
@Tag(name = "ManagedApps")
@RequiredArgsConstructor
public class ManagedAppController {
    private final AppService appService;
    private final MemberService memberService;
    /**
     * 앱 추가 API
     * @param appRequest 앱 환경 정보
     * @return 저장된 앱 정보 리스트
     */
    @Operation(summary = "내가 다운 받은 앱 리스트 추가(내부)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping
    public ResponseEntity<APIResponse<List<AppResponse.AppInfo>>> createAPP(@Valid @RequestBody AppRequest.CreateAppSetting appRequest){
        Member getMember = memberService.findByMemberId(appRequest.getMemberId()); //유저 조회
        APIResponse response = appService.createApp(appRequest, getMember); //저장한 앱 리스트
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
