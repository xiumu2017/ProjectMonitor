package com.paradise.project.service;

import com.paradise.project.domain.CheckRecord;
import com.paradise.project.mapper.CheckRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 巡检记录service
 *
 * @author Paradise
 * @date 2019-4-12 14:25:47
 */
@Service
@Slf4j
public class CheckRecordService {

    @Resource
    private CheckRecordMapper recordMapper;

    public int insert(CheckRecord record) {
        if (record != null) {
            if (recordMapper.selectByProjectId(record.getProjectId()) != null) {
                return recordMapper.update(record);
            }
            return recordMapper.insert(record);
        }
        return 0;
    }

    public CheckRecord selectByProjectId(String id) {
        return recordMapper.selectByProjectId(id);
    }
}
