package com.tampham;

import com.tampham.models.Role;
import com.tampham.models.User;
import com.tampham.repository.RoleRepository;
import com.tampham.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@DataJpaTest
public class TestCreateUser {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository usersRepository;

    @Test
    public void createUser(){
        Role adminRole = roleRepository.findByAuthority("ADMIN").get();
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);

        User user = new User("tampham", passwordEncoder.encode("12345"), "Phạm Hoàng Tâm", roles);
        usersRepository.save(user);
    }
}
