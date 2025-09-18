package com.example.shopapp.transfer.mappers;

import com.example.domain.models.entities.Role;
import com.example.domain.models.entities.User;
import com.example.domain.persistence.repositories.RoleRepository;
import com.example.shopapp.transfer.dtos.requests.UserDTO;
import com.example.shopapp.transfer.dtos.responses.RoleResponse;
import com.example.shopapp.transfer.dtos.responses.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    private TypeMap<UserDTO, User> fromRequestToEntityTypeMap;
    private TypeMap<User, UserResponse> fromEntityToResponseTypeMap;

    public User fromRequestToEntity(UserDTO userDTO, boolean includePassword) throws Exception {
        if (userDTO == null) return null;

        if (includePassword) {
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                throw new Exception("Mật khẩu nhập lại không khớp");
            }
        }

        TypeMap<UserDTO, User> typeMap = modelMapper.typeMap(UserDTO.class, User.class);
        typeMap.addMappings(mapper -> {
            mapper.skip(User::setRole);
            mapper.skip(User::setCart);
            if (!includePassword) {
                mapper.skip(User::setPassword);
            }
        });

        User user = typeMap.map(userDTO);

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Vai trò không tồn tại"));
        user.setRole(role);

        if (includePassword) {
            user.setPassword(userDTO.getPassword());
        }

        return user;
    }


    public UserResponse fromEntityToResponse(User user) {
        if (user == null) return null;

        if (fromEntityToResponseTypeMap == null) {
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(User.class, UserResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.addMappings(mapper -> {
                mapper.skip(UserResponse::setRole);
                mapper.skip(UserResponse::setCartId);
            });
            fromEntityToResponseTypeMap.implicitMappings();
        }

        UserResponse userResponse = fromEntityToResponseTypeMap.map(user);

        // map role
        if (user.getRole() != null) {
            RoleResponse roleResponse = roleMapper.fromEntityToResponse(user.getRole());
            userResponse.setRole(roleResponse);
        }

        // map cart id
        if (user.getCart() != null) {
            userResponse.setCartId(user.getCart().getId());
        }

        return userResponse;
    }
}
