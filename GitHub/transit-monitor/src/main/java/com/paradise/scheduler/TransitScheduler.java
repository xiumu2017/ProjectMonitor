package com.paradise.scheduler;

import com.paradise.monitor.MonitorTools;
import com.paradise.project.service.ProjectInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 过境项目巡检 调度器
 *
 * @author Paradise
 */
@Slf4j
@Component
public class TransitScheduler {

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private MonitorTools monitorTools;

    @Scheduled(cron = "0 0 1/2 * * ? ")
    public void run() {
        monitorTools.run();
    }

    /**
     * Linux 服务器巡检
     * 1. 获取需要巡检的服务器信息列表
     * 2. 调用方法进行巡检
     * 3. 汇总错误信息
     */
    public void linuxServerCheck() {
        log.info("start linuxServerCheck >>> ");
    }

    /**
     * Web服务巡检
     */
    public void transitWebCheck() {
        log.info("start transitWebCheck >>> ");
        if (projectInfoService != null) {
            log.info("Spring Bean Auto Success");
        }
    }

}
