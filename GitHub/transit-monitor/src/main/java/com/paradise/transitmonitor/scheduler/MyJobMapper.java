package com.paradise.transitmonitor.scheduler;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * Mapper for t_job
 * quartz 任务Mapper
 *
 * @author dzhang
 */
@Mapper
public interface MyJobMapper {

    /**
     * 查询启用的任务列表
     *
     * @return List<QuartzInfo> 任务列表
     */
    @Select("select * from t_job where status = 1")
    @Results(id = "resultMap", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "identity_name", property = "identityName"),
            @Result(column = "identity_group", property = "identityGroup"),
            @Result(column = "description", property = "description"),
            @Result(column = "cron", property = "cron"),
            @Result(column = "job_class_name", property = "jobClassName"),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)})
    public List<QuartzInfo> selectEnableJobList();

}
