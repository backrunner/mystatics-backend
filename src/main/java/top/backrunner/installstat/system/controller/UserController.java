package top.backrunner.installstat.system.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.installstat.system.entity.UserInfo;
import top.backrunner.installstat.system.entity.log.UserLoginLogInfo;
import top.backrunner.installstat.system.service.UserLogService;
import top.backrunner.installstat.system.service.UserService;
import top.backrunner.installstat.utils.common.R;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserLogService userLogService;

    @RequestMapping(value = "fetchUserInfo")
    @ResponseBody
    public R fetchUserInfo(){
        UserInfo info = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        //UserInfo info = userService.findUserByUsername(username);
        HashMap<String, Object> res = new HashMap<>();
        res.put("id", info.getId());
        res.put("username", info.getUsername());
        res.put("email", info.getEmail());
        res.put("phone", info.getPhone());
        res.put("avatar", info.getAvatar());
        return R.ok(res);
    }

    @RequestMapping(value = "/changePassword")
    @ResponseBody
    public R changePassword(String oldPassword, String newPassword, String newConfirmPassword){
        if (!ObjectUtils.allNotNull(oldPassword, newPassword, newConfirmPassword)){
            return R.badRequest("提交的参数不完整");
        }
        if (newPassword.length() < 6){
            return R.error("密码不得少于6个字符");
        }
        // 两次输入的密码进行核对
        if (!newPassword.equals(newConfirmPassword)){
            return R.error("两次输入的密码不一致");
        }
        // 校验用户身份和旧密码的正确性
        UserInfo info = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        if (!info.getUsername().equals(SecurityUtils.getSubject().getPrincipal())){
            return R.unauth("身份验证失败");
        }
        String hashedOldPassword = new SimpleHash("SHA-256", oldPassword, info.getSalt(), 32).toHex();
        if (!oldPassword.equals(hashedOldPassword)){
            return R.error("旧密码不正确");
        }
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        String hashedNewPassword = new SimpleHash("SHA-256", newPassword, salt, 32).toHex();
        info.setSalt(salt);
        info.setPassword(hashedNewPassword);
        if (userService.updateUser(info)){
            return R.ok("修改成功");
        } else {
            return R.error("修改失败");
        }
    }

    // 列出登录记录（前5条）
    @RequestMapping(value = "/listLoginLog")
    @ResponseBody
    public R listLoginLog(){
        UserInfo info = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        return R.ok(userLogService.getLatestLoginLogs(info.getId(), 5));
    }
}
