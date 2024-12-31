package com.sasoop.server.controller;

import com.sasoop.server.common.dto.APIResponse;
import com.sasoop.server.common.dto.ErrorResponse;
import com.sasoop.server.common.dto.enums.SuccessCode;
import com.sasoop.server.controller.dto.response.ReportResponse;
import com.sasoop.server.domain.member.Member;
import com.sasoop.server.service.MemberService;
import com.sasoop.server.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.PipedOutputStream;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Reports")
@RequiredArgsConstructor
public class ReportController {
    private final MemberService memberService;
    private final ReportService reportService;
    @Operation(summary = "전체 리포트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{memberId}/{startDate}")
    public ResponseEntity<APIResponse<List<ReportResponse.ReportInfo>>> getReports(@PathVariable("memberId") Long memberId, @PathVariable("startDate") String startDate) {
        Member getMember = memberService.findByMemberId(memberId);
        List<ReportResponse.ReportInfo> reportInfos = reportService.getReports(getMember, startDate);
        return new ResponseEntity<>(APIResponse.of(SuccessCode.SELECT_SUCCESS,reportInfos), HttpStatus.OK);
    }

    @Operation(summary = "앱 별 리포트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{memberId}/{startDate}/{appId}")
    public ResponseEntity<APIResponse<ReportResponse.AppReportInfo>> getAppReports(@PathVariable("memberId") Long memberId, @PathVariable("startDate") String startDate, @PathVariable("appId") Long appId)  {
        Member getMember = memberService.findByMemberId(memberId);
        APIResponse response = reportService.getAppReports(getMember, startDate, appId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "최대 이용 앱 지도 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/map/{memberId}/{startDate}")
    public ResponseEntity<APIResponse<List<ReportResponse.MapReport>>> getMap(@PathVariable("memberId") Long memberId, @PathVariable("startDate") String startDate) {
        Member getMember = memberService.findByMemberId(memberId);
        List<ReportResponse.Map> maps = reportService.getMap(getMember, startDate);
        List<ReportResponse.ReportInfo> reports = reportService.getReports(getMember, startDate);
        APIResponse response = APIResponse.of(SuccessCode.SELECT_SUCCESS, new ReportResponse.MapReport(maps, reports));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary = "앱별 이용 지도 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/map/{memberId}/{startDate}/{appId}")
    public ResponseEntity<APIResponse<ReportResponse.MapByApp>> getMapByApp(@PathVariable("memberId") Long memberId, @PathVariable("startDate") String startDate, @PathVariable("appId") Long appId) {
        Member getMember = memberService.findByMemberId(memberId);
        APIResponse response = reportService.getMapByApp(getMember, startDate, appId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
