package com.anabada.anabada_api.controller;

import com.anabada.anabada_api.domain.ReportVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.dto.MessageDTO;
import com.anabada.anabada_api.dto.PageReportDTO;
import com.anabada.anabada_api.dto.ReportDTO;
import com.anabada.anabada_api.dto.ValidationGroups;
import com.anabada.anabada_api.dto.item.ItemDTO;
import com.anabada.anabada_api.repository.ReportRepository;
import com.anabada.anabada_api.service.item.ItemFindService;
import com.anabada.anabada_api.service.report.ReportFindService;
import com.anabada.anabada_api.service.report.ReportUpdateService;
import com.anabada.anabada_api.service.user.UserFindService;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.util.List;

@Controller
@RequestMapping("/api")
public class ReportController {


    public ReportController(ReportFindService reportFindService, ReportUpdateService reportUpdateService, ItemFindService itemFindService) {
        this.reportFindService = reportFindService;
        this.reportUpdateService = reportUpdateService;
        this.itemFindService = itemFindService;
    }

    ReportFindService reportFindService;
    ReportUpdateService reportUpdateService;
    ItemFindService itemFindService;



    /**
     * 신고 단건 조회
     * 신고 단건 반환
     * @param idx report의 idx
     * @return 신고된 신고 정보
     * @throws NotFoundException 유효하지 않은 report idx
     */
    @GetMapping("/item/reports/{report-idx}")
    public ResponseEntity<ReportDTO> getReportByIdx(@PathVariable(value = "report-idx") Long idx) throws NotFoundException {
        ReportDTO dto = reportFindService.findByIdxDTO(idx);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }



    /**
     * 신고 전체조회
     * 신고 페이지로 반환
     * @param pageable 
     * @return PageReportDTO : 신고 리스트
     */
    @GetMapping("/item/reports")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageReportDTO> getAllReports(@PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable) {
        PageReportDTO page = reportFindService.findAll(pageable);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }
   



    /**
     * 특정 아이템의 신고 조회
     * @param idx : 신고한 아이템 idx
     * @param pageable
     * @return PageReportDTO : 신고 리스트 페이지
     * @throws NotFoundException : 유효하지 않은 item idx
     */
    @GetMapping("items/{item-idx}/reports")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PageReportDTO> getReportByItem(
            @PathVariable(value = "item-idx") Long idx,
            @PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable
    ) throws NotFoundException {
        ItemVO item = itemFindService.findByIdx(idx);
        PageReportDTO page = reportFindService.findByItem(item, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);

    }


    /**
     * 내가 신고한 내역 조회
     * @param pageable
     * @return PageReportDTO : 내가 신고한 내역 페이지
     * @throws AuthException 유효하지 않은 토큰
     */
    @GetMapping("/user/reports")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<PageReportDTO> getMyReports(
            @PageableDefault(size = 10,sort = "idx",direction = Sort.Direction.DESC)Pageable pageable)throws
            AuthException{
        PageReportDTO page=reportFindService.findAllWithAuth(pageable);
        return new ResponseEntity<>(page,HttpStatus.OK);
    }




    /**
     * 신고 저장 
     * @param itemIdx : 신고할 아이템 idx
     * @param reportDTO : 신고 정보
     *
     * @return reportDTO : 저장된 신고 정보
     * @throws AuthException : 유효하지 않은 토큰
     * @throws NotFoundException : 유효하지 않은 item idx
     */
    @PostMapping("/items/{item-idx}/reports")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ReportDTO> saveReport(
            @PathVariable(value = "item-idx") Long itemIdx,
            @RequestBody(required = true) @Validated(ValidationGroups.reportSaveGroup.class) ReportDTO reportDTO
    ) throws AuthException, NotFoundException {

        ReportDTO saveReport = reportUpdateService.save(itemIdx, reportDTO);

        return new ResponseEntity<>(saveReport, HttpStatus.CREATED);
    }

    /**
     * 신고 수정  ( 관리자만 허용 ) 
     * @param idx : 수정할 신고 idx
     * @param reportDTO : 수정할 신고 정보
     * @return reportDTO : 수정한 신고 정보
     * @throws AuthException : 유효하지 않은 토큰
     *                         접근 권한 오류
     * @throws NotFoundException : 존재하지 않는 신고 idx
     */
    @PutMapping("/items/reports/{report-idx}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ReportDTO> modifyReport(

            @PathVariable(value = "report-idx") Long idx,
            @RequestBody  ReportDTO reportDTO
            ) throws AuthException, NotFoundException {
     ReportDTO updatedReport = reportUpdateService.changeState(idx,reportDTO);
        return new ResponseEntity<>(updatedReport, HttpStatus.OK);
    }

    /**
     * 신고 삭제  ( 관리자만 허용 )
     * @param idx : 삭제할 신고 idx
     * @return message
     * @throws NotFoundException : 존재하지 않는 신고 idx
     */
    @DeleteMapping("items/reports/{report-idx}")
    public ResponseEntity<MessageDTO> deleteReport(
            @PathVariable(value = "report-idx") Long idx) throws NotFoundException {
        reportUpdateService.delete(idx);
        return new ResponseEntity<>(new MessageDTO("report deleted"), HttpStatus.NO_CONTENT);

    }


}
