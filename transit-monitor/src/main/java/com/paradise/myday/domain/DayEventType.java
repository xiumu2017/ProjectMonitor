package com.paradise.myday.domain;

import com.paradise.project.domain.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 日常事件类型
 *
 * @author Paradise
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DayEventType extends BaseDomain {
    private String name;
    private String icon;
}
