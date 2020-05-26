package com.alien.admin.service;


import com.alien.admin.entity.Role;
import com.alien.base.service.BaseService;

import java.util.List;

/**
 * 角色接口
 * @author Exrickx
 */
public interface RoleService extends BaseService<Role,String> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);
}
