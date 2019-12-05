package top.backrunner.installstat.app.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import top.backrunner.installstat.app.dao.UninstallLogDao;
import top.backrunner.installstat.app.entity.UninstallLogInfo;
import top.backrunner.installstat.core.dao.impl.BaseDaoImpl;

import java.util.Calendar;
import java.util.Date;

@Repository
public class UninstallLogDaoImpl extends BaseDaoImpl<UninstallLogInfo> implements UninstallLogDao {
    @Override
    public boolean checkReinstall(Long appId, String branch, String uuid) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("select count(*) from UninstallLogInfo left join VersionInfo on UninstallLogInfo.versionId = VersionInfo.id " +
                "where UninstallLogInfo .appId = :appId and VersionInfo.branch = :branch and UninstallLogInfo.uuid = :uuid and UninstallLogInfo.reinstallFlag = false");
        query.setParameter("appId", appId);
        query.setParameter("branch", branch);
        query.setParameter("uuid", uuid);
        query.setMaxResults(1);
        return ((Long)query.uniqueResult() > 0);
    }

    @Override
    public boolean checkReinstall(Long appId, Long vid, String uuid) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("select count(*) from UninstallLogInfo where appId = :appId and versionId = :versionId and uuid = :uuid and reinstallFlag = false");
        query.setParameter("appId", appId);
        query.setParameter("versionId", vid);
        query.setParameter("uuid", uuid);
        query.setMaxResults(1);
        return ((Long)query.uniqueResult()) > 0;
    }

    @Override
    public long updateReinstall(Long appId, String branch, String uuid) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("update UninstallLogInfo set reinstallFlag = true where appId = :appId and uuid = :uuid and reinstallFlag = false and versionId in (" +
                "select id from VersionInfo where branch = :branch)");
        query.setParameter("appId", appId);
        query.setParameter("branch", branch);
        query.setParameter("uuid", uuid);
        return query.executeUpdate();
    }

    @Override
    public long updateReinstall(Long appId, Long vid, String uuid) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("update UninstallLogInfo set UninstallLogInfo.reinstallFlag = true where appId = :appId and versionId = :versionId and uuid = :uuid and reinstallFlag = false");
        query.setParameter("appId", appId);
        query.setParameter("versionId", vid);
        query.setParameter("uuid", uuid);
        return query.executeUpdate();
    }

    @Override
    public long getRecentWeekCount(Long uid) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("select count(*) from UninstallLogInfo u inner join ApplicationInfo a on u.appId = a.id " +
                "where a.uid = :uid and u.createTime > :time");
        query.setParameter("uid", uid);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -7);
        query.setParameter("time", calendar.getTime());
        query.setMaxResults(1);
        return (Long) query.uniqueResult();
    }
}
