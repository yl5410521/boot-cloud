package com.alien.admin.dao;


import com.alien.admin.entity.Role;
import com.alien.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色数据处理层
 * @author yaolin
 */
public interface RoleDao extends BaseRepository<Role,String> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);
}
