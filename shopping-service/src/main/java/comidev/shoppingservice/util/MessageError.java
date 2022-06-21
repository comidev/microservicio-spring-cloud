package comidev.shoppingservice.util;

import java.util.List;

import org.springframework.validation.FieldError;

public class MessageError {
    public static String from(List<FieldError> errors) {
        String message = "";
        int indice = 1;
        for (FieldError err : errors) {
            String field = err.getField();
            String fieldMsg = err.getDefaultMessage();
            message += " |(" + indice + ") " + field + ": " + fieldMsg;
            indice++;
        }
        return message;
    }
}
