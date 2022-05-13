package com.anabada.anabada_api.domain.board.dto;

import com.anabada.anabada_api.domain.board.entity.PostVO;
import com.anabada.anabada_api.domain.user.dto.UserDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDTO {
    private Long idx;

    private String title;

    private String content;

    private String reply;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    UserDTO user;

    @Builder
    public PostDTO(Long idx, String title, String content, String reply, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, UserDTO user) {
        this.idx = idx;
        this.title = title;
        this.content = content;
        this.reply = reply;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.user = user;
    }

    public static PostDTO fromEntity(PostVO vo){
        return PostDTO.builder()
                .idx(vo.getIdx())
                .title(vo.getTitle())
                .content(vo.getContent())
                .reply(vo.getReply())
                .createdAt(vo.getCreatedAt())
                .updatedAt(vo.getUpdatedAt())
                .deletedAt(vo.getDeletedAt())
                .build();
    }

    public static PostDTO fromEntityWithUser(PostVO vo){
        return PostDTO.builder()
                .idx(vo.getIdx())
                .title(vo.getTitle())
                .content(vo.getContent())
                .reply(vo.getReply())
                .createdAt(vo.getCreatedAt())
                .updatedAt(vo.getUpdatedAt())
                .deletedAt(vo.getDeletedAt())
                .user(UserDTO.simpleFromEntity(vo.getUser()))
                .build();
    }
}
