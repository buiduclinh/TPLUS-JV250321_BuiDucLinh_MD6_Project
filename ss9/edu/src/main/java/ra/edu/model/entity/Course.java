package ra.edu.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long course_id;
    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;
    private Double price;
    private int durationHours;
    @Pattern(regexp = "DRAFT|PUBLISHED|ARCHIVED")
    private Status status = Status.DRAFT;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
