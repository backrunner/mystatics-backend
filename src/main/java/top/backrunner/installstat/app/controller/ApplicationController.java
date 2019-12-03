package top.backrunner.installstat.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.backrunner.installstat.app.entity.ApplicationInfo;
import top.backrunner.installstat.utils.common.R;

import java.util.List;

@Controller
@RequestMapping(value = "/app")
public class ApplicationController {

    @RequestMapping(value = "/getList")
    @ResponseBody
    public List<ApplicationInfo> getList(){

    }

    // 获取该用户的应用数量
    @RequestMapping(value = "/getCount")
    @ResponseBody
    public R getCount(){

    }

    // 添加应用
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public R addAction(){

    }

    // 更新信息
    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    @ResponseBody
    public R updateInfo(){

    }

    // 删除应用
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public R deleteAction() {

    }

    // 重置 App Key
    @RequestMapping(value = "/renewAppKey")
    @ResponseBody
    public R renewAppKey() {

    }


}
