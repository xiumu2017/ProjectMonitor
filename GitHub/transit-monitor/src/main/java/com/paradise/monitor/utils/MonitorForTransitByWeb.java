
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

    /**
     * 超时时长
     */
    private static final int DEFAULT_TIMEOUT = 15000;
    private static final int DEFAULT_LIMIT_SIZE = 10;

    private static final HttpClient client = HttpClients.createDefault();
    /**
     * 获取数据地址后缀
     */
    private static final String DATA_SERVICE_SUFFIX = "/smssended/sms.do?action=loadPageData&page=%s&start=%s&limit=%s&queryBeginTime=%s&queryEndTime=%s&sendResult=%s";

    /**
     * 外部调用检查方法
     */
    public static void check(ProjectInfo projectInfo) {
        long start = System.currentTimeMillis();
        //检查服务是否正常
        if (!webServerCheck(projectInfo).getCode().equals(MR.Result_Code.NORMAL)) {
            return;
        }
        //登录 获取首页信息
        if (doLogin(projectInfo).getCode().equals(MR.Result_Code.NORMAL)) {
            return;
        }
        //切换服务地址
        String serverUrl = projectInfo.getUrl();
        serverUrl = serverUrl.substring(0, serverUrl.lastIndexOf("/"));
        projectInfo.setUrl(serverUrl);
        //检查当天记录
        checkSmsStatus(projectInfo);
    }

    public static MR webServerCheck(ProjectInfo projectInfo) {
        HttpGet get = new HttpGet(projectInfo.getUrl());
        HttpResponse response = null;
        try {
            response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return MR.success();
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return MR.error(MR.Result_Code.INACCESSIBLE, "登录服务异常：" + e.getLocalizedMessage());
        } finally {
            get.abort();
        }
        return MR.error(MR.Result_Code.INACCESSIBLE, "登录服务异常：" + response.getStatusLine().getStatusCode());
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
            HttpResponse response = MonitorForTransitByWeb.client.execute(post);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return MR.error(MR.Result_Code.LOGIN_ERROR, "登录失败：" + response.getStatusLine().getStatusCode() + " > " + getEntityContent(response));
            }
            String result = getEntityContent(response);
            //验证登录结果
            if (!result.contains("window.location.href = \"/")) {
                return MR.error(MR.Result_Code.LOGIN_ERROR, "登录失败：" + "登录失败,用户名或密码错误");
            }
            return MR.success("Login Success");
        } catch (Exception e) {
            log.error("过境平台巡检出错: " + e.getLocalizedMessage(), e);
            return MR.error(MR.Result_Code.LOGIN_ERROR, "过境平台巡检失败：" + e.getLocalizedMessage());
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
        // TODO 首先获取首页信息，判断是否开启发送以及发送量
        HttpGet get = new HttpGet(getSmsDataUrl(project.getUrl(), today, 1, ""));
        try {
            HttpResponse response = client.execute(get);
            String result = getEntityContent(response);
            JSONObject json = JSON.parseObject(result);
            log.info(json.toJSONString());
            long totalCount = json.getLongValue("totalCount");
            // 今日无数据
            if (totalCount == 0) {
                return MR.error(MR.Result_Code.NO_SMS_SEND, "未检测到短信发送记录");
            }
            return MR.success("当天发送量：" + totalCount);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return MR.error(MR.Result_Code.ERROR, e.getLocalizedMessage());
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
     * @param pageNo  页码
     * @param type    sendResult
     * @return String
     * @author jie_huang@woyitech.com
     */
    private static String getSmsDataUrl(String baseUrl, String today, Integer pageNo, String type) {
        if (pageNo == null) {
            pageNo = 1;
        }
        return baseUrl + String.format(DATA_SERVICE_SUFFIX, String.valueOf(pageNo), String.valueOf((pageNo - 1) * 10), 10, today, today, type);
    }

}
