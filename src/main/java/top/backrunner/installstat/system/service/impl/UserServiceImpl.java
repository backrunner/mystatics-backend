package top.backrunner.installstat.system.service.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.backrunner.installstat.core.service.impl.BaseServiceImpl;
import top.backrunner.installstat.system.entity.UserInfo;
import top.backrunner.installstat.system.service.UserService;
import top.backrunner.installstat.utils.filter.SQLFilter;

@Repository
@Transactional
public class UserServiceImpl extends BaseServiceImpl<UserInfo> implements UserService {

    @Override
    public UserInfo findByUserName(String username) {
        return this.getByHql("FROM UserInfo WHERE username = '" + SQLFilter.filter(username) +"'");
    }

    @Override
    public boolean usernameExisted(String username) throws Exception {
        long res = this.countByHql("FROM UserInfo WHERE username = '" + SQLFilter.filter(username) + "'");
        if (res < 0){
            throw new Exception("用户名检查失败");
        }
        return res != 0;
    }
}
