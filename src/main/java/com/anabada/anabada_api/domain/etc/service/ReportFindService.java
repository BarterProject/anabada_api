package com.anabada.anabada_api.domain.etc.service;

import com.anabada.anabada_api.domain.etc.repository.ReportRepository;
import com.anabada.anabada_api.domain.etc.entity.ReportVO;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.etc.dto.PageReportDTO;
import com.anabada.anabada_api.domain.etc.dto.ReportDTO;
import com.anabada.anabada_api.domain.item.service.ItemFindService;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportFindService {

    ReportRepository reportRepository;

    ItemFindService itemFindService;
    UserFindService userFindService;


    public ReportFindService(ReportRepository reportRepository, ItemFindService itemFindService, UserFindService userFindService) {
        this.reportRepository = reportRepository;
        this.itemFindService = itemFindService;
        this.userFindService = userFindService;
    }

    @Transactional(readOnly = true)
    public ReportVO findByIdx(Long idx) {
        Optional<ReportVO> report = reportRepository.findById(idx);
        if (report.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);
        return report.get();
    }

    @Transactional(readOnly = true)
    public ReportVO findWithALlByIdx(Long idx) {
        Optional<ReportVO> report = reportRepository.findWithAllByIdx(idx);
        if (report.isEmpty())
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);
        return report.get();
    }

    @Transactional(readOnly = true)
    public Page<ReportVO> findAll(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }


    @Transactional(readOnly = true)
    public Page<ReportVO> findByItem(Long itemIdx, Pageable pageable) {
        ItemVO item = itemFindService.findByIdx(itemIdx);
        return reportRepository.findByItem(item, pageable);
    }
}

//    @Transactional(readOnly = true)
//    public PageReportDTO findAllWithAuth(Pageable pageable) throws AuthException {
//        UserVO user = userFindService.getMyUserWithAuthorities();
//
//        Page<ReportVO> page = reportRepository.findAllByUser(user, pageable);
//        List<ReportDTO> reports = page.stream().map(i -> i.dto(true, true)).collect(Collectors.toList());
//        return PageReportDTO.builder()
//                .reports(reports)
//                .currentPage(pageable.getPageNumber())
//                .totalPage(page.getTotalPages() - 1)
//                .build();
//    }