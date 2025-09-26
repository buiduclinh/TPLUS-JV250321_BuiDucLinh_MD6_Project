package ra.edu.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @ManyToOne
    @JoinColumn(name = "student_id", unique = true)
    private User student;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_id", unique = true)
    private Course course;
    private LocalDateTime enrollmentDate = LocalDateTime.now();
    @NotNull
    @Pattern(regexp = "ENROLLED|COMPLETED|DROPPED")
    private String status = "ENROLLED";

    private LocalDateTime completionDate;
    @NotNull
    @Max(100)
    @Min(0)
    private Double progressPercentage = 0.0;
}
