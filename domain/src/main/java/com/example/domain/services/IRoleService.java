package com.example.domain.services;

import com.example.domain.models.entities.Role;

import java.util.List;

public interface IRoleService {
    Role createRole(Role role) throws Exception;
    Role getRoleById(int roleId) throws Exception;
    List<Role> getAllRoles();
}
