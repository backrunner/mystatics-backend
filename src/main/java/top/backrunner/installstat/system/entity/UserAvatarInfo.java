package top.backrunner.installstat.system.entity;

import top.backrunner.installstat.core.entity.CoreEntityInfo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ms_user_avatar")
public class UserAvatarInfo extends CoreEntityInfo implements Serializable {
    // 头像对应的用户
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent")
    private UserInfo parent;
    // 头像的文件路径
    private String fileUrl;
}