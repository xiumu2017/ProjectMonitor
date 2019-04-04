package com.paradise.transitmonitor.scheduler;

import lombok.Data;

import java.util.Date;

/**
 * @author dzhang
 */
@Data
public class QuartzInfo {

    private String id;
    private String jobClassName;
    private String identityGroup;
    private String identityName;
    private String description;
    private String cron;
    private String parameter;
    private Date createTime;
    private Date updateTime;
//    private Class<? extends Job> classLoader;

    @Override
    public String toString() {
        return "QuartzInfo{" +
                "id='" + id + '\'' +
                ", jobClassName='" + jobClassName + '\'' +
                ", identityGroup='" + identityGroup + '\'' +
                ", identityName='" + identityName + '\'' +
                ", description='" + description + '\'' +
                ", cron='" + cron + '\'' +
                '}';
    }
}
