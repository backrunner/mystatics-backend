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
        return this.countByHql("select count(*) from InstallLogInfo where appId="+appId+" and versionId="+vid+" and uuid='"+ SQLFilter.filter(uuid) +"'") > 0;
    }

    @Override
    public boolean logExists(Long appId, String uuid) {
        return this.countByHql("select count(*) from InstallLogInfo where appId="+appId+" and uuid = '"+SQLFilter.filter(uuid)+"'") > 0;
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
    public List<Map> getMonthCount(Long appId) {
        Session session = this.getHibernateSession();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        Query query = session.createQuery("select count(id) as installCount, year(createTime) as y, month(createTime) as m, day(createTime) as d from InstallLogInfo where appId = :appId and createTime > :monthBefore " +
                "group by year(createTime), month(createTime), day(createTime)").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("appId", appId);
        query.setParameter("monthBefore", calendar.getTime());
        List<Map> res = query.list();
        return res;
    }
}
