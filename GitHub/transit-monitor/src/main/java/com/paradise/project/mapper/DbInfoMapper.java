package com.paradise.project.mapper;

import com.paradise.project.domain.DbInfo;
import org.apache.ibatis.annotations.*;

/**
 * tm_db Mapper
 *
 * @author Paradise
 */
@Mapper
public interface DbInfoMapper {

    /**
     * 新增
     *
     * @param dbInfo 实体信息
     * @return 新增结果
     */
    @SelectKey(statement = "select uuid()", keyColumn = "id", keyProperty = "id", before = true, resultType = String.class)
    @Insert("insert into tm_db (id,ip,port,url,user_name,password,status,remark)" +
            " values (#{id},#{ip},#{port},#{url},#{userName},#{password},#{status},#{remark})")
    @Options(keyProperty = "id", keyColumn = "id", useGeneratedKeys = true)
    int insert(DbInfo dbInfo);

    /**
     * 更新
     *
     * @param dbInfo 实体信息
     * @return 更新结果
     */
    @Update("update tm_db set type=#{type},ip=#{ip},port=#{port},url=#{url}," +
            "user_name=#{userName},password=#{password},remark=#{remark},status=#{status}  where id=#{id}")
    int update(DbInfo dbInfo);

    /**
     * 查询
     *
     * @param id 主键
     * @return 数据库信息
     */
    @Select("select * from tm_db where id=#{id}")
    DbInfo selectInfo(String id);
}
