package com.spring.boot.application.common.auth;

import com.spring.boot.application.common.enums.Status;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;

public class AuthUser implements UserDetails {
    @Getter
    private String id;
    @Getter
    private String avatar;
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String email;
    @Getter
    private String phoneNumber;
    @Getter
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Getter
    @Enumerated(EnumType.STRING)
    private Status status;

    public AuthUser(User user) {
        this.id = user.getId();
        this.avatar = user.getAvatar();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
