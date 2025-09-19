package ra.edu.security.jwt;

import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
@Builder
public class JWTProvider {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access.expiration}")
    private String access_expiration;
    @Value("${jwt.refresh.expiration}")
    private String refresh_expiration;

    public String generateToken(String username){
        Date now = new Date();
        Date exp = new Date(now.getTime() + Long.parseLong(access_expiration));
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    public String generateRefreshToken(String username){
        Date now = new Date();
        Date refresh = new Date(now.getTime() + Long.parseLong(refresh_expiration));
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(refresh)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (UnsupportedJwtException e){
            log.error("Invalid JWT token"+e.getMessage());
        }catch (ExpiredJwtException e){
            log.error("Expired JWT token"+e.getMessage());
        }catch (IllegalArgumentException e){
            log.error("Illegal JWT token"+e.getMessage());
        }catch (MalformedJwtException e){
            log.error("Malformed JWT token"+e.getMessage());
        }
        return false;
    }
    public String getUsernameFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public Date getExpirationDateFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
    }
}
