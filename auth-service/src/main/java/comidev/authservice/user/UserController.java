package comidev.authservice.user;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import comidev.authservice.exception.badRequest.FieldInvalidException;
import comidev.authservice.jwt.JwtDTO;
import comidev.authservice.util.MessageError;
import comidev.authservice.util.RequestDTO;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtDTO login(@RequestBody UserRequest userRequest) {
        return userService.login(userRequest);
    }

    @PostMapping("/token/refresh")
    @ResponseStatus(HttpStatus.OK)
    public JwtDTO tokenRefresh(HttpServletRequest request) {
        String tokenRefresh = request.getHeader(HttpHeaders.AUTHORIZATION);
        return userService.createTokensByTokenRefresh(tokenRefresh);
    }

    @PostMapping("/token/validate")
    @ResponseStatus(HttpStatus.OK)
    public JwtDTO tokenIsValid(@RequestParam(name = "token", required = true) String token,
            @RequestBody RequestDTO requestDTO) {
        return userService.tokenIsValid(token, requestDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity create(@Valid @RequestBody UserEntity user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = MessageError.from(bindingResult.getFieldErrors());
            throw new FieldInvalidException(message);
        }
        return userService.create(user);
    }

    @DeleteMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @DeleteMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteByUsername(@PathVariable String username) {
        userService.deleteByUsername(username);
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserEntity getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserEntity getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }
}
