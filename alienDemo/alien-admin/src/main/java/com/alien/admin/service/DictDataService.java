package com.alien.admin.service;

import com.alien.admin.entity.DictData;
import com.alien.base.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 字典数据接口
 * @author Exrick
 */
public interface DictDataService extends BaseService<DictData,String> {

    /**
     * 多条件获取
     * @param dictData
     * @param pageable
     * @return
     */
    Page<DictData> findByCondition(DictData dictData, Pageable pageable);

    /**
     * 通过dictId获取启用字典 已排序
     * @param dictId
     * @return
     */
    List<DictData> findByDictId(String dictId);

    /**
     * 通过dictId删除
     * @param dictId
     */
    void deleteByDictId(String dictId);
}