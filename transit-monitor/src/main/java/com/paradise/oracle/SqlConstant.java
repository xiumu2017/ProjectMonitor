package com.paradise.oracle;

class SqlConstant {

    /**
     * 查询数据库时间
     */
    static final String QUERY_SYSDATE = "select sysdate from dual";

    /**
     * 查询系统配置表
     */
    static final String QUERY_SYS_CONFIG = "select t.id,\n" +
            "       t.title,\n" +
            "       t.sendenabled sendAble,\n" +
            "       t.pushmessagecontent smsContent,\n" +
            "       t.sendstarttime startHour,\n" +
            "       t.sendendtime endHour,\n" +
            "       t.messagetimeout smsTimeOut,\n" +
            "       t.sendcount,\n" +
            "       t.sendtype,\n" +
            "       t.sendflag,\n" +
            "       t.sendmonth,\n" +
            "       t.sendyear,\n" +
            "       t.smslength,\n" +
            "       t.sendday,\n" +
            "       t.sendtotalamount sendTotalCount from TBL_SMS_CONFIG t";

    /**
     * 查询最近的号码推送时间
     */
    static final String QUERY_LAST_PUSH_TIME = "select *" +
            "  from (select t.apptime from TBL_SMS_MOBILE t order by t.apptime desc)" +
            " where rownum <= 1";
    /**
     * 查询openMas表最近的短信发送时间
     */
    static final String QUERY_LAST_SEND_TIME = "select *" +
            "  from (select t.sendtime from TBL_SMS_SENDED_OPENMAS t order by t.sendtime desc)" +
            " where rownum <= 1";

    static final StringBuilder QUERY_SMS_COUNT = new StringBuilder("select count(*) from TBL_SMS_SENDED_OPENMAS t where ");
}