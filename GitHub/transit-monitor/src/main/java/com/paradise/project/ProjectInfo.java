package com.paradise.project;

import lombok.Data;

/**
 * 项目信息实体
 *
 * @author Paradise
 */
@Data
public class ProjectInfo {
    private String id;
    private String name;
    private String status;
    private String dbId;
    private String serverId;
    /**
     * level
     * 0: high
     * 1: common
     * 2: low
     */
    private Integer level;
}
