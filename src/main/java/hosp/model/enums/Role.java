package hosp.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    DOCTOR, NURSE, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
