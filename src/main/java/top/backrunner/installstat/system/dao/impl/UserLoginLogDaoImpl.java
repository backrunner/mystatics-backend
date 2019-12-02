package top.backrunner.installstat.system.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import top.backrunner.installstat.core.dao.impl.BaseDaoImpl;
import top.backrunner.installstat.system.dao.UserLoginLogDao;
import top.backrunner.installstat.system.entity.log.UserLoginLogInfo;

import java.util.List;

@Repository
public class UserLoginLogDaoImpl extends BaseDaoImpl<UserLoginLogInfo> implements UserLoginLogDao {
    @Override
    public List<UserLoginLogInfo> getList(Long uid){
        return this.findByHql("FROM UserLoginLogInfo WHERE uid = "+uid);
    }

    @Override
    public List<UserLoginLogInfo> getLimitedList(Long uid, int limited) {
        Session session = this.getHibernateSession();
        Query<UserLoginLogInfo> query = session.createQuery("FROM UserLoginLogInfo WHERE uid = "+uid+"order by createTime desc");
        query.setMaxResults(limited);
        return query.list();
    }

}
