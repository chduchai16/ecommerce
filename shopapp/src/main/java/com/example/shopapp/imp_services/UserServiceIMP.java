package com.example.shopapp.imp_services;

import com.example.domain.models.entities.User;
import com.example.domain.persistence.repositories.RoleRepository;
import com.example.domain.persistence.repositories.UserRepository;
import com.example.domain.services.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
            throw new DataIntegrityViolationException("Số điện thoại không được trùng lặp");
        }
        // mã hoá mật khẩu
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return this.userRepository.save(user);
    }

    @Override
    public void deleteUser(int userId) throws Exception {
        if(userRepository.findById(userId).isEmpty()){
            throw new EntityNotFoundException("Người dùng này không tồn tại");
        }
        else {
            userRepository.deleteById(userId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(User user) throws Exception {

        if(user.getId()== null) {
            throw new Exception("Id không được để trống khi cập nhật");
        }
        else if (!this.userRepository.existsById(user.getId())){
            throw new EntityNotFoundException("Người dùng này không tồn tại");
        }

        if (user.getPhoneNumber() != null && !user.getPhoneNumber().trim().isEmpty()) {
            userRepository.findByPhoneNumber(user.getPhoneNumber()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(user.getId())) {
                    throw new DataIntegrityViolationException("Số điện thoại đã tồn tại");
                }
            });
        }

        // mã hoá mật khẩu
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    @Override
    public User getUserById(int userId) throws Exception {
        return userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException("Không tìm thấy người dùng này"));
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
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(()-> new EntityNotFoundException("Không tìm thấy người dùng này")) ;
    }

}
