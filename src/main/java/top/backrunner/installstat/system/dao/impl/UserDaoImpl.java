package top.backrunner.installstat.system.dao.impl;

import org.springframework.stereotype.Repository;
import top.backrunner.installstat.core.dao.impl.BaseDaoImpl;
import top.backrunner.installstat.system.entity.UserInfo;
import top.backrunner.installstat.system.dao.UserDao;
import top.backrunner.installstat.utils.filter.SQLFilter;

@Repository
public class UserDaoImpl extends BaseDaoImpl<UserInfo> implements UserDao {

    @Override
    public UserInfo findByUserName(String username) {
        return this.getByHql("FROM UserInfo WHERE username = '" + SQLFilter.filter(username) +"'");
    }

    @Override
    public boolean usernameExisted(String username) throws Exception {
        long res = this.countByHql("SELECT COUNT(*) FROM UserInfo WHERE username = '" + SQLFilter.filter(username) + "'");
        if (res < 0){
            throw new Exception("用户名检查失败");
        }
        return res != 0;
    }
}
