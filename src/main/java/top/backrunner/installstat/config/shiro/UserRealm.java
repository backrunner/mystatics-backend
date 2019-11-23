package top.backrunner.installstat.config.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import top.backrunner.installstat.system.entity.UserInfo;
import top.backrunner.installstat.system.dao.UserDao;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserDao userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取session中的用户
        UserInfo user = (UserInfo) principalCollection.getPrimaryPrincipal();
        // 定义返回的info
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 查询数据库
        user = userService.findByUserName(user.getUsername());
        info.addRole(user.getRole().getName());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 从token获取用户名
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        // 查询数据库
        UserInfo user = userService.findByUserName(username);
        if (user != null){
            ByteSource salt = ByteSource.Util.bytes(user.getSalt());
            return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), salt, this.getName());
        } else {
            return null;
        }
    }
}
