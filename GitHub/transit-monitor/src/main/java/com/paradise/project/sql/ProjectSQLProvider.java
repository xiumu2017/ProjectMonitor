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

        if (StringUtils.isNotEmpty(info.getType())) {
            sql.append(" and type = '").append(info.getType()).append("'");
        }
        return sql.toString();
    }

    public String selectListForCheck() {
        return "select * from tm_project where 1=1 and type = '1' and enable = '1' and city = '宣城'";
    }

    public String selectByProjectId(String id) {
        return "select * from tm_record where project_id = '" + id + "'";
    }
}
