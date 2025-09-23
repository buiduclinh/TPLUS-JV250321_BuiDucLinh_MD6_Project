package ra.edu.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Lesson_progress")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LessonProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progressId;
    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    private Boolean idCompleted;
    private LocalDateTime completedAt;
    private LocalDateTime lastAccessedAt;
}
