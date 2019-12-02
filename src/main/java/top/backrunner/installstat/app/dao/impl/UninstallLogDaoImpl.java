package top.backrunner.installstat.app.dao.impl;

import org.springframework.stereotype.Repository;
import top.backrunner.installstat.app.dao.UninstallLogDao;
import top.backrunner.installstat.app.entity.UninstallLogInfo;
import top.backrunner.installstat.core.dao.impl.BaseDaoImpl;

@Repository
public class UninstallLogDaoImpl extends BaseDaoImpl<UninstallLogInfo> implements UninstallLogDao {
}
