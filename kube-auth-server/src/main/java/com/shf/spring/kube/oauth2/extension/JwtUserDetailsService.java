package com.shf.spring.kube.oauth2.extension;

import com.shf.spring.kube.base.entity.UserEntity;
import com.shf.spring.kube.base.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Description:
 * Load user info from db, then build the customized userDetails with user_entity.
 *
 * @Author: songhaifeng
 * @Date: 2019/7/5 16:12
 */
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity userEntity = userRepository.findByUsername(username);
        if (null == userEntity) {
            throw new UsernameNotFoundException("Username: " + username + " not found");
        }
        return new JwtUserDetails(userEntity);
    }
}
