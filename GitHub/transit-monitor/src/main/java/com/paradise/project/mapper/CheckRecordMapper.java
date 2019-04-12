package com.paradise.project.mapper;

import com.paradise.project.domain.CheckRecord;
import com.paradise.project.sql.ProjectSQLProvider;
import org.apache.ibatis.annotations.*;

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
    @SelectKey(statement = "select uuid()", keyProperty = "id", keyColumn = "id", before = true, resultType = String.class)
    @Insert("insert into tm_record (id,project_id,result,type,check_code,create_time) values (#{id},#{projectId},#{result},#{type},#{checkCode},now())")
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
    @Update("update tm_record set project_id=#{projectId},result=#{result},type=#{type},check_code=#{checkCode},create_time=now()")
    int update(CheckRecord record);
}
