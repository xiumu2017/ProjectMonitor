package com.paradise.monitor.utils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

/**
 * 钉钉机器人工具类
 * 
 * @ClassName DingdingRobotUtils
 * @author zhangshun
 * @date 2017年8月15日 下午3:09:14
 */
public abstract class DingdingRobotUtils {
    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(DingdingRobotUtils.class);
    /**
     * restTemplate
     */
    private static RestTemplate restTemplate;
    /**
     * httpHeaders
     */
    private static HttpHeaders httpHeaders;
    /**
     * 钉钉机器人URL
     */
    private static String dingdingRobotUrl = "https://oapi.dingtalk.com/robot/send?access_token=";
    
    /**
     * markdown
     */
    private static String markdownBody = "{\"msgtype\":\"markdown\",\"markdown\":{\"title\":\"%s\",\"text\":\"%s\"}}";
    /**
     * 静态代码块
     */
    static {
	// 初始化restTemplate
	StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(
		Charset.forName("UTF-8"));
	List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>(1);
	messageConverters.add(stringHttpMessageConverter);
	restTemplate = new RestTemplate(messageConverters);
	// 初始化httpHeaders
	httpHeaders = new HttpHeaders();
	httpHeaders.add("Content-Type", "application/json;charset=utf-8");
    }

    /**
     * 调用钉钉机器人
     * 
     * @Title revokeApi
     * @author llwu
     * @param content
     * @param token
     */
    public static void revokeApi(String content, @NotBlank String token) {
	revokeApi(dingdingRobotUrl + token, content, true, null);
    }

    /**
     * 调用钉钉机器人
     * 
     * @Title revokeApi
     * @author llwu
     * @param content
     * @param isAtAll
     * @param token
     */
    public static void revokeApi(String content, boolean isAtAll, @NotBlank String token) {
	revokeApi(dingdingRobotUrl + token, content, isAtAll, null);
    }

    /**
     * 调用钉钉API接口
     * 
     * @Title revokeApi
     * @author zhangshun
     * @param revokeURL
     *            调用URL
     * @param content
     *            内容
     * @param isAtAll
     *            是否at所有人
     * @param atMobiles
     *            需要具体at的用户手机号码
     */
    public static void revokeApi(String revokeURL, String content, boolean isAtAll, String[] atMobiles) {
	try {
	    Assert.isTrue(StringUtils.isNotBlank(revokeURL), "钉钉机器人调用URL不能为空!");
	    Assert.isTrue(StringUtils.isNotBlank(content), "消息内容不能为空");
	    TextMsg textMsg = new TextMsg();
	    textMsg.setContent(content);
	    textMsg.setAtMobiles(atMobiles);
	    textMsg.setIsAtAll(isAtAll);
	    HttpEntity<String> httpEntity = new HttpEntity<String>(JSON.toJSONString(textMsg), httpHeaders);
	    restTemplate.postForEntity(revokeURL, httpEntity, String.class);
	} catch (Exception e) {
	    logger.error("调用钉钉机器人接口出现异常：{}", e);
	}
    }

    /**
     * 发送Markdown
     * @Title  revokeMarkdownApi 
     * @author  jie_huang@woyitech.com
     * @param token
     * @param title
     * @param text
     * @return  void
     */
    public static void revokeMarkdownApi(String token, String title, String text) {
	try {
	    Assert.isTrue(StringUtils.isNotBlank(token), "钉钉机器人token不能为空!");
	    Assert.isTrue(StringUtils.isNotBlank(title), "标题不能为空");
	    Assert.isTrue(StringUtils.isNotBlank(text), "消息内容不能为空");
	    HttpEntity<String> httpEntity = new HttpEntity<String>(String.format(markdownBody, title, text), 
		    httpHeaders);
	    restTemplate.postForEntity(dingdingRobotUrl + token, httpEntity, String.class);
	} catch (Exception e) {
	    logger.error("调用钉钉机器人接口出现异常：{}", e);
	}
    }
    
    /**
     * 文本消息
     * <p>
     * 示例：{"msgtype": "text","text": {"content": "我就是我, 是不一样的烟火"},"at":
     * {"isAtAll": false}}
     * 
     * @ClassName TextMsg
     * @author zhangshun
     * @date 2017年8月15日 下午3:26:15
     */
    public static class TextMsg {
	/**
	 * 消息类型：此消息类型为固定为text
	 */
	private String msgtype = "text";
	/**
	 * text
	 */
	private Map<String, String> text = new HashMap<String, String>();
	/**
	 * at
	 * <p>
	 * 如果是at所有人，则填写的手机号码无效
	 */
	private Map<String, Object> at = new HashMap<String, Object>();

	/**
	 * 设置消息内容
	 * 
	 * @Title setContent
	 * @author zhangshun
	 * @param content
	 *            消息内容
	 */
	public void setContent(String content) {
	    text.put("content", content);
	}

	/**
	 * 设置是否at所有人
	 * 
	 * @Title setIsAtAll
	 * @author zhangshun
	 * @param isAtAll
	 */
	public void setIsAtAll(boolean isAtAll) {
	    at.put("isAtAll", isAtAll);
	}

	/**
	 * 设置被@人的手机号(在text内容里要有@手机号)
	 * 
	 * @Title setAtMobiles
	 * @author zhangshun
	 * @param atMobiles
	 */
	public void setAtMobiles(String[] atMobiles) {
	    at.put("atMobiles", atMobiles);
	}

	/**
	 * return the msgtype
	 * 
	 * @return the msgtype
	 */
	public String getMsgtype() {
	    return msgtype;
	}

	/**
	 * return the text
	 * 
	 * @return the text
	 */
	public Map<String, String> getText() {
	    return text;
	}

	/**
	 * return the at
	 * 
	 * @return the at
	 */
	public Map<String, Object> getAt() {
	    return at;
	}
    }
    
    
}
