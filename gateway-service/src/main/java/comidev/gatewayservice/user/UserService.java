package comidev.gatewayservice.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import comidev.gatewayservice.exception.conflict.ConflictException;
import comidev.gatewayservice.exception.notFound.NotFoundException;
import comidev.gatewayservice.role.RoleRepo;
import comidev.gatewayservice.utils.UsuarioRol;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOpt = userRepo.findByUsername(username);
        if (userEntityOpt.isEmpty()) {
            throw new UsernameNotFoundException("Username no existente: " + username);
        }
        UserEntity userEntity = userEntityOpt.get();
        String password = userEntity.getPassword();
        List<SimpleGrantedAuthority> authorities = userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new User(username, password, authorities);
    }

    public UserEntity create(UserEntity user) {
        String username = user.getUsername();
        if (userRepo.existsByUsername(username)) {
            String message = "Username existente: " + username;
            throw new ConflictException(message);
        }
        user.getRoles().add(roleRepo.findByName(UsuarioRol.CLIENTE.toString()));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public void deleteById(Long id) {
        userRepo.delete(getById(id));
    }

    public void deleteByUsername(String username) {
        userRepo.delete(getByUsername(username));
    }

    public UserEntity getById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> {
            String message = "Usuario inexistente: " + id;
            return new NotFoundException(message);
        });
    }

    public UserEntity getByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> {
            String message = "Username inexistente: " + username;
            return new NotFoundException(message);
        });
    }
}
