package top.backrunner.installstat.system.service.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.backrunner.installstat.system.dao.RoleDao;
import top.backrunner.installstat.system.dao.UserDao;
import top.backrunner.installstat.system.entity.RoleInfo;
import top.backrunner.installstat.system.entity.UserInfo;
import top.backrunner.installstat.system.service.UserService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;

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
        return userDao.updateEntity(user);
    }

    @Override
    public RoleInfo findRoleByName(String name) {
        return roleDao.findByName(name);
    }

    @Override
    public RoleInfo initRole(String name) {
        RoleInfo role = new RoleInfo(name);
        if (roleDao.add(role)){
            return roleDao.findByName(name);
        }
        return null;
    }
}
