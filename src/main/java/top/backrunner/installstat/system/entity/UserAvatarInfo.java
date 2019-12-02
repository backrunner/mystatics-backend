package top.backrunner.installstat.system.entity;

import top.backrunner.installstat.core.entity.CoreEntityInfo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ms_user_avatar")
public class UserAvatarInfo extends CoreEntityInfo implements Serializable {
    // 头像对应的用户
    private String parentId;
    // 头像的文件路径
    private String fileUrl;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}