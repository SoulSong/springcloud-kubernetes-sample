package com.shf.spring.kube.base.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 *
 * @Author: songhaifeng
 * @Date: 2019/7/5 15:35
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable {

    private int id;
    private String username;
    private String password;
    private String organization;
    private List<String> roles;

}
