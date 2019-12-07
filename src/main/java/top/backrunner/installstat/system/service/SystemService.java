package top.backrunner.installstat.system.service;

import top.backrunner.installstat.system.entity.AnnouncementInfo;

import java.util.List;

public interface SystemService {
    // 公告
    public List<AnnouncementInfo> getAnnouncementList();
    public AnnouncementInfo getAnnouncement(Long id);
    public AnnouncementInfo getLatestAnnouncement();
    public boolean addAnnouncement(AnnouncementInfo info);
    public boolean updateAnnouncement(AnnouncementInfo info);
    public boolean deleteAnnouncement(Long id);
}
