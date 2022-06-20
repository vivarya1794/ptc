package com.ptc.commons.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.ptc.commons.dto.error.ApiError;
import com.ptc.commons.dto.response.ApiResponse;
import com.ptc.commons.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A global exception handler that handles the defined exceptions and writes the response to the body
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class WebExceptionHandler {


    @ExceptionHandler(JsonMappingException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError handleException(final JsonMappingException e) {
        log.error(e.getMessage(), e);
        return new ApiError(HttpStatus.BAD_REQUEST, e.getOriginalMessage(), e.getLocalizedMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleException(final ValidationException e) {
        log.error(e.getMessage() + " (" + e.getDetailMessage() + ")", e);
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), e.getDetailMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleException(final MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        final List<String> errors = e.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return new ApiError(HttpStatus.BAD_REQUEST, "Invalid input", errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleException(final HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMostSpecificCause().getMessage().split(":")[0], e.getMostSpecificCause().getMessage().split(":")[0]);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleException(final IllegalArgumentException e) {
        log.error("IllegalArgumentException thrown ", e);
        return ApiResponse.builder().message("IllegalArgumentException thrown in Handler").status(HttpStatus.BAD_REQUEST).code(String.valueOf(HttpStatus.BAD_REQUEST.value())).build();
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(final Exception e) {
        log.error(e.getMessage(), e);
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e.getLocalizedMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ApiError handleException(final HttpMediaTypeNotSupportedException e) {
        log.error(e.getMessage(), e);
        return new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage(), "Supported mediaTypes are " + e.getSupportedMediaTypes());
    }

    @ExceptionHandler(ParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleException(final ParseException e) {
        log.error(e.getMessage(), e);
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), "Parsing failed at " + e.getErrorOffset());
    }
}