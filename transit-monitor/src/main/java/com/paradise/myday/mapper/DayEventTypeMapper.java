package com.paradise.myday.mapper;

import com.paradise.myday.domain.DayEventType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 日常事件类型 mapper md_event_type
 *
 * @author Paradise
 */
@Mapper
public interface DayEventTypeMapper {

    /**
     * 查询类型列表
     *
     * @return 类型列表
     */
    @Select("select * from md_event_type")
    List<DayEventType> getTypeList();
}
