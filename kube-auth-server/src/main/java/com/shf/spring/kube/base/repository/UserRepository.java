package com.shf.spring.kube.base.repository;

import com.shf.spring.kube.base.entity.UserEntity;
import com.shf.spring.kube.runner.InitializeUserRunner;
import org.springframework.stereotype.Repository;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/7/5 15:47
 */
@Repository
public class UserRepository {

    public UserEntity findByUsername(final String userName) {
        return InitializeUserRunner.getUsers().get(userName);
    }

}
