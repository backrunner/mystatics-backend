package top.backrunner.installstat.core.service.impl;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.backrunner.installstat.core.service.BaseService;
import top.backrunner.installstat.core.entity.Page;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
@Primary
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
public class BaseServiceImpl<T> extends HibernateDaoSupport implements BaseService<T> {

    @Override
    public Session getHibernateSession() {
        return this.getSessionFactory().getCurrentSession();
    }

    @Override
    public Connection getConnection(){
        Connection connection = null;
        try{
            connection = SessionFactoryUtils.getDataSource(this.getSessionFactory()).getConnection();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public HibernateTemplate getTemplate() {
        return this.getHibernateTemplate();
    }

    @Override
    public boolean updateByHql(String hql) {
        boolean flag = false;
        try {
            int num = this.getHibernateTemplate().bulkUpdate(hql);
            flag = num > 0 ? true : false;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return flag;
    }

    @Override
    public boolean removeByHql(String hql) {
        boolean flag = false;
        try {
            flag = this.executeHql(hql) > 0;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return flag;
    }

    @Override
    public long countByHql(String hql) {
        Session session = getHibernateSession();
        try{
            return (Long)session.createQuery(hql).setMaxResults(1).uniqueResult();
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public long countBySql(String sql) {
        Session session = getHibernateSession();
        try{
            return (Long)session.createSQLQuery(sql).setMaxResults(1).uniqueResult();
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<T> showPage(String hql, Page page) {
        Session session = this.getHibernateSession();
        try {
            return session.createQuery(hql).setFirstResult(page.getCurrentPage() * page.getPageSize()).setMaxResults(page.getPageSize()).list();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> showPage(String hql, int currentPage, int pageSize) {
        Session session = this.getHibernateSession();
        try {
            return session.createQuery(hql).setFirstResult(currentPage * pageSize).setMaxResults(pageSize).list();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findBySql(String sql) {
        try {
            return this.getHibernateSession().createSQLQuery(sql).list();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long executeSql(String sql){
        try{
            return this.getHibernateSession().createSQLQuery(sql).executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public long executeHql(String hql) {
        try {
            return this.getHibernateTemplate().bulkUpdate(hql);
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<T> find(DetachedCriteria criteria) {
        List list = null;
        try{
            list = this.getTemplate().findByCriteria(criteria);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return list;
    }

    @Override
    public List<T> getEntities(String hql, Object[] obj) {
        List<T> list;
        try {
            list = (List<T>) this.getHibernateTemplate().find(hql, obj);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return list;
    }

    @Override
    public List<T> getEntities(Class clazz) {
        List<T> list;
        try {
            list = (List<T>) this.getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(clazz));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return list;
    }

    @Override
    public List<T> getEntitiesByHql(String hql) {
        List<T> list;
        try {
            list = (List<T>) this.getHibernateTemplate().find(hql);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    @Override
    public T getByHql(String hql) {
        T t;
        try {
            t = (T) this.getHibernateSession().createQuery(hql).setMaxResults(1).uniqueResult();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return t;
    }

    @Override
    public T getById(Class clazz, Serializable id) {
        T t;
        try {
            t = (T) this.getHibernateTemplate().get(clazz, id);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return t;
    }

    @Override
    public T getById(Class clazz, String id) {
        T t;
        try {
            t = (T) this.getHibernateTemplate().get(clazz, id);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return t;
    }

    @Override
    public T getById(Class clazz, int id) {
        T t;
        try {
            t = (T) this.getHibernateTemplate().get(clazz, id);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return t;
    }

    @Override
    public boolean removeEntity(Object entity) {
        try {
            this.getHibernateTemplate().delete(entity);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateEntity(Object entity) {
        try {
            this.getHibernateTemplate().update(entity);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<T> findByHql(String hql){
        List<T> list;
        try{
            list = (List<T>) this.getHibernateTemplate().find(hql);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return list;
    }

    @Override
    public Integer addThenGetNumKey(Object entity) {
        Integer id;
        try {
            id = (Integer) this.getHibernateTemplate().save(entity);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return id;
    }

    @Override
    public String addThenGetKey(Object entity) {
        String id;
        try {
            id = (String) this.getHibernateTemplate().save(entity);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return id;
    }

    @Override
    public boolean add(Object entity) {
        boolean flag = false;
        try {
            Serializable serializable = this.getHibernateTemplate().save(entity);
            if (serializable != null){
                flag = true;
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return flag;
    }

}
