package com.anabada.anabada_api.service.report;

import com.anabada.anabada_api.domain.ReportVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.user.UserVO;
import com.anabada.anabada_api.dto.PageReportDTO;
import com.anabada.anabada_api.dto.ReportDTO;
import com.anabada.anabada_api.repository.ReportRepository;
import com.anabada.anabada_api.service.item.ItemFindService;
import com.anabada.anabada_api.service.user.UserFindService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    ItemFindService itemFindService;
    UserFindService userFindService;


    public ReportFindService(ReportRepository reportRepository, ItemFindService itemFindService, UserFindService userFindService) {
        this.reportRepository = reportRepository;
        this.itemFindService = itemFindService;
        this.userFindService = userFindService;
    }

    @Transactional(readOnly = true)
    public ReportVO findByIdx(Long idx)throws NotFoundException{
        Optional<ReportVO> report=reportRepository.findById(idx);
        if(report.isEmpty())
            throw new NotFoundException("invalid");
        return report.get();
    }

    @Transactional(readOnly = true)
    public ReportDTO findByIdxDTO(Long idx)throws NotFoundException{
        ReportVO report=this.findByIdx(idx);

        return report.dto(true,true);
    }

    @Transactional(readOnly = true)
    public PageReportDTO findAll(Pageable pageable){
        Page<ReportVO> page=reportRepository.findAll(pageable);
        List<ReportDTO>reports=page.stream().map(i->i.dto(false,true)).collect(Collectors.toList());

        return PageReportDTO.builder()
                .reports(reports)
                .currentPage(pageable.getPageNumber())
                .totalPage(page.getTotalPages()-1)
                .build();
    }


    public PageReportDTO findByItem(ItemVO item,Pageable pageable) {
        Page<ReportVO>page=reportRepository.findByItem(item,pageable);

        List<ReportDTO> reports=page.stream().map(i->i.dto(true,true)).collect(Collectors.toList());

        return PageReportDTO.builder()
                .reports(reports)
                .currentPage(page.getTotalPages())
                .totalPage(page.getTotalPages()-1)
                .build();

    }

    public PageReportDTO findAllWithAuth(Pageable pageable)throws AuthException{
        UserVO user=userFindService.getMyUserWithAuthorities();

        Page<ReportVO>page=reportRepository.findAllByUser(user,pageable);
        List<ReportDTO>reports=page.stream().map(i->i.dto(true,true)).collect(Collectors.toList());
        return PageReportDTO.builder()
                .reports(reports)
                .currentPage(pageable.getPageNumber())
                .totalPage(page.getTotalPages()-1)
                .build();
    }
}
