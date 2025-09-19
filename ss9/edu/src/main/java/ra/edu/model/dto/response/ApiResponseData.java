package ra.edu.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponseData<T> {
    private T data;
    private String message;
    private HttpStatus status;
    private Boolean success;
    private Map<String,String> errors;
}
