package top.backrunner.installstat.app.dao.impl;

import org.springframework.stereotype.Repository;
import top.backrunner.installstat.app.dao.InstallLogDao;
import top.backrunner.installstat.app.entity.InstallLogInfo;
import top.backrunner.installstat.core.dao.impl.BaseDaoImpl;

@Repository
public class InstallLogDaoImpl extends BaseDaoImpl<InstallLogInfo> implements InstallLogDao {
}
