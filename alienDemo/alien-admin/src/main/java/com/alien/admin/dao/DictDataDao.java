package com.alien.admin.dao;


import com.alien.admin.entity.DictData;
import com.alien.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典数据数据处理层
 * @author yaolin
 */
public interface DictDataDao extends BaseRepository<DictData,String> {


    /**
     * 通过dictId和状态获取
     * @param dictId
     * @param status
     * @return
     */
    List<DictData> findByDictIdAndStatusOrderBySortOrder(String dictId, Integer status);

    /**
     * 通过dictId删除
     * @param dictId
     */
    void deleteByDictId(String dictId);
}