package top.backrunner.installstat.core.service.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.internal.CriteriaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import top.backrunner.installstat.core.service.BaseService;
import top.backrunner.installstat.core.entity.Page;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("deprecation")
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Session getHibernateSession() {
        return this.entityManager.unwrap(Session.class);
    }

    public SessionFactory getSessionFactory(){
        return this.getHibernateSession().getSessionFactory();
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
    public boolean updateByHql(String hql) {
        boolean flag;
        try {
            flag = this.executeHql(hql) > 0;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return flag;
    }

    @Override
    public boolean removeByHql(String hql) {
        boolean flag;
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
        try{
            return (Long)this.getHibernateSession().createQuery(hql).setMaxResults(1).uniqueResult();
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public long countBySql(String sql) {
        try{
            return (Long)this.getHibernateSession().createSQLQuery(sql).setMaxResults(1).uniqueResult();
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<T> showPage(String hql, Page page) {
        Session session = this.getHibernateSession();
        try {
            return this.getHibernateSession().createQuery(hql).setFirstResult(page.getCurrentPage() * page.getPageSize()).setMaxResults(page.getPageSize()).list();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> showPage(String hql, int currentPage, int pageSize) {
        try {
            return  this.getHibernateSession().createQuery(hql).setFirstResult(currentPage * pageSize).setMaxResults(pageSize).list();
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
            return this.getHibernateSession().createSQLQuery(hql).executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
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
    public T getBySql(String sql) {
        T t;
        try {
            t = (T) this.getHibernateSession().createSQLQuery(sql).setMaxResults(1).uniqueResult();
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
            t = (T) this.getHibernateSession().get(clazz, id);
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
            t = (T) this.getHibernateSession().get(clazz, id);
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
            t = (T) this.getHibernateSession().get(clazz, id);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return t;
    }

    @Override
    public List getEntityList(DetachedCriteria dc){
        Session session = this.getHibernateSession();
        Criteria criteria = dc.getExecutableCriteria(session);
        CriteriaImpl impl = (CriteriaImpl) criteria;
        Projection projection = impl.getProjection();
        criteria.setProjection(projection);
        if (projection == null) {
            criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        return criteria.list();
    }

    @Override
    public boolean removeEntity(Object entity) {
        try {
            this.getHibernateSession().delete(entity);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateEntity(Object entity) {
        try {
            this.getHibernateSession().update(entity);
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
            list = (List<T>) this.getHibernateSession().createQuery(hql).list();
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
            id = (Integer) this.getHibernateSession().save(entity);
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
            id = (String) this.getHibernateSession().save(entity);
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
            Serializable serializable = this.getHibernateSession().save(entity);
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
