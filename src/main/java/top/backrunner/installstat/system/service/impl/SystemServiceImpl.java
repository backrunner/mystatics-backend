package top.backrunner.installstat.system.service.impl;

import org.springframework.stereotype.Service;
import top.backrunner.installstat.system.dao.AnnouncementDao;
import top.backrunner.installstat.system.entity.AnnouncementInfo;
import top.backrunner.installstat.system.service.SystemService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SystemServiceImpl implements SystemService {

    @Resource
    private AnnouncementDao announcementDao;

    @Override
    public List<AnnouncementInfo> getAnnouncementList() {
        return announcementDao.findByHql("FROM AnnouncementInfo");
    }

    @Override
    public AnnouncementInfo getAnnouncement(Long id) {
        return announcementDao.getById(AnnouncementInfo.class, id);
    }

    @Override
    public AnnouncementInfo getLatestAnnouncement() {
        return announcementDao.getLatestAnnouncement();
    }

    @Override
    public boolean addAnnouncement(AnnouncementInfo info) {
        info.setCreateTime(new Date());
        info.setLastUpdateTime(new Date());
        return announcementDao.add(info);
    }

    @Override
    public boolean updateAnnouncement(AnnouncementInfo info) {
        info.setLastUpdateTime(new Date());
        return announcementDao.updateEntity(info);
    }

    @Override
    public boolean deleteAnnouncement(Long id) {
        return announcementDao.removeByHql("DELETE FROM AnnouncementInfo WHERE id = " + id);
    }


}
