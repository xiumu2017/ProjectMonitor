package com.paradise.dingding.chatbot;

import com.alibaba.fastjson.JSONObject;
import com.paradise.dingding.chatbot.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author dzhang
 */
@Slf4j
public class DingRobotClient {

    private HttpClient httpclient = HttpClients.createDefault();

    private static String webHookPrefix = "https://oapi.dingtalk.com/robot/send?access_token=";

    private SendResult push(String webHook, Message message) {
        HttpPost httppost = new HttpPost(webHook);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        StringEntity se = new StringEntity(message.toJsonString(), "utf-8");
        httppost.setEntity(se);

        SendResult sendResult = new SendResult();
        try {
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity());
                JSONObject obj = JSONObject.parseObject(result);

                Integer errCode = obj.getInteger("errcode");
                sendResult.setErrorCode(errCode);
                sendResult.setErrorMsg(obj.getString("errmsg"));
                sendResult.setIsSuccess(errCode.equals(0));
            }
        } catch (IOException e) {
            log.error("dingding msg push error: ");
            log.error(e.getLocalizedMessage(), e);
        }
        return sendResult;
    }

    public void send(String tokens, Message message) {
        for (String token : parseToken(tokens)) {
            SendResult sendResult = this.push(webHookPrefix + token, message);
            if (sendResult.isSuccess()) {
                log.info("dingding push success");
            } else {
                log.error("dingding push fail");
                log.error(sendResult.getErrorMsg());
            }
        }
    }

    private String[] parseToken(String tokens) {
        if (StringUtils.isNotEmpty(tokens)) {
            return tokens.split("\\|");
        }
        return new String[]{};
    }

}


