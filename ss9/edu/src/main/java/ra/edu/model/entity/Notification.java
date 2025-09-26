package ra.edu.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String message;
    @Pattern(regexp = "NEW_COURSE|LESSON_UPDATED|ENROLLMENT_CONFIRMED")
    @Length(max = 50)
    private String type;
    @Length(max = 500)
    private String targetUrl;
    @NotNull
    private Boolean idRead = false;
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();
}
