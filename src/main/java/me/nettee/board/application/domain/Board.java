package me.nettee.board.application.domain;

import lombok.*;

import java.time.Instant;
import me.nettee.board.application.domain.type.BoardStatus;

@Getter
@NoArgsConstructor
public class Board {

    private Long id;

    private String title;

    private BoardStatus status;

    private String content;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant deletedAt;

    @Builder
    public Board(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.deletedAt = null;
    }

}
