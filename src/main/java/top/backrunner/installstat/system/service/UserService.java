package top.backrunner.installstat.system.service;

import top.backrunner.installstat.core.service.BaseService;
import top.backrunner.installstat.system.entity.UserInfo;

public interface UserService extends BaseService<UserInfo> {
    public UserInfo findByUserName(String username);

}
