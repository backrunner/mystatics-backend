package top.backrunner.installstat.core.service;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate5.HibernateTemplate;
import top.backrunner.installstat.core.entity.Page;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

public interface BaseService<T> {
    // 获取hibernate会话
    Session getHibernateSession();
    // 获取一个数据库连接
    Connection getConnection();
    // 获取模板
    HibernateTemplate getTemplate();
    // 添加一个实体
    <T> boolean add(T entity);
    // 添加一个实体，返回主键
    <T> String addThenGetKey(T entity);
    // 添加一个实体，返回数字主键
    <T> Integer addThenGetNumKey (T entity);
    // 执行HQL，返回影响了多少条数据
    <T> long executeHql(String hql);
    // 执行HQL，返回实体列表
    <T> List<T> findByHql(String hql);
    // 执行SQL，返回影响了多少条数据
    <T> long executeSql(String sql);
    // 执行SQL，返回实体列表
    <T> List<T> findBySql(String sql);
    // 更新一个实体
    <T> boolean updateEntity(T entity);
    boolean updateByHql(String hql);
    // 删除一个实体
    <T> boolean removeEntity(T entity);
    boolean removeByHql(String hql);
    // 根据ID获取一个实体
    <T> T getById(Class<T> clazz, int id);
    <T> T getById(Class<T> clazz, String id);
    <T> T getById(Class<T> clazz, Serializable id);
    // 根据HQL获取实体
    <T> T getByHql(String hql);
    <T> List<T> getEntitiesByHql(String hql);
    // 动态查询
    <T> List<T> getEntities(Class<T> clazz);
    // 批量查询
    <T> List<T> getEntities(String hql, Object[] obj);
    // 根据动态查询条件查询
    <T> List<T> find(DetachedCriteria criteria);
    // 分页查询
    <T> List<T> showPage(String hql, Page<T> page);
    <T> List<T> showPage(String hql, int currentPage, int pageSize);
    // 计数
    long countByHql(String hql);
    long countBySql(String sql);
}
