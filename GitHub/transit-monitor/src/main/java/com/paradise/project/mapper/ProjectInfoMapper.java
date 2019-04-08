package com.paradise.project.mapper;

import com.paradise.project.domain.ProjectInfo;
import com.paradise.project.sql.ProjectSQLProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * tm_project Mapper
 *
 * @author Paradise
 */
@Mapper
public interface ProjectInfoMapper {

    /**
     * 查询全部
     *
     * @param projectInfo 查询参数
     * @return 项目信息列表
     */
    @SelectProvider(type = ProjectSQLProvider.class, method = "selectByPage")
    public List<ProjectInfo> getAllEnableProjects(ProjectInfo projectInfo);

    /**
     * 新增
     *
     * @param info 项目信息
     * @return 返回结果
     */
    @Insert("insert into tm_project (id,name,city,url,user_name,password,remark,mas_type)" +
            " values (UUID(),#{name},#{city},#{url},#{userName},#{password},#{remark},#{masType})")
    int insert(ProjectInfo info);

    /**
     * 更新
     *
     * @param info 项目信息
     * @return 更新结果
     */
    @Update("update tm_project set city=#{city}, name=#{name}, url=#{url}, user_name=#{userName}," +
            "password =#{password}, remark=#{remark}, mas_type=#{masType} where id = #{id}")
    int update(ProjectInfo info);

    /**
     * 状态更新
     *
     * @param id     项目id
     * @param enable 目标状态
     * @return 更新结果
     */
    @Update("update tm_project set enable=#{enable} where id = #{id}")
    int updateEnable(String id, String enable);

    /**
     * 更新数据库关联
     *
     * @param id       项目id
     * @param dbInfoId 数据库信息id
     * @return 更新结果
     */
    @Update("update tm_project set db_id=#{dbInfoId} where id = #{id}")
    int updateDbId(String id, String dbInfoId);

    /**
     * 更新服务器关联
     *
     * @param id           项目id
     * @param serverInfoId 服务器信息id
     * @return 更新结果
     */
    @Update("update tm_project set server_id=#{serverInfoId} where id = #{id}")
    int updateServerId(String id, String serverInfoId);
}
