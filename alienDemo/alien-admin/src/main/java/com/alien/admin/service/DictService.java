package com.alien.admin.service;


import com.alien.admin.entity.Dict;
import com.alien.base.service.BaseService;

import java.util.List;

/**
 * 字典接口
 * @author Exrick
 */
public interface DictService extends BaseService<Dict,String> {

    /**
     * 排序获取全部
     * @return
     */
    List<Dict> findAllOrderBySortOrder();

    /**
     * 通过type获取
     * @param type
     * @return
     */
    Dict findByType(String type);

    /**
     * 模糊搜索
     * @param key
     * @return
     */
    List<Dict> findByTitleOrTypeLike(String key);
}