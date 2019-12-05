package top.backrunner.installstat.system.service.impl;

import org.apache.catalina.User;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import top.backrunner.installstat.system.dao.RoleDao;
import top.backrunner.installstat.system.dao.UserAvatarDao;
import top.backrunner.installstat.system.dao.UserDao;
import top.backrunner.installstat.system.entity.RoleInfo;
import top.backrunner.installstat.system.entity.UserAvatarInfo;
import top.backrunner.installstat.system.entity.UserInfo;
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
    @Resource
    private UserAvatarDao userAvatarDao;

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
    public UserAvatarInfo findAvatarById(Long id) {
        if (id == null){
            return null;
        }
        return userAvatarDao.getById(UserAvatarInfo.class, id);
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
    public List<Map<String, Object>> getUserList(int pageSize, int page) {
        List<UserInfo> userList = userDao.showPage("FROM UserInfo", page, pageSize);
        List<Map<String, Object>> res = new ArrayList<>();
        for (UserInfo user : userList){
            // 对获取到的数据做脱敏处理
            Map<String, Object> publicUserInfo = new HashMap<>();
            publicUserInfo.put("id", user.getId());
            publicUserInfo.put("createTime", user.getCreateTime());
            publicUserInfo.put("username", user.getUsername());
            publicUserInfo.put("email", user.getEmail());
            publicUserInfo.put("phone", user.getPhone());
        }
        return res;
    }

    @Override
    public boolean banUser(Long id) throws EntityNotFoundException {
        UserInfo user = userDao.getById(UserInfo.class, id);
        if (user == null){
            throw new EntityNotFoundException();
        }
        user.setLastUpdateTime(new Date());
        user.setEnabled(false);
        return userDao.updateEntity(user);
    }
}
