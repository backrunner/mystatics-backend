package top.backrunner.installstat.app.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import top.backrunner.installstat.app.dao.UninstallLogDao;
import top.backrunner.installstat.app.entity.UninstallLogInfo;
import top.backrunner.installstat.core.dao.impl.BaseDaoImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        Query query = session.createQuery("update UninstallLogInfo set reinstallFlag = true where appId = :appId and versionId = :versionId and uuid = :uuid and reinstallFlag = false");
        query.setParameter("appId", appId);
        query.setParameter("versionId", vid);
        query.setParameter("uuid", uuid);
        return query.executeUpdate();
    }

    @Override
    public long getTotalCount(Long uid) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("select count(*) from UninstallLogInfo u inner join ApplicationInfo a on u.appId = a.id " +
                "where a.uid = :uid");
        query.setParameter("uid", uid);
        query.setMaxResults(1);
        return (Long) query.uniqueResult();
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

    @Override
    public long getAppRecentWeekCount(Long appId) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("select count(*) from UninstallLogInfo where appId = :appId and createTime > :time");
        query.setParameter("appId", appId);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -7);
        query.setParameter("time", calendar.getTime());
        query.setMaxResults(1);
        return (Long) query.uniqueResult();
    }

    @Override
    public long getAppMonthCount(Long appId) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("select count(*) from UninstallLogInfo where appId = :appId and createTime > :time");
        query.setParameter("appId", appId);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -30);
        query.setParameter("time", calendar.getTime());
        query.setMaxResults(1);
        return (Long) query.uniqueResult();
    }

    @Override
    public List<Map> getMonthCountByUser(Long userId) {
        Session session = this.getHibernateSession();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -30);
        Query query = session.createQuery("select count(*) as uninstallCount, year(u.createTime) as y, month(u.createTime) as m, day(u.createTime) as d from UninstallLogInfo u inner join ApplicationInfo a on u.appId = a.id where a.uid = :uid and u.createTime > :monthBefore " +
                "group by year(u.createTime), month(u.createTime), day(u.createTime)").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("uid", userId);
        query.setParameter("monthBefore", calendar.getTime());
        List<Map> res = query.list();
        return res;
    }
}
