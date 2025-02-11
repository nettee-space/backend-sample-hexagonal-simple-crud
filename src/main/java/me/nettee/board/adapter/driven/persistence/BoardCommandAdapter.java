package me.nettee.board.adapter.driven.persistence;
import lombok.RequiredArgsConstructor;
import me.nettee.board.adapter.driven.persistence.mapper.BoardEntityMapper;
import me.nettee.board.application.domain.Board;
import me.nettee.board.application.port.BoardCommandPort;
import org.springframework.stereotype.Repository;
import me.nettee.board.application.exception.BoardCommandErrorCode;
import java.lang.reflect.Field;
import jakarta.persistence.Column;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardCommandAdapter implements BoardCommandPort {

    private final BoardJpaRepository boardJpaRepository;
    private final BoardEntityMapper boardEntityMapper;

    @Override
    public Optional<Board> findById(Long id) {
        return Optional.empty();
    }

    private boolean isNotNullField(Field field) {
        return (field.isAnnotationPresent(Column.class) && !field.getAnnotation(Column.class).nullable())
                || field.isAnnotationPresent(NotNull.class);
    }
    private boolean isValidateNotNullFields(Object entity) {
        for (Field field: entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (isNotNullField(field) && field.get(entity) == null) {
                    return false;
                }
            } catch (IllegalAccessException e){
                throw new RuntimeException("필드 접근 실패", e);
            }
        }
        return true;
    }

    private boolean isValidateType(Object entity) {
        for (Field field: entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (value != null && !field.getType().isInstance(value)) {
                    return false;
                }
            } catch (IllegalAccessException e){
                throw new RuntimeException("필드 접근 실패", e);
            }
        }
        return true;
    }

    @Override
    public Board create(Board board) {
        var boardEntity = boardEntityMapper.toEntity(board);
        if (boardJpaRepository.existsById(boardEntity.getId())) throw BoardCommandErrorCode.DEFAULT.defaultException(); //키값 중복 확인
        if (!isValidateNotNullFields(boardEntity)) throw BoardCommandErrorCode.DEFAULT.defaultException(); //Not null대상이 null인지 확인
        if (!isValidateType(boardEntity)) throw BoardCommandErrorCode.DEFAULT.defaultException(); //Type확인

        return boardEntityMapper.toDomain(boardJpaRepository.save(boardEntity));
    }

    @Override
    public Board update(Board board) {
        var existBoard = boardJpaRepository.findById(board.getId())
                .orElseThrow(BoardCommandErrorCode.BOARD_NOT_FOUND::defaultException);
        if (!isValidateNotNullFields(existBoard)) throw BoardCommandErrorCode.DEFAULT.defaultException(); //Not null대상이 null인지 확인
        if (!isValidateType(existBoard)) throw BoardCommandErrorCode.DEFAULT.defaultException(); //Type확인
        //createAt은 아래에 아예 업데이트를 하지 않기 때문에 추가로 exception을 하지 않았습니다.

        existBoard.prepareUpdate()
                .title(board.getTitle())
                .content(board.getContent())
                .status(board.getStatus())
                .update();

        return boardEntityMapper.toDomain(boardJpaRepository.save(existBoard));
    }

    @Override
    public void delete(Long id) {
            if(!boardJpaRepository.existsById(id)) throw BoardCommandErrorCode.BOARD_NOT_FOUND.defaultException(); //id 존재여부 확인

            boardJpaRepository.deleteById(id);
    }
}
