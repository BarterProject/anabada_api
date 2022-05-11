package com.anabada.anabada_api.domain.etc.controller;

import com.anabada.anabada_api.domain.etc.dto.CreateReport;
import com.anabada.anabada_api.domain.etc.entity.ReportVO;
import com.anabada.anabada_api.domain.message.dto.MessageDTO;
import com.anabada.anabada_api.domain.etc.dto.PageReportDTO;
import com.anabada.anabada_api.domain.etc.dto.ReportDTO;
import com.anabada.anabada_api.domain.etc.dto.ValidationGroups;
import com.anabada.anabada_api.domain.item.service.ItemFindService;
import com.anabada.anabada_api.domain.etc.service.ReportFindService;
import com.anabada.anabada_api.domain.etc.service.ReportUpdateService;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
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
import java.util.stream.Collectors;

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

    @GetMapping("/item/reports/{report-idx}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ReportDTO> getReportByIdx(
            @PathVariable(value = "report-idx") Long idx) {

        ReportVO vo = reportFindService.findWithALlByIdx(idx);
        ReportDTO dto = ReportDTO.withAllFromEntity(vo);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/item/reports")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageReportDTO> getAllReports(
            @PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReportVO> page = reportFindService.findAll(pageable);

        List<ReportDTO> reports = page.stream().map(ReportDTO::fromEntity).collect(Collectors.toList());

        PageReportDTO dto = PageReportDTO.builder()
                .reports(reports)
                .currentPage(pageable.getPageNumber())
                .totalPage(page.getTotalPages() - 1)
                .build();

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("items/{item-idx}/reports")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageReportDTO> getReportByItem(
            @PathVariable(value = "item-idx") Long itemIdx,
            @PageableDefault(size = 10, sort = "idx", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ReportVO> page = reportFindService.findByItem(itemIdx, pageable);

        List<ReportDTO> reports = page.stream().map(ReportDTO::wtihUserfromEntity).collect(Collectors.toList());

        PageReportDTO dto = PageReportDTO.builder()
                .reports(reports)
                .currentPage(page.getNumber())
                .totalPage(page.getTotalPages() - 1)
                .build();

        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

    @PostMapping("/items/{item-idx}/reports")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<CreateReport.Response> saveReport(
            @PathVariable(value = "item-idx") Long itemIdx,
            @RequestBody(required = true) CreateReport.Request request
    ) {
        ReportVO vo = reportUpdateService.save(itemIdx, request);
        return new ResponseEntity<>(new CreateReport.Response(vo.getIdx()), HttpStatus.CREATED);
    }

    @PutMapping("/items/reports/{report-idx}/complete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> modifyReport(
            @PathVariable(value = "report-idx") Long idx
    ) {
        reportUpdateService.changeState(idx, ReportVO.STATE.COMPLETED.ordinal());

        return new ResponseEntity<>(new MessageDTO("state changed"), HttpStatus.OK);
    }

    @DeleteMapping("items/reports/{report-idx}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageDTO> deleteReport(
            @PathVariable(value = "report-idx") Long idx) {
        reportUpdateService.delete(idx);
        return new ResponseEntity<>(new MessageDTO("report deleted"), HttpStatus.NO_CONTENT);

    }


}

//    /**
//     * 내가 신고한 내역 조회
//     * @param pageable
//     * @return PageReportDTO : 내가 신고한 내역 페이지
//     * @throws AuthException 유효하지 않은 토큰
//     */
//    @GetMapping("/user/reports")
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
//    public ResponseEntity<PageReportDTO> getMyReports(
//            @PageableDefault(size = 10,sort = "idx",direction = Sort.Direction.DESC)Pageable pageable)throws
//            AuthException{
//        PageReportDTO page=reportFindService.findAllWithAuth(pageable);
//        return new ResponseEntity<>(page,HttpStatus.OK);
//    }
