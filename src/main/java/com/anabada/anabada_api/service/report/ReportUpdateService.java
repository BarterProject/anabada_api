package com.anabada.anabada_api.service.report;

import com.anabada.anabada_api.domain.ReportVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.ReportDTO;
import com.anabada.anabada_api.repository.ReportRepository;
import com.anabada.anabada_api.service.item.ItemFindService;
import com.anabada.anabada_api.service.item.ItemUpdateService;
import com.anabada.anabada_api.service.user.UserFindService;
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
    public ReportDTO save(Long itemIdx, ReportDTO reportDTO) throws AuthException, NotFoundException {

        UserVO user = userFindService.getMyUserWithAuthorities();

        //아이템에서 신고할 아이템 idx를 가져온다.
        ItemVO item = itemFindService.findByIdx(itemIdx);

        ReportVO report = ReportVO.builder()
                .title(reportDTO.getTitle())
                .content(reportDTO.getContent())
                .state(1L)
                .user(user)
                .item(item)
                .build();

        ReportVO savedReport = reportRepository.save(report);

        return savedReport.dto(true, true);

    }


    @Transactional
    public ReportDTO changeState(Long idx,ReportDTO reportDTO)throws NotFoundException{
        ItemVO item=itemFindService.findByIdx(idx);
        ReportVO report=reportFindService.findByIdx(idx);

        report.updateState(reportDTO.getState());
        this.save(report);
        return report.dto(true,true);
    }


    @Transactional
    public void delete(Long idx) throws NotFoundException {
        ItemVO item = itemFindService.findByIdx(idx);
        ReportVO report = reportFindService.findByIdx(idx);
        reportRepository.delete(report);

    }


}