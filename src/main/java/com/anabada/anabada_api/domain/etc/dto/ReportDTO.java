package com.anabada.anabada_api.domain.etc.dto;

import com.anabada.anabada_api.domain.etc.entity.ReportVO;
import com.anabada.anabada_api.domain.item.dto.ItemDTO;
import com.anabada.anabada_api.domain.user.dto.UserDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class
ReportDTO {
    private Long idx;

    @NotBlank(groups = {ValidationGroups.reportSaveGroup.class}, message = "신고 제목이 입력되지 않았습니다.")
    private String title;

    @NotBlank(groups = {ValidationGroups.reportSaveGroup.class}, message = "신고 내용이 입력되지 않았습니다.")
    private String content;

    private int state;

    private UserDTO user;

    private ItemDTO item;

    private String reply;

    @Builder
    public ReportDTO(Long idx, String title, String content, int state, String reply, UserDTO user, ItemDTO item) {
        this.idx = idx;
        this.title = title;
        this.content = content;
        this.item = item;
        this.state = state;
        this.user = user;
        this.reply = reply;
    }

    public static ReportDTO withAllFromEntity(ReportVO vo) {
        return ReportDTO.builder()
                .content(vo.getContent())
                .title(vo.getTitle())
                .state(vo.getState())
                .item(ItemDTO.listFromEntity(vo.getItem()))
                .reply(vo.getReply())
                .user(UserDTO.simpleFromEntity(vo.getUser()))
                .build();
    }

    public static ReportDTO fromEntity(ReportVO vo) {
        return ReportDTO.builder()
                .content(vo.getContent())
                .title(vo.getTitle())
                .state(vo.getState())
                .reply(vo.getReply())
                .build();
    }

    public static ReportDTO wtihUserfromEntity(ReportVO vo) {
        return ReportDTO.builder()
                .content(vo.getContent())
                .title(vo.getTitle())
                .state(vo.getState())
                .reply(vo.getReply())
                .user(UserDTO.simpleFromEntity(vo.getUser()))
                .build();
    }


}
