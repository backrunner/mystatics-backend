package top.backrunner.installstat.app.service;

import top.backrunner.installstat.app.entity.ApplicationInfo;
import top.backrunner.installstat.app.entity.InstallLogInfo;
import top.backrunner.installstat.app.entity.UninstallLogInfo;
import top.backrunner.installstat.app.entity.VersionInfo;
import top.backrunner.installstat.app.exception.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;

public interface ApplicationService {
    // Application
    public List<ApplicationInfo> getApplicationList(long uid);
    public List<ApplicationInfo> getApplicationList(long uid, int page, int pageSize);
    public long getApplicationCount();
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
    public List<Map<String, Object>> getAppStatData(Long uid);
    // Version
    public VersionInfo fetchVersion(Long versionId);
    public List<VersionInfo> getVersionList(Long appId);
    public List<VersionInfo> getVersionList(Long appId, String branch);
    public List<VersionInfo> getVersionList(Long appId, int page, int pageSize);
    public long getVersionCount(Long uid);
    public boolean addVersion(VersionInfo info);
    public boolean deleteVersion(Long versionId);
    public void deleteVersionByUser(Long uid);
    public boolean deleteVersionByApplication(Long appId);
    public long countVersion(Long appId);
    public boolean truncateApplication(Long appId);
    // Stat
    public boolean increaseInstallCount(String appKey, String branch, String version, String uuid, String ip) throws ApplicationNotFoundException, CannotAccessAppException;
    public boolean increaseUninstallCount(String appKey, String branch, String version, String uuid, String ip) throws UninstallCountStatDisabledException, ApplicationNotFoundException, VersionNotFoundException, CannotAccessAppException;
    public long getRecentWeekInstallCount(Long uid);
    public long getRecentWeekUninstallCount(Long uid);
    public long getInstallCount(Long uid);
    public long getUninstallCount(Long uid);
    public Map<String, Object> getMonthInstallCount(Long appId);
    public Map<String, Object> getMonthInstallCountByUser(Long uid);
    public Map<String, Object> getMonthUninstallCountByUser(Long uid);
    // Report
    public List<Map<String, Object>> getReport(Long uid, int page, int pageSize);
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
