package com.chengxin.sync_job.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Mr.Song
 * @create 2019-05-10 17:33
 * @desc 主页面
 **/
@Controller
@RequestMapping(value = "/main")
public class MainController {

    /**
     * 路由规则查询界面初始化
     * @return
     */
    @RequestMapping(value = "/init")
    public String init() {
        return "/main";
    }
}
