package ra.edu.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        });
        log.warn("Validation failed: {}",errors);
        ApiError apiError = new ApiError();
        apiError.setCode(400);
        apiError.setMessage(errors.get("message"));
        return ResponseEntity.badRequest().body(apiError);
    }
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<?> handleHttpClientErrorException(HttpClientErrorException.Unauthorized e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Missing Token or Invalidate Token", e.getMessage());
        log.error("Missing Token or Invalidate Token: {}",errors);
        ApiError apiError = new ApiError();
        apiError.setCode(401);
        apiError.setMessage(errors.get("Missing Token or Invalidate Token"));
        return ResponseEntity.unprocessableEntity().body(apiError);
    }
    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<?> handleHttpClientErrorException(HttpClientErrorException.Forbidden e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Can't allow to access", e.getMessage());
        log.error("Can't allow to access: {}",errors);
        ApiError apiError = new ApiError();
        apiError.setCode(403);
        apiError.setMessage(errors.get("Can't allow to access"));
        return ResponseEntity.unprocessableEntity().body(apiError);
    }
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<?> handleHttpClientErrorException(HttpClientErrorException.NotFound e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Not found", e.getMessage());
        log.error("Not found: {}",errors);
        ApiError apiError = new ApiError();
        apiError.setCode(404);
        apiError.setMessage(errors.get("Not found"));
        return ResponseEntity.unprocessableEntity().body(apiError);
    }
    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public ResponseEntity<?> handleHttpClientErrorException(HttpClientErrorException.Conflict e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Can't access", e.getMessage());
        log.error("Can't access: {}",errors);
        ApiError apiError = new ApiError();
        apiError.setCode(409);
        apiError.setMessage(errors.get("Can't access"));
        return ResponseEntity.unprocessableEntity().body(apiError);
    }
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<?> handleHttpServerErrorException(HttpServerErrorException.InternalServerError e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Internal Server Error", e.getMessage());
        log.error("Internal Server Error: {}",errors);
        ApiError apiError = new ApiError();
        apiError.setCode(500);
        apiError.setMessage(errors.get("Internal Server Error"));
        return ResponseEntity.unprocessableEntity().body(apiError);
    }
}
