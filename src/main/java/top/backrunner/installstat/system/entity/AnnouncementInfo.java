package top.backrunner.installstat.system.entity;

import top.backrunner.installstat.core.entity.CoreEntityInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ms_announcement")
public class AnnouncementInfo extends CoreEntityInfo {
    private String description;
    @Column(length = 16777215)
    private String content;

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
