package com.example.exona_tech.mappers;

import com.example.domain.dtos.requests.UserDTO;
import com.example.domain.dtos.resposnes.RoleResponse;
import com.example.domain.entities.Role;
import com.example.domain.entities.User;
import com.example.domain.repositories.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper ;
    private final RoleRepository roleRepository ;
    private final PasswordEncoder passwordEncoder ;

    private TypeMap<UserDTO , User>  fromRequestToResponseTypeMap ;

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
}
