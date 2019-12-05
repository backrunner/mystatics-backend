package top.backrunner.installstat.app.controller;

import org.apache.catalina.User;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.installstat.app.entity.ApplicationInfo;
import top.backrunner.installstat.app.service.ApplicationService;
import top.backrunner.installstat.system.entity.UserInfo;
import top.backrunner.installstat.utils.common.R;
import top.backrunner.installstat.utils.security.AuthUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/app")
public class ApplicationController {

    @Resource
    private ApplicationService applicationService;

    @RequestMapping(value = "/userOverview")
    @ResponseBody
    public R userOverview(){
        Map<String, Object> result = new HashMap<>();
        long uid = AuthUtils.getUserId();
        result.put("totalAppCount", applicationService.getApplicationCount(uid));
        result.put("recentInstallCount", applicationService.getRecentWeekInstallCount(uid));
        result.put("recentUninstallCount", applicationService.getRecentWeekUninstallCount(uid));
        return R.ok(result);
    }

    @RequestMapping(value = "/getAppList")
    @ResponseBody
    public R getAppList(){
        List<ApplicationInfo> appList = applicationService.getApplicationList(AuthUtils.getUserId());
        if (appList.isEmpty()){
            return R.error();
        } else {
            return R.ok(appList);
        }
    }

    @RequestMapping(value = "/addApp")
    @ResponseBody
    public R addApp(String bundleId, String name, String desc, String website, boolean statUninstall){
        if (!ObjectUtils.allNotNull(bundleId, name, desc, website)){
            return R.badRequest("提交的参数不完整");
        }
        // 检查 Bundle ID
        if (!bundleId.matches("^\\w+\\.\\w+\\.\\w+$")){
            return R.badRequest("Bundle ID 格式错误");
        }
        if (applicationService.bundleIdExists(bundleId)){
            return R.error("Bundle ID 已存在");
        }
        ApplicationInfo info = new ApplicationInfo();
        info.setBundleId(bundleId);
        info.setDisplayName(name);
        info.setDescription(desc);
        info.setWebsite(website);
        info.setStatUninstall(statUninstall);
        if (applicationService.addApplication(info)){
            return R.ok("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @RequestMapping(value = "/editApp")
    @ResponseBody
    public R editApp(Long appId, String name, String desc, String website, boolean statUninstall){
        if (!ObjectUtils.allNotNull(appId, name, desc, website)){
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo app = applicationService.fetchAppInfo(appId);
        if (app == null){
            return R.error("无该应用");
        }
        if (!AuthUtils.getUserId().equals(app.getUid())){
            return R.unauth("无权操作");
        }
        app.setDisplayName(name);
        app.setDescription(desc);
        app.setWebsite(website);
        app.setStatUninstall(statUninstall);
        if (applicationService.updateApplication(app)){
            return R.ok("编辑成功");
        } else {
            return R.error("编辑失败");
        }
    }

    @RequestMapping(value = "/renewAppKey")
    @ResponseBody
    public R renewAppKey(Long appId){
        if (!ObjectUtils.allNotNull(appId)){
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo app = applicationService.fetchAppInfo(appId);
        if (app == null){
            return R.error("无该应用");
        }
        if (!AuthUtils.getUserId().equals(app.getUid())){
            return R.unauth("无权操作");
        }
        String newKey = applicationService.renewAppKey(app);
        if (newKey != null){
            return R.ok("重置成功", newKey);
        } else {
            return R.error("重置失败");
        }
    }
}
