package ra.edu.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LessonPreviewResponse {
    private Long lessonId;
    private String title;
    private String previewContent;
}
