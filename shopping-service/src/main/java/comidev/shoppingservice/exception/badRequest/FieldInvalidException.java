package comidev.shoppingservice.exception.badRequest;

public class FieldInvalidException extends BadRequestException {
    private static final String DESCRIPTION = "Field(s) Invalid";

    public FieldInvalidException(String detail) {
        super(DESCRIPTION + ": " + detail);
    }
}
