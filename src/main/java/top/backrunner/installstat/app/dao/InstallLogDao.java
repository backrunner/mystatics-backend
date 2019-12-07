package top.backrunner.installstat.app.dao;

import top.backrunner.installstat.app.entity.InstallLogInfo;
import top.backrunner.installstat.core.dao.BaseDao;

import java.util.List;
import java.util.Map;

public interface InstallLogDao extends BaseDao<InstallLogInfo> {
    public boolean logExists(Long appId, Long vid, String uuid);
    public boolean logExists(Long appId, String uuid);
    public long getTotalCount(Long uid);
    public long getRecentWeekCount(Long uid);
    public long getAppRecentWeekCount(Long appId);
    public long getAppMonthCount(Long appId);
    public List<Map> getMonthCount(Long appId);
    public List<Map> getMonthCountByUser(Long userId);
}
