package com.ecsite.springbootoracle.config;

import com.ecsite.springbootoracle.model.Admin;
import com.ecsite.springbootoracle.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

public class AdminServiceConfig implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;


    //This method will use the findByUsername function, find the user, if there is a user, return the user object, if not, it will return an error message.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsername(username);
        if (admin == null){
            throw new UsernameNotFoundException("Could not find username");
        }
        return new User(
                admin.getUsername(),
                admin.getPassword(),
                admin.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }
}
