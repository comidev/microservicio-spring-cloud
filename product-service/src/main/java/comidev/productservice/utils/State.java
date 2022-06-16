package comidev.productservice.utils;

public enum State {
    CREATED("CREATED"),
    DELETED("DELETED");

    private State(String content) {
        this.content = content;
    }

    private String content;

    @Override
    public String toString() {
        return content;
    }
}
