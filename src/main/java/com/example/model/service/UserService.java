package com.example.model.service;

import com.example.model.BalanceAppException;
import com.example.model.domain.entity.User;
import com.example.model.domain.form.ChangePasswordForm;
import com.example.model.domain.form.SignUpForm;
import com.example.model.domain.vo.UserVo;
import com.example.model.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpForm signUpForm) {

        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        userRepo.save(new User(signUpForm));

    }

    public UserVo findByLoginId(String username) {
        return userRepo.findOneByLoginId(username).map(UserVo::new).orElseThrow();
    }

    @Transactional
    public void updateContact(String username, String email, String phone) {
        userRepo.findOneByLoginId(username).ifPresent(user -> {
            user.setPhone(phone);
            user.setEmail(email);
        });
    }

    public List<UserVo> search(Boolean status, String name, String phone) {
        Specification<User> spec =
                (root, query, builder) -> builder.equal(root.get("role"), User.Role.MEMBER);

        if (status != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get(
                    "active"), status));
        }
        if (StringUtils.hasLength(name)) {
            spec = spec.and((root, query, builder) ->
                    builder
                            .like(builder.lower(root.get("name")), name.toLowerCase().concat("%")));
        }
        if (StringUtils.hasLength(phone)) {
            spec = spec.and((root, query, builder) ->
                    builder.like(root.get("phone"), phone.concat("%")));
        }
        return userRepo.findAll(spec).stream().map(UserVo::new).toList();
    }

    @Transactional
    public void changesStatus(int id, boolean status) {
        userRepo.findById(id).ifPresent(user -> user.setActive(status));

    }

    @Transactional
    public void changePassword(ChangePasswordForm form) {
        System.out.println("old Password " + form.getOldPassword());
        System.out.println("new Password " + form.getNewPassword());
        System.out.println("Login Id " + form.getLoginId());
        System.out.println("check 2 password " + form.getNewPassword().equals(form.getOldPassword()));

        if (!StringUtils.hasLength(form.getOldPassword())) {
            throw new BalanceAppException("Please Enter old Password");
        }

        if (!StringUtils.hasLength(form.getNewPassword())) {
            throw new BalanceAppException("Please Enter New Password");
        }

        if (form.getNewPassword().equals(form.getOldPassword())) {
            throw new BalanceAppException("Please Enter Different Password without old password");
        }
        
        var user = userRepo.findOneByLoginId(form.getLoginId()).orElseThrow();
        System.out.println("user = " + user);
        System.out.println("userpassword = " + passwordEncoder.matches(form.getOldPassword(), user.getPassword()));
        if (!passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
            throw new BalanceAppException("Please Check Your Old Password");
        }




        user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        
    }
}
