package com.example.shopapp.transfer.mappers;

import com.example.domain.models.entities.Role;
import com.example.domain.models.entities.User;
import com.example.domain.persistence.repositories.RoleRepository;
import com.example.domain.persistence.repositories.UserRepository;
import com.example.domain.persistence.specifications.UserSpecification;
import com.example.shopapp.transfer.dtos.requests.UserDTO;
import com.example.shopapp.transfer.dtos.requests.UserPasswordDTO;
import com.example.shopapp.transfer.dtos.responses.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private TypeMap<UserDTO, User> fromRequestToEntityTypeMap;
    private TypeMap<User, UserResponse> fromEntityToResponseTypeMap;
    private TypeMap<UserPasswordDTO , User> fromPasswordRequestToEntityTypeMap;

    public User fromRequestToEntity(UserDTO userDTO) throws Exception {
        if (userDTO == null) return null;

        if(this.fromRequestToEntityTypeMap == null) {
            this.fromRequestToEntityTypeMap = this.modelMapper.createTypeMap(UserDTO.class, User.class);
            this.fromRequestToEntityTypeMap.getMappings().clear();
            this.fromRequestToEntityTypeMap.addMappings(mapper -> {
                mapper.skip(User::setRole);
                mapper.skip(User::setCart);
                mapper.skip(User::setPassword);
            });
            this.fromRequestToEntityTypeMap.implicitMappings();
        }
        User user = this.fromRequestToEntityTypeMap.map(userDTO) ;
       Role role = roleRepository.findById(userDTO.getRoleId())
               .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy vai trò với ID: " + userDTO.getRoleId()));
       user.setRole(role);
        return user;
    }

    public UserResponse fromEntityToResponse(User user) {
        if (user == null) return null;

        if (fromEntityToResponseTypeMap == null) {
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(User.class, UserResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.addMappings(mapper -> {
                mapper.skip(UserResponse::setRoleName);
                mapper.skip(UserResponse::setCartId);
            });
            fromEntityToResponseTypeMap.implicitMappings();
        }

        UserResponse userResponse = fromEntityToResponseTypeMap.map(user);

        // map role
        if (user.getRole() != null) {
            userResponse.setRoleName(user.getRole().getName());
            userResponse.setRoleId(user.getRole().getId());
        }

        // map cart id
        if (user.getCart() != null) {
            userResponse.setCartId(user.getCart().getId());
        }

        return userResponse;
    }

    public User fromPasswordRequestToEntity(UserPasswordDTO userPasswordDTO) throws Exception {
        if (userPasswordDTO == null) return null;

        if(this.fromPasswordRequestToEntityTypeMap == null) {
            this.fromPasswordRequestToEntityTypeMap = this.modelMapper.createTypeMap(UserPasswordDTO.class, User.class);
            this.fromPasswordRequestToEntityTypeMap.getMappings().clear();
            this.fromPasswordRequestToEntityTypeMap.addMappings(mapper -> {
                mapper.skip(User::setRole);
                mapper.skip(User::setCart);
                mapper.skip(User :: setPassword);
            });
            this.fromPasswordRequestToEntityTypeMap.implicitMappings();
        }

        if (!userPasswordDTO.getNewPassword().equals(userPasswordDTO.getConfirmNewPassword())) {
            throw new Exception("Mật khẩu nhập lại không khớp");
        }

        Specification<User> spec = Specification.where(UserSpecification.hasId(userPasswordDTO.getId())) ;
        Optional<User> userOptional = userRepository.findOne(spec) ;
        if(userOptional.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy người dùng với ID: " + userPasswordDTO.getId());
        }
        User user = userOptional.get() ;
        user.setPassword(userPasswordDTO.getNewPassword());
        return user;
    }
}
