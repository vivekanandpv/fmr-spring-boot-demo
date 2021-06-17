package in.athenaeum.fmrspringbootdemo.ancillary;

import in.athenaeum.fmrspringbootdemo.exception.DomainValidationException;
import in.athenaeum.fmrspringbootdemo.exception.RecordNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandlerAdvice {
    @ExceptionHandler({RecordNotFoundException.class})
    public ResponseEntity<?> handleRecordNotFoundException(RecordNotFoundException exception, WebRequest request) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<?> handleDomainValidationException(DomainValidationException exception, WebRequest request) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleAllExceptions(Throwable throwable, WebRequest request) {
        return ResponseEntity.status(503).build();
    }
}
