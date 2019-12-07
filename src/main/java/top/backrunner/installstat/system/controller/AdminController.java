package top.backrunner.installstat.system.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.installstat.system.entity.AnnouncementInfo;
import top.backrunner.installstat.system.service.SystemService;
import top.backrunner.installstat.utils.common.R;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Resource
    private SystemService systemService;

    @RequestMapping(value = "/getAnnouncementList")
    @ResponseBody
    public R getAnnouncementList(){
        List<AnnouncementInfo> list = systemService.getAnnouncementList();
        if (list.isEmpty()){
            return R.error("列表为空");
        } else {
            return R.ok(list);
        }
    }

    @RequestMapping(value = "/addAnnouncement")
    @ResponseBody
    public R addAnnouncement(String desc, String content){
        if (!ObjectUtils.allNotNull(desc, content)){
            return R.badRequest("提交的参数不完整");
        }
        AnnouncementInfo info = new AnnouncementInfo();
        info.setDescription(desc);
        info.setContent(content);
        if (systemService.addAnnouncement(info)){
            return R.ok("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @RequestMapping(value = "/updateAnnouncement")
    @ResponseBody
    public R updateAnnouncement(Long id, String desc, String content){
        if (!ObjectUtils.allNotNull(desc, content)){
            return R.badRequest("提交的参数不完整");
        }
        AnnouncementInfo info = systemService.getAnnouncement(id);
        if (info == null){
            return R.error("无此公告");
        }
        info.setDescription(desc);
        info.setContent(content);
        if (systemService.updateAnnouncement(info)){
            return R.ok("编辑成功");
        } else {
            return R.error("编辑失败");
        }
    }

    @RequestMapping(value = "/deleteAnnouncement")
    @ResponseBody
    public R deleteAnnouncement(Long id){
        if (!ObjectUtils.allNotNull(id)){
            return R.badRequest("提交的参数不完整");
        }
        if (systemService.deleteAnnouncement(id)){
            return R.ok("删除成功");
        } else {
            return R.error("删除失败");
        }
    }
}
