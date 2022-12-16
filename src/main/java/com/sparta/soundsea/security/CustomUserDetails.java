package com.sparta.soundsea.security;

import com.sparta.soundsea.user.entity.User;
import com.sparta.soundsea.user.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final User user;
    private final String userLoginId;
    private final String password;

    public CustomUserDetails(User user, String userLoginId, String password) {
        this.user = user;
        this.userLoginId = userLoginId;
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRole role = user.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    // Getters
    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.userLoginId;
    }

    // 계정의 만료 여부 리턴
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    // 계정의 잠김 여부 리턴
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    // 비밀번호 만료 여부 리턴
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    // 계정의 활성화 여부 리턴
    @Override
    public boolean isEnabled() {
        return true;
    }
}
