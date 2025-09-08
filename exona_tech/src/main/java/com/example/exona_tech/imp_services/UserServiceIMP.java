package com.example.exona_tech.imp_services;

import com.example.domain.entities.User;
import com.example.domain.repositories.RoleRepository;
import com.example.domain.repositories.UserRepository;
import com.example.domain.services.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceIMP implements IUserService {

    private final UserRepository userRepository ;
    private final RoleRepository roleRepository ;
    private final PasswordEncoder passwordEncoder ;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User createUser(User user) throws Exception {
        if(!userRepository.findByPhoneNumber(user.getPhoneNumber()).isEmpty()){
            throw new Exception("Phone number must not be duplicated");
        }
        // mã hoá mật khẩu
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return this.userRepository.save(user);
    }

    @Override
    public void deleteUser(int userId) throws Exception {
        if(userRepository.findById(userId).isEmpty()){
            throw new Exception("This user does not exist");
        }
        else {
            userRepository.deleteById(userId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(User user) throws Exception {

        if(user.getId()== null) {
            throw new Exception("Id must not be null to update");
        }
        else if (!this.userRepository.existsById(user.getId())){
            throw new EntityNotFoundException("This user does not exist");
        }

        if (user.getPhoneNumber() != null && !user.getPhoneNumber().trim().isEmpty()) {
            if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
                throw new Exception("Phone number already exists");
            }
        }

        // mã hoá mật khẩu
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    @Override
    public User getUserById(int userId) throws Exception {
        return userRepository.findById(userId).orElseThrow(()->new Exception("Cannot find this user"));
    }

    @Override
    public Page<User> searchUsers(String keyword ,Pageable pageable) {
        return userRepository.findByKeyword(keyword,pageable) ;
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable) ;
    }

    @Override
    public User getUserByPhoneNumber (String phoneNumber) throws Exception {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(()-> new Exception("Cannot find this user")) ;
    }

}
