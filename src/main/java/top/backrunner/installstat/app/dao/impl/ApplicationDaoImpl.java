package top.backrunner.installstat.app.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import top.backrunner.installstat.app.dao.ApplicationDao;
import top.backrunner.installstat.app.entity.ApplicationInfo;
import top.backrunner.installstat.core.dao.impl.BaseDaoImpl;

@Repository
public class ApplicationDaoImpl extends BaseDaoImpl<ApplicationInfo> implements ApplicationDao {
    @Override
    public ApplicationInfo getByAppKey(String appKey) {
        Session session = this.getHibernateSession();
        Query<ApplicationInfo> query = session.createQuery("FROM ApplicationInfo WHERE appKey = :appKey");
        query.setParameter("appKey", appKey);
        return query.uniqueResult();
    }

    @Override
    public boolean bundleIdExists(String bundleId) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("SELECT COUNT(*) FROM ApplicationInfo WHERE bundleId = :bundleId");
        query.setParameter("bundleId", bundleId);
        query.setMaxResults(1);
        return ((Long)query.uniqueResult()) > 0;
    }
}
