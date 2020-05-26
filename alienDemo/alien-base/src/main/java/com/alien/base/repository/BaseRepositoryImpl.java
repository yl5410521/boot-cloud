package com.alien.base.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.CriteriaImpl.OrderEntry;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alien.base.entity.BaseEntity;
import com.alien.base.page.Pager;
import com.alien.base.utils.reflex.ReflectionUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;


/**
 * Created by
 */
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> 
		implements BaseRepository<T, ID>  {

	private static final String ORDER_LIST_PROPERTY_NAME = "orderList";// "排序"属性名称
	private static final String CREATE_DATE_PROPERTY_NAME = "createDate";// "创建日期"属性名称

	@SuppressWarnings("unused")
	private JPAQueryFactory queryFactory;

	@Autowired
	private EntityManager entityManager;

	private Class<T> clazz;

	@SuppressWarnings({ "rawtypes", "unused" })
	private final JpaEntityInformation entityInformation;

	// 父类没有不带参数的构造方法，这里手动构造父类

	public BaseRepositoryImpl(JpaEntityInformation<T, ?> ef, EntityManager em) {

		super(ef, em);

		this.entityManager = em;
		this.clazz = ef.getJavaType();
		this.entityInformation = ef;
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	public Session getSession() {
		Session session = entityManager.unwrap(Session.class);
		return session;

	}

	public SessionFactory getSessionFactory() {
		Session session = (Session) entityManager.getDelegate();
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		return sessionFactory;
	}

	// 通过EntityManager来完成查询
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> listBySQL(String sql) {
		return getSession().createSQLQuery(sql).getResultList();
	}

	@Override
	public void updateBySql(String sql, Object... args) {
		Query query = getSession().createSQLQuery(sql);
		int i = 0;
		for (Object arg : args) {
			query.setParameter(++i, arg);
		}
		query.executeUpdate();
	}

	@Override
	public void updateByHql(String hql, Object... args) {
		Query query = getSession().createQuery(hql);
		int i = 0;
		for (Object arg : args) {
			System.out.println(arg);
			query.setParameter(++i, arg);
		}
		query.executeUpdate();
	}

	public void saveEntity(T t) {
		getSession().persist(t);
	}

	@SuppressWarnings("unchecked")
	public T findById(T t, Object id) {
		return (T) getSession().find(t.getClass(), id);
	}

	public void deleteEntity(Object id) {
		Query query = getSession().createQuery("delete from " + clazz.getSimpleName() + " p where p.id = ?1");
		query.setParameter(1, id);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		String hql = "select t from " + clazz.getSimpleName() + " t";
		Query query = getSession().createQuery(hql);
		List<T> beans = query.getResultList();
		return beans;
	}

	@SuppressWarnings({ "unchecked" })
	public Pager findsqlpage(String tableName, String fields, String sqlCondition, List<String> list, Pager pager) {
		List<T> param = new ArrayList<T>();
		String sqls = null;
		if (tableName != null && fields != null) {
			sqls = "select " + fields + " from " + tableName;

			try {
				if (sqlCondition != null) {
					sqls = sqls + " " + sqlCondition;
				}

				System.out.println("----------" + sqls);
				Query query = getSession().createSQLQuery(pager.getPageMySQL(sqls, pager.getPageNumber(), pager.getPageSize()));

				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						query.setParameter(i + 1, list.get(i));
					}
					param = query.getResultList();
					pager.setTotalCount(findsqltotalcount(tableName, fields, sqlCondition, list));
					pager.initPageBean(pager.getTotalCount(), pager.getPageSize());
					pager.setResult(param);
					pager.setCurrentCount(param.size());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pager;
	}

	@SuppressWarnings({ "unchecked" })
	public Pager findsqlpage(String tableName, String fields, List<String> list, Pager pager) {
		List<T> param = new ArrayList<T>();
		String sqls = null;
		if (tableName != null && fields != null) {
			sqls = "select " + fields + " from " + tableName;

			try {

				Query query = getSession()
						.createSQLQuery(pager.getPageMySQL(sqls, pager.getPageNumber(), pager.getPageSize()));

				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						query.setParameter(i + 1, list.get(i));
					}
					param = query.getResultList();
					pager.setTotalCount(findsqltotalcount(tableName, fields, list));
					pager.initPageBean(pager.getTotalCount(), pager.getPageSize());
					pager.setResult(param);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pager;
	}

	@SuppressWarnings("rawtypes")
	public int findsqltotalcount(String tableName, String fields, List<String> list) {
		int totalcount = 0;
		String sqls = "select " + fields + " from " + tableName;
		try {
			Query query = getSession().createSQLQuery(sqls);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					query.setParameter(i + 1, list.get(i));
				}
				List lists = query.getResultList();
				totalcount = lists.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalcount;
	}

	@SuppressWarnings("rawtypes")
	public int findsqltotalcount(String tableName, String fields, String sqlCondition, List<String> list) {
		int totalcount = 0;
		String sqls = "select " + fields + " from " + tableName;
		try {

			if (sqlCondition != null) {
				sqls = sqls + " " + sqlCondition;
			}
			Query query = getSession().createSQLQuery(sqls);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					query.setParameter(i + 1, list.get(i));
				}
				List lists = query.getResultList();
				totalcount = lists.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalcount;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public int deleteEqualField(String field, Object value) {
		Query query = getSession()
				.createQuery("delete from " + clazz.getSimpleName() + " p where p." + field + " = ?1");
		query.setParameter(1, value);
		return query.executeUpdate();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateEntity(T bean) {
		getSession().merge(bean);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int batchDelete(List<Object> ids) {
		StringBuffer hql = new StringBuffer("delete from " + clazz.getSimpleName() + " where id  in(:ids)");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("ids", ids);
		return query.executeUpdate();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteLikeField(String field, String value) {
		Query query = getSession()
				.createQuery("delete from " + clazz.getSimpleName() + " p where p." + field + " like ?1");
		query.setParameter(1, value);
		return query.executeUpdate();
	}



	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<T> getAllList() {
		ClassMetadata classMetadata = getSessionFactory().getClassMetadata(clazz);
		String hql;
		if (ArrayUtils.contains(classMetadata.getPropertyNames(), ORDER_LIST_PROPERTY_NAME)) {
			hql = "from " + clazz.getName() + " as entity order by entity." + ORDER_LIST_PROPERTY_NAME + " desc";
		} else {
			hql = "from " + clazz.getName();
		}
		return getSession().createQuery(hql).list();
	}

	public Long getTotalCount() {
		String hql = "select count(1) from " + clazz.getName();
		return (Long) getSession().createQuery(hql).setCacheable(true).uniqueResult();
	}

	public void update(T entity) {
		Assert.notNull(entity, "entity is required");
		if (entity instanceof BaseEntity) {
			try {
				Method method = entity.getClass().getMethod(BaseEntity.ON_UPDATE_METHOD_NAME);
				method.invoke(entity);
				getSession().update(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			getSession().update(entity);
		}
	}


	public void updateOther(T entity) {
		Assert.notNull(entity, "entity is required");
		if (entity instanceof BaseEntity) {
			try {
				Method method = entity.getClass().getMethod(BaseEntity.ON_UPDATE_METHOD_NAME);
				method.invoke(entity);
				getSession().merge(entity);
				getSession().flush();
				getSession().clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			getSession().merge(entity);
		}
	}




	public void deleteById(String id) {
		Assert.notNull(id, "id is required");
		T entity = (T) getSession().load(clazz, (Serializable) id);
		getSession().delete(entity);
	}


	public void deleteByIds(Object[] ids) {
		Assert.notEmpty(ids, "ids must not be empty");
		for (Object id : ids) {
			T entity = (T) getSession().load(clazz, (Serializable) id);
			getSession().delete(entity);
		}
	}

	public void flush() {
		getSession().flush();
	}

	public void evict(Object object) {
		Assert.notNull(object, "object is required");
		getSession().evict(object);
	}

	public void clear() {
		getSession().clear();
	}

	@SuppressWarnings("unchecked")
	public List<T> getBySQL(String sql) {
		return (List<T>) getSession().createSQLQuery(sql).addEntity(clazz).list();
	}

	@SuppressWarnings("deprecation")
	public Pager findPager(Pager pager) {
		Criteria criteria = getSession().createCriteria(clazz);
		criteria.setCacheable(true);
		return findPager(pager, criteria);
	}

	@SuppressWarnings("deprecation")
	public Pager findPager(Pager pager, Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(clazz);
		for (Criterion criterion : criterions) {
			criteria.add(criterion);
		}
		criteria.setCacheable(true);
		return findPager(pager, criteria);
	}

	@SuppressWarnings("deprecation")
	public Pager findPager(Pager pager, Order... orders) {
		Criteria criteria = getSession().createCriteria(clazz);
		for (Order order : orders) {
			criteria.addOrder(order);
		}
		criteria.setCacheable(true);
		return findPager(pager, criteria);
	}

	@SuppressWarnings("deprecation")
	public Pager findPager(Pager pager, Criteria criteria) {
		Assert.notNull(pager, "pager is required");
		Assert.notNull(criteria, "criteria is required");

		Integer pageNumber = pager.getPageNumber();
		Integer pageSize = pager.getPageSize();
		String searchBy = pager.getSearchBy();
		String keyword = pager.getKeyword();
		String orderBy = pager.getOrderBy();
		Pager.Order order = pager.getOrder();

		if (StringUtils.isNotEmpty(searchBy) && StringUtils.isNotEmpty(keyword)) {
			if (searchBy.contains(".")) {
				String alias = StringUtils.substringBefore(searchBy, ".");
				criteria.createAlias(alias, alias);
			}
			criteria.add(Restrictions.like(searchBy, "%" + keyword + "%"));
		}

		pager.setTotalCount(criteriaResultTotalCount(criteria));

		if (StringUtils.isNotEmpty(orderBy) && order != null) {
			if (order == Pager.Order.asc) {
				criteria.addOrder(Order.asc(orderBy));
			} else {
				criteria.addOrder(Order.desc(orderBy));
			}
		}

		ClassMetadata classMetadata = getSessionFactory().getClassMetadata(clazz);
		if (!StringUtils.equals(orderBy, ORDER_LIST_PROPERTY_NAME)
				&& ArrayUtils.contains(classMetadata.getPropertyNames(), ORDER_LIST_PROPERTY_NAME)) {
			criteria.addOrder(Order.asc(ORDER_LIST_PROPERTY_NAME));
			criteria.addOrder(Order.desc(CREATE_DATE_PROPERTY_NAME));
			if (StringUtils.isEmpty(orderBy) || order == null) {
				pager.setOrderBy(ORDER_LIST_PROPERTY_NAME);
				pager.setOrder(Pager.Order.asc);
			}
		} else if (!StringUtils.equals(orderBy, CREATE_DATE_PROPERTY_NAME)
				&& ArrayUtils.contains(classMetadata.getPropertyNames(), CREATE_DATE_PROPERTY_NAME)) {
			criteria.addOrder(Order.desc(CREATE_DATE_PROPERTY_NAME));
			if (StringUtils.isEmpty(orderBy) || order == null) {
				pager.setOrderBy(CREATE_DATE_PROPERTY_NAME);
				pager.setOrder(Pager.Order.desc);
			}
		}
		criteria.setFirstResult((pageNumber - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		criteria.setCacheable(true);
		pager.setResult(criteria.list());
		return pager;
	}

	@SuppressWarnings("deprecation")
	public Pager findPagers(Pager pager, Criteria criteria) {
		Assert.notNull(pager, "pager is required");
		Assert.notNull(criteria, "criteria is required");

		Integer pageNumber = pager.getPageNumber();
		Integer pageSize = pager.getPageSize();
		String searchBy = pager.getSearchBy();
		String keyword = pager.getKeyword();
		String orderBy = pager.getOrderBy();
		Pager.Order order = pager.getOrder();

		if (StringUtils.isNotEmpty(searchBy) && StringUtils.isNotEmpty(keyword)) {
			if (searchBy.contains(".")) {
				String alias = StringUtils.substringBefore(searchBy, ".");
				criteria.createAlias(alias, alias);
			}
			criteria.add(Restrictions.like(searchBy, "%" + keyword + "%"));
		}

		pager.setTotalCount(criteriaResultTotalCount(criteria));

		if (StringUtils.isNotEmpty(orderBy) && order != null) {
			if (order == Pager.Order.asc) {
				criteria.addOrder(Order.asc(orderBy));
			} else {
				criteria.addOrder(Order.desc(orderBy));
			}
		}

		ClassMetadata classMetadata = getSessionFactory().getClassMetadata(clazz);
		if (!StringUtils.equals(orderBy, ORDER_LIST_PROPERTY_NAME)
				&& ArrayUtils.contains(classMetadata.getPropertyNames(), ORDER_LIST_PROPERTY_NAME)) {
			criteria.addOrder(Order.asc(ORDER_LIST_PROPERTY_NAME));
			criteria.addOrder(Order.desc(CREATE_DATE_PROPERTY_NAME));
			if (StringUtils.isEmpty(orderBy) || order == null) {
				pager.setOrderBy(ORDER_LIST_PROPERTY_NAME);
				pager.setOrder(Pager.Order.asc);
			}
		} else if (!StringUtils.equals(orderBy, CREATE_DATE_PROPERTY_NAME)
				&& ArrayUtils.contains(classMetadata.getPropertyNames(), CREATE_DATE_PROPERTY_NAME)) {
			criteria.addOrder(Order.desc(CREATE_DATE_PROPERTY_NAME));
			if (StringUtils.isEmpty(orderBy) || order == null) {
				pager.setOrderBy(CREATE_DATE_PROPERTY_NAME);
				pager.setOrder(Pager.Order.desc);
			}
		}
		criteria.setFirstResult((pageNumber - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		criteria.setCacheable(true);

		pager.setResult(criteria.list());
		return pager;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Pager findPager(Class clazz, Pager pager, List<String> criteriaList, Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(clazz);
		if (criteriaList != null) {
			for (String str : criteriaList) {
				criteria.createCriteria(str, str);
			}
		}
		for (Criterion criterion : criterions) {
			criteria.add(criterion);
		}
		criteria.setCacheable(true);
		return findPager(clazz, pager, criteria);
	}

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
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public Pager findPager(Class clazz, Pager pager, Criteria criteria) {
		Assert.notNull(pager, "pager is required");
		Assert.notNull(criteria, "criteria is required");

		Integer pageNumber = pager.getPageNumber();
		Integer pageSize = pager.getPageSize();
		String searchBy = pager.getSearchBy();
		String keyword = pager.getKeyword();
		String orderBy = pager.getOrderBy();
		Pager.Order order = pager.getOrder();

		if (StringUtils.isNotEmpty(searchBy) && StringUtils.isNotEmpty(keyword)) {
			if (searchBy.contains(".")) {
				String alias = StringUtils.substringBefore(searchBy, ".");
				criteria.createAlias(alias, alias);
			}
			criteria.add(Restrictions.like(searchBy, "%" + keyword + "%"));
		}

		pager.setTotalCount(criteriaResultTotalCount(criteria));

		if (StringUtils.isNotEmpty(orderBy) && order != null) {
			if (order == Pager.Order.asc) {
				criteria.addOrder(Order.asc(orderBy));
			} else {
				criteria.addOrder(Order.desc(orderBy));
			}
		}

		ClassMetadata classMetadata = getSessionFactory().getClassMetadata(clazz);
		if (!StringUtils.equals(orderBy, ORDER_LIST_PROPERTY_NAME)
				&& ArrayUtils.contains(classMetadata.getPropertyNames(), ORDER_LIST_PROPERTY_NAME)) {
			criteria.addOrder(Order.asc(ORDER_LIST_PROPERTY_NAME));
			criteria.addOrder(Order.desc(CREATE_DATE_PROPERTY_NAME));
			if (StringUtils.isEmpty(orderBy) || order == null) {
				pager.setOrderBy(ORDER_LIST_PROPERTY_NAME);
				pager.setOrder(Pager.Order.asc);
			}
		} else if (!StringUtils.equals(orderBy, CREATE_DATE_PROPERTY_NAME)
				&& ArrayUtils.contains(classMetadata.getPropertyNames(), CREATE_DATE_PROPERTY_NAME)) {
			criteria.addOrder(Order.desc(CREATE_DATE_PROPERTY_NAME));
			if (StringUtils.isEmpty(orderBy) || order == null) {
				pager.setOrderBy(CREATE_DATE_PROPERTY_NAME);
				pager.setOrder(Pager.Order.desc);
			}
		}
		criteria.setFirstResult((pageNumber - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		criteria.setCacheable(true);
		pager.setTotalPages(pager.getTotalCount() / pager.getPageSize());
		pager.setResult(criteria.list());
		return pager;
	}

	// 获取Criteria查询数量
	@SuppressWarnings("unchecked")
	private int criteriaResultTotalCount(Criteria criteria) {
		Assert.notNull(criteria, "criteria is required");

		int criteriaResultTotalCount = 0;
		try {
			CriteriaImpl criteriaImpl = (CriteriaImpl) criteria;
			criteria.setCacheable(true);
			Projection projection = criteriaImpl.getProjection();
			ResultTransformer resultTransformer = criteriaImpl.getResultTransformer();
			List<OrderEntry> orderEntries = (List<OrderEntry>) ReflectionUtil.getFieldValue(criteriaImpl, "orderEntries");
			ReflectionUtil.setFieldValue(criteriaImpl, "orderEntries", new ArrayList<Object>());

			Integer totalCount = ((Long) criteriaImpl.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			if (totalCount != null) {
				criteriaResultTotalCount = totalCount;

			}

			criteriaImpl.setProjection(projection);
			if (projection == null) {
				criteriaImpl.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
			}
			if (resultTransformer != null) {
				criteriaImpl.setResultTransformer(resultTransformer);
			}
			ReflectionUtil.setFieldValue(criteriaImpl, "orderEntries", orderEntries);
		} catch (Exception e) {

		}
		return criteriaResultTotalCount;
	}

	/**
	 * 执行sql语句
	 * 
	 * @param sql
	 * @param list
	 * @return
	 */

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public boolean runsql(String sql, List<String> list) {
		boolean flag = false;
		try {
			NativeQuery query = getSession().createNativeQuery(sql);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					query.setString(i, list.get(i));
				}
			}
			query.executeUpdate();
			getSession().flush();
			getSession().clear();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 数组中添加元素
	 * 
	 * @param ori 原数组
	 * @param val 添加的元素值
	 * @param index 下标
	 * @return
	 */
	public static String[] insert(String[] ori, String val, int index) {
		for (int i = ori.length - 1; i > index; i--)
			ori[i] = ori[i - 1];
		ori[index] = val;
		return ori;
	}

	/** -------------sql分页---------- */
	/**
	 * compcode传入的参数 pageNumber页码 pageSize每页记录数
	 */
	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })

	public Pager runsqlpage(String tableName, String sqlCondition, List<String> list, Pager pager) {
		List<T> param = new ArrayList<T>();
		String sqls = "select * from " + tableName;
		try {
			if (sqlCondition != null) {
				sqls = sqls + " " + sqlCondition;
			}

			NativeQuery query = getSession()
					.createNativeQuery(pager.getOrclPageSql(sqls, pager.getPageNumber(), pager.getPageSize()))
					.addEntity(clazz);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					query.setString(i, list.get(i));
				}
				query.setCacheable(true);
				param = query.list();
				pager.setTotalCount(findtotalcount(tableName, sqlCondition, list));
				pager.initPageBean(pager.getTotalCount(), pager.getPageSize());
				pager.setResult(param);
				getSession().flush();
				getSession().clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pager;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public int findtotalcount(String tableName, String sqlCondition, List<String> list) {
		int totalcount = 0;
		String sqls = "select count(1) as count from " + tableName;
		try {

			if (sqlCondition != null) {
				sqls = sqls + " " + sqlCondition;
			}
			Query query = (NativeQuery) getSession().createNativeQuery(sqls);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					((org.hibernate.query.Query) query).setString(i, list.get(i));
				}
				List lists = ((org.hibernate.query.Query) query).list();
				totalcount = ((BigDecimal) lists.get(0)).intValue();
			}
			getSession().flush();
			getSession().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalcount;
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@Override
	public List<T> runsqlList(String sql, List<String> list) {
		List<T> lists = new ArrayList<T>();
		try {
			NativeQuery query = getSession().createNativeQuery(sql).addEntity(clazz);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					query.setString(i, list.get(i));
				}
				lists = query.list();
			}
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void importTxt(File myTxt, String myTxtFileName, String sql) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(myTxt));// 构造一个BufferedReader类来读取文件
			String s = null;
			int lineNum = 0;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				lineNum++;
				if (lineNum == 1)
					continue;
				String line = s.replaceAll("\\|\\|", ",");
				System.out.println(line);
				String[] custArray = line.split("\\,");
				System.out.println(JSON.toJSON(custArray));
				insertTxt(custArray, sql);
				Thread.sleep(10);
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insertTxt(String[] strArray, String sql) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < strArray.length; i++) {
			list.add(strArray[i]);
		}
		runsql(sql, list);

	}

	/**
	 * 查询全部列表,并做分页
	 * 
	 * @param start  开始页数
	 * @param pageSize 每页显示的数据条数
	 * @param sort 排序
	 */


	public Iterator<T> selectAll(int start, int pageSize,String sort) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		Pageable pageable=null;
		if(sort.toLowerCase().equals("asc")) {
		Sort asc = Sort.by(Sort.Order.asc("id"));
		pageable = PageRequest.of(start, pageSize,asc); // 根据start、size、sort创建分页对象
		}
		if(sort.toLowerCase().equals("desc")) {
			  Sort desc = Sort.by(Sort.Order.desc("id"));
			  pageable = PageRequest.of(start, pageSize,desc); // 根据start、size、sort创建分页对象
		}
		Page<T> page = findAll(pageable);
		Iterator<T> iterator = page.iterator();
		return iterator;
	}

	/**
	 * 
	 * @param size  每页多少条
	 * @param start 从第几页开始
	 * @param sort  排序 desc,asc 
	 * @return
	 */

	@SuppressWarnings({ "unchecked" })
	public Page<T> findPage(int size, int start,String sort) {
		Pageable pageable=null;
		if(sort.toLowerCase().equals("asc")) {
		Sort asc = Sort.by(Sort.Order.asc("id"));
		pageable = PageRequest.of(start, size,asc); // 根据start、size、sort创建分页对象
		}
		if(sort.toLowerCase().equals("desc")) {
			  Sort desc = Sort.by(Sort.Order.desc("id"));
			  pageable = PageRequest.of(start, size,desc); // 根据start、size、sort创建分页对象
		}
		
		@SuppressWarnings("rawtypes")
		Page<T> page = (Page) findAll(pageable);
		return page;
	}

	/**
	 * 
	 * @param start 页数
	 * @param size 每页条数
	 * @param map  条件键值对
	 * @return
	 */
	
	@SuppressWarnings("serial")
	public Page<T> findCriteria( int start, int size, String sort,Map<String, Object> map) {
		Pageable pageable=null;
		if(sort.toLowerCase().equals("asc")) {
		Sort asc = Sort.by(Sort.Order.asc("id"));
		pageable = PageRequest.of(start, size,asc); // 根据start、size、sort创建分页对象
		}
		if(sort.toLowerCase().equals("desc")) {
			  Sort desc = Sort.by(Sort.Order.desc("id"));
			  pageable = PageRequest.of(start, size,desc); // 根据start、size、sort创建分页对象
		}
		
		Page<T> t = findAll(new Specification<T>() {
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (map != null) {
					map.keySet().forEach(key -> list
							.add(criteriaBuilder.like(root.get(key).as(String.class), "%" + map.get(key) + "%")));
				}
				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));
			}
		}, pageable);
		return t;
	}
	

	@SuppressWarnings("serial")
	public Page<T> findPage( int start,int size,String sort,Map<String, Object> map) {
		Pageable pageable=null;
		if(sort.toLowerCase().equals("asc")) {
		Sort asc = Sort.by(Sort.Order.asc("id"));
		pageable = PageRequest.of(start, size,asc); // 根据start、size、sort创建分页对象
		}
		if(sort.toLowerCase().equals("desc")) {
			  Sort desc = Sort.by(Sort.Order.desc("id"));
			  pageable = PageRequest.of(start, size,desc); // 根据start、size、sort创建分页对象
		}
		if (map != null) {
		Page<T> userpage =findAll(new Specification<T>() {
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				//List<Predicate> pList=new ArrayList<Predicate>();
				 for (String key : map.keySet()) {
						Predicate p = criteriaBuilder.like(root.get(key).as(String.class), map.get(key).toString());
						query.where(criteriaBuilder.and(p));
						   }
				return query.getRestriction();
			}
		}, pageable);
		return userpage;
		}
		return null;
	}

	/**
	 * 根据SQL获取实体对象
	 * 
	 * @param sql
	 * 
	 * @return 实体对象
	 */
	@SuppressWarnings("unchecked")
	public T getBySql(String sql) {
		return (T) getSession().createSQLQuery(sql).addEntity(clazz).uniqueResult();
	}

	

}