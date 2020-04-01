package top.backrunner.installstat.app.service.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Service;
import top.backrunner.installstat.app.dao.ApplicationDao;
import top.backrunner.installstat.app.dao.InstallLogDao;
import top.backrunner.installstat.app.dao.UninstallLogDao;
import top.backrunner.installstat.app.dao.VersionDao;
import top.backrunner.installstat.app.entity.ApplicationInfo;
import top.backrunner.installstat.app.entity.InstallLogInfo;
import top.backrunner.installstat.app.entity.UninstallLogInfo;
import top.backrunner.installstat.app.entity.VersionInfo;
import top.backrunner.installstat.app.exception.ApplicationNotFoundException;
import top.backrunner.installstat.app.exception.CannotAccessAppException;
import top.backrunner.installstat.app.exception.UninstallCountStatDisabledException;
import top.backrunner.installstat.app.exception.VersionNotFoundException;
import top.backrunner.installstat.app.service.ApplicationService;
import top.backrunner.installstat.utils.application.VersionUtils;
import top.backrunner.installstat.utils.common.UUIDUtils;
import top.backrunner.installstat.utils.filter.SQLFilter;
import top.backrunner.installstat.utils.misc.GeoIPUtils;
import top.backrunner.installstat.utils.misc.IPLocation;
import top.backrunner.installstat.utils.security.AuthUtils;
import top.backrunner.installstat.utils.security.HashUtils;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    @Resource
    private ApplicationDao applicationDao;

    @Resource
    private VersionDao versionDao;

    @Resource
    private InstallLogDao installLogDao;

    @Resource
    private UninstallLogDao uninstallLogDao;

    @Override
    public List<ApplicationInfo> getApplicationList(long uid) {
        return applicationDao.findByHql("FROM ApplicationInfo WHERE uid = "+uid);
    }

    @Override
    public List<ApplicationInfo> getApplicationList(long uid, int page, int pageSize) {
        return applicationDao.showPage("FROM ApplicationInfo WHERE uid = "+uid, page, pageSize);
    }

    @Override
    public long getApplicationCount() {
        return applicationDao.countByHql("select count(*) from ApplicationInfo");
    }

    @Override
    public long getApplicationCount(Long uid) {
        return applicationDao.countByHql("select count(*) from ApplicationInfo where uid = "+uid);
    }

    @Override
    public ApplicationInfo fetchAppInfo(Long appId) {
        return applicationDao.getById(ApplicationInfo.class, appId);
    }

    @Override
    public boolean addApplication(ApplicationInfo application) {
        // 设置用户id
        application.setUid(AuthUtils.getUserId());
        // 初始化
        application.setCreateTime(new Date());
        application.setAppKey(HashUtils.sha1(UUIDUtils.generate()));
        application.setEnabled(true);
        return applicationDao.add(application);
    }

    @Override
    public boolean updateApplication(ApplicationInfo application) {
        application.setLastUpdateTime(new Date());
        return applicationDao.updateEntity(application);
    }

    @Override
    public boolean deleteApplication(Long appId) {
        // 删除相关的日志
        this.deleteInstallLogByApp(appId);
        this.deleteUninstallLogByApp(appId);
        // 删除和应用相关的版本
        this.deleteVersionByApplication(appId);
        // 删除应用
        return applicationDao.removeByHql("DELETE FROM ApplicationInfo WHERE id = "+appId);
    }

    @Override
    public boolean deleteApplicationByUser(Long uid) {
        this.deleteVersionByUser(uid);
        return applicationDao.removeEntitiesByHql("DELETE FROM ApplicationInfo WHERE uid = "+uid) >= 0;
    }

    @Override
    public String fetchAppKey(Long appId) throws EntityNotFoundException {
        ApplicationInfo info = applicationDao.getById(ApplicationInfo.class, appId);
        if (info == null){
            throw new EntityNotFoundException();
        }
        return info.getAppKey();
    }

    @Override
    public String renewAppKey(ApplicationInfo app){
        String newKey = HashUtils.sha1(UUIDUtils.generate());
        app.setAppKey(newKey);
        app.setLastUpdateTime(new Date());
        if (applicationDao.updateEntity(app)){
            return newKey;
        } else {
            return null;
        }
    }

    @Override
    public ApplicationInfo getAppInfoByKey(String appKey) {
        return applicationDao.getByAppKey(appKey);
    }

    @Override
    public boolean bundleIdExists(String bundleId) {
        return applicationDao.bundleIdExists(bundleId);
    }

    @Override
    public List<Map<String, Object>> getAppStatData(Long uid) {
        List<Map<String, Object>> res = new ArrayList<>();
        List<ApplicationInfo> appList = applicationDao.getRecentApps(uid);
        for (ApplicationInfo app : appList){
            Map<String, Object> map = new HashMap<>();
            map.put("name", app.getDisplayName());
            map.put("installCount", app.getInstallCount());
            map.put("uninstallCount", app.getUninstallCount());
            res.add(map);
        }
        return res;
    }

    @Override
    public VersionInfo fetchVersion(Long versionId) {
        return versionDao.getById(VersionInfo.class, versionId);
    }

    @Override
    public List<VersionInfo> getVersionList(Long appId) {
        return versionDao.findByHql("FROM VersionInfo WHERE appId = "+appId);
    }

    @Override
    public List<VersionInfo> getVersionList(Long appId, String branch) {
        return versionDao.findByHql("FROM VersionInfo WHERE appId = "+appId + " and branch = '"+SQLFilter.filter(branch)+"'");
    }

    @Override
    public List<VersionInfo> getVersionList(Long appId, int page, int pageSize) {
        return versionDao.showPage("FROM VersionInfo WHERE appId = "+appId+" order by createTime desc", page, pageSize);
    }

    @Override
    public long getVersionCount(Long uid) {
        return versionDao.getCountByUser(uid);
    }

    @Override
    public boolean addVersion(VersionInfo info) {
        info.setCreateTime(new Date());
        info.setEnabled(true);
        return versionDao.add(info);
    }

    @Override
    public boolean deleteVersion(Long versionId) {
        // 删除日志
        this.deleteInstallLogByVersion(versionId);
        this.deleteUninstallLogByVersion(versionId);
        // 在删除之前获取版本的信息
        VersionInfo version = versionDao.getById(VersionInfo.class, versionId);
        // 删除版本
        if (versionDao.removeByHql("DELETE FROM VersionInfo WHERE id = "+versionId)) {
            // 版本删除成功后修改安装量
            ApplicationInfo info = applicationDao.getById(ApplicationInfo.class, version.getAppId());
            info.setInstallCount(info.getInstallCount() - version.getInstallCount());
            info.setUninstallCount(info.getUninstallCount() - version.getUninstallCount());
            // 如果应用的当前版本为被删除的版本，则自动指定一个应用版本
            Map<String, String> currentVersionMap = info.getCurrentVersion();
            String cv = currentVersionMap.get(version.getBranch());
            if (cv != null && version.getVersion().equals(cv)){
                List<VersionInfo> versions = this.getVersionList(info.getId(), version.getBranch());
                if (versions.size() > 0){
                    currentVersionMap.put(version.getBranch(), VersionUtils.max(versions, version.getBranch()));
                } else {
                    currentVersionMap.remove(version.getBranch());
                }
                info.setCurrentVersion(currentVersionMap);
            }
            applicationDao.updateEntity(info);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteVersionByUser(Long uid) {
        List<ApplicationInfo> appList = this.getApplicationList(uid);
        for (ApplicationInfo app : appList){
            // 级联删除版本相关的所有记录
            this.deleteInstallLogByApp(app.getId());
            this.deleteUninstallLogByVersion(app.getId());
            this.deleteVersionByApplication(app.getId());
        }
    }

    @Override
    public boolean truncateApplication(Long appId) {
        if (versionDao.removeByHql("DELETE FROM VersionInfo WHERE appId = "+appId)){
            ApplicationInfo info = applicationDao.getById(ApplicationInfo.class, appId);
            info.setInstallCount(0);
            info.setUninstallCount(0);
            return applicationDao.updateEntity(info);
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteVersionByApplication(Long appId) {
        return versionDao.removeByHql("DELETE FROM VersionInfo WHERE appId = "+appId);
    }

    @Override
    public long countVersion(Long appId) {
        return versionDao.getCount(appId);
    }

    @Override
    public boolean increaseInstallCount(String appKey, String branch, String version, String uuid, String ip) throws ApplicationNotFoundException, CannotAccessAppException {
        ApplicationInfo appInfo = applicationDao.getByAppKey(appKey);
        if (appInfo == null){
            throw new ApplicationNotFoundException();
        }
        if (!appInfo.isEnabled()){
            throw new CannotAccessAppException();
        }
        long appId = appInfo.getId();
        VersionInfo v = versionDao.getVersion(appId, branch, version);
        if (v == null){
            v = new VersionInfo();
            // 版本不存在
            v.setAppId(appId);
            v.setVersion(version);
            v.setBranch(branch);
            v.setInstallCount(1);
            v.setUninstallCount(0);
            if (this.addVersion(v)){
                // 修改应用总计数
                appInfo.setInstallCount(appInfo.getInstallCount()+1);
                // 检查应用的当前版本
                Map<String, String> currentVersion = appInfo.getCurrentVersion();
                if (currentVersion.containsKey(branch)){
                    // 有对应的Key，比较版本
                    String current = currentVersion.get(branch);
                    if (VersionUtils.compare(version, current) > 0){
                        // 提交的版本更大，替换
                        currentVersion.put(branch, version);
                        appInfo.setCurrentVersion(currentVersion);
                    }
                } else {
                    // 没有对应的Key，则提交当前的Key
                    currentVersion.put(branch, version);
                    appInfo.setCurrentVersion(currentVersion);
                }
                // 添加安装记录
                this.createInstallLog(appId, v.getId(), uuid, ip);
                return applicationDao.updateEntity(appInfo);
            } else {
                return false;
            }
        } else {
            // 版本存在
            boolean flag_checkReinstall = false;
            // 检查该uuid在提交的platform下是否有对应的已提交记录
            if (!installLogDao.logExists(appId, v.getId(), uuid)){
                // 没有提交过，则计数器+1
                v.setInstallCount(v.getInstallCount() + 1);
                // 检查该uuid是否提交过曾经提交过该应用的任何一个版本
                if (!installLogDao.logExists(appId, uuid)) {
                    // 没有提交过，应用总安装量 + 1
                    appInfo.setInstallCount(appInfo.getInstallCount() + 1);
                } else {
                    if (appInfo.isStatUninstall()) {
                        // 检查之前的应用记录，是否为升级安装，如是，则扣除应用卸载量1点
                        if (uninstallLogDao.checkReinstall(appId, branch, uuid)){
                            appInfo.setUninstallCount(appInfo.getUninstallCount() - 1);
                            uninstallLogDao.updateReinstall(appId, branch, uuid);
                        }
                    }
                }
                // 创建安装记录
                this.createInstallLog(appId, v.getId(), uuid, ip);
            } else {
                if (appInfo.isStatUninstall()) {
                    // 有提交记录，需要检查重装，如果是卸载重装则扣除应用及版本卸载量1点
                    if (uninstallLogDao.checkReinstall(appId, v.getId(), uuid)){
                        v.setUninstallCount(v.getInstallCount() - 1);
                        appInfo.setUninstallCount(appInfo.getUninstallCount() - 1);
                        uninstallLogDao.updateReinstall(appId, v.getId(), uuid);
                    }
                }
            }
            return versionDao.updateEntity(v) && applicationDao.updateEntity(appInfo);
        }
    }

    @Override
    public boolean increaseUninstallCount(String appKey, String branch, String version, String uuid, String ip) throws UninstallCountStatDisabledException, ApplicationNotFoundException, VersionNotFoundException, CannotAccessAppException {
        ApplicationInfo appInfo = applicationDao.getByAppKey(appKey);
        if (appInfo == null){
            throw new ApplicationNotFoundException();
        }
        if (!appInfo.isEnabled()){
            throw new CannotAccessAppException();
        }
        if (!appInfo.isStatUninstall()){
            throw new UninstallCountStatDisabledException();
        }
        VersionInfo v = versionDao.getVersion(appInfo.getId(), branch, version);
        if (v == null){
            throw new VersionNotFoundException();
        }
        // 存在对应应用和对应版本
        appInfo.setUninstallCount(appInfo.getUninstallCount() + 1);
        v.setUninstallCount(v.getUninstallCount() + 1);
        // 添加卸载记录
        this.createUninstallLog(appInfo.getId(), v.getId(), uuid, ip);
        return applicationDao.updateEntity(appInfo) && versionDao.updateEntity(v);
    }

    @Override
    public long getRecentWeekInstallCount(Long uid) {
        return installLogDao.getRecentWeekCount(uid);
    }

    @Override
    public long getRecentWeekUninstallCount(Long uid) {
        return uninstallLogDao.getRecentWeekCount(uid);
    }

    @Override
    public long getInstallCount(Long uid) {
        return installLogDao.getTotalCount(uid);
    }

    @Override
    public long getUninstallCount(Long uid) {
        return uninstallLogDao.getTotalCount(uid);
    }

    @Override
    public Map<String, Object> getMonthInstallCount(Long appId) {
        List<Map> monthRes = installLogDao.getMonthCount(appId);
        Map<String, Object> res = new HashMap<>();
        for (Map map : monthRes){
            res.put(map.get("y").toString()+"-"+map.get("m").toString()+"-"+map.get("d").toString(), map.get("installCount"));
        }
        return res;
    }

    @Override
    public Map<String, Object> getMonthInstallCountByUser(Long uid) {
        List<Map> monthRes = installLogDao.getMonthCountByUser(uid);
        Map<String, Object> res = new HashMap<>();
        for (Map map : monthRes){
            res.put(map.get("y").toString()+"-"+map.get("m").toString()+"-"+map.get("d").toString(), map.get("installCount"));
        }
        return res;
    }

    @Override
    public Map<String, Object> getMonthUninstallCountByUser(Long uid) {
        List<Map> monthRes = uninstallLogDao.getMonthCountByUser(uid);
        Map<String, Object> res = new HashMap<>();
        for (Map map : monthRes){
            res.put(map.get("y").toString()+"-"+map.get("m").toString()+"-"+map.get("d").toString(), map.get("uninstallCount"));
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> getReport(Long uid, int page, int pageSize) {
        List<ApplicationInfo> apps = this.getApplicationList(uid);
        if (apps.isEmpty()){
            return null;
        }
        List<Map<String, Object>> res = new ArrayList<>();
        for (ApplicationInfo app : apps){
            Map<String, Object> map = new HashMap<>();
            map.put("id", app.getId());
            map.put("bundleId", app.getBundleId());
            map.put("name", app.getDisplayName());
            map.put("installCount", app.getInstallCount());
            map.put("uninstallCount", app.getUninstallCount());
            map.put("weekInstallCount", installLogDao.getAppRecentWeekCount(app.getId()));
            map.put("weekUninstallCount", uninstallLogDao.getAppRecentWeekCount(app.getId()));
            map.put("monthInstallCount", installLogDao.getAppMonthCount(app.getId()));
            map.put("monthUninstallCount", uninstallLogDao.getAppMonthCount(app.getId()));
            res.add(map);
        }
        return res;
    }

    @Override
    public List<ApplicationInfo> getAllApplicationList(int page, int pageSize) {
        return applicationDao.showPage("FROM ApplicationInfo", page, pageSize);
    }

    @Override
    public List<VersionInfo> getAllVersionList(int page, int pageSize) {
        return versionDao.showPage("FROM VersionInfo", page, pageSize);
    }

    @Override
    public boolean banApp(Long appId) throws EntityNotFoundException {
        ApplicationInfo info = applicationDao.getById(ApplicationInfo.class, appId);
        if (info == null) {
            throw new EntityNotFoundException();
        }
        info.setEnabled(false);
        return applicationDao.updateEntity(info);
    }

    @Override
    public boolean createInstallLog(Long appId, Long versionId, String uuid, String ip) {
        InstallLogInfo info = new InstallLogInfo();
        info.setCreateTime(new Date());
        info.setAppId(appId);
        info.setVersionId(versionId);
        IPLocation location = GeoIPUtils.o.getIPLocation(ip);
        info.setGeo(location.toString());
        info.setUuid(uuid);
        return installLogDao.add(info);
    }

    @Override
    public boolean createUninstallLog(Long appId, Long versionId, String uuid, String ip) {
        UninstallLogInfo info = new UninstallLogInfo();
        info.setCreateTime(new Date());
        info.setAppId(appId);
        info.setVersionId(versionId);
        IPLocation location = GeoIPUtils.o.getIPLocation(ip);
        info.setGeo(location.toString());
        info.setUuid(uuid);
        return uninstallLogDao.add(info);
    }

    @Override
    public boolean deleteInstallLogByVersion(Long vid) {
        return installLogDao.removeByHql("DELETE FROM InstallLogInfo WHERE versionId = "+vid);
    }

    @Override
    public boolean deleteUninstallLogByVersion(Long vid) {
        return uninstallLogDao.removeByHql("DELETE FROM UninstallLogInfo WHERE versionId = "+vid);
    }

    @Override
    public boolean deleteInstallLogByApp(Long appId) {
        return installLogDao.removeByHql("DELETE FROM InstallLogInfo WHERE appId = "+appId);
    }

    @Override
    public boolean deleteUninstallLogByApp(Long appId) {
        return uninstallLogDao.removeByHql("DELETE FROM InstallLogInfo WHERE appId = "+appId);
    }
}
