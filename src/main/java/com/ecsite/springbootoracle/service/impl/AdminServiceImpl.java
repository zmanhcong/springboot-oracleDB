package com.ecsite.springbootoracle.service.impl;

import com.ecsite.springbootoracle.dto.AdminDto;
import com.ecsite.springbootoracle.model.Admin;
import com.ecsite.springbootoracle.repository.AdminRepository;
import com.ecsite.springbootoracle.repository.RoleRepository;
import com.ecsite.springbootoracle.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    @Override
    public Admin save(AdminDto adminDto) {
        Admin admin = new Admin();
        admin.setFirstName(adminDto.getFirstName());
        admin.setLastName(adminDto.getLastName());
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(adminDto.getPassword());
        admin.setUsertype(adminDto.getUsertype());
        admin.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));
        return adminRepository.save(admin);
    }
}
