package com.anabada.anabada_api.domain.etc.service;

import com.anabada.anabada_api.domain.etc.dto.CreateReport;
import com.anabada.anabada_api.domain.etc.repository.ReportRepository;
import com.anabada.anabada_api.domain.etc.entity.ReportVO;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.etc.dto.ReportDTO;
import com.anabada.anabada_api.domain.item.service.ItemFindService;
import com.anabada.anabada_api.domain.item.service.ItemUpdateService;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;

@Service
public class ReportUpdateService {
    ReportRepository reportRepository;

    ReportFindService reportFindService;
    ItemFindService itemFindService;
    UserFindService userFindService;
    ItemUpdateService itemUpdateService;

    public ReportUpdateService(ReportRepository reportRepository, ReportFindService reportFindService, ItemFindService itemFindService, UserFindService userFindService, ItemUpdateService itemUpdateService) {
        this.reportRepository = reportRepository;
        this.reportFindService = reportFindService;
        this.itemFindService = itemFindService;
        this.userFindService = userFindService;
        this.itemUpdateService = itemUpdateService;
    }

    @Transactional
    public ReportVO save(ReportVO report) {
        return reportRepository.save(report);
    }

    @Transactional
    public ReportVO save(Long itemIdx, CreateReport.Request request) {

        UserVO user = userFindService.getMyUserWithAuthorities();

        ItemVO item = itemFindService.findByIdx(itemIdx);

        ReportVO report = ReportVO.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .state(ReportVO.STATE.APPLIED.ordinal())
                .user(user)
                .item(item)
                .build();

        return reportRepository.save(report);
    }


    @Transactional
    public void changeState(Long idx, int state) {
        ReportVO report = reportFindService.findByIdx(idx);
        report.updateState(state);
    }


    @Transactional
    public void delete(Long idx) {
        ReportVO report = reportFindService.findByIdx(idx);
        reportRepository.delete(report);
    }


}
