package restSecrurity.exceptions;

import io.javalin.http.HttpStatus;
import lombok.Getter;

@Getter
public class ApiException extends Exception {
    HttpStatus statusCode;
    public ApiException(int statusCode,String msg){
        super(msg);
        this.statusCode = HttpStatus.forStatus(statusCode);
    }
}
