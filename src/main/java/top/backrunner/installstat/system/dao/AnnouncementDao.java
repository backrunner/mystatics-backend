package top.backrunner.installstat.system.dao;

import top.backrunner.installstat.core.dao.BaseDao;
import top.backrunner.installstat.system.entity.AnnouncementInfo;

public interface AnnouncementDao extends BaseDao<AnnouncementInfo> {
    public AnnouncementInfo getLatestAnnouncement();
}
