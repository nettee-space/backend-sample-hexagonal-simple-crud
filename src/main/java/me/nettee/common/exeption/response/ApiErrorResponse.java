package me.nettee.common.exeption.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Map;
import lombok.Builder;

@Builder
public record ApiErrorResponse(
        int status,
        String code,
        String message,
        @JsonInclude(Include.NON_EMPTY)
        Map<String, Object> payload
) {}
