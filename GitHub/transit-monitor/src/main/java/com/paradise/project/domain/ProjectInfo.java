package com.paradise.project.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 项目信息实体
 *
 * @author Paradise
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ProjectInfo extends BaseDomain {

    private String id;
    private String name;
    private String province;
    private String city;
    private String url;
    private String userName;
    private String password;
    private String smsStatus;
    private String serverId;
    private String masType;
    private String masId;
    private String dbId;
    private String monitorType;
    private String enable;
    private String status;
    private String remark;

    /**
     * Mas类型 - 枚举
     */
    public enum MAS_TYPE {
        /**
         *
         */
        OA("1", "移动OA-老版"),
        OA_WEBSERVICE("2", "移动OA-WebService版"),
        MAS("3", "若雅MAS"),
        OPEN_MAS("4", "OpenMas"),
        OTHER("5", "其它"),
        ;

        MAS_TYPE(String code, String name) {
            this.code = code;
            this.name = name;
        }

        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 短信发送状态 - 枚举
     */
    public enum SMS_STATUS {
        /**
         *
         */
        NORMAL("1", "正常发送"),
        ABNORMAL("2", "异常"),
        OVER_LIMIT("3", "超出限额"),
        UNABLE_SEND("4", "无法发送");

        SMS_STATUS(String code, String name) {
            this.code = code;
            this.name = name;
        }

        @Setter
        @Getter
        private String code;
        @Setter
        @Getter
        private String name;

        //短信发送状态（1：正常发送 2：异常 3：超出限额 4：欠费或者业务暂停）
    }

    /**
     * 监控巡检方式
     * 1. 数据库直连
     * 2. web接口调用
     */
    public enum MONITOR_TYPE {
        ;
        private static final String DB_ = "DB";
        private static final String WEB_ = "WEB";
    }

    /**
     * 项目是否启用
     */
    public enum PROJECT_ENABLE {
        ;
        public static final String ENABLE = "1";
        public static final String DISABLE = "0";
    }

    /**
     * level
     * 1: low
     * 2: common
     * 3: high
     */
    private Integer importance;
}
