package com.paradise.project.mapper;

import com.paradise.project.domain.ServerInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.StatementType;

/**
 * tm_server Mapper
 *
 * @author Paradise
 */
@Mapper
public interface ServerInfoMapper {

    /**
     * 新增
     *
     * @param serverInfo 服务器信息
     * @return 新增结果
     */
    @SelectKey(statement = "select uuid()", keyProperty = "id", keyColumn = "id", before = true, resultType = String.class)
    @Insert("insert into tm_server (id,ip,port,user_name,password,status,enable,remark)" +
            " values (#{id},#{ip},#{port},#{userName},#{password},#{status},#{enable},#{remark})")
    @Options(keyColumn = "id", keyProperty = "id", useGeneratedKeys = true)
    int insert(ServerInfo serverInfo);

    /**
     * 更新
     *
     * @param serverInfo 服务器信息
     * @return 更新结果
     */
    @Update("update tm_server set ip=#{ip},port=#{port},user_name=#{userName},password=#{password},remark=#{remark},status=#{status}," +
            "enable=#{enable},os=#{os},memory=#{memory},os_version = #{osVersion},type=#{type}  where id=#{id}")
    int update(ServerInfo serverInfo);

    /**
     * 查询
     *
     * @param id 主键
     * @return 服务器信息
     */
    @Select("select * from tm_server where id = #{id}")
    ServerInfo select(String id);

    /**
     * 更新服务器状态
     *
     * @param id     服务器serverId
     * @param status 目标状态
     * @return 更新结果
     */
    @Update("update tm_server set status = #{status} where id = #{id}")
    int updateStatus(String id, String status);
}
