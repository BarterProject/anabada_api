package com.anabada.anabada_api.domain.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.FileInfo;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "ITEM_IMAGE_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImageVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", updatable = false, nullable = false)
    private Long idx;

    @Column(name = "name")
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "order",updatable = true,nullable = false)
    private Long order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_idx_fk")
    ItemVO item;

    @Embedded
    private FileInfo fileInfo;

    @Builder
    public ItemImageVO(String name, FileInfo fileInfo,Long order) {
        this.name = name;
        this.fileInfo = fileInfo;
        this.order=order;
    }

    public void setItem(ItemVO item) {
        this.item = item;
    }

}

