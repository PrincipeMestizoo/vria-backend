package com.vria.notifications.controller;

import com.vria.notifications.dto.NotificationPageDTO;
import com.vria.notifications.dto.UnreadCountDTO;
import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.service.NotificationQueryService;
import com.vria.notifications.service.NotificationReadTracker;
import com.vria.users.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Historial de movimientos visible segun el rol del usuario")
@PreAuthorize("isAuthenticated()")
public class NotificationController {

    private final NotificationQueryService queryService;
    private final NotificationReadTracker readTracker;

    @GetMapping
    public ResponseEntity<NotificationPageDTO> findAll(@AuthenticationPrincipal User user,
                                                       @RequestParam(required = false) NotificationModule module,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(queryService.findVisible(user, module, page, size));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<UnreadCountDTO> unreadCount(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(queryService.countUnread(user));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal User user) {
        readTracker.markAllAsRead(user);
        return ResponseEntity.noContent().build();
    }
}
