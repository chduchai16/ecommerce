package com.example.shopapp.imp_services;

import com.example.domain.models.entities.Role;
import com.example.domain.persistence.repositories.RoleRepository;
import com.example.domain.services.IRoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceIMP implements IRoleService {

    private final RoleRepository roleRepository ;

    @Override
    public Role createRole(Role role) throws Exception {
        if(roleRepository.findByName(role.getName()).isEmpty()){
            return roleRepository.save(role);
        }
        else {
            throw new DataIntegrityViolationException("Tên vai trò đã tồn tại");
        }
    }

    @Override
    public Role getRoleById(int roleId) throws Exception {
        return roleRepository.findById(roleId).orElseThrow(()->new EntityNotFoundException("Không tìm thấy vai trò này"));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
