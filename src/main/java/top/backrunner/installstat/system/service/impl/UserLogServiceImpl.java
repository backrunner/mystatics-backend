package top.backrunner.installstat.system.service.impl;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import top.backrunner.installstat.system.dao.UserLoginLogDao;
import top.backrunner.installstat.system.entity.log.UserLoginLogInfo;
import top.backrunner.installstat.system.service.UserLogService;
import top.backrunner.installstat.utils.misc.GeoIPUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserLogServiceImpl implements UserLogService {

    private static final Logger logger = LoggerFactory.getLogger(UserLogService.class);

    @Resource
    private UserLoginLogDao userLoginLogDao;

    @Override
    public void createLoginLog(Long uid, String ip) {
        UserLoginLogInfo log = new UserLoginLogInfo();
        log.setCreateTime(new Date());
        log.setUid(uid);
        log.setIp(ip);
        log.setGeo(GeoIPUtils.o.getIPLocation(ip).toString());
        userLoginLogDao.add(log);
    }

    @Override
    public List<UserLoginLogInfo> getLatestLoginLogs(Long uid, int count) {
        List<UserLoginLogInfo> logs = userLoginLogDao.getLimitedList(uid, count);
        return logs;
    }
}
