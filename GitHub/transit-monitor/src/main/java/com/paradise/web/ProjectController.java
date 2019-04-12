package com.paradise.web;

import com.paradise.monitor.MR;
import com.paradise.monitor.MonitorTools;
import com.paradise.monitor.utils.MonitorForTransitByWeb;
import com.paradise.oracle.QueryForTransit;
import com.paradise.project.domain.DbInfo;
import com.paradise.project.domain.ProjectInfo;
import com.paradise.project.domain.ServerInfo;
import com.paradise.project.service.ProjectInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.SQLException;
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
@RequestMapping("/project")
public class ProjectController {

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private MonitorTools monitorTools;

    /**
     * 查询全部列表
     *
     * @param info 查询条件
     * @return 项目列表
     * TODO 分页查询处理
     */
    @RequestMapping("/list")
    public R getList(ProjectInfo info) {
        List<ProjectInfo> list = projectInfoService.getAllEnableProjectList(info);
        return R.success(list);
    }

    /**
     * @return 返回mas类型列表
     */
    @RequestMapping("/masTypeList")
    public R getMasTypeList() {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map;
        for (ProjectInfo.MAS_TYPE type : ProjectInfo.MAS_TYPE.values()) {
            map = new HashMap<>(2);
            map.put("code", type.getCode());
            map.put("name", type.getName());
            list.add(map);
        }
        return R.success(list);
    }

    /**
     * @return 返回项目类型列表
     */
    @RequestMapping("/projectTypeList")
    public R projectTypeList() {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map;
        for (ProjectInfo.Type type : ProjectInfo.Type.values()) {
            map = new HashMap<>(2);
            map.put("code", type.getCode());
            map.put("name", type.getName());
            list.add(map);
        }
        return R.success(list);
    }

    /**
     * 保存项目信息 （根据是否有id判断新增 or 更新）
     *
     * @param info 项目信息
     * @return 保存结果
     */
    @RequestMapping("/saveProject")
    public R saveProject(ProjectInfo info) {
        int i = projectInfoService.insert(info);
        if (i == 1) {
            return R.success();
        } else {
            return R.error();
        }
    }

    /**
     * 修改启用状态
     *
     * @param id     项目id
     * @param enable 原状态
     * @return 处理结果
     */
    @RequestMapping("/changeEnable")
    public R changeEnable(String id, String enable) {
        return projectInfoService.changeEnable(id, enable);
    }

    /**
     * 保存数据库信息
     *
     * @param dbInfo 数据库信息
     * @return 保存结果
     */
    @RequestMapping("/saveDb")
    public R saveDb(DbInfo dbInfo) {
        log.info(dbInfo.toString());
        return projectInfoService.saveDb(dbInfo);
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
     * 初始化化数据库信息
     *
     * @param id db_id
     * @return 数据库信息
     */
    @RequestMapping("/db")
    public R getDbInfo(String id) {
        return R.success(projectInfoService.getDbInfo(id));
    }

    /**
     * 数据库连接测试
     *
     * @param info 数据库信息
     * @return R
     */
    @RequestMapping("/dbConnectTest")
    public R dbConnectTest(DbInfo info) {
        return projectInfoService.dbConnectTest(info);
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

    /**
     * 数据库巡检
     *
     * @param id 项目id
     * @return Map
     */
    @RequestMapping("/transitCheck")
    public R transitCheck(String id) {
        ProjectInfo projectInfo = projectInfoService.selectById(id);
        ServerInfo serverInfo = projectInfoService.getServerInfo(projectInfo.getServerId());
        DbInfo dbInfo = projectInfoService.getDbInfo(projectInfo.getDbId());
        Map<String, Object> resultMap;
        try {
            resultMap = QueryForTransit.transitCheck(dbInfo);
        } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            log.error(e.getLocalizedMessage(), e);
            return R.error(e.getLocalizedMessage());
        }
        return R.success(resultMap);
    }

    /**
     * web登录测试
     *
     * @param info 项目信息
     * @return R
     */
    @RequestMapping("/webLoginCheck")
    public R webLoginCheck(ProjectInfo info) {
        MR result = MonitorForTransitByWeb.webServerCheck(info);
        if (result.getCode().equals(MR.Result_Code.NORMAL)) {
            MR monitorResult = MonitorForTransitByWeb.doLogin(info);
            if (monitorResult.getCode().equals(MR.Result_Code.NORMAL)) {
                return R.success(monitorResult.getName());
            }
            return R.error(result.getName());
        }
        return R.error(result.getName());
    }

    @RequestMapping("/startCheck")
    public R startCheck(String token) {
        if ("paradise".equals(token)) {
            monitorTools.run();
        }
        return R.success();
    }
}
