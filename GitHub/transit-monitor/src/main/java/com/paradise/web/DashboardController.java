package com.paradise.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 主界面 controller
 *
 * @author Paradise
 */
@Slf4j
@Controller
@RequestMapping("/index")
public class DashboardController {

    @RequestMapping
    @ResponseBody
    public String init() {
        return "index";
    }

    // api
    // 限制30分钟内只能查询一次

    // 服务状态 - 能否登录
    // 数据库状态
    // 服务器状态
    // 系统配置情况
    // 短信发送情况
    // 号码推送情况
    // 日志查询 - 下载

    // 信息维护界面

}
