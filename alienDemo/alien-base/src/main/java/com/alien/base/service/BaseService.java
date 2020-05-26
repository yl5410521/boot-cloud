package com.alien.base.service;

import com.alien.base.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
@FunctionalInterface
public interface BaseService <T, ID extends Serializable>{

    public BaseRepository<T, ID> getRepository();


    /**
     * 根据ID获取
     * @param id
     * @return
     */
    public default T get(ID id) {
        return getRepository().findById(id).orElse(null);
    }

    /**
     * 获取所有列表
     * @return
     */
    public default List<T> getAll() {
        return getRepository().findAll();
    }

    /**
     * 获取总数
     * @return
     */
    public default Long getTotalCount() {
        return getRepository().count();
    }

    /**
     * 保存
     * @param entity
     * @return
     */
    public default T save(T entity) {

        return getRepository().save(entity);
    }

    /**
     * 修改
     * @param entity
     * @return
     */
    public default T update(T entity) {
        return getRepository().saveAndFlush(entity);
    }

    /**
     * 批量保存与修改
     * @param entities
     * @return
     */
    public default Iterable<T> saveOrUpdateAll(Iterable<T> entities) {
        return getRepository().saveAll(entities);
    }

    /**
     * 删除
     * @param entity
     */
    public default void delete(T entity) {
        getRepository().delete(entity);
    }

    /**
     * 根据Id删除
     * @param id
     */
    public default void delete(ID id) {
        getRepository().deleteById(id);
    }

    /**
     * 批量删除
     * @param entities
     */
    public default void delete(Iterable<T> entities) {
        getRepository().deleteAll(entities);
    }

    /**
     * 清空缓存，提交持久化
     */
    public default void flush() {
        getRepository().flush();
    }

    /**
     * 根据条件查询获取
     * @param spec
     * @return
     */
    public default List<T> findAll(Specification<T> spec) {
        return getRepository().findAll(spec);
    }

    /**
     * 分页获取
     * @param pageable
     * @return
     */
    public default Page<T> findAll(Pageable pageable){
        return getRepository().findAll(pageable);
    }

    /**
     * 根据查询条件分页获取
     * @param spec
     * @param pageable
     * @return
     */
    public default Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return getRepository().findAll(spec, pageable);
    }

    /**
     * 获取查询条件的结果数
     * @param spec
     * @return
     */
    public default long count(Specification<T> spec) {
        return getRepository().count(spec);
    }

}
