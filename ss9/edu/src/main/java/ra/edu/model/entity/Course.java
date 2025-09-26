package ra.edu.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
    @Length(min = 1, max = 255)
    @NotBlank(message = "Can't not blank the title")
    @NotNull
    private String title;
    private String description;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;
    @NotNull
    private Double price = 0.0 ;
    private int durationHours;
    @Pattern(regexp = "DRAFT|PUBLISHED|ARCHIVED")
    private String status = "DRAFT";
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Lesson> lessons;
}
