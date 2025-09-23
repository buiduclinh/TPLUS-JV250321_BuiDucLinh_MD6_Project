package ra.edu.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseStatisticByStudents {
    private Long courseId;
    private String courseName;
    private Long totalSubmit;
}
