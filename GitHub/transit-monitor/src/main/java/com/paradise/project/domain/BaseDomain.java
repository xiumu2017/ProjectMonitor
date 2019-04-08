package com.paradise.project.domain;

import lombok.Data;

import java.util.Date;

/**
 * 实体类基类
 *
 * @author Paradise
 */
@Data
public class BaseDomain {
    private String id;
    private String creator;
    private String modifier;
    private Date createTime;
    private Date modifyTime;
}
