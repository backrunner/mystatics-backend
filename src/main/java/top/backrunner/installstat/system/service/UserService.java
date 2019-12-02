package top.backrunner.installstat.system.service;

import top.backrunner.installstat.system.entity.RoleInfo;
import top.backrunner.installstat.system.entity.UserInfo;

public interface UserService {
    // 用户
    public boolean addUser(UserInfo user);
    public UserInfo findUserByUsername(String username);
    public UserInfo findUserById(Long uid);
    public boolean usernameExists(String username);
    public boolean userExists(Long uid);
    public boolean updateUser(UserInfo user);
    // 权限
    public RoleInfo findRoleByName(String name);
    public RoleInfo initRole(String name);
}
