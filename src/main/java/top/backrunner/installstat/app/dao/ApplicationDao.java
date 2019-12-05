package top.backrunner.installstat.app.dao;

import top.backrunner.installstat.app.entity.ApplicationInfo;
import top.backrunner.installstat.core.dao.BaseDao;

public interface ApplicationDao extends BaseDao<ApplicationInfo> {
    public ApplicationInfo getByAppKey(String appKey);
    public boolean bundleIdExists(String bundleId);
}
