package top.backrunner.installstat.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.installstat.app.entity.VersionInfo;
import top.backrunner.installstat.utils.common.R;

import java.util.List;

@Controller
@RequestMapping(value = "/version")
public class VersionController {

    @RequestMapping(value = "/getList")
    @ResponseBody
    public List<VersionInfo> getList(){

    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public R deleteAction(){

    }
}
