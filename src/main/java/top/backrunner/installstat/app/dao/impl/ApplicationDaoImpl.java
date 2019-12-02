package top.backrunner.installstat.app.dao.impl;

import org.springframework.stereotype.Repository;
import top.backrunner.installstat.app.dao.ApplicationDao;
import top.backrunner.installstat.app.entity.ApplicationInfo;
import top.backrunner.installstat.core.dao.impl.BaseDaoImpl;

@Repository
public class ApplicationDaoImpl extends BaseDaoImpl<ApplicationInfo> implements ApplicationDao {
}
