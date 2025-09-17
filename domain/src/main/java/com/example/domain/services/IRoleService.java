package com.example.domain.services;

import com.example.domain.models.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRoleService {
    Page<Role> filterRoles(String name , Pageable pageable);
    Role createRole(Role role) throws Exception;
    Role updateRole(Role role) throws Exception;
    void deleteRole(Integer id) throws Exception;
}
