package spring_boot_jwt_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Lombok annotations for generating getters, setters, constructors, etc.
@Data
@NoArgsConstructor
@AllArgsConstructor
// JPA annotations for mapping the class to a database table
@Entity
@Table(name = "user_table")
public class UserModel implements UserDetails {
    // Primary key with auto-increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "first_Name")
    private String firstName;
    @Column(name = "last_Name")
    private String lastName;
    @Column(name = "user_Name")
    private String username;
    @Column(name = "password")
    private String password;

    // User's role (e.g., ADMIN, USER)
    @Enumerated(value = EnumType.STRING)
    private RoleModel roleModel;

     @OneToMany(mappedBy = "userModel")
    private List<Token> tokens;

    // Implement the getAuthorities method from UserDetails interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roleModel.name()));
    }

    // Implement the getUsername method from UserDetails interface
    @Override
    public String getUsername() {
        return username;
    }

    // Implement the remaining methods from UserDetails interface
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
