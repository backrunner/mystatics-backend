package top.backrunner.installstat.app.dao;

import top.backrunner.installstat.app.entity.InstallLogInfo;
import top.backrunner.installstat.core.dao.BaseDao;

import java.util.List;
import java.util.Map;

public interface InstallLogDao extends BaseDao<InstallLogInfo> {
    public boolean logExists(Long appId, Long vid, String uuid);
    public boolean logExists(Long appId, String uuid);
    public long getRecentWeekCount(Long uid);
    public List<Map> getMonthCount(Long appId);
}
