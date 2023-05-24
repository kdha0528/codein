package com.codein.responsedto;

import com.codein.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponseDto {

    private String message;
    private int status;
    private List<FieldError> errors;
    private String code;

    private ErrorResponseDto(int status, String code, String message, List<FieldError> errors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    private ErrorResponseDto(ErrorCode code, List<FieldError> errors) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.errors = errors;
        this.code = code.getCode();
    }

    private ErrorResponseDto(final ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.errors = new ArrayList<>();
        this.code = code.getCode();
    }

    public static ErrorResponseDto of(int status, String code, final BindingResult bindingResult) {
        List<FieldError> fieldError = FieldError.of(bindingResult);
        StringBuilder message = new StringBuilder();
        for (FieldError field : fieldError) {
            message.append(field.getReason()).append("\n\n");
        }
        if (message.length() > 0) {
            message.setLength(message.length() - 2);
        }
        return new ErrorResponseDto(status, code, message.toString(), fieldError);
    }
    public static ErrorResponseDto of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponseDto(code, FieldError.of(bindingResult));
    }

    public static ErrorResponseDto of(final ErrorCode code) {
        return new ErrorResponseDto(code);
    }

    public static ErrorResponseDto of(final ErrorCode code, final List<FieldError> errors) {
        return new ErrorResponseDto(code, errors);
    }

    public static ErrorResponseDto of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<ErrorResponseDto.FieldError> errors = ErrorResponseDto.FieldError.of(e.getName(), value, e.getErrorCode());
        return new ErrorResponseDto(ErrorCode.INVALID_TYPE_VALUE, errors);
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
