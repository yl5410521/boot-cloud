package com.alien.base.repository;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alien.base.page.Pager;


/**
 * 
 */
@NoRepositoryBean
//@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

	/**
	 * 获取Session
	 */
	public Session getSession();

	/**
	 * 獲取sessionFactory
	 * 
	 * @return
	 */
	public SessionFactory getSessionFactory();

	public List<Object[]> listBySQL(String sql);

	@Transactional
	public void updateBySql(String sql, Object... args);

	@Transactional
	public void updateByHql(String hql, Object... args);

	void saveEntity(T t);

	/**
	 * 
	 * @param t  实体对象
	 * @param id 主键
	 * @return 实体
	 */
	T findById(T t, Object id);

	/**
	 * 
	 * @param t 实体对象
	 */
	void update(T t);

	/**
	 * @param id 主键
	 */
	void deleteById(String id);

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	public int batchDelete(List<Object> ids);

	/**
	 * like匹配删除
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	int deleteLikeField(String field, String value);

	/**
	 * 查询所有数据
	 */
	public List<T> findAll();

	/**
	 * 
	 * @param tableName    表名
	 * @param fields       查询的字段 eg:id,name……
	 * @param sqlCondition 查询条件eg:where id=?
	 * @param list         条件参数集合 list.add()
	 * @param pager        分页实体
	 * @return
	 */
	public Pager findsqlpage(String tableName, String fields, String sqlCondition, List<String> list, Pager pager);

	/**
	 * 
	 * @param tableName 表名
	 * @param fields    查询的字段 eg:id,name……
	 * @param list      条件参数集合 list.add()
	 * @param pager     分页实体
	 * @return
	 */
	public Pager findsqlpage(String tableName, String fields, List<String> list, Pager pager);

	/**
	 * 
	 * @param tableName    表名
	 * @param fields       查询的字段 eg:id,name……
	 * @param sqlCondition 查询条件eg:where id=?
	 * @param list         条件参数集合 list.add()
	 * @return
	 */
	public int findsqltotalcount(String tableName, String fields, String sqlCondition, List<String> list);

	/**
	 * 
	 * @param tableName    表名
	 * @param fields       查询的字段 eg:id,name……
	 * @param list         条件参数集合 list.add()
	 * @return
	 */
	public int findsqltotalcount(String tableName, String fields, List<String> list);

	/**
	 * 根据字段删除数据
	 * 
	 * @param field 字段
	 * @param value 值
	 * @return
	 */
	int deleteEqualField(String field, Object value);

	/**
	 * 执行sql语句，存储过程需写{call xxx(?,?,?)}
	 * 
	 * @param sql
	 * @param list
	 * @return
	 */

	public boolean runsql(String sql, List<String> list);

	/**
	 * 根据SQL获取实体对象
	 * 
	 * @param sql
	 * 
	 * @return 实体对象
	 */
	public T getBySql(String sql);

	/**
	 * 获取所有实体对象集合
	 * 
	 * @return 实体对象集合
	 */
	public List<T> getAllList();

	/**
	 * 获取所有实体对象总数
	 * 
	 * @return 实体对象总数
	 */
	public Long getTotalCount();

	/**
	 * 更新实体对象
	 * 
	 * @param entity 对象
	 */
	public void updateEntity(T entity);

	/**
	 * 更新部分实体对象
	 * 
	 * @param entity
	 */
	public void updateOther(T entity);

	/**
	 * 根据ID数组删除实体对象
	 * 
	 * @param ids ID数组
	 */
	public void deleteByIds(Object[] ids);

	/**
	 *
	 * @param id
	 */
	public void deleteEntity(Object id);

	/**
	 * 刷新session
	 * 
	 */
	public void flush();

	/**
	 * 清除对象
	 * 
	 * @param object 需要清除的对象
	 */
	public void evict(Object object);

	/**
	 * 清除Session
	 * 
	 */
	public void clear();

	/**
	 * 根据SQL查询
	 * 
	 * @param sql
	 * @return 实体对象
	 */
	public List<T> getBySQL(String sql);

	/**
	 * 根据Pager进行查询(提供分页、查找、排序功能)
	 * 
	 * @param pager Pager对象
	 * 
	 * @return Pager对象
	 */
	public Pager findPager(Pager pager);

	/**
	 * 根据Pager、Criterion进行查询(提供分页、查找、排序功能)
	 * 
	 * @param pager      Pager对象
	 * 
	 * @param criterions Criterion数组
	 * 
	 * @return Pager对象
	 */
	public Pager findPager(Pager pager, Criterion... criterions);

	/**
	 * 根据Pager、Criterion进行查询(提供分页、查找、排序功能)
	 * 
	 * @param pager  Pager对象
	 * 
	 * @param orders Order数组
	 * 
	 * @return Pager对象
	 */
	public Pager findPager(Pager pager, Order... orders);

	/**
	 * 根据Pager、Criteria进行查询(提供分页、查找、排序功能)
	 * 
	 * @param pager    Pager对象
	 * 
	 * @param criteria Criteria对象
	 * 
	 * @return Pager对象
	 */
	public Pager findPager(Pager pager, Criteria criteria);

	/**
	 * 根据Pager、Criteria进行查询(提供分页、查找、排序功能)
	 * 
	 * @param pager    Pager对象
	 * 
	 * @param criteria Criteria对象
	 * 
	 * @return Pager对象 无二级缓存
	 */
	public Pager findPagers(Pager pager, Criteria criteria);

	/**
	 * 分页
	 * 
	 * @param clazz
	 * @param pager
	 * @param criteriaList
	 * @param criterions
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Pager findPager(Class clazz, Pager pager, List<String> criteriaList, Criterion... criterions);

	/**
	 * 新增指定对象类型查询接口
	 * 
	 * @author hpzxyj
	 * @createTime 2012-8-23
	 * @param clazz
	 * @param pager
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Pager findPager(Class clazz, Pager pager, Criteria criteria);

	/*-------------------sql分页-----------------*/

	/**
	 *
	 * @param tableName sql表名
	 * @param sqlCondition sql条件
	 * @param list 条件集合
	 * @param pager
	 * @return
	 */
	public Pager runsqlpage(String tableName, String sqlCondition, List<String> list, Pager pager);

	/**
	 * 计算总条数
	 *
	 * @param tableName sql表名
	 * @param sqlCondition sql 条件
	 * @param list 条件集合
	 * @return
	 */
	public int findtotalcount(String tableName, String sqlCondition, List<String> list);

	/**
	 * 根据sql查询集合
	 * 
	 * @param sql
	 * @param list
	 * @return
	 */
	public List<T> runsqlList(String sql, List<String> list);
	
	/**
	 * 查询全部列表,并做分页
	 * 
	 * @param start  开始页数
	 * @param sort  排序
	 * @param pageSize 每页显示的数据条数
	 */

	public Iterator<T> selectAll(int start, int pageSize,String sort);

	/**
	 * 
	 * @param size  每页多少条
	 * @param start 从第几页开始
	 * @param sort  排序 desc,asc 
	 * @return
	 */

	
	public Page<T> findPage(int size, int start,String sort);

	/**
	 * 
	 * @param start 页数
	 * @param size 每页条数
	 * @param map  条件键值对
	 * @return
	 */
	
	public Page<T> findCriteria( int start, int size, String sort,Map<String, Object> map);
	
	/**
	 * 
	 *
	 * @param start
	 * @param size
	 * @param sort
	 * @param map
	 * @return
	 */
	public Page<T> findPage( int start,int size,String sort,Map<String, Object> map);
	

}