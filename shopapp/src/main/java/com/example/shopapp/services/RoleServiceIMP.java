package com.example.shopapp.services;

import com.example.shopapp.models.entities.Role;
import com.example.shopapp.repositories.RoleRepository;
import com.example.shopapp.specifications.RoleSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceIMP implements IRoleService {

    private final RoleRepository roleRepository ;

    @Override
    public Page<Role> filterRoles(String name, Pageable pageable) {
        Specification<Role> spec = Specification.where(RoleSpecification.hasName(name));
        return roleRepository.findAll(spec, pageable);
    }

    @Override
    public Role createRole(Role role) throws Exception {

        Specification<Role> spec = Specification.where(RoleSpecification.hasExactName(role.getName()));
        Optional<Role> existingRole = roleRepository.findOne(spec);
        if(existingRole.isPresent()) {
            throw new DataIntegrityViolationException("Tên vai trò đã tồn tại");
        }
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Role role) throws Exception {
        if(role.getId() == null) {
            throw new IllegalArgumentException("ID vai trò không được để trống");
        }
        Specification<Role> spec = Specification.where(RoleSpecification.hasId(role.getId()));

        boolean existById = roleRepository.exists(spec);
        if(!existById){
            throw new EntityNotFoundException("Vai trò không tồn tại");
        }
        spec = Specification.where(RoleSpecification.hasExactName(role.getName()));
        boolean checkByName = roleRepository.exists(spec);
        if(checkByName){
            throw new DataIntegrityViolationException("Tên vai trò đã tồn tại");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Integer id) throws Exception {
        Specification<Role> spec = Specification.where(RoleSpecification.hasId(id));
        Optional<Role> existingRole = roleRepository.findOne(spec);
        if(existingRole.isEmpty()) {
            throw new EntityNotFoundException("Vai trò không tồn tại");
        }
        roleRepository.deleteById(id);
    }
}
