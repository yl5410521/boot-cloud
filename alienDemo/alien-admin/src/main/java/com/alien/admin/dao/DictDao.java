package com.alien.admin.dao;

import com.alien.admin.entity.Dict;
import com.alien.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典数据处理层
 * @author yaolin
 */
public interface DictDao extends BaseRepository<Dict,String> {

    /**
     * 排序获取全部
     * @return
     */
    @Query(value = "select * from t_dict d order by d.sort_order", nativeQuery = true)
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
    @Query(value = "select * from t_dict d where d.title like %:key% or d.type like %:key% order by d.sort_order", nativeQuery = true)
    List<Dict> findByTitleOrTypeLike(@Param("key") String key);
}