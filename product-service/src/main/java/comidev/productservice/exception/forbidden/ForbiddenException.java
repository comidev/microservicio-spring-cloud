package comidev.productservice.exception.forbidden;

public class ForbiddenException extends RuntimeException {

    private static final String DESCRIPTION = "Forbbiden Exception (403)";

    public ForbiddenException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }
}
