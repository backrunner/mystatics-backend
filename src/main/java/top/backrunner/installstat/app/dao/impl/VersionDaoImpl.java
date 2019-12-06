package top.backrunner.installstat.app.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import top.backrunner.installstat.app.dao.VersionDao;
import top.backrunner.installstat.app.entity.ApplicationInfo;
import top.backrunner.installstat.app.entity.VersionInfo;
import top.backrunner.installstat.core.dao.impl.BaseDaoImpl;
import top.backrunner.installstat.utils.filter.SQLFilter;

@Repository
public class VersionDaoImpl extends BaseDaoImpl<VersionInfo> implements VersionDao {
    @Override
    public VersionInfo getVersion(Long appId, String branch, String version) {
        Session session = this.getHibernateSession();
        Query<VersionInfo> query = session.createQuery("FROM VersionInfo WHERE appId=:appId and branch = :branch and version = :version");
        query.setParameter("appId", appId);
        query.setParameter("branch", branch);
        query.setParameter("version", version);
        return query.uniqueResult();
    }

    @Override
    public long getCount(Long appId) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("select count(*) from VersionInfo where appId = :appId");
        query.setParameter("appId", appId);
        return (Long)query.uniqueResult();
    }
}
