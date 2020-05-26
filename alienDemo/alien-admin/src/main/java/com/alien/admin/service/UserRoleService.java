package com.alien.admin.service;


import com.alien.admin.entity.UserRole;
import com.alien.base.service.BaseService;

import java.util.List;

/**
 * 用户角色接口
 * @author Exrickx
 */
public interface UserRoleService extends BaseService<UserRole,String> {

    /**
     * 通过roleId查找
     * @param roleId
     * @return
     */
    List<UserRole> findByRoleId(String roleId);

    /**
     * 删除用户角色
     * @param userId
     */
    void deleteByUserId(String userId);
}
