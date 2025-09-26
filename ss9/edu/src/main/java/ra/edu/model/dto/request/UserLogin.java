package ra.edu.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLogin {
    @NotNull
    @NotBlank(message = "Can't not blank username")
    @Length(min = 1, max = 50, message = "username <= 50")
    private String username;
    @NotNull
    @NotBlank(message = "Can't not blank password")
    @Length(min = 1, max = 255, message = "username <= 255")
    private String password;
}
