package top.backrunner.installstat.app.dao.impl;

import org.springframework.stereotype.Repository;
import top.backrunner.installstat.app.dao.VersionDao;
import top.backrunner.installstat.app.entity.VersionInfo;
import top.backrunner.installstat.core.dao.impl.BaseDaoImpl;

@Repository
public class VersionDaoImpl extends BaseDaoImpl<VersionInfo> implements VersionDao {
}
