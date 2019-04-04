package com.paradise.transitmonitor.scheduler;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Quartz 任务 Service
 *
 * @author dzhang
 */
@Service
public class MyJobService {
    @Resource
    MyJobMapper myJobMapper;

    public List<QuartzInfo> selectEnableJobList() {
        return myJobMapper.selectEnableJobList();
    }
}
