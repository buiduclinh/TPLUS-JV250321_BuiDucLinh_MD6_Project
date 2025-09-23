package ra.edu.service;

import org.springframework.data.domain.Page;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.CourseStatisticByStudents;
import ra.edu.model.dto.response.CourseStatisticByTeacher;
import ra.edu.model.dto.response.LessonEnrollmentCourseByStudent;
import ra.edu.model.entity.Notification;

public interface NotificationService {
    ApiResponseData<Page<CourseStatisticByStudents>> courseStatisticByStudents(int page, int size);
    ApiResponseData<Page<LessonEnrollmentCourseByStudent>> lessonEnrollmentCourseByStudents(Long studentId, int page, int size);
    ApiResponseData<Page<CourseStatisticByTeacher>> courseStatisticByTeacher(int page, int size, Long teacherId);
    ApiResponseData<Page<Notification>> getNotifications(int page, int size);
    Notification findById(Long id);
    ApiResponseData<Notification> notificationIsRead(Long notificationId);
    ApiResponseData<Notification> createNotification(Notification notification,Long userId);
    void deleteNotification(Long notificationId);
}
