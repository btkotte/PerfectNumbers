package com.example.perfect.web;

import com.example.perfect.exception.InvalidRequestException;
import lombok.Getter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerWebInputException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler {


    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(ServerWebInputException.class)
    public Error invalidInputException(ServerWebInputException ex) {
        String message = ex.getMessage();

        if (ex.getRootCause() instanceof NumberFormatException) {
            message = "Input numbers should be between 1 and " + Long.MAX_VALUE;
        }
        return new Error(ex.getStatus().value(), "Invalid Input", message);
    }

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({InvalidRequestException.class, ConstraintViolationException.class})
    public Error invalidRequestException(Exception ex) {
        String message = "";
        if (ex instanceof ConstraintViolationException) {
            for (ConstraintViolation<?> contraint : ((ConstraintViolationException) ex).getConstraintViolations())
                message += contraint.getMessage() + ".";
        } else {
            message = ex.getMessage();
        }
        return new Error(400, "Invalid Input", message);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Error unhandledException(Exception ex) {
        return new Error(500, "Unknown reason", ex.getMessage());
    }

    @Getter
    static class Error {
        private final int status;
        private final String reason;
        private final String message;

        Error(int status, String reason, String message) {
            this.status = status;
            this.reason = reason;
            this.message = message;
        }
    }
}