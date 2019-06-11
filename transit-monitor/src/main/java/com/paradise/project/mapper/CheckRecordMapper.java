package com.paradise.project.mapper;

import com.paradise.project.domain.CheckRecord;
import com.paradise.project.sql.ProjectSQLProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * tm_record Mapper
 *
 * @author Paradise
 */
@Mapper
public interface CheckRecordMapper {

    /**
     * 新增
     *
     * @param record 巡检记录
     * @return 新建结果
     */
    @Insert("insert into tm_record (project_id,result,type,check_code,create_time) values (#{projectId},#{result},#{type},#{checkCode},now())")
    int insert(CheckRecord record);


    /**
     * 查询
     *
     * @param id project_id 项目id
     * @return 巡检记录
     */
    @SelectProvider(type = ProjectSQLProvider.class, method = "selectByProjectId")
    CheckRecord selectByProjectId(String id);

    /**
     * 更新
     *
     * @param record 巡检记录
     * @return 更新结果
     */
    @Update("update tm_record set result=#{result},type=#{type},check_code=#{checkCode},create_time=now() where project_id=#{projectId}")
    int update(CheckRecord record);

    /**
     * 条件查询巡检记录
     *
     * @param record 查询条件
     * @return 巡检记录列表
     */
    @SelectProvider(method = "selectByRecord", type = ProjectSQLProvider.class)
    List<CheckRecord> selectByRecord(CheckRecord record);
}
