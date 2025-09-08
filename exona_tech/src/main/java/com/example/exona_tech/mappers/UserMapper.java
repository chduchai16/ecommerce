package com.example.exona_tech.mappers;

import com.example.exona_tech.dtos.requests.UserDTO;
import com.example.exona_tech.dtos.resposnes.RoleResponse;
import com.example.exona_tech.dtos.resposnes.UserResponse;
import com.example.domain.entities.Role;
import com.example.domain.entities.User;
import com.example.domain.repositories.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper ;
    private final RoleRepository roleRepository ;
    private final RoleMapper roleMapper;

    private TypeMap<UserDTO , User>  fromRequestToResponseTypeMap ;
    private TypeMap<User , UserResponse> fromEntityToResponseTypeMap ;

    public User fromRequestToEntity(UserDTO userDTO) throws Exception {
        if(userDTO == null) return null ;
        if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
            throw new Exception("Password does not match");
        }

        if(fromRequestToResponseTypeMap == null) {
            fromRequestToResponseTypeMap = modelMapper.createTypeMap(UserDTO.class , User.class) ;
            fromRequestToResponseTypeMap.getMappings().clear();
            fromRequestToResponseTypeMap.addMappings(mapper -> {
                mapper.skip(User :: setRole);
                mapper.skip(User:: setCart);
                mapper.skip(User :: setPassword);
            });
            fromRequestToResponseTypeMap.implicitMappings();
        }

        User user = fromRequestToResponseTypeMap.map(userDTO);
        // map role
        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(()-> new EntityNotFoundException("This role does not exist"));
        user.setRole(role);
        // map password
        if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
            throw new Exception("Password does not match");
        }

        return user ;
    }

    public UserResponse fromEntityToResponse (User user){
        if(user == null) return null ;
        if(fromEntityToResponseTypeMap == null) {
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(User.class , UserResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.addMappings(mapper -> {
                mapper.skip(UserResponse :: setRole);
                mapper.skip(UserResponse :: setCartId);
            });
            fromEntityToResponseTypeMap.implicitMappings();
        }
        UserResponse userResponse = fromEntityToResponseTypeMap.map(user) ;

        // map role
        if(user.getRole() != null){
            RoleResponse roleResponse = roleMapper.fromEntityToResponse(user.getRole());
            userResponse.setRole(roleResponse);
        }

        // map cart id
        if(user.getCart() != null){
            userResponse.setCartId(user.getCart().getId());
        }

        return userResponse ;
    }
}
