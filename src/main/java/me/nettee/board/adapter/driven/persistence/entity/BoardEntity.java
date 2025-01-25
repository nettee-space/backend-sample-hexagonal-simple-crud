package me.nettee.board.adapter.driven.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import me.nettee.board.application.domain.type.BoardStatus;
import me.nettee.core.jpa.entity.BaseTimeEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "board")
public class BoardEntity extends BaseTimeEntity {
    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private BoardStatus status;


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
        Objects.requireNonNull(title, "title cannot be null");
        Objects.requireNonNull(content, "content cannot be null");
        Objects.requireNonNull(status, "status cannot be null");

        this.title = title;
        this.content = content;
        this.status = status;
    }
}
