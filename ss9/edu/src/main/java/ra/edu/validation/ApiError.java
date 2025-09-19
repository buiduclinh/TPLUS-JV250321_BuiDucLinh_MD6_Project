package ra.edu.validation;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiError {
    private int code;
    private String message;
    private Map<String,String> errors;
}
