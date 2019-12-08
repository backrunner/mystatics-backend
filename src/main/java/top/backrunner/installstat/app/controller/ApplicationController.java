package top.backrunner.installstat.app.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.installstat.app.entity.ApplicationInfo;
import top.backrunner.installstat.app.entity.VersionInfo;
import top.backrunner.installstat.app.service.ApplicationService;
import top.backrunner.installstat.system.entity.AnnouncementInfo;
import top.backrunner.installstat.system.service.SystemService;
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

    @Resource
    private SystemService systemService;

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
        if (!app.isEnabled()){
            return R.error("该应用不可用，无法操作");
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

    @RequestMapping(value = "/deleteApp")
    @ResponseBody
    public R editApp(Long appId) {
        if (!ObjectUtils.allNotNull(appId)) {
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo app = applicationService.fetchAppInfo(appId);
        if (app == null){
            return R.error("无该应用");
        }
        if (!AuthUtils.getUserId().equals(app.getUid())){
            return R.unauth("无权操作");
        }
        if (!app.isEnabled()){
            return R.error("该应用不可用，无法操作");
        }
        if (applicationService.deleteApplication(appId)){
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
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
        if (!app.isEnabled()){
            return R.error("该应用不可用，无法操作");
        }
        String newKey = applicationService.renewAppKey(app);
        if (newKey != null){
            return R.ok("重置成功", newKey);
        } else {
            return R.error("重置失败");
        }
    }

    @RequestMapping(value = "/fetchInfo")
    @ResponseBody
    public R fetchInfo(Long appId){
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
        return R.ok(app);
    }

    @RequestMapping(value = "/getVersionList")
    @ResponseBody
    public R getVersionList(Long appId, int currentPage, int pageSize){
        if (!ObjectUtils.allNotNull(appId, currentPage, pageSize)){
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo app = applicationService.fetchAppInfo(appId);
        if (app == null){
            return R.error("无该应用");
        }
        if (!AuthUtils.getUserId().equals(app.getUid())){
            return R.unauth("无权操作");
        }
        List<VersionInfo> list = applicationService.getVersionList(appId, currentPage, pageSize);
        Long total = applicationService.countVersion(appId);
        HashMap<String, Object> response = new HashMap<>();
        response.put("total", total);
        response.put("list", list);
        return R.ok(response);
    }

    @RequestMapping(value = "/deleteVersion")
    @ResponseBody
    public R deleteVersion(Long appId, Long versionId){
        if (!ObjectUtils.allNotNull(appId, versionId)){
            return R.badRequest("提交的参数不完整");
        }
        ApplicationInfo app = applicationService.fetchAppInfo(appId);
        if (app == null){
            return R.error("无该应用");
        }
        if (!AuthUtils.getUserId().equals(app.getUid())){
            return R.unauth("无权操作");
        }
        if (!app.isEnabled()){
            return R.error("该应用不可用，无法操作");
        }
        VersionInfo version = applicationService.fetchVersion(versionId);
        if (version == null) {
            return R.error("无该版本");
        }
        if (!version.getAppId().equals(app.getId())){
            return R.error("App与版本不匹配");
        }
        if (applicationService.deleteVersion(versionId)){
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @RequestMapping(value = "/monthInstallCount")
    @ResponseBody
    public R monthInstallCount(Long appId){
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
        return R.ok(applicationService.getMonthInstallCount(appId));
    }

    @RequestMapping(value = "/getLatestAnnouncement")
    @ResponseBody
    public R getLatestAnnouncement(){
        AnnouncementInfo info = systemService.getLatestAnnouncement();
        if (info == null){
            return R.error("无公告");
        } else {
            return R.ok(info);
        }
    }

    @RequestMapping(value = "/getDashboardOverview")
    @ResponseBody
    public R getDashboardOverview(){
        Map<String, Object> result = new HashMap<>();
        long uid = AuthUtils.getUserId();
        result.put("appCount", applicationService.getApplicationCount(uid));
        result.put("versionCount", applicationService.getVersionCount(uid));
        result.put("installCount", applicationService.getInstallCount(uid));
        result.put("uninstallCount", applicationService.getUninstallCount(uid));
        return R.ok(result);
    }

    @RequestMapping(value = "/getAppStatData")
    @ResponseBody
    public R getAppStatData(){
        List<Map<String, Object>> list = applicationService.getAppStatData(AuthUtils.getUserId());
        if (list.isEmpty()){
            return R.error("无数据");
        } else {
            return R.ok(list);
        }
    }

    @RequestMapping(value = "/getIncreaseData")
    @ResponseBody
    public R getIncreaseData() {
        Map<String, Object> result = new HashMap<>();
        result.put("install", applicationService.getMonthInstallCountByUser(AuthUtils.getUserId()));
        result.put("uninstall", applicationService.getMonthUninstallCountByUser(AuthUtils.getUserId()));
        return R.ok(result);
    }

    @RequestMapping(value = "/getReport")
    @ResponseBody
    public R getReport(int page, int pageSize){
        if (!ObjectUtils.allNotNull(page, pageSize)){
            return R.badRequest("提交的参数不完整");
        }
        List<Map<String, Object>> res = applicationService.getReport(AuthUtils.getUserId(), page, pageSize);
        if (res.isEmpty()){
            return R.error("无结果");
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("total", applicationService.getApplicationCount(AuthUtils.getUserId()));
            data.put("listData", res);
            return R.ok(data);
        }
    }
}
