package com.example.shopapp.imp_services;

import com.example.domain.models.entities.User;
import com.example.domain.persistence.repositories.UserRepository;
import com.example.domain.persistence.specifications.UserSpecification;
import com.example.domain.services.IUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceIMP implements IUserService {

    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder ;

    @Override
    public Page<User> filterUsers(String name, String email, String phoneNumber, String address, Integer status , Pageable pageable) throws Exception {
        Specification<User> spec = Specification.where(UserSpecification.hasName(name))
                .and(UserSpecification.hasEmail(email))
                .and(UserSpecification.hasPhoneNumber(phoneNumber))
                .and(UserSpecification.hasAddress(address))
                .and(UserSpecification.hasStatus(status));
        return userRepository.findAll(spec , pageable);
    }

    @Override
    public User getUserByPhoneNumber(String phoneNumber){
        Specification<User> spec = Specification.where(UserSpecification.hasPhoneNumberExact(phoneNumber));
        return userRepository.findOne(spec).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với số điện thoại: " + phoneNumber));
    }

    @Override
    public User getUserById(int userId) throws Exception {
        Specification<User> spec = Specification.where(UserSpecification.hasId(userId));
        return userRepository.findOne(spec).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID: " + userId));
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public User createUser(User user) throws Exception {
        Specification<User> spec = Specification.where(UserSpecification.hasPhoneNumberExact(user.getPhoneNumber()))
                .and(UserSpecification.notId(user.getId()));
        boolean exist = userRepository.exists(spec) ;
        if(exist) {
            throw new DataIntegrityViolationException("Số điện thoại đã tồn tại trong hệ thống");
        }

        spec = Specification.where(UserSpecification.hasEmailExact(user.getEmail()));
        exist = userRepository.exists(spec) ;
        if(exist) {
            throw new DataIntegrityViolationException("Email đã tồn tại trong hệ thống");
        }
        user.setStatus(1); // mặc định kích hoạt
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public User updateUser(User user) throws Exception {
        if(user.getId() == null) {
            throw new Exception("ID người dùng không được để trống khi cập nhật");
        }

        // kiểm tra tồn tại
        Specification<User> spec = Specification.where(UserSpecification.hasId(user.getId()));
        Optional<User> existingUserOp = userRepository.findOne(spec);
        if(existingUserOp.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy người dùng với ID: " + user.getId());
        }

        User existingUser = existingUserOp.get();

        // kiểm tra trùng số điện thoại
        spec = Specification.where(
                UserSpecification.hasPhoneNumberExact(user.getPhoneNumber())
                        .and(UserSpecification.notId(user.getId()))
        );
        boolean existByPhoneNumber = userRepository.exists(spec) ;
        if(existByPhoneNumber) {
            throw new DataIntegrityViolationException("Số điện thoại đã tồn tại trong hệ thống");
        }

        // kiểm tra trùng email
        spec = Specification.where(
                UserSpecification.hasEmailExact(user.getEmail())
                        .and(UserSpecification.notId(user.getId()))
        );
        boolean existByEmail = userRepository.exists(spec) ;
        if(existByEmail) {
            throw new DataIntegrityViolationException("Email đã tồn tại trong hệ thống");
        }

        existingUser.setFullName(user.getFullName());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setEmail(user.getEmail());
        existingUser.setAddress(user.getAddress());
        existingUser.setDateOfBirth(user.getDateOfBirth());
        existingUser.setGender(user.getGender());
        existingUser.setRole(user.getRole());
        existingUser.setStatus(user.getStatus());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    @Override
    public void changeUserPassword(User user) throws Exception {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }


    @Override
    public void deleteUser(int userId){
        Specification<User> spec = Specification.where(UserSpecification.hasId(userId));
        boolean exist = userRepository.exists(spec) ;
        if(!exist) {
            throw new EntityNotFoundException("Không tìm thấy người dùng với ID: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
