package top.backrunner.installstat.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import top.backrunner.installstat.system.dao.RoleDao;
import top.backrunner.installstat.system.entity.RoleInfo;
import top.backrunner.installstat.system.entity.UserInfo;
import top.backrunner.installstat.system.dao.UserDao;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserDao userDao;

    @Autowired
    @Lazy
    private RoleDao roleDao;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取session中的用户
        UserInfo user = (UserInfo) principalCollection.getPrimaryPrincipal();
        // 定义返回的info
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 查询数据库
        user = userDao.findByUserName(user.getUsername());
        RoleInfo role = roleDao.getById(RoleInfo.class, user.getRoleId());
        info.addRole(role.getName());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 从token获取用户名
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        // 查询数据库
        UserInfo user = userDao.findByUserName(username);
        if (user != null){
            ByteSource salt = ByteSource.Util.bytes(user.getSalt());
            return new SimpleAuthenticationInfo(user, user.getPassword(), salt, this.getName());
        } else {
            return null;
        }
    }
}
