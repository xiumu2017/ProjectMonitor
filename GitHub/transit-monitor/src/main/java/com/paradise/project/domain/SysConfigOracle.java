package com.paradise.project.domain;

import lombok.Data;

/**
 * 过境平台系统配置表信息
 *
 * @author Paradise
 * @date 2019年4月4日
 */
@Data
public class SysConfigOracle {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 开启发送 1
     * 关闭发送 2
     */
    private String sendAble;
    /**
     * 开始时间
     */
    private Integer startHour;
    /**
     * 结束时间
     */
    private Integer endHour;
    /**
     * 超时时间 (分钟)
     */
    private Integer smsTimeOut;
    /**
     * 当天发送量
     */
    private Integer sendCount;
    /**
     * 当月发送量
     */
    private Integer sendTotalCount;
    /**
     * 发送类型
     */
    private Integer sendType;
    /**
     * 限制发送量: 1
     * 不限制 ：2
     */
    private Integer sendFlag;
    /**
     * 日发送量限额
     */
    private Integer sendDay;
    /**
     * 月发送量限额
     */
    private Integer sendMonth;
    /**
     * 年发送量限额
     */
    private Integer sendYear;
    /**
     * 发送内容
     */
    private String smsContent;
    /**
     * 短信长度
     */
    private Integer smsLength;
}
