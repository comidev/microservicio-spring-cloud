package comidev.authservice.user;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import comidev.authservice.role.RoleName;
import comidev.authservice.role.RoleRepo;
import comidev.authservice.security.RouteProtected;
import comidev.authservice.util.RequestDTO;

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

    public JwtDTO login(UserDTO userDTO) {
        String username = userDTO.getUsername();
        Optional<User> userEntityOpt = userRepo.findByUsername(username);
        if (userEntityOpt.isEmpty()) {
            throw new NotFoundException("Username no encontrado: " + username);
        }

        User userEntity = userEntityOpt.get();

        String password = userEntity.getPassword();
        if (!passwordEncoder.matches(userDTO.getPassword(), password)) {
            throw new BadRequestException("Password incorrecto");
        }

        List<String> roles = userEntity.getRoles().stream()
                .map(role -> role.getName().toString())
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

    public User createAdmin(UserDTO user) {
        return create(user, RoleName.ADMIN);
    }

    public User createCliente(UserDTO user) {
        return create(user, RoleName.CLIENTE);
    }

    private User create(UserDTO user, RoleName roleName) {
        String username = user.getUsername();
        if (userRepo.existsByUsername(username)) {
            String message = "Username existente: " + username;
            throw new ConflictException(message);
        }

        // La primera vez se crea los roles
        Optional<Role> optRoleDB = roleRepo.findByName(roleName);
        Role roleDB = optRoleDB.isPresent()
                ? optRoleDB.get()
                : roleRepo.save(new Role(roleName));

        User userDB = User.builder()
                .username(username)
                .roles(Set.of(
                        roleDB))
                .password(passwordEncoder.encode(user.getPassword()))
                .build();

        return userRepo.save(userDB);
    }

    public void deleteById(Long id) {
        userRepo.delete(getById(id));
    }

    public void deleteByUsername(String username) {
        userRepo.delete(getByUsername(username));
    }

    public User getById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> {
            String message = "Usuario inexistente: " + id;
            return new NotFoundException(message);
        });
    }

    public User getByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> {
            String message = "Username inexistente: " + username;
            return new NotFoundException(message);
        });
    }
}
