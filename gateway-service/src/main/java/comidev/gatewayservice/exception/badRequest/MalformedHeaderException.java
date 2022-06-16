package comidev.gatewayservice.exception.badRequest;

public class MalformedHeaderException extends BadRequestException {
    private static final String DESCRIPTION = "Field Malformed :(";

    public MalformedHeaderException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
