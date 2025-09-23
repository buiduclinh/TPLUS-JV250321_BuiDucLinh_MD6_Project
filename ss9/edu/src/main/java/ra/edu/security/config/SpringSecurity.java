package ra.edu.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ra.edu.security.jwt.JWTAuthFilter;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SpringSecurity {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JWTAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JWTAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProviderBean() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/verify").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers("/api/auth/me").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers("/api/auth/logout").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/courses").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers("/api/courses/{course_id}").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers(HttpMethod.POST,"/api/courses").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/courses").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/courses").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/courses/{course_id}/lessons").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers(HttpMethod.GET,"/api/lessons/{lesson_id}").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers(HttpMethod.GET,"/api/courses/{course_id}/lessons").hasAnyRole("ADMIN","TEACHER")
                        .requestMatchers(HttpMethod.PUT,"/api/lessons/{lesson_id}").hasAnyRole("ADMIN","TEACHER")
                        .requestMatchers(HttpMethod.PUT,"/api/lessons/{lesson_id}/publish").hasAnyRole("ADMIN","TEACHER")
                        .requestMatchers(HttpMethod.DELETE,"/api/lessons/{lesson_id}").hasAnyRole("ADMIN","TEACHER")
                        .requestMatchers("/api/enrollments").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.PUT,"/api/users/{user_id}").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers(HttpMethod.PUT,"/api/users/{user_id}/password").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers(HttpMethod.GET,"/api/courses/search").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers(HttpMethod.GET,"/api/courses/teacher").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers(HttpMethod.GET,"/api/notifications").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers(HttpMethod.PUT,"/api/notifications/{notification_id}/read").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers(HttpMethod.POST,"/api/notifications").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/notifications/{notification_id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/reports/top_courses").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/reports/student_progress/{student_id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/reports/teacher_courses_overview/{teacher_id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/courses/{course_id}/reviews").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/courses/{course_id}/reviews").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.PUT,"/api/reviews/{review_id}").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers(HttpMethod.DELETE,"/api/reviews/{review_id}").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .requestMatchers(HttpMethod.GET,"/api/lessons/{lesson_id}/content_preview").hasAnyRole("STUDENT", "ADMIN","TEACHER")
                        .anyRequest().authenticated())
                .exceptionHandling(ex->jwtAuthenticationEntryPoint())
                .authenticationProvider(authenticationProviderBean())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
