package top.backrunner.installstat.app.service;

import top.backrunner.installstat.app.entity.InstallLogInfo;
import top.backrunner.installstat.app.entity.UninstallLogInfo;

public interface ApplicationLogService {
    // 创建统计记录
    public boolean createInstallLog(InstallLogInfo info);
    public boolean createUninstallLog(UninstallLogInfo info);
    // 删除统计记录
    public boolean deleteInstallLogByVersion(Long vid);
    public boolean deleteUninstallLogByVersion(Long vid);
    public boolean deleteInstallLogByApp(Long vid);
    public boolean deleteUninstallLogByApp(Long vid);
}
