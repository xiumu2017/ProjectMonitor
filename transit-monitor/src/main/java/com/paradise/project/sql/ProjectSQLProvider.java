package com.paradise.project.sql;

import com.paradise.project.domain.CheckRecord;
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

        if (StringUtils.isNotEmpty(info.getEnable())) {
            sql.append(" and enable = '").append(info.getEnable()).append("'");
        }

        if (StringUtils.isNotEmpty(info.getStatus())) {
            sql.append(" and status = '").append(info.getStatus()).append("'");
        }
        if (info.isHiddenNoCheck()) {
            sql.append(" and monitor_type != '").append("NO'");
        }
        sql.append(" order by type,city");
        return sql.toString();
    }


    public String selectForExport(ProjectInfo info) {
        StringBuilder sql = new StringBuilder("select tm.*,ts.os serverOs,ts.status serverStatus from tm_project tm left join tm_server ts on ts.id = tm.server_id where 1=1 ");
        if (StringUtils.isNotEmpty(info.getCity())) {
            sql.append(" and tm.city = '").append(info.getCity()).append("'");
        }

        if (StringUtils.isNotEmpty(info.getName())) {
            sql.append(" and tm.name like '%").append(info.getName()).append("%'");
        }

        if (StringUtils.isNotEmpty(info.getType())) {
            sql.append(" and tm.type = '").append(info.getType()).append("'");
        }

        if (StringUtils.isNotEmpty(info.getEnable())) {
            sql.append(" and tm.enable = '").append(info.getEnable()).append("'");
        }

        if (StringUtils.isNotEmpty(info.getStatus())) {
            sql.append(" and tm.status = '").append(info.getStatus()).append("'");
        }
        if (info.isHiddenNoCheck()) {
            sql.append(" and tm.monitor_type != '").append("NO'");
        }
        sql.append(" order by type,city");
        return sql.toString();
    }

    public String selectListForCheck() {
        return "select * from tm_project where 1=1 and type = '1' and enable = '1' and monitor_type != 'NO'";
    }

    public String selectByProjectId(String id) {
        return "select * from tm_record where project_id = '" + id + "'";
    }

    public String selectByRecord(CheckRecord record) {
        StringBuilder sql = new StringBuilder("SELECT\n" +
                "\ttr.*, tp.`name` AS projectName\n" +
                "FROM\n" +
                "\ttm_record tr\n" +
                "LEFT JOIN tm_project tp ON tr.project_id = tp.id\n" +
                "WHERE\n" +
                "\ttr.check_code NOT IN ('-1', '0')");
        if (StringUtils.isNotEmpty(record.getCheckCode())) {
            sql.append(" AND tr.check_code = '").append(record.getCheckCode()).append("'");
        }
        return sql.toString();
    }
}
