package gr.aueb.cf.managementapp.model;

import gr.aueb.cf.managementapp.core.enums.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Principal;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements IdentifiableEntity, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Override
    public String getName() {
        return username;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public RoleType getRoleType() {
        return roleType;
    }
}
