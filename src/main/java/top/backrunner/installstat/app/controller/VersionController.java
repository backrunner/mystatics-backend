package top.backrunner.installstat.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.installstat.utils.common.R;

@Controller
@RequestMapping(value = "/version")
public class VersionController {
    @RequestMapping(value = "/delete")
    @ResponseBody
    public R deleteAction(){

    }
}
