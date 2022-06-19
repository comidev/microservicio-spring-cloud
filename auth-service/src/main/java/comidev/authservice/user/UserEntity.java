package comidev.authservice.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import comidev.authservice.role.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "el campo no puede ser vacio")
    @Size(min = 6, message = "minimo 6")
    @Column(unique = true, nullable = false)
    private String username;

    @NotEmpty(message = "el campo no puede ser vacio")
    @Size(min = 3, message = "minimo 3")
    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_rol", joinColumns = @JoinColumn(referencedColumnName = "id", name = "usuario_id"), inverseJoinColumns = @JoinColumn(referencedColumnName = "id", name = "rol_id"))
    private Set<Role> roles;

    public UserEntity() {
        this.roles = new HashSet<>();
    }
}
