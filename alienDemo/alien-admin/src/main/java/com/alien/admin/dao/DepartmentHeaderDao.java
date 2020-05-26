package com.alien.admin.dao;

import com.alien.admin.entity.DepartmentHeader;
import com.alien.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门负责人数据处理层
 * @author yaolin
 */

public interface DepartmentHeaderDao extends BaseRepository<DepartmentHeader,String> {

    /**
     * 通过部门和负责人类型获取
     * @param departmentId
     * @param type
     * @return
     */
    List<DepartmentHeader> findByDepartmentIdAndType(String departmentId, Integer type);

    /**
     * 通过部门id删除
     * @param departmentId
     */
    void deleteByDepartmentId(String departmentId);

    /**
     * 通过userId删除
     * @param userId
     */
    void deleteByUserId(String userId);
}