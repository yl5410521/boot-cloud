package com.alien.admin.dao;


import com.alien.admin.entity.Log;
import com.alien.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * 日志数据处理层
 * @author yaolin
 */
public interface LogDao extends BaseRepository<Log,String> {

}
