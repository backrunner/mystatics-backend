package top.backrunner.installstat.core.dao;

import top.backrunner.installstat.core.dao.BaseDao;

public interface LogDao<T> extends BaseDao<T> {
    public boolean create(T log);
}
