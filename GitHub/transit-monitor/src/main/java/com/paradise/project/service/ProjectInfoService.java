package com.paradise.project.service;

import com.paradise.oracle.QueryForTransit;
import com.paradise.project.domain.DbInfo;
import com.paradise.project.domain.ProjectInfo;
import com.paradise.project.domain.ServerInfo;
import com.paradise.project.mapper.DbInfoMapper;
import com.paradise.project.mapper.ProjectInfoMapper;
import com.paradise.project.mapper.ServerInfoMapper;
import com.paradise.ssh.ConnectLinuxCommand;
import com.paradise.web.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * tm_project Service
 *
 * @author Paradise
 */
@Service
@Slf4j
public class ProjectInfoService {

    @Resource
    private ProjectInfoMapper projectInfoMapper;
    @Resource
    private DbInfoMapper dbInfoMapper;
    @Resource
    private ServerInfoMapper serverInfoMapper;

    private List<ProjectInfo> getProjectPage(ProjectInfo info, int pageNo, int pageSize) {
        List<ProjectInfo> projectInfoList = projectInfoMapper.getAllEnableProjects(info);
        if (!projectInfoList.isEmpty() && projectInfoList.size() > pageNo) {
            return projectInfoList.subList((pageNo - 1) * pageSize, pageNo * pageSize);
        } else {
            return projectInfoList;
        }
    }

    /**
     * 查询全部启用项目列表
     *
     * @return 项目信息列表
     */
    public List<ProjectInfo> getAllEnableProjectList(ProjectInfo info) {
        return projectInfoMapper.getAllEnableProjects(info);
    }

    /**
     * 新增 or 更新 项目信息
     *
     * @param info 项目信息
     * @return 处理结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int insert(ProjectInfo info) {
        if (StringUtils.isNotBlank(info.getId())) {
            return projectInfoMapper.update(info);
        } else {
            return projectInfoMapper.insert(info);
        }
    }

    /**
     * 修改项目 是否启用状态
     *
     * @param id     项目id
     * @param enable 原始状态
     * @return 修改结果
     */
    @Transactional(rollbackFor = Exception.class)
    public R changeEnable(String id, String enable) {
        if (ProjectInfo.PROJECT_ENABLE.ENABLE.equals(enable)) {
            enable = ProjectInfo.PROJECT_ENABLE.DISABLE;
        } else {
            enable = ProjectInfo.PROJECT_ENABLE.ENABLE;
        }
        if (projectInfoMapper.updateEnable(id, enable) == 1) {
            return R.success();
        } else {
            return R.error();
        }
    }

    /**
     * 保存数据库信息
     *
     * @param dbInfo 数据库信息
     * @return 保存结果
     */
    @Transactional(rollbackFor = Exception.class)
    public R saveDb(DbInfo dbInfo) {
        if (StringUtils.isNotEmpty(dbInfo.getId())) {
            dbInfoMapper.update(dbInfo);
            return R.success();
        } else {
            if (1 == dbInfoMapper.insert(dbInfo) && StringUtils.isNotEmpty(dbInfo.getId())) {
                if (projectInfoMapper.updateDbId(dbInfo.getProjectId(), dbInfo.getId()) == 1) {
                    return R.success();
                }
            }
        }
        return R.error();
    }

    /**
     * 保存服务器信息
     *
     * @param id         project_id 项目信息id
     * @param serverInfo 服务器信息
     * @return 保存结果
     */
    @Transactional(rollbackFor = Exception.class)
    public R saveServer(String id, ServerInfo serverInfo) {
        if (StringUtils.isNotEmpty(serverInfo.getId())) {
            if (serverInfoMapper.update(serverInfo) == 1) {
                return R.success();
            }
        } else {
            if (serverInfoMapper.insert(serverInfo) == 1 && StringUtils.isNotEmpty(serverInfo.getId())) {
                if (projectInfoMapper.updateServerId(id, serverInfo.getId()) == 1) {
                    return R.success();
                }
            }
        }
        return R.error();
    }

    /**
     * 获取服务器信息
     *
     * @param id server_id
     * @return 服务器信息
     */
    public ServerInfo getServerInfo(String id) {
        return serverInfoMapper.select(id);
    }

    /**
     * 获取 数据库信息
     *
     * @param id db_id
     * @return 数据库信息
     */
    public DbInfo getDbInfo(String id) {
        return dbInfoMapper.selectInfo(id);
    }

    /**
     * 数据库连接测试
     *
     * @param info server_info
     * @return 正常状态下返回最近一次接收到号码推送的时间 appTime
     */
    public R dbConnectTest(DbInfo info) {
        try {
            // TODO 增加后端校验逻辑
            String lastPushTime = QueryForTransit.queryLastPushTime(info);
            return R.success(lastPushTime);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return R.error(e.getLocalizedMessage());
        }
    }

    /**
     * 服务器连接测试
     *
     * @param info 服务器信息
     * @return 测试结果 - 简单认证
     */
    public R serverConnectTest(ServerInfo info) {
        log.info(info.toString());
        try {
            // TODO 增加后端校验逻辑
            // TODO 增加 多种校验以及结果处理
            if (ConnectLinuxCommand.login(info)) {
                return R.success(ConnectLinuxCommand.execute("date"));
            } else {
                return R.error();
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return R.error(e.getLocalizedMessage());
        }
    }

    /**
     * 项目信息查询
     *
     * @param id 项目id
     * @return 项目信息
     */
    public ProjectInfo selectById(String id) {
        return projectInfoMapper.select(id);
    }
}
