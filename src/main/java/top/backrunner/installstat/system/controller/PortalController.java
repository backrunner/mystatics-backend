package top.backrunner.installstat.system.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.installstat.common.service.RecaptchaService;
import top.backrunner.installstat.system.entity.UserInfo;
import top.backrunner.installstat.system.service.UserService;
import top.backrunner.installstat.utils.common.R;

@Controller
@RequestMapping(value = "/portal", method = RequestMethod.POST)
public class PortalController {
    @Autowired
    private UserService userService;
    @Autowired
    private RecaptchaService recaptchaService;

    @RequestMapping(value = "/login")
    @ResponseBody
    public R login(String username, String password, String recaptchaToken, Boolean rememberMe){
        if (!ObjectUtils.allNotNull(username, password, recaptchaToken)){
            return R.badRequest("提交的参数不完整");
        }
        // 先验证reCaptcha的状态
        if (!recaptchaService.verify(recaptchaToken)){
            return R.error("未通过reCaptcha验证");
        }
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()){
            return R.error("重复登录");
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 设置 rememberMe
        token.setRememberMe(rememberMe == null ? false : rememberMe);
        try {
            subject.login(token);
        } catch (UnknownAccountException uae){
            return R.error("用户名或密码不正确");
        } catch (IncorrectCredentialsException ice){
            return R.error("用户名或密码不正确");
        } catch (ExcessiveAttemptsException eae){
            return R.error("登录失败错误次数过多，请15分钟后重试");
        } catch (AuthenticationException ae){
            return R.error("登录失败");
        }
        if (subject.isAuthenticated()){
            return R.ok("登录成功");
        } else {
            return R.error("登录失败");
        }
    }

    @RequestMapping(value = "/register")
    public R register(String username, String password, String confirmPassword, String recaptchaToken){
        if (!ObjectUtils.allNotNull(username, password, confirmPassword, recaptchaToken)){
            return R.badRequest("提交的参数不完整");
        }
        // 判断用户名是否重复
        try {
            if (userService.usernameExisted(username)){
                return R.error("用户名已存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        UserInfo user = new UserInfo();
        user.setUsername(username);
        String salt = username + "_" + new SecureRandomNumberGenerator().nextBytes().toHex();
        user.setPassword(new Sha256Hash(password, salt, 32).toString());
        user.setSalt(salt);
        if (userService.add(user)){
            return R.ok("注册成功");
        } else {
            return R.error("注册失败");
        }
    }

    @RequestMapping(value = "/logout")
    @ResponseBody
    public R logout(){
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()){
            subject.logout();
            return R.ok("登出成功");
        } else {
            return R.error("用户未登录");
        }
    }
}
