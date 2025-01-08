package me.nettee.board.adapter.driven.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import me.nettee.board.application.domain.type.BoardStatus;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Entity(name = "board")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", length = 20, nullable = false)
    private String title;

    @Column(name = "content", length = 300, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BoardStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // 삭제
    public void softDelete() {
        this.updatedAt = Instant.now();
        this.status = BoardStatus.REMOVED;
    }
}