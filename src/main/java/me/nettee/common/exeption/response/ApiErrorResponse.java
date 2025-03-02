package me.nettee.common.exeption.response;

import java.util.Map;
import lombok.Builder;

@Builder
public record ApiErrorResponse(
        int status,
        String code,
        String message,
        Map<String, Object> payload
) {
        public ApiErrorResponse {
                if (payload != null && payload.isEmpty()) {
                        payload = null;
                }
        }
}

