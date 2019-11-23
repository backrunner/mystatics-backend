package top.backrunner.installstat.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.backrunner.installstat.system.dao.RoleDao;
import top.backrunner.installstat.system.dao.UserDao;
import top.backrunner.installstat.system.entity.RoleInfo;
import top.backrunner.installstat.system.entity.UserInfo;
import top.backrunner.installstat.system.service.UserService;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public boolean addUser(UserInfo user) {
        return userDao.add(user);
    }

    @Override
    public UserInfo findUserByUsername(String username) {
        return userDao.findByUserName(username);
    }

    @Override
    public boolean usernameExisted(String username) {
        try {
            return userDao.usernameExisted(username);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
