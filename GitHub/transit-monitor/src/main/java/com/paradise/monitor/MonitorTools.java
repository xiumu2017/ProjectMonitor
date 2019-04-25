package com.paradise.monitor;

import com.paradise.dingding.chatbot.message.MarkdownMessage;
import com.paradise.dingding.chatbot.message.Message;
import com.paradise.oracle.QueryForTransit;
import com.paradise.project.domain.CheckRecord;
import com.paradise.project.domain.DbInfo;
import com.paradise.project.domain.ProjectInfo;
import com.paradise.project.domain.ServerInfo;
import com.paradise.project.service.CheckRecordService;
import com.paradise.project.service.ProjectInfoService;
import com.paradise.ssh.CmdClients;
import com.paradise.ssh.LinuxCmdClient;
import com.paradise.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Paradise
 */
@Slf4j
@Component
public class MonitorTools {

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private CheckRecordService checkRecordService;

    // 巡检指令
    // web 、 sql 、server

    /**
     * 巡检核心方法
     */
    public void run() {
        // 查询需要巡检的项目列表
        List<ProjectInfo> projectInfoList = projectInfoService.selectListForCheck();
        if (!projectInfoList.isEmpty()) {
            long start = System.currentTimeMillis();
            for (ProjectInfo projectInfo : projectInfoList) {
                check(projectInfo, false);
            }
            log.info(" task finish cost : " + (System.currentTimeMillis() - start) + " ms");
        }
    }

    public void check(ProjectInfo projectInfo, boolean isManual) {
        // 判断上次巡检时间 -- 1h以内不再巡检 -- 错误未处理不再巡检
        if (!available(projectInfo.getId()) && !isManual) {
            return;
        }
        String projectName = projectInfo.getName();
        log.info(projectName + "  start check >>> ");
        // step: web服务是否正常
        MR webServerCheckResult = MonitorForTransitByWeb.webServerCheck(projectInfo);
        DbInfo dbInfo = projectInfoService.getDbInfo(projectInfo.getDbId());
        if (webServerCheckResult.getCode().equals(MR.Result_Code.NORMAL)) {
            // 判断数据库能否直连
            boolean dbAccessible = false;
            if (projectInfo.getMonitorType().equals(ProjectInfo.MONITOR_TYPE.DB.getCode())) {
                String oracleDate;
                if (dbInfo != null) {
                    try {
                        oracleDate = QueryForTransit.checkAccessible(dbInfo);
                        if (StringUtils.isNotEmpty(oracleDate)) {
                            dbAccessible = true;
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        log.error(e.getLocalizedMessage(), e);
                    }
                }
            }
            if (dbAccessible) {
                dbCheck(dbInfo, projectInfo);
            } else {
                // web接口 巡检
                MR login = MonitorForTransitByWeb.doLogin(projectInfo);
                if (isNormal(login)) {
                    MR smsResult = MonitorForTransitByWeb.checkSmsStatus(projectInfo);
                    projectInfoService.updateProjectStatus(projectInfo.getId(), smsResult.getCode());
                    if (isNormal(smsResult)) {
                        checkRecordService.insert(new CheckRecord.Builder(projectInfo.getId(), smsResult.getName())
                                .checkCode(smsResult.getCode()).build());
                    } else {
                        checkRecordService.insert(new CheckRecord.Builder(projectInfo.getId(), smsResult.getName())
                                .checkCode(smsResult.getCode()).build());
                    }
                } else {
                    log.info(projectName + "login fail...");
                    projectInfoService.updateProjectStatus(projectInfo.getId(), login.getCode());
                    checkRecordService.insert(new CheckRecord.Builder(projectInfo.getId(), "无法直连数据库巡检，web登录失败！" + login.getName())
                            .checkCode(login.getCode()).build());
                }
            }
        } else {
            dealWebInAccessible(projectInfo);
        }
    }

    /**
     * 判断是否需要巡检
     *
     * @param id 项目信息id
     * @return true-需要 false - 不需要
     */
    private boolean available(String id) {
        CheckRecord record = checkRecordService.selectByProjectId(id);
        if (record != null) {
            if (DateUtils.dateCompare(record.getCreateTime(), 60L)) {
                return false;
            } else {
                return record.getCheckCode().equals(MR.Result_Code.NORMAL);
            }
        }
        return true;
    }

    /**
     * web 无法访问处理
     *
     * @param projectInfo 项目信息
     */
    private void dealWebInAccessible(ProjectInfo projectInfo) {
        projectInfoService.updateProjectStatus(projectInfo.getId(), MR.Result_Code.WEB_INACCESSIBLE);
        log.info(projectInfo.getName() + "Web 无法访问 start Linux check ==========================================");
        ServerInfo serverInfo = projectInfoService.getServerInfo(projectInfo.getServerId());
        if (serverInfo == null) {
            checkRecordService.insert(new CheckRecord.Builder(projectInfo.getId(), "Web 无法访问；缺少服务器信息，无法进行服务器检查").checkCode(MR.Result_Code.CHECK_ERROR).build());
            return;
        }
        LinuxCmdClient client = CmdClients.createLinuxClient(serverInfo);
        try {
            MR mr = client.login();
            if (isNormal(mr)) {
                String serverDate = client.date();
                String result = "WEB服务无法访问，服务器SSH连接正常，服务器当前时间：" + serverDate;
                // 保存记录到巡检记录表
                checkRecordService.insert(new CheckRecord.Builder(projectInfo.getId(), result).checkCode(mr.getCode()).build());
            } else {
                // 服务器异常
                String result = "WEB服务无法访问，服务器SSH无法连接：" + mr.getCode() + " /r/n" + mr.getName();
                checkRecordService.insert(new CheckRecord.Builder(projectInfo.getId(), result).checkCode(mr.getCode()).build());
                // 更新服务器状态
                projectInfoService.updateServerStatus(projectInfo.getServerId(), mr.getCode());
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
            // 服务器异常
            String result = "WEB服务无法访问，服务器SSH无法连接：" + " /r/n" + e.getLocalizedMessage();
            checkRecordService.insert(new CheckRecord.Builder(projectInfo.getId(), result).checkCode(MR.Result_Code.SERVER_CONNECT_ERROR).build());
            // 更新服务器状态
            projectInfoService.updateServerStatus(projectInfo.getServerId(), MR.Result_Code.SERVER_CONNECT_ERROR);
        } finally {
            client.closeClient();
        }
    }

    /**
     * 数据库巡检
     *
     * @param dbInfo      数据库信息
     * @param projectInfo 项目信息
     */
    private void dbCheck(DbInfo dbInfo, ProjectInfo projectInfo) {
        try {
            if (dbInfo != null) {
                log.info(" start oracle query... ");
                MR checkResult = QueryForTransit.dbCheckForTransit(dbInfo);
                projectInfoService.updateProjectStatus(projectInfo.getId(), checkResult.getCode());
                if (isNormal(checkResult)) {
                    checkRecordService.insert(new CheckRecord.Builder(projectInfo.getId(), "")
                            .checkCode(checkResult.getCode())
                            .build());
                } else {
                    checkRecordService.insert(new CheckRecord.Builder(projectInfo.getId(), checkResult.getName())
                            .checkCode(checkResult.getCode()).build());
                }
            }

        } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            log.error(e.getLocalizedMessage(), e);
            checkRecordService.insert(new CheckRecord.Builder(projectInfo.getId(), "数据库巡检异常" + e.getLocalizedMessage())
                    .checkCode(MR.Result_Code.CHECK_ERROR).build());
        }
    }

    private boolean isNormal(MR mr) {
        return mr.getCode().equals(MR.Result_Code.NORMAL);
    }

    /**
     * 从巡检记录中合成错误报告 markdown
     *
     * @param records 巡检记录
     * @return 钉钉推送message
     */
    public Message getMessage(List<CheckRecord> records) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String title = simpleDateFormat.format(new Date()) + "巡检结果";
        // 生成推送的markdown
        MarkdownMessage markdownMessage = new MarkdownMessage();
        markdownMessage.setTitle(title);
        markdownMessage.add(MarkdownMessage.getHeaderText(1, title));
        List<String> webList = new ArrayList<>();
        List<String> serverList = new ArrayList<>();
        List<String> smsList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        int count = 1;
        for (CheckRecord record : records) {
            MarkdownMessage child = new MarkdownMessage();
            child.add(MarkdownMessage.getHeaderText(4, count++ + "." + record.getProjectName()));
            MarkdownMessage childChild = new MarkdownMessage();
            childChild.add(MarkdownMessage.getBoldText(record.getCheckCode()) + ": ");
            childChild.add(record.getResult() + " \n");
            String remark = projectInfoService.selectById(record.getProjectId()).getRemark();
            if (StringUtils.isNotEmpty(remark)) {
                childChild.add(MarkdownMessage.getReferenceText("*备注信息*：" + remark));
            }
            child.add(MarkdownMessage.getReferenceText(childChild.getText()));
            if (record.getCheckCode().startsWith("WEB")) {
                webList.add(child.getText());
            } else if (record.getCheckCode().startsWith("SERVER")) {
                serverList.add(child.getText());
            } else if (record.getCheckCode().startsWith("SMS")) {
                smsList.add(child.getText());
            } else {
                list.add(child.getText());
            }
        }
        if (!webList.isEmpty()) {
            markdownMessage.add(MarkdownMessage.getHeaderText(3, "web 异常："));
            markdownMessage.add("  ");
            markdownMessage.add(listToString(webList));
        }
        if (!serverList.isEmpty()) {
            markdownMessage.add(MarkdownMessage.getHeaderText(3, "服务器 异常："));
            markdownMessage.add("  ");
            markdownMessage.add(listToString(serverList));
        }

        if (!smsList.isEmpty()) {
            markdownMessage.add(MarkdownMessage.getHeaderText(3, "短信发送 异常："));
            markdownMessage.add("  ");
            markdownMessage.add(listToString(smsList));
        }

        if (!list.isEmpty()) {
            markdownMessage.add(MarkdownMessage.getHeaderText(3, "其它 异常："));
            markdownMessage.add("  ");
            markdownMessage.add(listToString(list));
        }
        markdownMessage.add("  ");
        markdownMessage.add(MarkdownMessage.getLinkText("详细信息点击链接查看", "http://192.168.1.134/#/transit/project"));
        log.info(markdownMessage.toJsonString());
        return markdownMessage;
    }

    private String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
        }
        return sb.toString();
    }

}
