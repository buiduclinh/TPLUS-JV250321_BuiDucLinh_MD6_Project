package ra.edu.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.entity.Notification;
import ra.edu.service.NotificationService;

import java.net.URI;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponseData<Page<Notification>>> getNotification(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        ApiResponseData<Page<Notification>> apiResponseData = notificationService.getNotifications(page, size);
        return ResponseEntity.ok(apiResponseData);
    }

    @PutMapping("/{notification_id}/read")
    public ResponseEntity<ApiResponseData<Notification>> updateNotification(@Valid @RequestBody Notification notification, @PathVariable Long notification_id) {
        notification.setNotificationId(notification_id);
        ApiResponseData<Notification> apiResponseData = notificationService.notificationIsRead(notification_id);
        return ResponseEntity.ok(apiResponseData);
    }

    @PostMapping
    public ResponseEntity<ApiResponseData<Notification>> createNotification(@Valid @RequestBody Notification notification,@RequestParam Long userId) {
        ApiResponseData<Notification> apiResponseData = notificationService.createNotification(notification,userId);
        return  ResponseEntity.created(URI.create("/api/notifications/"+notification.getNotificationId())).body(apiResponseData);
    }

    @DeleteMapping("/{notification_id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notification_id) {
        notificationService.deleteNotification(notification_id);
        return ResponseEntity.noContent().build();
    }
}
