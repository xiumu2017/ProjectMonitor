package com.paradise.myday.mapper;

import com.paradise.myday.domain.DayEvent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectKey;

/**
 * 日常事件 mapper md_event
 *
 * @author Paradise
 */
@Mapper
public interface DayEventMapper {

    /**
     * 新增
     *
     * @param dayEvent 事件
     * @return 结果
     */
    @SelectKey(statement = "select uuid()", keyColumn = "id", keyProperty = "id", before = true, resultType = String.class)
    @Insert("insert into md_event (id,start_time,end_time,type,events)" +
            " values (#{id},#{startTime},#{endTime},#{type},#{events})")
    @Options(keyProperty = "id", keyColumn = "id", useGeneratedKeys = true)
    int insert(DayEvent dayEvent);
}
