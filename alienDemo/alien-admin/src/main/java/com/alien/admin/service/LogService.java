package com.alien.admin.service;


import com.alien.admin.common.vo.SearchVo;
import com.alien.admin.entity.Log;
import com.alien.base.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 日志接口
 * @author Exrickx
 */
public interface LogService extends BaseService<Log,String> {

    /**
     * 分页搜索获取日志
     * @param type
     * @param key
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<Log> findByConfition(Integer type, String key, SearchVo searchVo, Pageable pageable);
    /**
     * 删除所有
     */
    void deleteAll();
}
