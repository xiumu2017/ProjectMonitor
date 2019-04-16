package com.paradise.web;

import com.paradise.project.domain.CheckRecord;
import com.paradise.project.service.CheckRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 巡检记录 - web 控制器
 *
 * @author Paradise
 */
@RequestMapping("/record")
@RestController
@Slf4j
public class RecordController {

    private final CheckRecordService checkRecordService;

    public RecordController(CheckRecordService checkRecordService) {
        this.checkRecordService = checkRecordService;
    }

    @RequestMapping("/list")
    public R list(CheckRecord record) {
        List<CheckRecord> recordList = checkRecordService.selectByRecord(record);
        return R.success(recordList);
    }


}
