package com.ecsite.springbootoracle;

import com.ecsite.springbootoracle.model.Admin;
import com.ecsite.springbootoracle.model.Role;
import com.ecsite.springbootoracle.repository.AdminRepository;
import com.ecsite.springbootoracle.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SpringBootApplication
public class SpringbootOracleApplication implements CommandLineRunner {

    @Autowired
    private RoleRepository roleReposity;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    public static void main(String[] args) {
        SpringApplication.run(SpringbootOracleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

//        Role r1 = new Role("admin");
//        Role r2 = new Role("custom");
//        Role r3 = new Role("user");
//        Role r4 = new Role("student");
//        roleReposity.save(r1);
//        roleReposity.save(r2);
//        roleReposity.save(r3);
//        roleReposity.save(r4);


//        Admin admin = new Admin();
//        admin.setFirstName(adminDto.getFirstName());
//        admin.setLastName(adminDto.getLastName());
//        admin.setUsername(adminDto.getUsername());
//        admin.setPassword(adminDto.getPassword());
//        admin.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));
//        return adminRepository.save(admin);

        //Create new user/
        Admin findadmin = adminRepository.findByUsername("zmanhcong2@gmail.com");
        if (findadmin == null){
            List<Role> adminRoles = Arrays.asList(new Role("ADMIN"));
            Collection<Role> ADMIN = new ArrayList<>(adminRoles);
            Admin admin = new Admin("manh", "cong", "zmanhcong2@gmail.com", bCryptPasswordEncoder.encode("123123"), ADMIN);
            adminRepository.save(admin);
        }
    }
}
