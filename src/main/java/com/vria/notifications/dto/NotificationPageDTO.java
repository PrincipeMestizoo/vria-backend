package com.vria.notifications.dto;

import java.util.List;

public record NotificationPageDTO(
        List<NotificationDTO> content,
        int page,
        int size,
        long totalElements,
        boolean last
) {
}
