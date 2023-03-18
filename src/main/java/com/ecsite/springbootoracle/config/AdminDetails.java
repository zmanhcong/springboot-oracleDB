package com.ecsite.springbootoracle.config;

import com.ecsite.springbootoracle.model.Admin;
import com.ecsite.springbootoracle.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//This class is called by the AdminServiceConfig class in the loadUserByUsername() method,
//which returns an instance of AdminDetails that contains the information for the specified Admin user.
public class AdminDetails implements UserDetails {
    private Admin admin;

    //This code defines a class called AdminDetails that implements the UserDetails interface. This class is used to provide Spring Security with information about an Admin user, which includes the user's roles and authentication information (such as username and password).
    //The getAuthorities() method in this class is used to retrieve the roles of the Admin user and convert them into Spring Security GrantedAuthority objects. These GrantedAuthority objects are used by Spring Security to check if the user is authorized to perform certain actions.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : admin.getRoles()){
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getUsername();
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
        return true;
    }
}
