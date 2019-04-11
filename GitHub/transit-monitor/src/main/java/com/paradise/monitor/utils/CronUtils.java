/**   
 * Copyright © 2017 Hefei Wo Yi Information Technology Co., Ltd. All rights reserved.
 * 
 * @Title  CronUtils.java 
 * @Prject  mhub-xunjian
 * @Package  com.woyi.mhub.expand.xunjian.util 
 * @author  jie_huang@woyitech.com
 * @date  2017年12月1日 上午11:44:51 
 * @version  V2.0   
 */
package com.paradise.monitor.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;

/** 
 * Cron表达式工具
 * @ClassName  CronUtils 
 * @author  jie_huang@woyitech.com
 * @date  2017年12月1日 上午11:44:51 
 */
public class CronUtils {

    public static void main(String[] args) {
	System.out.println((int)Math.ceil(11/(float)5));
	
	//System.out.println(nextDate("0 0 * * * ? *", null));
    }
    /**
     * 获取周期时间点
     * @Title  getDate 
     * @author  llwu
     * @return  List<Date>
     */
    public static List<String> getDate(String cron) {
	CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
	List<String> list = new ArrayList<String>();
	try {
	    cronTriggerImpl.setCronExpression(cron);
	    // 这里写要准备猜测的cron表达式
	    Calendar calendar = Calendar.getInstance();
	    Date now = calendar.getTime();
	    calendar.add(Calendar.DAY_OF_WEEK, 2);// 统计的区间段设置,根据业务调整为2天
	    List<Date> dates = TriggerUtils.computeFireTimesBetween(cronTriggerImpl, null, now, calendar.getTime());// 这个是重点，一行代码搞定~~
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    for (int i = 0; i < dates.size(); i++) {
		if (i > 2) {// 这个是提示的日期个数
		    break;
		}
		list.add(dateFormat.format(dates.get(i)));
	    }
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return list;
    }
    
    /**
     * 计算下次执行时间
     * @Title  nextDate
     * @author  jie_huang@woyitech.com
     * @param expression
     * @param lastdate
     * @return  Date
     */
    public static Date nextDate(String cronExpression, Date lastdate){
	try {
	    CronExpression cron = new CronExpression(cronExpression);
	    if(lastdate == null){
		lastdate = new Date();
	    }
	    //此处注意, 这里有巧合情况(1分钟内)
	    return cron.getNextValidTimeAfter(lastdate);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }
    
    /**
     * 验证cron有效性
     * @Title  isValidExpression 
     * @author  jie_huang@woyitech.com
     * @param cronexpression
     * @return  boolean
     */
    public static boolean isValidExpression(String cronExpression){
	return CronExpression.isValidExpression(cronExpression);
    }

}
