package top.backrunner.installstat.system.service.impl;

import org.springframework.stereotype.Service;
import top.backrunner.installstat.system.dao.RoleDao;
import top.backrunner.installstat.system.dao.UserDao;
import top.backrunner.installstat.system.entity.RoleInfo;
import top.backrunner.installstat.system.entity.UserInfo;
import top.backrunner.installstat.system.exception.CannotBanAdminException;
import top.backrunner.installstat.system.exception.UserNotFoundException;
import top.backrunner.installstat.system.service.UserService;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private RoleDao roleDao;

    @Override
    public boolean addUser(UserInfo user) {
        // 设置创建日期
        user.setCreateTime(new Date());
        return userDao.add(user);
    }

    @Override
    public UserInfo findUserByUsername(String username) {
        return userDao.findByUserName(username);
    }

    @Override
    public UserInfo findUserById(Long uid) {
        return userDao.findById(uid);
    }

    @Override
    public boolean usernameExists(String username) {
        try {
            return userDao.usernameExists(username);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean userExists(Long uid) {
        try {
            return userDao.userExists(uid);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUser(UserInfo user) {
        user.setLastUpdateTime(new Date());
        return userDao.updateEntity(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        return userDao.removeByHql("DELETE FROM UserInfo WHERE id = "+id);
    }

    @Override
    public RoleInfo findRoleByName(String name) {
        return roleDao.findByName(name);
    }

    @Override
    public RoleInfo findRoleById(Long id) {
        return roleDao.findById(id);
    }

    @Override
    public RoleInfo initRole(String name) {
        RoleInfo role = new RoleInfo(name);
        if (roleDao.add(role)){
            return roleDao.findByName(name);
        }
        return null;
    }

    @Override
    public boolean roleExists(String name) {
        return roleDao.exists(name);
    }

    @Override
    public List<Map<String, Object>> getUserList(int page, int pageSize) {
        List<UserInfo> userList = userDao.showPage("FROM UserInfo", page, pageSize);
        // 获得权限做缓存
        List<RoleInfo> roleList = roleDao.findByHql("FROM RoleInfo");
        // 转换成Map
        Map<Long, String> roleMap = new HashMap<>();
        for (RoleInfo r:roleList){
            roleMap.put(r.getId(), r.getName());
        }
        List<Map<String, Object>> res = new ArrayList<>();
        for (UserInfo user : userList){
            // 对获取到的数据做脱敏处理
            Map<String, Object> publicUserInfo = new HashMap<>();
            publicUserInfo.put("id", user.getId());
            publicUserInfo.put("createTime", user.getCreateTime());
            publicUserInfo.put("username", user.getUsername());
            publicUserInfo.put("email", user.getEmail());
            publicUserInfo.put("phone", user.getPhone());
            publicUserInfo.put("role", roleMap.get(user.getRoleId()));
            res.add(publicUserInfo);
        }
        return res;
    }

    @Override
    public long getUserCount() {
        return userDao.countByHql("select count(*) from UserInfo");
    }

    @Override
    public boolean banUser(Long id) throws UserNotFoundException, CannotBanAdminException {
        UserInfo user = userDao.getById(UserInfo.class, id);
        if (user == null){
            throw new UserNotFoundException();
        }
        if (roleDao.getById(RoleInfo.class, user.getRoleId()).getName().equals("admin")){
            throw new CannotBanAdminException();
        }
        user.setLastUpdateTime(new Date());
        user.setEnabled(false);
        return userDao.updateEntity(user);
    }

    @Override
    public boolean unbanUser(Long id) throws UserNotFoundException {
        UserInfo user = userDao.getById(UserInfo.class, id);
        if (user == null){
            throw new UserNotFoundException();
        }
        user.setLastUpdateTime(new Date());
        user.setEnabled(true);
        return userDao.updateEntity(user);
    }

    @Override
    public boolean setRoleToUser(Long id) throws UserNotFoundException {
        UserInfo user = userDao.getById(UserInfo.class, id);
        if (user == null){
            throw new UserNotFoundException();
        }
        user.setRoleId(roleDao.findByName("user").getId());
        return userDao.updateEntity(user);
    }

    @Override
    public boolean setRoleToAdmin(Long id) throws UserNotFoundException {
        UserInfo user = userDao.getById(UserInfo.class, id);
        if (user == null){
            throw new UserNotFoundException();
        }
        user.setRoleId(roleDao.findByName("admin").getId());
        return userDao.updateEntity(user);
    }


}
