package com.paradise.web;

import com.paradise.monitor.MonitorTools;
import com.paradise.project.domain.ProjectInfo;
import com.paradise.project.domain.ServerInfo;
import com.paradise.project.service.CheckRecordService;
import com.paradise.project.service.ProjectInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目信息 - Controller
 *
 * @author Paradise
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/server")
public class ProjectServerController {

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private MonitorTools monitorTools;

    @Resource
    private CheckRecordService checkRecordService;

    /**
     * 查询全部列表
     *
     * @param info 查询条件
     * @return 项目列表
     */
    @RequestMapping("/list")
    public R getList(ProjectInfo info) {
        List<ProjectInfo> list = projectInfoService.getAllEnableProjectList(info);
        return R.success(list);
    }

    /**
     * @return 返回服务器类型列表
     */
    @RequestMapping("/serverTypeList")
    public R serverTypeList() {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map;
        for (ServerInfo.Type type : ServerInfo.Type.values()) {
            map = new HashMap<>(2);
            map.put("code", type.getCode());
            map.put("name", type.getName());
            list.add(map);
        }
        return R.success(list);
    }


    /**
     * @return 返回os类型列表
     */
    @RequestMapping("/serverOsList")
    public R osTypeList() {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map;
        for (ServerInfo.OS type : ServerInfo.OS.values()) {
            map = new HashMap<>(2);
            map.put("code", type.getCode());
            map.put("name", type.getName());
            list.add(map);
        }
        return R.success(list);
    }

    /**
     * 保存服务器信息
     *
     * @param serverInfo 服务器信息
     * @return 保存结果
     */
    @RequestMapping("/saveServer")
    public R saveServer(ServerInfo serverInfo) {
        log.info(serverInfo.toString());
        return projectInfoService.saveServer(serverInfo.getProjectId(), serverInfo);
    }

    /**
     * 根据id初始化服务器信息
     *
     * @param id server_id
     * @return 服务器信息
     */
    @RequestMapping("/server")
    public R getServerInfo(String id) {
        return R.success(projectInfoService.getServerInfo(id));
    }

    /**
     * 服务器连接测试
     *
     * @param info 服务器信息
     * @return R
     */
    @RequestMapping("/serverConnectTest")
    public R serverConnectTest(ServerInfo info) {
        return projectInfoService.serverConnectTest(info);
    }
}
