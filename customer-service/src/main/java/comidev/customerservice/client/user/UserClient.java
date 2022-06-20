package comidev.customerservice.client.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;

// @RequestMapping("/users")
@FeignClient(name = "auth-service", fallback = UserFallback.class)
public interface UserClient {

    @PostMapping("/users/cliente")
    @ResponseStatus(HttpStatus.CREATED)
    public User createCliente(@RequestBody User user);

    @GetMapping("/users/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getById(@PathVariable Long id);
}
