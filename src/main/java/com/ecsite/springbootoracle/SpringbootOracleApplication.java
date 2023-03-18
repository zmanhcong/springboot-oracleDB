package com.ecsite.springbootoracle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootOracleApplication implements CommandLineRunner {

    @Autowired
    private RoleRepository roleReposity;

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
    }
}
