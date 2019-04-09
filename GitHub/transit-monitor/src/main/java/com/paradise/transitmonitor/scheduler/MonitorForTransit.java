package com.paradise.transitmonitor.scheduler;

import com.paradise.oracle.QueryForTransit;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 过境平台重点巡检
 *
 * @author dzhang
 */
public class MonitorForTransit implements Job {

    private static final Logger logger = LoggerFactory.getLogger(MonitorForTransit.class);

    /**
     * 报警时间差值，默认10m 600000毫秒
     */
    private static final Long WARNING_LEVEL = 600000L;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 查询重点巡检项目

        // 连接巡检项目数据库
        try {
            String lastTime = QueryForTransit.queryLastPushTime(null);
            logger.info("[ProjectName: 九华山文明燃香]  最近一次接收到后台号码推送时间：" + lastTime);
            Date lastDate = simpleDateFormat.parse(lastTime);
            if (Calendar.getInstance().getTimeInMillis() - lastDate.getTime() > WARNING_LEVEL) {
                // 记录到推送表

                // or 直接推送
                logger.info("[ProjectName:] 已超时，请检查后台数据同步任务！");

            } else {
                // 记录巡检结果
                logger.info("[ProjectName:] 运行正常！");
            }

        } catch (ParseException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 查询结果
        // 校验逻辑
        // 推送信息
    }

}
