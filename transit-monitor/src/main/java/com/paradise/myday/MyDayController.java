package com.paradise.myday;


import com.paradise.myday.domain.DayEvent;
import com.paradise.myday.service.MyDayService;
import com.paradise.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MyDay 核心控制器 - CRUD
 *
 * @author Paradise
 */
@Slf4j
@RestController
@RequestMapping("/myday")
@CrossOrigin
public class MyDayController {
    private final MyDayService myDayService;

    @Autowired
    public MyDayController(MyDayService myDayService) {
        this.myDayService = myDayService;
    }

    /**
     * 查询类型列表
     *
     * @return 类型列表
     */
    @RequestMapping("/getTypeList")
    public R getTypeList() {
        return myDayService.getEventTypeList();
    }

    /**
     * 新增接口
     *
     * @param dayEvent 事件信息
     * @return 返回结果
     */
    @RequestMapping("/doAdd")
    public R doAdd(DayEvent dayEvent) {
        log.info(dayEvent.toString());
        return myDayService.doAdd(dayEvent);
    }

    /**
     * 自定义时间格式处理
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //转换日期
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // CustomDateEditor为自定义日期编辑器
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
