package com.ecsite.springbootoracle.service.impl;

import com.ecsite.springbootoracle.dto.AdminDto;
import com.ecsite.springbootoracle.model.Admin;
import com.ecsite.springbootoracle.repository.AdminRepository;
import com.ecsite.springbootoracle.repository.RoleRepository;
import com.ecsite.springbootoracle.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

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
        admin.setUsertype("NORMAL");
        if (adminDto.getAccountRole().equals("admin")){
            admin.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));
            admin.setActive(true);
        }else {
            admin.setRoles(Arrays.asList(roleRepository.findByName("USER")));
            admin.setActive(false);
        }
        System.out.println("save ok");
        return adminRepository.save(admin);
    }

    @Override
    public Admin update(AdminDto adminDto) {
        Optional<Admin> existingAdmin = adminRepository.findById(adminDto.getId());

        if (!existingAdmin.isPresent()) {
            return null;
        }

        Admin admin = existingAdmin.get();
        admin.setFirstName(adminDto.getFirstName());
        admin.setLastName(adminDto.getLastName());
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(adminDto.getPassword());
        admin.setActive(adminDto.isActive());
        admin.setUsertype("NORMAL");
        if (adminDto.getAccountRole().equals("admin")){
            admin.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));
        }else {
            admin.setRoles(Arrays.asList(roleRepository.findByName("USER")));
        }
        System.out.println("update ok");
        return adminRepository.save(admin);
    }

//    public <S extends Admin> S save(S entity) {
//        return adminRepository.save(entity);
//    }

//    @Override
//    public Admin findByIdnew1(Long id) {
//        return adminRepository.findByIdnew1(id);
//    }
}
