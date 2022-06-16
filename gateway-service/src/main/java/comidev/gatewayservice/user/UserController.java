package comidev.gatewayservice.user;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import comidev.gatewayservice.exception.badRequest.FieldInvalidException;
import comidev.gatewayservice.jwt.JwtDTO;
import comidev.gatewayservice.jwt.JwtService;
import comidev.gatewayservice.utils.MessageError;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/token/refresh")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JwtDTO tokenRefresh(HttpServletRequest request) {
        String tokenRefresh = request.getHeader(HttpHeaders.AUTHORIZATION);
        return jwtService.createTokensByTokenRefresh(tokenRefresh);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserEntity create(@Valid @RequestBody UserEntity user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = MessageError.from(bindingResult.getFieldErrors());
            throw new FieldInvalidException(message);
        }
        return userService.create(user);
    }

    @DeleteMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @DeleteMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteByUsername(@PathVariable String username) {
        userService.deleteByUsername(username);
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserEntity getById(@PathVariable Long id) {
        return userService.getById(id);

    }

    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserEntity getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);

    }
}
