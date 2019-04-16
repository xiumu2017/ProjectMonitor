
package com.paradise.monitor.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.paradise.monitor.MR;
import com.paradise.project.domain.ProjectInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 1. web服务能否登录
 * 2. 获取首页信息
 * 3. 判断发送情况
 *
 * @author Paradise
 */
@Slf4j
public class MonitorForTransitByWeb {

    private static final HttpClient CLIENT = HttpClients.createDefault();
    /**
     * 获取数据地址后缀
     */
    private static final String DATA_SERVICE_SUFFIX = "/smssended/sms.do?action=loadPageData&page=%s&start=%s&limit=%s&queryBeginTime=%s&queryEndTime=%s&sendResult=%s";

    /**
     * Web 服务巡检
     *
     * @param projectInfo 项目信息
     * @return 巡检结果
     */
    public static MR webServerCheck(ProjectInfo projectInfo) {
        HttpGet get = new HttpGet(projectInfo.getUrl());
        HttpResponse response;
        try {
            response = CLIENT.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return MR.success();
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return MR.error(MR.Result_Code.WEB_INACCESSIBLE, "访问Web服务异常：" + e.getLocalizedMessage());
        } finally {
            get.abort();
        }
        return MR.error(MR.Result_Code.WEB_INACCESSIBLE, "访问Web服务异常：" + response.getStatusLine().getStatusCode());
    }

    /**
     * 登录
     *
     * @param project 项目信息
     * @return boolean
     * @author jie_huang@woyitech.com
     */
    public static MR doLogin(ProjectInfo project) {
        HttpPost post = new HttpPost(project.getUrl());
        try {
            // 参数构建
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("userName", project.getUserName()));
            parameters.add(new BasicNameValuePair("password", project.getPassword()));
            parameters.add(new BasicNameValuePair("pwd", project.getPassword()));
            parameters.add(new BasicNameValuePair("button", "登 录"));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
            post.setEntity(formEntity);
            HttpResponse response = MonitorForTransitByWeb.CLIENT.execute(post);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return MR.error(MR.Result_Code.WEB_LOGIN_ERROR, "登录失败：" + response.getStatusLine().getStatusCode() + " > " + getEntityContent(response));
            }
            String result = getEntityContent(response);
            log.info("登陆后的返回结果-----------------------------------------------------------------------------");
            log.info(result);
            //验证登录结果
            String regex = "window.location.href = \"/";
            if (!result.contains(regex)) {
                return MR.error(MR.Result_Code.WEB_LOGIN_ERROR, "登录失败：" + "登录失败,用户名或密码错误");
            }
            return MR.success("Login Success");
        } catch (Exception e) {
            log.error("过境平台巡检出错: " + e.getLocalizedMessage(), e);
            return MR.error(MR.Result_Code.WEB_LOGIN_ERROR, "过境平台巡检失败：" + e.getLocalizedMessage());
        } finally {
            post.abort();
        }
    }

    /**
     * 检查是否有记录
     *
     * @param project 项目信息
     * @return boolean
     * @author jie_huang@woyitech.com
     */
    public static MR checkSmsStatus(ProjectInfo project) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        String url = getSmsDataUrl(project.getUrl(), today);
        log.info("checkSmsStatus - url: " + url);
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = CLIENT.execute(get);
            String result = getEntityContent(response);
            if (result.contains("<!DOCTYPE")) {
                return MR.error(MR.Result_Code.CHECK_ERROR, "web检查记录时出错！");
            } else {

                JSONObject json = JSON.parseObject(result);
                log.info(json.toJSONString());
                long totalCount = json.getLongValue("totalCount");
                if (totalCount == 0) {
                    return MR.error(MR.Result_Code.SMS_NO_SEND, "未检测到短信发送记录");
                }
                return MR.success("当天发送量：" + totalCount);
            }
            // 今日无数据
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return MR.error(MR.Result_Code.CHECK_ERROR, e.getLocalizedMessage());
        }
    }

    /**
     * 获取响应
     *
     * @param response 响应
     * @return String
     * @author jie_huang@woyitech.com
     */
    private static String getEntityContent(HttpResponse response) {
        try {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return "";
    }

    /**
     * 获取短信发送地址
     *
     * @param baseUrl 原始地址
     * @param today   yyyy-MM-dd
     * @return String
     * @author jie_huang@woyitech.com
     */
    private static String getSmsDataUrl(String baseUrl, String today) {
        return baseUrl + String.format(DATA_SERVICE_SUFFIX, "1", "0", 10, today, today, "");
    }

}
