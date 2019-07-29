package com.paradise.myday.service;

import com.paradise.myday.domain.DayEvent;
import com.paradise.myday.mapper.DayEventMapper;
import com.paradise.myday.mapper.DayEventTypeMapper;
import com.paradise.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * MyDay Service
 *
 * @author Paradise
 */
@Service
@Slf4j
public class MyDayService {

    @Resource
    private DayEventMapper dayEventMapper;

    @Resource
    private DayEventTypeMapper dayEventTypeMapper;

    /**
     * 分页查询
     *
     * @param dayEvent 查询参数
     * @param pageNo   页码
     * @param pageSize 每页数量
     * @return 分页数据
     */
    public List<DayEvent> getEventPage(DayEvent dayEvent, int pageNo, int pageSize) {
        return new ArrayList<>();
    }

    /**
     * 新增事件
     *
     * @param dayEvent 事件信息
     * @return 新增结果
     */
    public R doAdd(DayEvent dayEvent) {
        if (dayEventMapper.insert(dayEvent) == 1) {
            return R.success();
        } else {
            return R.error();
        }
    }

    /**
     * 查询类型列表
     * @return 事件类型列表
     */
    public R getEventTypeList() {
       return R.success(dayEventTypeMapper.getTypeList());
    }
}
