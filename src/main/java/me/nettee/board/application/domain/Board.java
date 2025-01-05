package me.nettee.board.application.domain;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class Board {

    private Long id;

    private String title;

    private String content;

    private Instant deletedAt;

    @Builder
    public Board(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.deletedAt = null;
    }

}
