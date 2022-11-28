package com.example.security;

import com.example.model.domain.entity.User;
import com.example.model.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AppUserInit {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo repo;

    @Transactional
    @EventListener(classes = ContextRefreshedEvent.class)
    public void initUser() {
        if (repo.count() == 0) {
            var admin = new User();
            admin.setName("Admin User");
            admin.setLoginId("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(User.Role.ADMIN);
            admin.setActive(true);
            repo.save(admin);

            var member = new User();
            member.setName("Member User");
            member.setLoginId("member");
            member.setPassword(passwordEncoder.encode("member"));
            member.setRole(User.Role.MEMBER);
            member.setActive(true);
            repo.save(member);
        }
    }

}
