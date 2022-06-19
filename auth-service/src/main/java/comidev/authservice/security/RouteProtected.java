package comidev.authservice.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import comidev.authservice.util.RequestDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "admin-paths")
public class RouteProtected {
    private List<RequestDTO> paths;

    public boolean validRequest(RequestDTO requestDTO, List<String> roles) {
        List<RequestDTO> rutaList = paths.stream()
                .filter((path) -> path.getUri().equals(requestDTO.getUri())
                        && path.getMethod().equals(requestDTO.getMethod()))
                .collect(Collectors.toList());

        if (rutaList.isEmpty()) {
            return true; // Es ruta para cliente
        }

        List<String> rutaRoles = rutaList.get(0).getRoles();
        return roles.stream().anyMatch(rutaRoles::contains);
    }
}
