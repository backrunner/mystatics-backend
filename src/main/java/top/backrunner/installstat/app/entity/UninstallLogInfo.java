package top.backrunner.installstat.app.entity;

import top.backrunner.installstat.core.entity.CoreEntityInfo;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ms_log_uninstall")
public class UninstallLogInfo extends CoreEntityInfo implements Serializable {
    private Long appId;
    private Long versionId;
    private String uuid;
    private String geo;

    private boolean reinstallFlag;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public boolean isReinstallFlag() {
        return reinstallFlag;
    }

    public void setReinstallFlag(boolean reinstallFlag) {
        this.reinstallFlag = reinstallFlag;
    }
}
