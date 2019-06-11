package com.paradise.scheduler;

import com.paradise.dingding.chatbot.DingRobotClient;
import com.paradise.dingding.chatbot.message.Message;
import com.paradise.monitor.MonitorTools;
import com.paradise.project.domain.CheckRecord;
import com.paradise.project.service.CheckRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 钉钉机器人 消息推送  任务调度
 *
 * @author Paradise
 */
@Component
@Slf4j
public class DingdingRobotScheduler {

    private final CheckRecordService checkRecordService;
    private final MonitorTools monitorTools;

    public DingdingRobotScheduler(CheckRecordService checkRecordService, MonitorTools monitorTools) {
        this.checkRecordService = checkRecordService;
        this.monitorTools = monitorTools;
    }

    @Scheduled(cron = "0 0 8,12,16,19 * * ?")
    public void pushTransitMsg() {
        log.info("start dingding push ...");
        String webHook = "c7152d897b4403229406ef47927690a5f7d9167d67bbbb254a61b3df5999f1b9";
        List<CheckRecord> recordList = checkRecordService.selectByRecord(new CheckRecord());
        if (!recordList.isEmpty()) {
            // 生成推送的markdown
            Message markdownMessage = monitorTools.getMessage(recordList);
            DingRobotClient client = new DingRobotClient();
            client.send(webHook, markdownMessage);
        }
    }
}
