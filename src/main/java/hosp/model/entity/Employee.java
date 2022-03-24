package hosp.model.entity;


import hosp.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Имя не может быть нулевой длины")
    @Size(min = 2, max = 20, message = "От 2х до 20 символов")
    private String firstName;
    @NotEmpty(message = "Фамилия не может быть нулевой длины")
    @Size(min = 2, max = 20, message = "От 2х до 20 символов")
    private String secondName;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Укажите 'должность'")
    private Role role;
    @NotEmpty(message = "Логин не может быть нулевой длины")
    @Size(min = 4, max = 12, message = "От 4х до 12 символов")
    private String username;
    @NotEmpty(message = "Пароль не может быть нулевой длины")
    @Size(min = 4, message = "Не менее 4х символов")
    private String password;
    private boolean active;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> roleList = new ArrayList<>();
        roleList.add(getRole());
        return roleList;
    }

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
        return isActive();
    }
}
