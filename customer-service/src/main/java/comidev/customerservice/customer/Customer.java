package comidev.customerservice.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import comidev.customerservice.client.user.User;
import comidev.customerservice.region.Region;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "no puede ser vacio")
    @Size(min = 8, max = 8, message = "tamaño del dni es 8")
    @Column(name = "dni", unique = true, length = 8, nullable = false)
    private String dni;

    @Column(name = "user_id", nullable = true, unique = true)
    private Long userId;

    @NotEmpty(message = "no puede ser vacio")
    @Column(name = "first_name", nullable = false)
    private String name;

    @NotEmpty(message = "no puede ser vacio")
    @Email(message = "mal formato")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "photo_url")
    private String photoUrl;

    private String state;

    @Transient
    private User user;

    @NotNull(message = "no puede ser vacio")
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;
}
