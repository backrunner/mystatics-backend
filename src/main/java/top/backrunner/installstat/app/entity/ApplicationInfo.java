package top.backrunner.installstat.app.entity;

import top.backrunner.installstat.core.entity.CoreEntityInfo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "ms_apps", uniqueConstraints = @UniqueConstraint(columnNames = {"bundleName"}))
public class ApplicationInfo extends CoreEntityInfo{
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
    // 图标
    private String logo;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VersionInfo> versions = new ArrayList<VersionInfo>();

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<VersionInfo> getVersions() {
        return versions;
    }

    public void setVersions(List<VersionInfo> versions) {
        this.versions = versions;
    }
}
