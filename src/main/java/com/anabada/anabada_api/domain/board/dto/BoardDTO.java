package com.anabada.anabada_api.domain.board.dto;

import com.anabada.anabada_api.domain.board.entity.BoardVO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDTO {

    private Long idx;

    private String name;

    private String description;


    @Builder
    public BoardDTO(Long idx, String name, String description) {
        this.idx = idx;
        this.name = name;
        this.description = description;
    }

    public static BoardDTO fromEntity(BoardVO vo){
        return BoardDTO.builder()
                .idx(vo.getIdx())
                .name(vo.getName())
                .description(vo.getDescription())
                .build();
    }
}
