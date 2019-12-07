package top.backrunner.installstat.system.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import top.backrunner.installstat.core.dao.impl.BaseDaoImpl;
import top.backrunner.installstat.system.dao.AnnouncementDao;
import top.backrunner.installstat.system.entity.AnnouncementInfo;

@Repository
public class AnnouncementDaoImpl extends BaseDaoImpl<AnnouncementInfo> implements AnnouncementDao {
    @Override
    public AnnouncementInfo getLatestAnnouncement() {
        Session session = this.getHibernateSession();
        Query<AnnouncementInfo> query = session.createQuery("FROM AnnouncementInfo ORDER BY lastUpdateTime desc");
        query.setMaxResults(1);
        return query.uniqueResult();
    }
}
