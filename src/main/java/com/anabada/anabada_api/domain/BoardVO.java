package com.anabada.anabada_api.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Table(name = "BOARD_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx",updatable = false,nullable = false)
    private Long idx;

    @Column(name = "name",updatable = true,nullable = false,length = 100)
    private String name;

    @Column(name = "description",updatable = true,nullable = true,length = 200)
    private String description;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "board",orphanRemoval = true)
    private List<PostVO>posts;

    @Builder
    public BoardVO(String name,String description){
        this.name=name;
        this.description=description;
    }

}
