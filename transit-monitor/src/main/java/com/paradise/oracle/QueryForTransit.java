package com.paradise.oracle;

import com.paradise.monitor.MR;
import com.paradise.project.ProjectConstant;
import com.paradise.project.domain.DbInfo;
import com.paradise.project.domain.SysConfigOracle;
import com.paradise.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static String checkAccessible(DbInfo dbInfo) throws SQLException, ClassNotFoundException {
        String res = null;
        if (null == dbInfo) {
            return null;
        }
        Connection connection = getConnection(dbInfo);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SqlConstant.QUERY_SYSDATE);
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

    public static MR dbCheckForTransit(DbInfo dbInfo) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 建立数据库连接
        Connection connection = getConnection(dbInfo);
        Statement statement = connection.createStatement();
        Timestamp oracleSysDate = null;
        ResultSet resultSet = statement.executeQuery(SqlConstant.QUERY_SYSDATE);
        while (resultSet.next()) {
            oracleSysDate = resultSet.getTimestamp(1);
            log.info("oracleSysDate: " + oracleSysDate);
        }
        // step1: 查询系统配置表信息
        SysConfigOracle config = getConfigFromSql(statement);
        // step2: 查询最近的短信推送情况
        Timestamp lastPushTime = null;
        ResultSet resSet = statement.executeQuery(SqlConstant.QUERY_LAST_PUSH_TIME);
        while (resSet.next()) {
            lastPushTime = resSet.getTimestamp(1);
            log.info("lastPushTime: " + lastPushTime);
        }
        if (!DateUtils.dateCompare(lastPushTime, oracleSysDate, 20)) {
            return MR.error(MR.Result_Code.SMS_PUSH_ERROR, "超过20min没有短信推送，上次短信推送时间：" + lastPushTime);
        }
        // 开启发送
        if ("1".equals(config.getSendAble())) {
            Calendar calendar = Calendar.getInstance();
            // 已到发送时间
            if (config.getStartHour() != null && calendar.get(Calendar.HOUR) > config.getStartHour()) {
                Timestamp lastSendTime = null;
                ResultSet lastSendSet = statement.executeQuery(SqlConstant.QUERY_LAST_SEND_TIME);
                while (lastSendSet.next()) {
                    lastSendTime = lastSendSet.getTimestamp(1);
                    log.info("lastSendTime: " + lastSendTime);
                }
                if (!DateUtils.dateCompare(lastSendTime, oracleSysDate, 30)) {
                    return MR.error(MR.Result_Code.SMS_NO_SEND, "超过30m无短信发送，上次短信发送时间： " + lastSendTime);
                }
            }
        }
        return MR.success();
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
        SysConfigOracle config = getConfigFromSql(statement);
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

    /**
     * 查询系统配置表信息
     *
     * @param statement statement
     * @return 系统配置信息
     * @throws SQLException           sql-e
     * @throws InstantiationException eee
     * @throws IllegalAccessException eee
     */
    private static SysConfigOracle getConfigFromSql(Statement statement) throws SQLException, InstantiationException, IllegalAccessException {
        ResultSet sysConfigSet = statement.executeQuery(SqlConstant.QUERY_SYS_CONFIG);
        SysConfigOracle configOracle;
        configOracle = (SysConfigOracle) getObjectFromResultSet(sysConfigSet, SysConfigOracle.class);
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
