package top.backrunner.installstat.system.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.installstat.app.entity.ApplicationInfo;
import top.backrunner.installstat.app.service.ApplicationService;
import top.backrunner.installstat.system.entity.AnnouncementInfo;
import top.backrunner.installstat.system.entity.UserInfo;
import top.backrunner.installstat.system.exception.CannotBanAdminException;
import top.backrunner.installstat.system.exception.UserNotFoundException;
import top.backrunner.installstat.system.service.SystemService;
import top.backrunner.installstat.system.service.UserService;
import top.backrunner.installstat.utils.common.R;

import javax.annotation.Resource;
import javax.validation.ReportAsSingleViolation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private UserService userService;

    @Resource
    private SystemService systemService;

    // 公告

    @RequestMapping(value = "/getAnnouncementList")
    @ResponseBody
    public R getAnnouncementList(){
        List<AnnouncementInfo> list = systemService.getAnnouncementList();
        if (list.isEmpty()){
            return R.error("列表为空");
        } else {
            return R.ok(list);
        }
    }

    @RequestMapping(value = "/addAnnouncement")
    @ResponseBody
    public R addAnnouncement(String desc, String content){
        if (!ObjectUtils.allNotNull(desc, content)){
            return R.badRequest("提交的参数不完整");
        }
        AnnouncementInfo info = new AnnouncementInfo();
        info.setDescription(desc);
        info.setContent(content);
        if (systemService.addAnnouncement(info)){
            return R.ok("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @RequestMapping(value = "/updateAnnouncement")
    @ResponseBody
    public R updateAnnouncement(Long id, String desc, String content){
        if (!ObjectUtils.allNotNull(desc, content)){
            return R.badRequest("提交的参数不完整");
        }
        AnnouncementInfo info = systemService.getAnnouncement(id);
        if (info == null){
            return R.error("无此公告");
        }
        info.setDescription(desc);
        info.setContent(content);
        if (systemService.updateAnnouncement(info)){
            return R.ok("编辑成功");
        } else {
            return R.error("编辑失败");
        }
    }

    @RequestMapping(value = "/deleteAnnouncement")
    @ResponseBody
    public R deleteAnnouncement(Long id){
        if (!ObjectUtils.allNotNull(id)){
            return R.badRequest("提交的参数不完整");
        }
        if (systemService.deleteAnnouncement(id)){
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    // 应用

    @RequestMapping(value = "/getApplicationList")
    @ResponseBody
    public R getApplicationList(int page, int pageSize){
        if (!ObjectUtils.allNotNull(page, pageSize)){
            return R.badRequest("提交的参数不完整");
        }
        List<ApplicationInfo> apps = applicationService.getAllApplicationList(page, pageSize);
        if (apps.isEmpty()) {
            return R.error("无查询结果");
        } else {
            Map<String, Object> res = new HashMap<>();
            res.put("total", applicationService.getApplicationCount());
            res.put("list", apps);
            return R.ok(res);
        }
    }

    @RequestMapping(value = "/banApp")
    @ResponseBody
    public R banApp(Long appId){
        if (!ObjectUtils.allNotNull(appId)) {
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo app = applicationService.fetchAppInfo(appId);
        if (app == null){
            return R.error("无该应用");
        }
        if (!app.isEnabled()){
            return R.error("该应用已经被禁用");
        }
        app.setEnabled(false);
        if (applicationService.updateApplication(app)){
            return R.ok("封禁成功");
        } else {
            return R.error("封禁失败");
        }
    }

    @RequestMapping(value = "/unbanApp")
    @ResponseBody
    public R unbanApp(Long appId){
        if (!ObjectUtils.allNotNull(appId)) {
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo app = applicationService.fetchAppInfo(appId);
        if (app == null){
            return R.error("无该应用");
        }
        if (app.isEnabled()){
            return R.error("该应用未被禁用");
        }
        app.setEnabled(true);
        if (applicationService.updateApplication(app)){
            return R.ok("解封成功");
        } else {
            return R.error("解封失败");
        }
    }

    @RequestMapping(value = "/deleteApp")
    @ResponseBody
    public R deleteApp(Long appId){
        if (!ObjectUtils.allNotNull(appId)) {
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo app = applicationService.fetchAppInfo(appId);
        if (app == null){
            return R.error("无该应用");
        }
        if (applicationService.deleteApplication(appId)){
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    // 用户

    @RequestMapping(value = "/getUserList")
    @ResponseBody
    public R getUserList(int page, int pageSize){
        if (!ObjectUtils.allNotNull(page, pageSize)) {
            return R.badRequest("提交的参数不完整");
        }
        List<Map<String, Object>> users = userService.getUserList(page, pageSize);
        if (users.isEmpty()){
            return R.error("无查询结果");
        } else {
            Map<String, Object> res = new HashMap<>();
            res.put("total", userService.getUserCount());
            res.put("list", users);
            return R.ok(res);
        }
    }

    @RequestMapping(value = "/banUser")
    @ResponseBody
    public R banUser(Long uid){
        if (!ObjectUtils.allNotNull(uid)) {
            return R.badRequest("提交的参数不完整");
        }
        try {
            if (userService.banUser(uid)){
                return R.ok("封禁成功");
            } else {
                return R.error("封禁失败");
            }
        } catch (UserNotFoundException e) {
            return R.error("找不到该用户");
        } catch (CannotBanAdminException e) {
            return R.error("无法封禁管理员");
        }
    }

    @RequestMapping(value = "/unbanUser")
    @ResponseBody
    public R unbanUser(Long uid){
        if (!ObjectUtils.allNotNull(uid)) {
            return R.badRequest("提交的参数不完整");
        }
        try {
            if (userService.unbanUser(uid)){
                return R.ok("解封成功");
            } else {
                return R.error("解封失败");
            }
        } catch (UserNotFoundException e) {
            return R.error("找不到该用户");
        }
    }

    @RequestMapping(value = "/setUserRole")
    @ResponseBody
    public R setUserRole (String role, Long uid){
        if (!ObjectUtils.allNotNull(role, uid)) {
            return R.badRequest("提交的参数不完整");
        }
        if (!role.equals("admin") && !role.equals("user")){
            return R.badRequest("非法参数");
        }
        try {
            if ("admin".equals(role)) {
                if (userService.setRoleToAdmin(uid)) {
                    return R.ok("设置成功");
                } else {
                    return R.error("设置失败");
                }
            } else if ("user".equals(role)){
                if (userService.setRoleToUser(uid)) {
                    return R.ok("设置成功");
                } else {
                    return R.error("设置失败");
                }
            }
        } catch (UserNotFoundException e) {
            return R.error("找不到该用户");
        }
        return R.error();
    }

    @RequestMapping(value = "deleteUser")
    @ResponseBody
    public R deleteUser(Long uid){
        if (!ObjectUtils.allNotNull(uid)) {
            return R.badRequest("提交的参数不完整");
        }
        UserInfo info = userService.findUserById(uid);
        if (info == null){
            return R.error("无此用户");
        }
        if (userService.deleteUser(uid)){
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }
}
