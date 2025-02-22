package me.nettee.board.adapter.driven.persistence.entity.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BoardEntityStatusConverter implements AttributeConverter<BoardEntityStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BoardEntityStatus status) {
        return status.getValue();
    }

    @Override
    public BoardEntityStatus convertToEntityAttribute(Integer value) {
        return BoardEntityStatus.valueOf(value);
    }
}
