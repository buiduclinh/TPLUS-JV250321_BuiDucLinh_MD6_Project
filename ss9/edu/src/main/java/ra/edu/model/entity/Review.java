package ra.edu.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_id",unique = true)
    private Course course;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "student_id",unique = true)
    private User student;
    @NotNull
    @Max(5)
    @Min(1)
    private int rating;
    private String comment;
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();
}
