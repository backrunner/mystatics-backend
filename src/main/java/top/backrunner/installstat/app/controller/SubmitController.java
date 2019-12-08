package top.backrunner.installstat.app.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.installstat.app.exception.ApplicationNotFoundException;
import top.backrunner.installstat.app.exception.CannotAccessAppException;
import top.backrunner.installstat.app.exception.UninstallCountStatDisabledException;
import top.backrunner.installstat.app.exception.VersionNotFoundException;
import top.backrunner.installstat.app.service.ApplicationService;
import top.backrunner.installstat.utils.application.VersionUtils;
import top.backrunner.installstat.utils.common.R;
import top.backrunner.installstat.utils.misc.GeoIPStringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/submit")
public class SubmitController {

    @Resource
    ApplicationService applicationService;

    @RequestMapping(value = "/install", method = RequestMethod.POST)
    @ResponseBody
    public R submitInstall(String appKey, String branch, String version, String uuid, HttpServletRequest req){
        if (!ObjectUtils.allNotNull(appKey, appKey, version, uuid)){
            return R.badRequest("提交的参数不完整");
        }
        // 对version进行校验
        if (!VersionUtils.validate(version)){
            return R.badRequest("version格式错误");
        }
        String ipAddress = GeoIPStringUtils.getIPAddress(req);
        try {
            if (applicationService.increaseInstallCount(appKey, branch, version, uuid, ipAddress)){
                return R.ok("记录已添加");
            } else {
                return R.error("记录添加失败");
            }
        } catch (ApplicationNotFoundException e) {
            return R.error("找不到该应用");
        } catch (CannotAccessAppException e) {
            return R.error("应用不可用");
        }
    }

    @RequestMapping(value = "/uninstall", method = RequestMethod.POST)
    @ResponseBody
    public R submitUninstall(String appKey, String branch, String version, String uuid, HttpServletRequest req){
        if (!ObjectUtils.allNotNull(appKey, appKey, version, uuid)){
            return R.badRequest("提交的参数不完整");
        }
        // 对version进行校验
        if (!VersionUtils.validate(version)){
            return R.badRequest("version格式错误");
        }
        String ipAddress = GeoIPStringUtils.getIPAddress(req);
        try {
            if (applicationService.increaseUninstallCount(appKey, branch, version, uuid, ipAddress)){
                return R.ok("记录已添加");
            } else {
                return R.error("记录添加失败");
            }
        } catch (ApplicationNotFoundException e) {
            return R.error("找不到该应用");
        } catch (UninstallCountStatDisabledException e) {
            return R.error("应用未启用记录卸载量");
        } catch (VersionNotFoundException e) {
            return R.error("找不到该版本");
        } catch (CannotAccessAppException e) {
            return R.error("应用不可用");
        }
    }
}
