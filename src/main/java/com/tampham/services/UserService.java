package com.tampham.services;

import com.tampham.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Service này sẽ implements method loadUsername từ UserDetailsService,
 * username được lấy ra từ db và trả về cho việc sử dụng authenticate
 * **/
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User " + username + " is not valid"));
    }
}
