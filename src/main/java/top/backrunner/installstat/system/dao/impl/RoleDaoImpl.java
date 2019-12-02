package top.backrunner.installstat.system.dao.impl;

import org.springframework.stereotype.Repository;
import top.backrunner.installstat.core.dao.impl.BaseDaoImpl;
import top.backrunner.installstat.system.entity.RoleInfo;
import top.backrunner.installstat.system.dao.RoleDao;
import top.backrunner.installstat.utils.filter.SQLFilter;

@Repository
public class RoleDaoImpl extends BaseDaoImpl<RoleInfo> implements RoleDao {
    @Override
    public RoleInfo findById(Long id) {
        return this.getById(RoleInfo.class, id);
    }

    @Override
    public RoleInfo findByName(String name) {
        return this.getByHql("FROM RoleInfo WHERE name='"+ SQLFilter.filter(name) +"'");
    }
}
