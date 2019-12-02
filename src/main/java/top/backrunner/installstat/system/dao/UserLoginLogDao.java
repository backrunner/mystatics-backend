package top.backrunner.installstat.system.dao;

import top.backrunner.installstat.core.dao.BaseDao;
import top.backrunner.installstat.system.entity.log.UserLoginLogInfo;

import java.util.List;

public interface UserLoginLogDao extends BaseDao<UserLoginLogInfo> {
    public List<UserLoginLogInfo> getList(Long uid);
    public List<UserLoginLogInfo> getLimitedList(Long uid, int limited);
}
