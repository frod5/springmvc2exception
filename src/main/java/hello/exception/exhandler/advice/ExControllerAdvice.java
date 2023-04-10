package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice // 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler, @InitBinder 기능을 부여해준다. 대상을 지정하지 않으면 모든 컨트롤러에 적용(글로벌)
//@ControllerAdvice(annotations = RestController.class) - 어노테이션 지정
//@ControllerAdvice("org.example.controllers") - 패키지 지정
//@ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class}) - 특정 클래스 지정
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST) //상태코드를 바꿔주지 않으면, 예외를 잡아 정상적으로 처리하였기 때문에 200으로 나가기 때문에 변경.
    @ExceptionHandler(IllegalArgumentException.class)  //해당 선언된 컨트롤러에서 해당 예외 발생시 호출된다. , 또한 서블릿에 다시가서 /error를 타고 내려와서 호출되지 않고, 여기서 응답하고 종료.
    public ErrorResult illegalExHandler(IllegalArgumentException e) {  //@RestController에서 ErrorResult를 리턴하면 JSON으로 변경해준다.
        log.error("[exceptionHandler] ex",e);
        return new ErrorResult("BAD",e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex",e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult,HttpStatus.BAD_REQUEST) ;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex",e);
        return new ErrorResult("EX","내부오류");
    }
}
