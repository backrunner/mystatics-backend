package top.backrunner.installstat.app.entity;

import top.backrunner.installstat.core.entity.CoreEntityInfo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ms_version")
public class VersionInfo extends CoreEntityInfo implements Serializable {
    // 版本所属App
    private Long appId;

    // 版本号
    @Column(nullable = false)
    private String version;

    // 分支
    @Column(nullable = false)
    private String branch;

    // 该版本的安装量
    private long installCount;

    // 该版本的卸载量
    private long uninstallCount;

    // 是否可用
    private boolean isEnabled;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public long getInstallCount() {
        return installCount;
    }

    public void setInstallCount(long installCount) {
        this.installCount = installCount;
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
