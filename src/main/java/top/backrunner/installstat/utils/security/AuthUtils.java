package top.backrunner.installstat.utils.security;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import top.backrunner.installstat.system.entity.UserInfo;
import top.backrunner.installstat.system.service.UserService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class AuthUtils {
    @Resource
    private UserService _userService;

    private static UserService userService;

    @PostConstruct
    public void init() {
        userService = this._userService;
    }

    public static UserInfo getUser() {
        return (UserInfo) SecurityUtils.getSubject().getPrincipal();
    }
    public static Long getUserId(){
        UserInfo user = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        if (user.getId() == null) {
            user = userService.findUserByUsername(user.getUsername());
            if (user != null) {
                return user.getId();
            } else {
                throw new RuntimeException("不能获取用户的信息");
            }
        } else {
            return user.getId();
        }
    }

    public static String getCurrentUsername(){
        return ((UserInfo) SecurityUtils.getSubject().getPrincipal()).getUsername();
    }
}
