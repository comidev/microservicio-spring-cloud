package comidev.authservice.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import comidev.authservice.exception.badRequest.BadRequestException;
import comidev.authservice.exception.conflict.ConflictException;
import comidev.authservice.exception.forbidden.ForbiddenException;
import comidev.authservice.exception.notFound.NotFoundException;
import comidev.authservice.jwt.JwtDTO;
import comidev.authservice.jwt.JwtService;
import comidev.authservice.role.Role;
import comidev.authservice.role.RoleRepo;
import comidev.authservice.security.RouteProtected;
import comidev.authservice.util.RequestDTO;
import comidev.authservice.util.UsuarioRol;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RouteProtected routeProtected;

    public JwtDTO login(UserRequest userRequest) {
        String username = userRequest.getUsername();
        Optional<UserEntity> userEntityOpt = userRepo.findByUsername(username);
        if (userEntityOpt.isEmpty()) {
            throw new NotFoundException("Username no encontrado: " + username);
        }

        UserEntity userEntity = userEntityOpt.get();
        
        String password = userEntity.getPassword();
        if (!passwordEncoder.matches(userRequest.getPassword(), password)) {
            throw new BadRequestException("Password incorrecto");
        }

        List<String> roles = userEntity.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return jwtService.createTokensByUsernameAndRoles(username, roles);
    }

    public JwtDTO createTokensByTokenRefresh(String tokenRefresh) {
        return jwtService.createTokensByTokenRefresh(tokenRefresh);
    }

    public JwtDTO tokenIsValid(String token, RequestDTO requestDTO) {
        String username = jwtService.username(token);
        if (!userRepo.existsByUsername(username)) {
            String message = "Usuario del token no encontrado: " + username;
            throw new NotFoundException(message);
        }

        List<String> roles = jwtService.roles(token);
        if (!routeProtected.validRequest(requestDTO, roles)) {
            String message = "No tiene rol | permiso | acceso a este recurso: ";
            throw new ForbiddenException(message);
        }

        return new JwtDTO(token, null);
    }

    public UserEntity create(UserEntity user) {
        String username = user.getUsername();
        if (userRepo.existsByUsername(username)) {
            String message = "Username existente: " + username;
            throw new ConflictException(message);
        }
        user.getRoles().add(roleRepo.findByName(UsuarioRol.CLIENTE.toString()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
