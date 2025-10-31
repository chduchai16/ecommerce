package com.example.shopapp.imp_services;

import com.example.shopapp.configurations.JwtConfiguration;
import com.example.domain.models.entities.Role;
import com.example.domain.models.entities.User;
import com.example.domain.persistence.repositories.RoleRepository;
import com.example.domain.persistence.repositories.UserRepository;
import com.example.domain.persistence.specifications.RoleSpecification;
import com.example.domain.persistence.specifications.UserSpecification;
import com.example.domain.services.IAuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceIMP implements IAuthService {

    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder ;
    private final JwtConfiguration jwtConfiguration ;
    private final RoleRepository roleRepository ;

    @Override
    public String signIn(String phoneNumber , String password, Integer role ,  Boolean remember) throws Exception {
        Specification<User> spec = Specification.where(UserSpecification.hasPhoneNumberExact(phoneNumber));
        Optional<User> user = userRepository.findOne(spec) ;

        if(remember == null) {
            remember = false ;
        }

        if(user.isEmpty()) {
            throw new BadCredentialsException("Số điện thoại hoặc mật khẩu không đúng") ;
        }

        User existingUser = user.get() ;
        if(existingUser.getRole() == null) {
            throw new EntityNotFoundException("Người dùng này chưa được phân quyền, vui lòng liên hệ quản trị viên") ;
        }

        // Admin có thể đăng nhập với bất kỳ vai trò nào, các vai trò khác phải khớp
        if(!existingUser.getRole().getName().equals("admin") && existingUser.getRole().getId() != role) {
            throw new BadCredentialsException("Tài khoản này không có quyền đăng nhập với vai trò hiện tại") ;
        }

        if (passwordEncoder.matches(password , existingUser.getPassword())){
            return jwtConfiguration.generateToken(existingUser, remember) ;
        }
        else {
            throw new BadCredentialsException("Số điện thoại hoặc mật khẩu không đúng") ;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User signUp(User user) throws Exception {
        Specification<User> userSpec = Specification.where(UserSpecification.hasPhoneNumberExact(user.getPhoneNumber()));
        // kiểm tra số điện thoại đã tồn tại chưa
        Optional<User> optionalUser = userRepository.findOne(userSpec) ;
        if(optionalUser.isPresent()) {
            throw new DataIntegrityViolationException("Số điện thoại này đã tồn tại.") ;
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword()) ;
        user.setPassword(encodedPassword);
        Specification<Role> roleSpec = Specification.where(RoleSpecification.hasId(user.getRole().getId()));
        Optional<Role> optionalRole = roleRepository.findOne(roleSpec) ;
        if(optionalRole.isEmpty()) {
            throw new EntityNotFoundException("Vai trò này không tồn tại");
        }
        user.setRole(optionalRole.get());
        // nếu không nhập tên thì tạo tên ngẫu nhiên
        if(user.getUsername() == null) {
            String uuid = UUID.randomUUID().toString().substring(0,10);
            String name = "user_" + uuid ;
            user.setFullName(name);
        }
        return userRepository.save(user);
    }
}
