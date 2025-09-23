package ra.edu.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String message;
    @Pattern(regexp = "NEW_COURSE|LESSON_UPDATED|ENROLLMENT_CONFIRMED")
    private String type;
    private String targetUrl;
    private Boolean idRead = false;
    private LocalDateTime createdAt;
}
