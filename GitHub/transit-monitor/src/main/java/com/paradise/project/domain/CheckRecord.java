package com.paradise.project.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 巡检记录
 *
 * @author Paradise
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CheckRecord extends BaseDomain {
    private String id;
    private String projectId;
    private String result;
    private String type;
    private String checkCode;
    private Date createTime;

    public CheckRecord() {

    }

    private CheckRecord(Builder builder) {
        this.projectId = builder.projectId;
        this.result = builder.result;
        this.type = builder.type;
        this.checkCode = builder.checkCode;
    }

    public enum Type {
        /**
         * 服务器问题
         */
        SERVER("1"),
        WEB("2"),
        MAS("3");

        Type(String code) {
            this.code = code;
        }

        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class Builder {
        private String id;
        private String projectId;
        private String result;
        private String type;
        private String checkCode;
        private Date createTime;

        public Builder(String projectId, String result) {
            this.projectId = projectId;
            this.result = result;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder checkCode(String checkCode) {
            this.checkCode = checkCode;
            return this;
        }

        public CheckRecord build() {
            return new CheckRecord(this);
        }
    }
}
