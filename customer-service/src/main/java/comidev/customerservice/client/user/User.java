package comidev.customerservice.client.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private Long id;
    @NotEmpty(message = "el campo no puede ser vacio")
    @Size(min = 6, message = "minimo 6")
    private String username;
    @NotEmpty(message = "el campo no puede ser vacio")
    @Size(min = 3, message = "minimo 3")
    private String password;
}
