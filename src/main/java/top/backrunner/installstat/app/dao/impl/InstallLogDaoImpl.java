package top.backrunner.installstat.app.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import top.backrunner.installstat.app.dao.InstallLogDao;
import top.backrunner.installstat.app.entity.InstallLogInfo;
import top.backrunner.installstat.core.dao.impl.BaseDaoImpl;
import top.backrunner.installstat.utils.filter.SQLFilter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class InstallLogDaoImpl extends BaseDaoImpl<InstallLogInfo> implements InstallLogDao {
    @Override
    public boolean logExists(Long appId, Long vid, String uuid) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("select count(*) from InstallLogInfo where appId=:appId and versionId=:vid and uuid=:uuid");
        query.setParameter("appId", appId);
        query.setParameter("vid", vid);
        query.setParameter("uuid", uuid);
        query.setMaxResults(1);
        return ((Long) query.uniqueResult()) > 0;
    }

    @Override
    public boolean logExists(Long appId, String uuid) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("select count(*) from InstallLogInfo where appId=:appId and uuid = :uuid");
        query.setParameter("appId", appId);
        query.setParameter("uuid", uuid);
        query.setMaxResults(1);
        return ((Long) query.uniqueResult()) > 0;
    }

    @Override
    public long getTotalCount(Long uid) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("select count(*) from InstallLogInfo i inner join ApplicationInfo a on i.appId = a.id " +
                "where a.uid = :uid");
        query.setParameter("uid", uid);
        query.setMaxResults(1);
        return (Long) query.uniqueResult();
    }

    @Override
    public long getRecentWeekCount(Long uid) {
        Session session = this.getHibernateSession();
        Query query = session.createQuery("select count(*) from InstallLogInfo i inner join ApplicationInfo a on i.appId = a.id " +
                "where a.uid = :uid and i.createTime > :time");
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
        Query query = session.createQuery("select count(*) from InstallLogInfo where appId = :appId and createTime > :time");
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
        Query query = session.createQuery("select count(*) from InstallLogInfo where appId = :appId and createTime > :time");
        query.setParameter("appId", appId);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -30);
        query.setParameter("time", calendar.getTime());
        query.setMaxResults(1);
        return (Long) query.uniqueResult();
    }

    @Override
    public List<Map> getMonthCount(Long appId) {
        Session session = this.getHibernateSession();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -30);
        Query query = session.createQuery("select count(id) as installCount, year(createTime) as y, month(createTime) as m, day(createTime) as d from InstallLogInfo where appId = :appId and createTime > :monthBefore " +
                "group by year(createTime), month(createTime), day(createTime)").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("appId", appId);
        query.setParameter("monthBefore", calendar.getTime());
        List<Map> res = query.list();
        return res;
    }

    @Override
    public List<Map> getMonthCountByUser(Long userId) {
        Session session = this.getHibernateSession();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -30);
        Query query = session.createQuery("select count(*) as installCount, year(i.createTime) as y, month(i.createTime) as m, day(i.createTime) as d from InstallLogInfo i inner join ApplicationInfo a on i.appId = a.id where a.uid = :uid and i.createTime > :monthBefore " +
                "group by year(i.createTime), month(i.createTime), day(i.createTime)").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("uid", userId);
        query.setParameter("monthBefore", calendar.getTime());
        List<Map> res = query.list();
        return res;
    }
}
