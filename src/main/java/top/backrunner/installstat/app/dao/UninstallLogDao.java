package top.backrunner.installstat.app.dao;

import top.backrunner.installstat.app.entity.UninstallLogInfo;
import top.backrunner.installstat.core.dao.BaseDao;

public interface UninstallLogDao extends BaseDao<UninstallLogInfo> {
    public boolean checkReinstall(Long appId, String branch, String uuid);
    public boolean checkReinstall(Long appId, Long vid, String uuid);
    public long updateReinstall(Long appId, String branch, String uuid);
    public long updateReinstall(Long appId, Long vid, String uuid);
    public long getRecentWeekCount(Long uid);
}
