package hello.exception.api;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ApiExceptionV2Controller {

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

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if(id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }

        if(id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if(id.equals("user-ex")){
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id,"hello "+id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
