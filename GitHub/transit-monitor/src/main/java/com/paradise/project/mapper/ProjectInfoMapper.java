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
    List<ProjectInfo> getAllEnableProjects(ProjectInfo projectInfo);

    /**
     * 查询for巡检
     *
     * @return 项目信息列表
     */
    @SelectProvider(type = ProjectSQLProvider.class, method = "selectListForCheck")
    List<ProjectInfo> selectListForCheck();

    /**
     * 新增
     *
     * @param info 项目信息
     * @return 返回结果
     */
    @Insert("insert into tm_project (id,type,name,city,url,user_name,password,remark,mas_type,importance,monitor_type)" +
            " values (UUID(),#{type},#{name},#{city},#{url},#{userName},#{password},#{remark},#{masType},#{importance},#{monitorType})")
    int insert(ProjectInfo info);

    /**
     * 更新
     *
     * @param info 项目信息
     * @return 更新结果
     */
    @Update("update tm_project set city=#{city},type=#{type}, name=#{name}, url=#{url}, user_name=#{userName},monitor_type=#{monitorType}," +
            "password =#{password}, remark=#{remark}, mas_type=#{masType},importance=#{importance} where id = #{id}")
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

    /**
     * 查询
     *
     * @param id 项目id
     * @return 项目信息
     */
    @Select("select * from tm_project where id=#{id}")
    ProjectInfo select(String id);

    /**
     * 更新SMS_STATUS
     *
     * @param id     项目id
     * @param status SMS_STATUS 状态编码
     * @return 更新结果
     */
    @Update("update tm_project set sms_status = #{status} where id = #{id}")
    int updateSmsStatus(String id, String status);

    /**
     * 更新STATUS
     *
     * @param id     项目id
     * @param status STATUS 状态编码
     * @return 更新结果
     */
    @Update("update tm_project set status = #{status} where id = #{id}")
    int updateStatus(String id, String status);

    /**
     * 删除
     *
     * @param id 项目id
     * @return 删除结果
     */
    @Delete("delete from tm_project where id=#{id}")
    int delete(String id);

    /**
     * 导出查询
     *
     * @param info 查询条件
     * @return 列表
     */
    @SelectProvider(type = ProjectSQLProvider.class, method = "selectForExport")
    List<ProjectInfo> selectForExport(ProjectInfo info);
}
