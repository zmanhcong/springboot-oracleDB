package com.ecsite.springbootoracle.service;

import com.ecsite.springbootoracle.dto.AdminDto;
import com.ecsite.springbootoracle.model.Admin;
import org.springframework.stereotype.Service;

public interface AdminService {
    Admin findByUsername(String username);
    Admin save(AdminDto adminDto);
}
