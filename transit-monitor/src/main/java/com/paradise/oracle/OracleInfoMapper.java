package com.paradise.oracle;

import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Mapper for t_orcl_info
 *
 * @author dzhang
 */
@Mapper
public interface OracleInfoMapper {

    /**
     * 查询全部
     *
     * @return List<OracleInfo> 信息列表
     */
    @Select("select * from t_orcl_info")
    public List<OracleInfo> selectAll();

    /**
     * 根据项目id查询数据库信息
     *
     * @param projectId 项目id
     * @return 数据库信息
     */
    @Select("select * from t_orcl_info where project_id = #{projectId}")
    @Results(id = "baseResultMap", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "url", property = "url"),
            @Result(column = "user", property = "user"),
            @Result(column = "password", property = "password"),
            @Result(column = "ip", property = "ip"),
            @Result(column = "port", property = "port"),
            @Result(column = "project_id", property = "projectId")})
    public OracleInfo selectByProjectId(@Param("projectId") String projectId);


}
