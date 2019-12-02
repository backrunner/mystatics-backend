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

    // 类型 如: win, linux, mac, android, ios, other
    @Column(nullable = false)
    private String platform;

    // 该版本的安装量
    private long installCount;

    // 该版本的卸载量
    private long uninstallCount;

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

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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
}
