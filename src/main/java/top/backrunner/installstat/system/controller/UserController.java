package top.backrunner.installstat.system.controller;

import org.apache.catalina.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.installstat.system.entity.UserInfo;
import top.backrunner.installstat.system.service.UserService;
import top.backrunner.installstat.utils.common.R;

import java.util.HashMap;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "fetchUserInfo")
    @ResponseBody
    public R fetchUserInfo(){
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        UserInfo info = userService.findUserByUsername(username);
        HashMap<String, Object> res = new HashMap<>();
        res.put("username", info.getUsername());
        res.put("email", info.getEmail());
        res.put("phone", info.getPhone());
        res.put("avatar", info.getAvatar());
        return R.ok(res);
    }
}
