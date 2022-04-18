package com.anabada.anabada_api.dto.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeDTO {

    private Long idx;

    private String content;

    private LocalDateTime createdAt;

    private Long state;

    private String route;

    private String kind;

    UserDTO user;

    @Builder
    public NoticeDTO(Long idx, String content, LocalDateTime createdAt, Long state, String route, String kind, UserDTO user) {
        this.idx = idx;
        this.content = content;
        this.createdAt = createdAt;
        this.state = state;
        this.route = route;
        this.kind = kind;
        this.user = user;
    }
}
