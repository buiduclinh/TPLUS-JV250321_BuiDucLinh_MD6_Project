package ra.edu.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @ManyToOne
    @JoinColumn(name = "enrollment_id", unique = true)
    private Enrollment enrollment;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "lesson_id", unique = true)
    private Lesson lesson;
    @NotNull
    private Boolean idCompleted = false;
    private LocalDateTime completedAt;
    @NotNull
    private LocalDateTime lastAccessedAt =  LocalDateTime.now();
}
