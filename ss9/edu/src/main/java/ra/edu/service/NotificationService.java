package ra.edu.service;

import org.springframework.data.domain.Page;
import ra.edu.model.dto.response.ApiResponseData;

import ra.edu.model.entity.Notification;

public interface NotificationService {
    ApiResponseData<Page<Notification>> getNotifications(int page, int size);
    Notification findById(Long id);
    ApiResponseData<Notification> notificationIsRead(Long notificationId);
    ApiResponseData<Notification> createNotification(Notification notification,Long userId);
    void deleteNotification(Long notificationId);
}
