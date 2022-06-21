package comidev.authservice.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotEmpty(message = "el campo no puede ser vacio")
    @Size(min = 6, message = "minimo 6")
    private String username;
    @NotEmpty(message = "el campo no puede ser vacio")
    @Size(min = 3, message = "minimo 3")
    private String password;
}
