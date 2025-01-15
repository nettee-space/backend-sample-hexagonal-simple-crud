package me.nettee.board.adapter.driven.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import me.nettee.board.application.domain.type.BoardStatus;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.Objects;

@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "board")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private BoardStatus status;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Builder
    public BoardEntity(String title, String content, BoardStatus status) {
        this.title = title;
        this.content = content;
        this.status = status;
    }

    @Builder(
            builderClassName = "UpdateBoardBuilder",
            builderMethodName = "prepareUpdate",
            buildMethodName = "update"
    )
    public void updateBoard(String title, String content, BoardStatus status) {
        // DB단계가 아닌 Application단계에서도 null 체크
        Objects.requireNonNull(title, "title cannot be null");
        Objects.requireNonNull(content, "content cannot be null");
        Objects.requireNonNull(status, "status cannot be null");

        this.title = title;
        this.content = content;
        this.status = status;
    }

    // delete의 경우 soft delete로 처리
    // 삭제 시 deletedAt을 사용 안하고 updatedAt으로 관리
    public void softDelete() {
        this.status = BoardStatus.REMOVED;
    }
}
