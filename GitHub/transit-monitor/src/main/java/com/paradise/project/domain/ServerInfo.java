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

    private String projectId;


}
