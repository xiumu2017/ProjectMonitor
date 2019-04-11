package com.paradise.oracle;

import com.paradise.project.ProjectConstant;
import com.paradise.project.domain.DbInfo;
import com.paradise.project.domain.SysConfigOracle;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 过境前台Oracle查询方法
 *
 * @author dzhang
 */
@Slf4j
public class QueryForTransit {


    private static Connection getConnection(DbInfo dbInfo) throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(dbInfo.getUrl(), dbInfo.getUserName(), dbInfo.getPassword());
    }


    /**
     * 查询过境前台数据库最近一条号码数据的时间 appTime
     *
     * @param dbInfo 数据库信息
     * @return String appTime
     */
    public static String queryLastPushTime(DbInfo dbInfo) throws SQLException, ClassNotFoundException {
        String res = null;
        if (null == dbInfo) {
            return null;
        }
        Connection connection = getConnection(dbInfo);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SqlConstant.QUERY_LAST_PUSH_TIME);
        while (resultSet.next()) {
            res = resultSet.getString(1);
        }
        return res;
    }

    private static String sqlMaker(int result) {
        StringBuilder sql = new StringBuilder(SqlConstant.QUERY_SMS_COUNT);
        Calendar calendar = Calendar.getInstance();
        sql.append("  t.appyear = '").append(calendar.get(Calendar.YEAR))
                .append("' and t.appmonth = '").append(calendar.get(Calendar.MONTH) + 1)
                .append("' and t.appday = '").append(calendar.get(Calendar.DATE))
                .append("' and t.sendresult = '").append(result).append("'");
        log.info(sql.toString());
        return sql.toString();
    }

    private static String getCount(Statement statement, int res) throws SQLException {
        int count = 0;
        ResultSet set = statement.executeQuery(sqlMaker(res));
        while (set.next()) {
            count = set.getInt(1);
        }
        return String.valueOf(count);
    }

    /**
     * 当天发送情况统计 - 性能太差废弃了，循环执行很慢
     *
     * @param resultMap 结果集Map
     * @param statement statement
     * @throws SQLException sqlError
     */
    private static void sendCountDeal(Map<String, Object> resultMap, Statement statement) throws SQLException {
        // 查询当天的短信发送情况
        // 发送成功，发送失败，提交成功，提交失败，超出月发送量，超出日发送量
        log.info(">>> start count query... ");
        resultMap.put("sendSuccessCount", getCount(statement, 2));
        resultMap.put("sendFailCount", getCount(statement, 3));
        resultMap.put("submitFailCount", getCount(statement, 0));
        resultMap.put("submitSuccessCount", getCount(statement, 1));
        resultMap.put("overDayCount", getCount(statement, 7));
        resultMap.put("overMonthCount", getCount(statement, 5));
    }

    /**
     * 过境oracle 巡检核心方法
     *
     * @param dbInfo oracle success
     */
    public static Map<String, Object> transitCheck(DbInfo dbInfo) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Map<String, Object> resultMap = new HashMap<>(16);
        // 建立数据库连接
        Connection connection = getConnection(dbInfo);
        Statement statement = connection.createStatement();
        // step1: 查询系统配置表信息
        ResultSet sysConfigSet = statement.executeQuery(SqlConstant.QUERY_SYS_CONFIG);
        SysConfigOracle config = getConfigFromSql(sysConfigSet);
        resultMap.put("config", config);
        // step2: 查询最近的短信推送情况
        String lastPushTime = null;
        ResultSet resultSet = statement.executeQuery(SqlConstant.QUERY_LAST_PUSH_TIME);
        while (resultSet.next()) {
            lastPushTime = resultSet.getString(1);
        }
        resultMap.put("lastPushTime", lastPushTime);
        // step3: 查询当天的短信发送情况
        sendCountDeal(resultMap, statement);
        // step4: 查询最近的短信发送记录
        String lastSendTime = null;
        ResultSet lastSendSet = statement.executeQuery(SqlConstant.QUERY_LAST_SEND_TIME);
        while (lastSendSet.next()) {
            lastSendTime = lastSendSet.getString(1);
        }
        resultMap.put("lastSendTime", lastSendTime);
        // step5: 查询首页变更记录

        connection.close();
        log.info("query complete...");
        return resultMap;
    }

    public static String queryCount(DbInfo dbInfo) {
        String res = null;
        try {
            Connection connection = getConnection(dbInfo);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SqlConstant.QUERY_COUNT_MOBILE);
            while (resultSet.next()) {
                res = resultSet.getString(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage(), e);
        }
        return res;
    }

    private static SysConfigOracle getConfigFromSql(ResultSet resultSet) throws SQLException, InstantiationException, IllegalAccessException {
        SysConfigOracle configOracle = null;
        configOracle = (SysConfigOracle) getObjectFromResultSet(resultSet, SysConfigOracle.class);
        log.info(configOracle.toString());
        return configOracle;
    }

    private static Object getObjectFromResultSet(ResultSet resultSet, Class clazz) throws SQLException, IllegalAccessException, InstantiationException {
        // 结果集的元素对象 诸如列名称之类
        ResultSetMetaData metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();
        // 获取对象的属性数组
        Field[] fields = clazz.getDeclaredFields();
        Object obj = null;
        // 结果集的遍历
        while (resultSet.next()) {
            // 通过反射构建对象
            obj = clazz.newInstance();
            // 遍历结果行的每一列
            for (int i = 1; i <= count; i++) {
                for (Field field : fields) {
                    // TODO 增加驼峰法等特殊格式的处理
                    if (field.getName().equalsIgnoreCase(metaData.getColumnName(i))) {
                        field.setAccessible(true);
                        if ("java.lang.String".equals(field.getType().getName())) {
                            field.set(obj, resultSet.getString(i));
                        } else if ("java.lang.Integer".equals(field.getType().getName())) {
                            field.set(obj, resultSet.getInt(i));
                        }
                        field.setAccessible(false);
                    }
                }
            }
        }
        return obj;
    }
}
