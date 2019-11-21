package top.backrunner.installstat.system.entity;

import top.backrunner.installstat.core.entity.CoreInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ms_role", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class RoleInfo extends CoreInfo implements Serializable {
    // 角色名称
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    @JoinColumn(name="users")
    private List<UserInfo> users = new ArrayList<UserInfo>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfo> users) {
        this.users = users;
    }
}
