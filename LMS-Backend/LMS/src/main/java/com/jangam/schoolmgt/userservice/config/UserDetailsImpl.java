package com.jangam.schoolmgt.userservice.config;

import com.jangam.schoolmgt.userservice.model.Role;
import com.jangam.schoolmgt.userservice.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;


    public UserDetailsImpl(String username,String password, Collection<? extends GrantedAuthority> authorities){
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public static UserDetailsImpl build(User user){
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));

        return new UserDetailsImpl(user.getUsername(),user.getPassword(),authorities);
    }
}
