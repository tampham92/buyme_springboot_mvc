package com.tampham.services;

import com.tampham.dtos.RegisterDto;
import com.tampham.models.Role;
import com.tampham.models.User;
import com.tampham.repository.RoleRepository;
import com.tampham.repository.UserRepository;
import com.tampham.utils.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthService {
    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    public User registerUser(RegisterDto registerDto){
        User user = new User();

        // Create generator code
        String verifyCode = RandomStringUtils.getAlphaNumericString(6);

        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setAddress(registerDto.getAddress());
        user.setFullName(registerDto.getFullName());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setVerifyCode(verifyCode);

        // Các user mới mặc định chỉ cho quyền USER
        Role userRole = roleRepository.findByAuthority("USER").get();
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        user.setAuthorities(authorities);
        usersRepository.save(user);

        return user;
    }

//    public LoginResponseDto loginUser(LoginDto loginDto){
//        try{
//            Authentication auth = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
//            String token = jwtService.generateToken(auth);
//            User user = usersRepository.findByUsername(loginDto.getUsername()).get();
//
//            return new LoginResponseDto(user, token);
//
//        } catch (AuthenticationException e){
//            return new LoginResponseDto(null, "");
//        }
//    }

}
