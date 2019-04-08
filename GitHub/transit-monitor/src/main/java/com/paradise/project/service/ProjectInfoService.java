package com.paradise.project.service;

import com.paradise.project.domain.DbInfo;
import com.paradise.project.domain.ProjectInfo;
import com.paradise.project.domain.ServerInfo;
import com.paradise.project.mapper.DbInfoMapper;
import com.paradise.project.mapper.ProjectInfoMapper;
import com.paradise.project.mapper.ServerInfoMapper;
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

    @Transactional(rollbackFor = Exception.class)
    public int insert(ProjectInfo info) {
        if (StringUtils.isNotBlank(info.getId())) {
            return projectInfoMapper.update(info);
        } else {
            return projectInfoMapper.insert(info);
        }
    }

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

    public ServerInfo getServerInfo(String id) {
        return serverInfoMapper.select(id);
    }

    public DbInfo getDbInfo(String id) {
        return dbInfoMapper.selectInfo(id);
    }
}
