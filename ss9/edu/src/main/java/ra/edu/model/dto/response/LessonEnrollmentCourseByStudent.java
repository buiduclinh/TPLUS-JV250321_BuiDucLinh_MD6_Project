package ra.edu.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LessonEnrollmentCourseByStudent {
    private Long studentId;
    private Long courseId;
    private String courseName;
    private Double progressPercentage;
}
