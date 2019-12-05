package top.backrunner.installstat.app.entity;

import top.backrunner.installstat.core.entity.CoreEntityInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "ms_apps")
public class ApplicationInfo extends CoreEntityInfo implements Serializable {
    // 所属用户
    @Column(nullable = false)
    private long uid;
    // 唯一的ID 如com.example.app
    @Column(nullable = false)
    private String bundleId;
    // 名称
    @Column(nullable = false)
    private String displayName;
    // 描述
    private String description;
    // 网页
    private String website;
    // 当前版本
    @ElementCollection
    private Map<String, String> currentVersion = new HashMap<>();
    // 总安装量
    private long installCount;
    // 是否统计卸载量
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

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Map<String, String> getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(Map<String, String> currentVersion) {
        this.currentVersion = currentVersion;
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

    public long getInstallCount() {
        return installCount;
    }

    public void setInstallCount(long installCount) {
        this.installCount = installCount;
    }
}
