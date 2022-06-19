package comidev.customerservice.client.user;

import org.springframework.stereotype.Component;

@Component
public class UserFallback implements UserClient {

    @Override
    public User create(User user) {
        System.out.println("\n\n" + this.getClass().getSimpleName() + "::create" + "\n\n");
        return null;
    }

    @Override
    public User getById(Long id) {
        System.out.println("\n\n" + this.getClass().getSimpleName() + "::getById" + "\n\n");
        return null;
    }
}
