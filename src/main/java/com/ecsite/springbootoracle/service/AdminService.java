package com.ecsite.springbootoracle.service;

import com.ecsite.springbootoracle.dto.AdminDto;
import com.ecsite.springbootoracle.model.Admin;

public interface AdminService {
    Admin findByUsername(String username);
    Admin save(AdminDto adminDto);

    Admin update(AdminDto adminDto);

}
