package comidev.productservice.exception.unauthorized;

public class JwtException extends UnauthorizedException {
    private static final String DESCRIPTION = "JWT Exception";

    public JwtException(String detail) {
        super(DESCRIPTION + " | " + detail);
    }
}
