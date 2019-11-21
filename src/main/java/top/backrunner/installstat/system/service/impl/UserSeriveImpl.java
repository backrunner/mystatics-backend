package top.backrunner.installstat.system.service.impl;

import top.backrunner.installstat.core.service.impl.BaseServiceImpl;
import top.backrunner.installstat.system.entity.UserInfo;
import top.backrunner.installstat.system.service.UserService;
import top.backrunner.installstat.util.filter.SQLFilter;

public class UserSeriveImpl extends BaseServiceImpl<UserInfo> implements UserService {

    @Override
    public UserInfo findByUserName(String username) {
        username = SQLFilter.filter(username);
        return this.getByHql("'FROM UserInfo WHERE username = '" + username+"'");
    }
}
