package top.backrunner.installstat.app.dao;

import top.backrunner.installstat.app.entity.VersionInfo;
import top.backrunner.installstat.core.dao.BaseDao;

public interface VersionDao extends BaseDao<VersionInfo> {
    public VersionInfo getVersion(Long appId, String branch, String version);
    public long getCount(Long appId);
}
