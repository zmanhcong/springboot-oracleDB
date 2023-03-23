package com.ecsite.springbootoracle.repository;

import com.ecsite.springbootoracle.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUsername(String username);

//    @Override
//    <S extends Admin> S save(S entity);

//    @Query("SELECT a from Admin a where a.id = ?1")
//    Admin findByIdnew1(Long id);

}
