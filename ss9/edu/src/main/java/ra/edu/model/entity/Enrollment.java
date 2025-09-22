package ra.edu.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    private LocalDateTime enrollmentDate = LocalDateTime.now();
    @Pattern(regexp = "ENROLLED|COMPLETED|DROPPED")
    private String status = "ENROLLED";
    private LocalDateTime completionDate;
    private Double progressPercentage = 0.0;
}
