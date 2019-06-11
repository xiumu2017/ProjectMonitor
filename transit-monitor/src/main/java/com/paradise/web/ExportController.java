package com.paradise.web;

import com.paradise.project.domain.ProjectInfo;
import com.paradise.project.domain.ServerInfo;
import com.paradise.project.service.ProjectInfoService;
import com.paradise.utils.ExcelExportUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件导出
 *
 * @author Paradise
 */
@Controller
@CrossOrigin
@Slf4j
@RequestMapping("/project")
public class ExportController {

    private ProjectInfoService projectInfoService;

    public ExportController(ProjectInfoService projectInfoService) {
        this.projectInfoService = projectInfoService;
    }

    @RequestMapping("/excelExport")
    public void exportProjectInfo(ProjectInfo info, HttpServletRequest request, HttpServletResponse response) {
        // TODO
        List<ProjectInfo> projectInfoList = projectInfoService.selectForExport(info);
        if (!projectInfoList.isEmpty()) {
            List<List<Object>> dataList = getDataList(projectInfoList);
            ExcelExportUtils.ExcelExportBuilder builder = new ExcelExportUtils.ExcelExportBuilder(request, response)
                    .dataList(dataList)
                    .rowName(new String[]{"序号", "地市", "项目名称", "巡检方式", "巡检状态", "服务器操作系统", "服务器状态", "MAS类型", "MAS状态", "备注"})
                    .sheetName("项目信息")
                    .title("过境平台项目信息")
                    .build();
            ExcelExportUtils exportUtils = new ExcelExportUtils(builder);
            try {
                exportUtils.exportData();
            } catch (IOException e) {
                log.error("导出项目信息出现异常：");
                log.error(e.getLocalizedMessage(), e);
            }
        }

    }

    private List<List<Object>> getDataList(List<ProjectInfo> infos) {
        List<List<Object>> resultList = new ArrayList<>();
        int index = 1;
        for (ProjectInfo info : infos) {
            List<Object> objectList = new ArrayList<>();
            objectList.add(index++);
            objectList.add(info.getCity());
            objectList.add(info.getName());
            objectList.add(dealMonitorType(info));
            objectList.add(info.getStatus());
            objectList.add(dealOsName(info.getServerOs()));
            objectList.add(info.getServerStatus());
            objectList.add(dealMasType(info.getMasType()));
            objectList.add(info.getSmsStatus());
            objectList.add(info.getRemark());
            resultList.add(objectList);
        }
        return resultList;
    }

    private String dealMasType(String masType) {
        for (ProjectInfo.MAS_TYPE type : ProjectInfo.MAS_TYPE.values()) {
            if (type.getCode().equalsIgnoreCase(masType)) {
                return type.getName();
            }
        }
        return "/";
    }

    private String dealMonitorType(ProjectInfo info) {
        if (!ProjectInfo.PROJECT_ENABLE.ENABLE.equals(info.getEnable())) {
            return ProjectInfo.MONITOR_TYPE.NO.getName();
        }
        for (ProjectInfo.MONITOR_TYPE type : ProjectInfo.MONITOR_TYPE.values()) {
            if (type.getCode().equalsIgnoreCase(info.getMonitorType())) {
                return type.getName();
            }
        }
        return "/";
    }

    private String dealStatus(String status) {
        return "/";
    }

    private String dealOsName(String os) {
        for (ServerInfo.OS o : ServerInfo.OS.values()) {
            if (o.getCode().equalsIgnoreCase(os)) {
                return o.getName();
            }
        }
        return "/";

    }
}
