package top.backrunner.installstat.system.service;

import top.backrunner.installstat.system.entity.RoleInfo;
import top.backrunner.installstat.system.entity.UserAvatarInfo;
import top.backrunner.installstat.system.entity.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserService {
    // 用户
    public boolean addUser(UserInfo user);
    public UserInfo findUserByUsername(String username);
    public UserInfo findUserById(Long uid);
    public boolean usernameExists(String username);
    public boolean userExists(Long uid);
    public boolean updateUser(UserInfo user);
    public UserAvatarInfo findAvatarById(Long id);
    public boolean deleteUser(Long id);
    // 权限
    public RoleInfo findRoleByName(String name);
    public RoleInfo findRoleById(Long id);
    public RoleInfo initRole(String name);
    // 管理员管理
    public List<Map<String, Object>> getUserList(int pageSize, int page);
    public boolean banUser(Long id);
}
