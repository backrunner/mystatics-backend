package top.backrunner.installstat.core.dao.impl;

import org.springframework.stereotype.Repository;
import top.backrunner.installstat.core.dao.LogDao;

@Repository
public class LogDaoImpl<T> extends BaseDaoImpl<T> implements LogDao<T> {

    @Override
    public boolean create(T log) {
        return this.add(log);
    }
}
