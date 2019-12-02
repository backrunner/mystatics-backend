package top.backrunner.installstat.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.installstat.utils.common.R;

@Controller
@RequestMapping(value = "/submit")
public class SubmitController {
    @RequestMapping(value = "/install")
    @ResponseBody
    public R submitInstall(){

    }

    @RequestMapping(value = "/uninstall")
    @ResponseBody
    public R submitUninstall(){

    }
}
