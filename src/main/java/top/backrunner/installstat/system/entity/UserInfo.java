package top.backrunner.installstat.system.entity;

import top.backrunner.installstat.core.entity.CoreEntityInfo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ms_user", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "email", "phone"}))
public class UserInfo extends CoreEntityInfo implements Serializable {
    // 登录的用户名
    private String username;
    // 登录的密码
    private String password;
    // 加密用盐
    private String salt;
    // 邮箱
    private String email;
    // 电话
    private String phone;
    // 是否启用
    private boolean isEnabled;

    // 头像
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional=true)
    @JoinColumn(name = "avatar")
    private UserAvatarInfo avatar;

    // 角色
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, optional=true)
    @JoinColumn(name = "role")
    private RoleInfo role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public UserAvatarInfo getAvatar() {
        return avatar;
    }

    public void setAvatar(UserAvatarInfo avatar) {
        this.avatar = avatar;
    }

    public RoleInfo getRole() {
        return role;
    }

    public void setRole(RoleInfo role) {
        this.role = role;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
