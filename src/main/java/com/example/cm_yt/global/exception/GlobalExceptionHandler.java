package com.example.cm_yt.global.exception;

import com.example.cm_yt.global.ApiResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler 어노테이션은 MethodArgumentNotValidException 예외 발생 시 실행됨
    // cf. MethodArgumentNotValidException => Validation 실패시 발생하는 에러
    // 주로 @Valid 유효성 검사 실패 시 발생하는 예외이다.
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 예외 메시지를 여러 필드 에러에서 추출하여 하나의 문자열로 만듦
        String errorMsg = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        // 클라이언트에 400 Bad Request 상태 코드와 함께 통일된 실패 응답 형식으로 반환
        return ResponseEntity.badRequest().body(ApiResponse.fail(errorMsg));
    }

    // 모든 Exception을 포괄해서 처리하는 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleOtherExceptions(Exception ex) {
        // 실제 프로덕션 환경에서는 여기서 예외 내용을 로깅하여 문제 추적에 활용해야 함
        // 예: log.error("Unexpected error", ex);
        // 클라이언트에는 500 Internal Server Error 상태 코드와 일반화된 에러 메시지 반환
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail("서버 에러가 발생했습니다."));
    }
}

