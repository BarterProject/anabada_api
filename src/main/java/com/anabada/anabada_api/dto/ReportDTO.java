package com.anabada.anabada_api.dto;

import com.anabada.anabada_api.domain.ReportVO;
import com.anabada.anabada_api.dto.item.ItemDTO;
import com.anabada.anabada_api.dto.user.UserDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class
ReportDTO {
    private Long idx;

    @NotBlank(groups = {ValidationGroups.reportSaveGroup.class}, message = "신고 제목이 입력되지 않았습니다.")
    private String title;

    @NotBlank(groups = {ValidationGroups.reportSaveGroup.class}, message = "신고 내용이 입력되지 않았습니다.")
    private String content;

    private Long state;

    private UserDTO user;


    private ItemDTO item;

    private String reply;

    @Builder
    public ReportDTO(Long idx,String title,String content,Long state,String reply,UserDTO user,ItemDTO item){
        this.idx=idx;
        this.title=title;
        this.content=content;
        this.item=item;
        this.state=state;
        this.user=user;
        this.reply=reply;
    }

    public ReportVO toEntity(){
        return ReportVO.builder()
                .title(this.getTitle())
                .content(this.getContent())
                .state(this.state)
                .reply(this.reply)
                .build();
    }


}
