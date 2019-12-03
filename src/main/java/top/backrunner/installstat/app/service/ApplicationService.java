package top.backrunner.installstat.app.service;

import top.backrunner.installstat.app.entity.ApplicationInfo;
import top.backrunner.installstat.app.entity.VersionInfo;
import top.backrunner.installstat.core.entity.Page;

import java.util.List;

public interface ApplicationService {
    // Application
    public List<ApplicationInfo> getApplicationList(int pageSize, int page);
    public boolean addApplication(ApplicationInfo application);
    public boolean updateApplication (ApplicationInfo application);
    public boolean deleteApplication (Long appId);
    public String renewAppKey(Long appId);
    public ApplicationInfo getAppInfoByKey(String appKey);  // 根据 AppKey 获取 AppInfo
    // Version
    public List<VersionInfo> getVersionList(int appId, int pageSize, int page);
    public boolean addVersion(VersionInfo info);
    public boolean deleteVersion(Long versionId);
    // Stat
    public boolean updateInstallCount(Long appId, String version, String uuid);
    public boolean updateUninstallCount(Long appId, String version, String uuid);
    // Admin
    public List<ApplicationInfo> getAllApplicationList(int pageSize, int page);
    public List<VersionInfo> getAllVersionList(int pageSize, int page);
    public boolean banApp(Long appId);
}
