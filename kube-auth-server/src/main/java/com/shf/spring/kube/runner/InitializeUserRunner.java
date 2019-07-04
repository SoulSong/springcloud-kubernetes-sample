package com.shf.spring.kube.runner;

import com.shf.spring.kube.base.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Mock some users in db.
 *
 * @Author: songhaifeng
 * @Date: 2019/7/5 17:20
 */
@Component
public class InitializeUserRunner implements CommandLineRunner {

    private static final Map<String, UserEntity> USERS = new HashMap<>();

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitializeUserRunner(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        USERS.put("foo1", UserEntity.builder().id(1).username("foo1")
                .password(passwordEncoder.encode("foo123"))
                .roles(Arrays.asList("User", "Admin"))
                .organization("foo-org").build());

        USERS.put("foo2", UserEntity.builder().id(1).username("foo2")
                .password(passwordEncoder.encode("foo234"))
                .roles(Collections.singletonList("Guest"))
                .organization("foo-org").build());

        USERS.put("bar1", UserEntity.builder().id(1).username("bar1")
                .password(passwordEncoder.encode("bar123"))
                .roles(Arrays.asList("Guest", "Admin"))
                .organization("bar-org").build());

        USERS.put("bar2", UserEntity.builder().id(1).username("bar2")
                .password(passwordEncoder.encode("bar234"))
                .roles(Collections.singletonList("Admin"))
                .organization("bar-org").build());
    }

    public static Map<String, UserEntity> getUsers() {
        return USERS;
    }
}
