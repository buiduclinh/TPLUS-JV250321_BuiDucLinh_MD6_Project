package ra.edu.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegister {
    private String username;
    private String password;
    private String email;
    private Boolean isActive;
    private List<String> role;
}
