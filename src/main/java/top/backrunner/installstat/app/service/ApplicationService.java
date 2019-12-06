package top.backrunner.installstat.app.service;

import top.backrunner.installstat.app.entity.ApplicationInfo;
import top.backrunner.installstat.app.entity.InstallLogInfo;
import top.backrunner.installstat.app.entity.UninstallLogInfo;
import top.backrunner.installstat.app.entity.VersionInfo;
import top.backrunner.installstat.app.exception.ApplicationNotFoundException;
import top.backrunner.installstat.app.exception.NoAuthorityException;
import top.backrunner.installstat.app.exception.UninstallCountStatDisabledException;
import top.backrunner.installstat.app.exception.VersionNotFoundException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;

public interface ApplicationService {
    // Application
    public List<ApplicationInfo> getApplicationList(long uid);
    public long getApplicationCount(Long uid);
    public ApplicationInfo fetchAppInfo(Long appId);
    public boolean addApplication(ApplicationInfo application);
    public boolean updateApplication (ApplicationInfo application);
    public boolean deleteApplication (Long appId);
    public boolean deleteApplicationByUser(Long uid);
    public String fetchAppKey(Long appId) throws EntityNotFoundException;
    public String renewAppKey(ApplicationInfo app);
    public ApplicationInfo getAppInfoByKey(String appKey);  // 根据 AppKey 获取 AppInfo
    public boolean bundleIdExists(String bundleId);
    // Version
    public VersionInfo fetchVersion(Long versionId);
    public List<VersionInfo> getVersionList(Long appId);
    public List<VersionInfo> getVersionList(Long appId, int page, int pageSize);
    public boolean addVersion(VersionInfo info);
    public boolean deleteVersion(Long versionId);
    public void deleteVersionByUser(Long uid);
    public boolean deleteVersionByApplication(Long appId);
    public long countVersion(Long appId);
    public boolean truncateApplication(Long appId);
    // Stat
    public boolean increaseInstallCount(String appKey, String branch, String version, String uuid, String ip) throws ApplicationNotFoundException;
    public boolean increaseUninstallCount(String appKey, String branch, String version, String uuid, String ip) throws UninstallCountStatDisabledException, ApplicationNotFoundException, VersionNotFoundException;
    public long getRecentWeekInstallCount(Long uid);
    public long getRecentWeekUninstallCount(Long uid);
    public List<Map<String, Object>> getMonthInstallCount(Long appId);
    // Admin
    public List<ApplicationInfo> getAllApplicationList(int page, int pageSize);
    public List<VersionInfo> getAllVersionList(int page, int pageSize);
    public boolean banApp(Long appId) throws EntityNotFoundException;

    // 创建统计记录
    public boolean createInstallLog(Long appId, Long versionId, String uuid, String ip);
    public boolean createUninstallLog(Long appId, Long versionId, String uuid, String ip);
    // 删除统计记录
    public boolean deleteInstallLogByVersion(Long vid);
    public boolean deleteUninstallLogByVersion(Long vid);
    public boolean deleteInstallLogByApp(Long appId);
    public boolean deleteUninstallLogByApp(Long appId);
}
