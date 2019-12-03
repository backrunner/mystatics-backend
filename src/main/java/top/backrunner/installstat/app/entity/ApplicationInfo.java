package top.backrunner.installstat.app.entity;

import top.backrunner.installstat.core.entity.CoreEntityInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ms_apps", uniqueConstraints = @UniqueConstraint(columnNames = {"bundleName"}))
public class ApplicationInfo extends CoreEntityInfo implements Serializable {
    // 所属用户
    @Column(nullable = false)
    private long uid;
    // 唯一的ID 如com.example.app
    @Column(nullable = false)
    private String bundleName;
    // 名称
    @Column(nullable = false)
    private String name;
    // 描述
    private String desc;
    // 网页
    private String website;
    // 当前版本
    private String currentVersion;
    // 总安装量
    private long totalInstallCount;
    // 统计卸载量
    private boolean statUninstall;
    // 卸载量
    private long uninstallCount;
    // App Key
    private String appKey;

    // 是否可用
    public boolean isEnabled;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public long getTotalInstallCount() {
        return totalInstallCount;
    }

    public void setTotalInstallCount(long totalInstallCount) {
        this.totalInstallCount = totalInstallCount;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public boolean isStatUninstall() {
        return statUninstall;
    }

    public void setStatUninstall(boolean statUninstall) {
        this.statUninstall = statUninstall;
    }

    public long getUninstallCount() {
        return uninstallCount;
    }

    public void setUninstallCount(long uninstallCount) {
        this.uninstallCount = uninstallCount;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
