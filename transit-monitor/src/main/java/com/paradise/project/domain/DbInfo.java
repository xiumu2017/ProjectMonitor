package com.paradise.project.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * tm_db 实体类
 *
 * @author Paradise
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DbInfo extends BaseDomain {
    private String type;
    private String ip;
    private String port;
    private String url;
    private String userName;
    private String password;
    private String status;
    private String remark;

    private String projectId;

    public enum DB_TYPE {
        ;
        private static final String MYSQL = "MYSQL";
        private static final String ORACLE = "ORACLE";
    }
}
