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
@RequestMapping("/api/v1/apps")
@Tag(name = "Apps")
@RequiredArgsConstructor
public class AppController {
    private final AppService appService;
    private final MemberService memberService;


    @Operation(summary = "앱 추가 / 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping("{memberId}")
    public ResponseEntity<APIResponse<List<AppResponse.AppInfo>>> createApp(@PathVariable("memberId") Long memberId ,@Valid @RequestBody AppRequest.AddApp appRequest) {
        Member getMember = memberService.findByMemberId(memberId); //유저 조회
        APIResponse response = appService.addApps(getMember,appRequest.getApps());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Operation(summary = "앱 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("{memberId}")
    public ResponseEntity<APIResponse<List<AppResponse.AppInfo>>> getApps
            (@PathVariable("memberId") Long memberId,@RequestParam(name = "idAdd",defaultValue = "false",required = false) boolean add, @RequestParam( value = "search",required = false) String keyword){
        Member getMember = memberService.findByMemberId(memberId); //유저 조회
        APIResponse response = appService.findByFilter(add,keyword,getMember);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "앱 활성화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping("{memberId}/{appId}/status")
    public ResponseEntity<APIResponse<AppResponse.AppInfo>> activate(@PathVariable("memberId") Long memberId, @PathVariable("appId") Long appId, @RequestBody AppRequest.Activate activate) {
        Member getMember = memberService.findByMemberId(memberId); //유저 조회
        APIResponse response = appService.updateActivate(appId, activate.isActivate(), getMember);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
