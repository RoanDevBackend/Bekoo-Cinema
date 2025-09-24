package org.bekoocinema.exception;

import lombok.extern.slf4j.Slf4j;
import org.bekoocinema.response.ApiResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> exception(RuntimeException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> exception(HttpRequestMethodNotSupportedException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error("Phương thức '" + e.getMethod() + "' không được hỗ trợ cho truy vấn này"));
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ApiResponse> exception(TypeMismatchException e){
        log.error(e.getMessage());
        String field = Objects.requireNonNull(e.getPropertyName());
        int index = -1;
        for(int x = 0 ; x < field.length() ; x++){
            if(field.charAt(x) >= 'A' && field.charAt(x) <= 'Z'){
                index = x;
                break;
            }
        }
        String s1 = field.substring(0, index);
        String s2 = field.substring(index);

        field = s1.substring(0, 1).toUpperCase() + s1.substring(1) + " " + s2.substring(0, 1).toLowerCase() + s2.substring(1);
        return ResponseEntity.badRequest().body(ApiResponse.error(field + " không đúng định dạng"));
    }
    /**
     * Xử lí lỗi, chuyển đổi kiểu dữ liệu thời gian
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiResponse> exception(DateTimeParseException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error("Định dạng dữ liệu thời gian chưa chính xác"));
    }

    /**
     * Bắt lỗi thiếu tham số truyền vào qua request param
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParam(MissingServletRequestParameterException ex) {
        String errorMessage = "Tham số '" + ex.getParameterName() + "' là bắt buộc!";
        return ResponseEntity.badRequest().body(ApiResponse.error(errorMessage));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> illegalArgumentException(IllegalArgumentException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }

    /**
     * Lỗi kết nối đến database hay là bên thứ 3
     */
    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<ApiResponse> timeoutException(DataAccessResourceFailureException e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.error("Lỗi kết nối !"));
    }

    /**
     * Xử lí validate dữ liệu đầu vào của Body
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> methodArgument(MethodArgumentNotValidException e){
        log.error(e.getMessage());
        ObjectError error = e.getBindingResult().getAllErrors().get(0);
        StringBuilder message = new StringBuilder();
        if(Objects.requireNonNull(error.getDefaultMessage()).startsWith("Failed to convert property value of type")){
            FieldError fieldError = (FieldError) error;
            String field = fieldError.getField();
            message.append(field.substring(0, 1).toUpperCase()).append(field.substring(1)).append(" không đúng định dạng");
        }else{
            message.append(error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(ApiResponse.error(message.toString()));
    }
    /**
     * Xử lí lỗi của hệ thống tự định nghĩa ra
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> bookingCareAppException(AppException e){
        ErrorDetail errorDetail= e.getErrorDetail();
        return ResponseEntity.badRequest().body(ApiResponse.error(errorDetail.getCode(), errorDetail.getMessage()));
    }
}