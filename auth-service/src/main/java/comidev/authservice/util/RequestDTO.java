package comidev.authservice.util;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDTO {
    private String uri;
    private String method;
    private List<String> roles;
}
