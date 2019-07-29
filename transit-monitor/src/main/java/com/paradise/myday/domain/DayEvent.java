package com.paradise.myday.domain;

import com.paradise.project.domain.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 日常事件实体类
 *
 * @author Paradise
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DayEvent extends BaseDomain {
    private String id;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private String type;
    private String typeName;
    private String events;

}
