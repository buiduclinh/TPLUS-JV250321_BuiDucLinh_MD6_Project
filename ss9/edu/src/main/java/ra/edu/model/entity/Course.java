package ra.edu.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;
    private Double price;
    private int durationHours;
    @Pattern(regexp = "DRAFT|PUBLISHED|ARCHIVED")
    private String status = "DRAFT";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<Lesson> lessons;
}
