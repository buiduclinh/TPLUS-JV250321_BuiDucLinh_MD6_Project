package ra.edu.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lessons")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @NotNull
    @NotBlank(message = "Can't not blank the title")
    @Length(max = 255)
    private String title;
    @Length(max = 500)
    private String contentUrl;
    private String textContent;
    @NotNull
    private String orderIndex;
    @NotNull
    private Boolean isPublished = false;
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();
}
