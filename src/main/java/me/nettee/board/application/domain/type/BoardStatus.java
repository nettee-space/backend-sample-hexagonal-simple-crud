package me.nettee.board.application.domain.type;

import java.util.EnumSet;
import java.util.Set;

public enum BoardStatus {

    PENDING,
    ACTIVE,
    SUSPENDED,
    REMOVED;

    private static final Set<BoardStatus> GENERAL_QUERY_STATUS = EnumSet.of(ACTIVE, SUSPENDED);

    public static Set<BoardStatus> getGeneralQueryStatus() {
       return GENERAL_QUERY_STATUS;
    }

}
