package ra.edu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.edu.model.dto.response.ApiResponseData;
import ra.edu.model.dto.response.CourseStatisticByStudents;
import ra.edu.model.dto.response.CourseStatisticByTeacher;
import ra.edu.model.dto.response.LessonEnrollmentCourseByStudent;
import ra.edu.model.entity.Notification;
import ra.edu.model.entity.User;
import ra.edu.repo.NotificationRepository;
import ra.edu.service.NotificationService;
import ra.edu.service.UserService;


@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserService userService;


    public ApiResponseData<Page<Notification>> getNotifications(int page, int size){
        Page<Notification> notificationPage = notificationRepository.findAll(PageRequest.of(page, size));
        ApiResponseData<Page<Notification>> apiResponseData = new ApiResponseData<>();
        apiResponseData.setData(notificationPage);
        apiResponseData.setMessage("Notifications found");
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setSuccess(Boolean.TRUE);
        apiResponseData.setErrors(null);
        return apiResponseData;
    }

    public Notification findById(Long id){
        return notificationRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Notification not found"));
    }

    public ApiResponseData<Notification> notificationIsRead(Long notificationId){
        Notification notification = findById(notificationId);
        notification.setIdRead(true);
        notificationRepository.save(notification);
        ApiResponseData<Notification> apiResponseData = new ApiResponseData<>();
        apiResponseData.setData(notification);
        apiResponseData.setMessage("Notification had read");
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setSuccess(Boolean.TRUE);
        apiResponseData.setErrors(null);
        return apiResponseData;
    }

    public ApiResponseData<Notification> createNotification(Notification notification,Long userId){
        User user = userService.findByUserId(userId);
        notification.setUser(user);
        notificationRepository.save(notification);
        ApiResponseData<Notification> apiResponseData = new ApiResponseData<>();
        apiResponseData.setData(notification);
        apiResponseData.setMessage("Notification created");
        apiResponseData.setStatus(HttpStatus.OK);
        apiResponseData.setSuccess(Boolean.TRUE);
        apiResponseData.setErrors(null);
        return apiResponseData;
    }

    public void deleteNotification(Long notificationId){
       if(notificationRepository.findById(notificationId).isPresent()){
           notificationRepository.deleteById(notificationId);
       }
    }


}
