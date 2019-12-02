package top.backrunner.installstat.system.service;

import top.backrunner.installstat.system.entity.log.UserLoginLogInfo;

import java.util.List;
import java.util.Map;

public interface UserLogService {
    public void createLoginLog(Long uid, String ip);
    public List<UserLoginLogInfo> getLatestLoginLogs(Long uid, int count);
}
