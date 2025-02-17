//package me.nettee.board.application.service;
//
//import java.util.Set;
//import lombok.RequiredArgsConstructor;
//import me.nettee.board.application.domain.type.BoardStatus;
//import me.nettee.board.application.model.BoardReadDetailModel;
//import me.nettee.board.application.model.BoardReadSummaryModel;
//import me.nettee.board.application.port.BoardQueryPort;
//import me.nettee.board.application.usecase.BoardReadByStatusesUseCase;
//import me.nettee.board.application.usecase.BoardReadUseCase;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class BoardQueryService implements BoardReadUseCase, BoardReadByStatusesUseCase {
//
//    private final BoardQueryPort boardQueryPort;
//
//    @Override
//    public BoardReadDetailModel getBoard(Long id) {
//        return boardQueryPort.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
//    }
//
//    @Override
//    public Page<BoardReadSummaryModel> findByStatuses(Set<BoardStatus> statuses, Pageable pageable) {
//        return boardQueryPort.findByStatusesList(pageable, statuses);
//    }
//}
