package top.backrunner.installstat.core.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class CoreLogInfo extends CoreInfo implements Serializable {
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
