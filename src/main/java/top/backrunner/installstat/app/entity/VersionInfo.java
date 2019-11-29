package top.backrunner.installstat.app.entity;

import top.backrunner.installstat.core.entity.CoreEntityInfo;

import javax.persistence.*;

@Table(name = "ms_version")
public class VersionInfo extends CoreEntityInfo {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional=true)
    @JoinColumn(name = "parent")
    private ApplicationInfo parent;

    @Column(nullable = false)
    private String version;

    private long count;
}
