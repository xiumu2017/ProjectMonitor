package com.paradise.project.sql;

import com.paradise.project.domain.ProjectInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Paradise
 */
public class ProjectSQLProvider {

    public String selectByPage(ProjectInfo info) {
        StringBuilder sql = new StringBuilder("select * from tm_project where 1=1 ");
        if (StringUtils.isNotEmpty(info.getCity())) {
            sql.append(" and city = '").append(info.getCity()).append("'");
        }

        if (StringUtils.isNotEmpty(info.getName())) {
            sql.append(" and name like '%").append(info.getName()).append("%'");
        }
        return sql.toString();
    }
}
