package ra.edu.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JWTResponse {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Boolean isActive;
    private Collection<? extends GrantedAuthority> authorities;
    private String typeAuthentication;
    private String token;
}
