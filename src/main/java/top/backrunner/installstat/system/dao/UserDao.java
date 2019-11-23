package top.backrunner.installstat.system.dao;

import top.backrunner.installstat.core.dao.BaseDao;
import top.backrunner.installstat.system.entity.UserInfo;

public interface UserDao extends BaseDao<UserInfo> {
    // 根据用户名找用户信息
    public UserInfo findByUserName(String username);
    // 用户名是否存在
    public boolean usernameExisted(String username) throws Exception;
}
