package com.project.mrs.seeder;

import com.project.mrs.entity.User;
import com.project.mrs.enums.UserRole;
import com.project.mrs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNullApi;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SuperAdminSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Autowired
    SuperAdminSeeder(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository)
    {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loadSuperAdminUser();
    }

    private void loadSuperAdminUser()
    {
        User superAdmin = User
                .builder()
                .username("superAdmin")
                .password(bCryptPasswordEncoder.encode("superPassword@123"))
                .userEmail("superAdmin@mail.com")
                .firstName("Super")
                .lastName("Admin")
                .userRole(UserRole.ROLE_SUPER_ADMIN)
                .build();

        userRepository.save(superAdmin);
    }

}
