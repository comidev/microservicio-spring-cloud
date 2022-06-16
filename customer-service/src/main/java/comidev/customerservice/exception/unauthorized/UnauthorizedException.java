package comidev.customerservice.exception.unauthorized;

public class UnauthorizedException extends RuntimeException {

    private static final String DESCRIPTION = "UnAuthorized Exception (401)";

    public UnauthorizedException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
