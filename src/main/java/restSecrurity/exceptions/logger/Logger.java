package restSecrurity.exceptions.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.HttpStatus;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Logger {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectNode exceptionLog(HttpStatus code, String in){
        ObjectNode errorObject = objectMapper.createObjectNode();
        try(FileWriter fileWriter = new FileWriter("exceptionLog.txt", true)){
            fileWriter.write(in);
            fileWriter.append("Status code: "+ code.getCode());
            fileWriter.append(" | ");
            fileWriter.append(LocalDateTime.now().toString());
            fileWriter.append("\n");

            String timeStamp = String.valueOf(LocalDate.now());

            errorObject.put("status", code.getCode());
            errorObject.put("message", in);
            errorObject.put("timeStamp", timeStamp);

        }catch (IOException e){
            errorObject.put("Error",e.getMessage());
            return errorObject;
        }
        return errorObject;
    }

    public static void consoleLog(String in){
        System.out.println(in);
    }
}
