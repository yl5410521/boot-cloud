package com.alien.admin.dao;


import com.alien.admin.entity.UserRole;
import com.alien.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色数据处理层
 * @author yaolin
 */
public interface UserRoleDao extends BaseRepository<UserRole,String> {

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
