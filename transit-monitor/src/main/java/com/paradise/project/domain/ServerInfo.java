package com.paradise.project.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * tm_server 实体类
 *
 * @author Paradise
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServerInfo extends BaseDomain {

    private String ip;
    private String port;
    private String userName;
    private String password;
    private String status;
    private String name;
    private String remark;
    private String enable;
    private String projectId;

    private String type;
    private String os;
    private String osVersion;
    private String memory;

    public enum OS {
        /**
         * 云服务器
         */
        LINUX_32("1", "LINUX_32"),
        LINUX_64("2", "LINUX_64"),
        /**
         * 物理机
         */
        WINDOWS_32("3", "WINDOWS_32"),
        WINDOWS_64("4", "WINDOWS_64"),
        OTHER_OS("5", "其它"),
        ;
        private String code;
        private String name;

        OS(String code, String name) {
            this.code = code;
            this.name = name;
        }

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


    public enum Type {
        /**
         * 云服务器
         */
        CLOUD_SERVER("1", "云服务器"),
        /**
         * 物理机
         */
        PHYSICAL_SERVER("2", "物理机"),
        OTHER_SERVER("3", "其它"),
        ;
        private String code;
        private String name;

        Type(String code, String name) {
            this.code = code;
            this.name = name;
        }

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


}
