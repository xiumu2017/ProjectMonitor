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

    /**
     * @param dbInfo
     */
    public static Map<String, Object> transitCheck(DbInfo dbInfo) {
        Map<String, Object> resultMap = new HashMap<>(16);
        try {
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
            // 查询当天的短信发送情况
            // 发送成功，发送失败，提交成功，提交失败，超出月发送量，超出日发送量
            Calendar calendar = Calendar.getInstance();
            ResultSet smsSet = statement.executeQuery(SqlConstant.QUERY_SMS_COUNT
                    .append("  t.appyear = '").append(calendar.get(Calendar.YEAR)).append("' and t.appmonth = '")
                    .append(calendar.get(Calendar.MONTH)).append("' and t.appday = '")
                    .append(calendar.get(Calendar.DATE)).append("'").toString());
            int sendSuccessCount = 0;
            int sendFailCount = 0;
            int submitSuccessCount = 0;
            int submitFailCount = 0;
            int overMonthCount = 0;
            int overDayCount = 0;
            while (smsSet.next()) {
                int sendResult = smsSet.getInt(1);
                switch (sendResult) {
                    case 0:
                        submitFailCount++;
                        break;
                    case 1:
                        submitSuccessCount++;
                        break;
                    case 2:
                        sendSuccessCount++;
                        break;
                    case 3:
                        sendFailCount++;
                        break;
                    case 5:
                        overMonthCount++;
                        break;
                    case 7:
                        overDayCount++;
                    default:
                        break;
                }
            }
            resultMap.put("sendSuccessCount", sendSuccessCount);
            resultMap.put("sendFailCount", sendFailCount);
            resultMap.put("submitFailCount", submitFailCount);
            resultMap.put("submitSuccessCount", submitSuccessCount);
            resultMap.put("overDayCount", overDayCount);
            resultMap.put("overMonthCount", overMonthCount);
            // step4: 查询最近的短信发送记录
            String lastSendTime = null;
            ResultSet lastSendSet = statement.executeQuery(SqlConstant.QUERY_LAST_SEND_TIME);
            while (lastSendSet.next()) {
                lastSendTime = lastSendSet.getString(1);
            }
            resultMap.put("lastSendTime", lastSendTime);

            // step5: 查询首页变更记录

            // 判断是否开启发送 -- 一下判断改为前端，调用方自行处理
            if (config.getSendAble().equals(ProjectConstant.SMS_SEND_ENABLE)) {
                // 判断是否在发送区间
                int hour = Calendar.getInstance().get(Calendar.HOUR);
                if (config.getStartHour() <= hour && hour < config.getEndHour()) {
                    if (config.getSendFlag().equals(ProjectConstant.SMS_LIMIT_COUNT_TRUE)) {
                        // 判断发送量情况
                        // 1. 是否超出月发送量
                        if (config.getSendTotalCount() > config.getSendMonth()) {

                        }

                        // 2. 是否超出日发送量
                        if (config.getSendCount() > config.getSendDay()) {

                        }
                    }

                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            log.error(e.getLocalizedMessage(), e);
        }
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

    private static SysConfigOracle getConfigFromSql(ResultSet resultSet) throws SQLException {
        SysConfigOracle configOracle = null;
        try {
            configOracle = (SysConfigOracle) getObjectFromResultSet(resultSet, SysConfigOracle.class);
            log.info(configOracle.toString());
        } catch (IllegalAccessException | InstantiationException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return configOracle;
    }

    public static void main(String[] args) {
//        QueryForTransit.transitCheck(new DbInfo("jdbc:oracle:thin:@192.168.1.234:1521:orcl", "gjptqt", "gjptqt"));
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
                        boolean isAccessible = field.isAccessible();
                        field.setAccessible(true);
                        Object value = resultSet.getObject(i);
                        log.info(value.getClass().getName());
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
