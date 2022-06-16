package comidev.gatewayservice.utils;

public enum UsuarioRol {
    CLIENTE("CLIENTE"),
    ADMIN("ADMIN");

    private UsuarioRol(String content) {
        this.content = content;
    }

    private String content;

    @Override
    public String toString() {
        return content;
    }
}
