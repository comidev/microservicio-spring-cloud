package comidev.customerservice.client.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;

@FeignClient(name = "auth-service", fallback = UserFallback.class)
@RequestMapping("/users")
public interface UserClient {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user);

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getById(@PathVariable Long id);
}
