package ra.edu.model.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegister {
    @NotNull
    @NotBlank(message = "Can't not blank username")
    @Length(min = 1, max = 50, message = "username <= 50")
    private String username;
    @NotNull
    @NotBlank(message = "Can't not blank password")
    @Length(min = 1, max = 255, message = "username <= 255")
    private String password;
    @NotBlank(message = "Can't not blank email")
    @Column(nullable = false, unique = true)
    @Length(min = 1, max = 100)
    private String email;
    private Boolean isActive;
    private List<String> role;
}
