package top.backrunner.installstat.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.installstat.utils.common.R;

@Controller
@RequestMapping(value = "/error")
public class ErrorController {

    @RequestMapping(value = "/unauth")
    @ResponseBody
    public R unauth(){
        return R.unauth("请先登录");
    }
}
