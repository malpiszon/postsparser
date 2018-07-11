package net.malpiszon.stackexchange.postsparser.handlers;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        LOG.info("Validation exception: " + e.getMessage());
        return "";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        LOG.info("Validation exception: " + e.getMessage());
        return "";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = IOException.class)
    public String handleFileNotFoundException(IOException e) {
        LOG.info("File passed not exists: " + e.getMessage());
        return "";
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(value = SAXException.class)
    public String handleSAXException(SAXException e) {
        LOG.warn("SAX exception: " + e.getMessage());
        return "";
    }

    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(value = TimeoutException.class)
    public String handleTimeoutException(TimeoutException e) {
        LOG.info("Timeout exception: " + e.getMessage());
        return "";
    }
}