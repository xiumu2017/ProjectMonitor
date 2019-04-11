package com.paradise.scheduler;

import com.paradise.monitor.MR;
import com.paradise.monitor.utils.MonitorForTransitByWeb;
import com.paradise.oracle.QueryForTransit;
import com.paradise.project.domain.DbInfo;
import com.paradise.project.domain.ProjectInfo;
import com.paradise.project.service.ProjectInfoService;
import com.paradise.ssh.ConnectLinuxCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Scheduled(cron = "0 0 16,20 * * ? ")
    public void run() {
        // 查询需要巡检的项目列表
        List<ProjectInfo> projectInfoList = new ArrayList<>();
        projectInfoList.add(new ProjectInfo());
        if (!projectInfoList.isEmpty()) {
            for (ProjectInfo projectInfo : projectInfoList) {
                log.info(projectInfo.getName() + "start check >>> ");
                MR webServerCheckResult = MonitorForTransitByWeb.webServerCheck(projectInfo);
                DbInfo dbInfo = projectInfoService.getDbInfo(projectInfo.getDbId());
                if (webServerCheckResult.getCode().equals(MR.Result_Code.NORMAL)) {
                    // 判断数据库能否直连
                    boolean dbAccessible = true;
                    if (projectInfo.getMonitorType().equals(ProjectInfo.MONITOR_TYPE.DB_)) {
                        String oracleDate;
                        if (dbInfo != null) {
                            try {
                                oracleDate = QueryForTransit.checkAccessible(dbInfo);
                                if (StringUtils.isEmpty(oracleDate)) {
                                    dbAccessible = false;
                                }
                            } catch (SQLException | ClassNotFoundException e) {
                                log.error(e.getLocalizedMessage(), e);
                                dbAccessible = false;
                            }
                        }
                    } else {
                        dbAccessible = false;
                    }

                    if (dbAccessible) {
                        // 进行数据库巡检
                        try {
                            Map<String, Object> checkResult = QueryForTransit.transitCheck(dbInfo);
                            // 对结果进行解析

                        } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                            log.error(e.getLocalizedMessage(), e);
                        }
                    } else {
                        // web接口 巡检
                        MR login = MonitorForTransitByWeb.doLogin(projectInfo);
                        if (login.getCode().equals(MR.Result_Code.NORMAL)) {
                            MR smsResult = MonitorForTransitByWeb.checkSmsStatus(projectInfo);
                            if (smsResult.getCode().equals(MR.Result_Code.NORMAL)) {
                                log.info(smsResult.getName());
                            }
                        } else {
                            log.info(projectInfo.getName() + "login fail...");
                        }

                    }
                } else {
                    log.info(projectInfo.getName() + "Web 无法访问 ==========================================");
                    log.info("start Linux check ...");
                    try {
                        ConnectLinuxCommand.login(projectInfoService.getServerInfo(projectInfo.getServerId()));
                    } catch (IOException e) {
                        log.error(e.getLocalizedMessage(), e);
                    }
                }
            }
        }
    }

    private boolean dateCompare(String oracleDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date sysDate = simpleDateFormat.parse(oracleDate);
            return Math.abs(sysDate.getTime() - System.currentTimeMillis()) <= 10 * 60 * 1000;
        } catch (ParseException e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
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
