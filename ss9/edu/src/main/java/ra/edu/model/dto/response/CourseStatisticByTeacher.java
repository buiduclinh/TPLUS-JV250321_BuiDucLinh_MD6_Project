package ra.edu.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseStatisticByTeacher {
    private Long courseId;
    private String courseName;
    private Long totalStudents;
    private Long totalLessons;
    private Double totalProgressPercentage;
}
