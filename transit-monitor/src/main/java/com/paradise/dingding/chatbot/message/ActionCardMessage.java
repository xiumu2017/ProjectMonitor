package com.paradise.dingding.chatbot.message;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author dustin
 * @date 2017/3/17
 */
public class ActionCardMessage implements Message {
    private static final int MAX_ACTION_BUTTON_CNT = 5;
    private static final int MIN_ACTION_BUTTON_CNT = 1;

    private String title;
    private String bannerURL;
    private String briefTitle;
    private String briefText;
    private boolean hideAvatar;
    private ActionButtonStyle actionButtonStyle = ActionButtonStyle.VERTICAL;
    private List<ActionCardAction> actions = new ArrayList<ActionCardAction>();

    public boolean isHideAvatar() {
        return hideAvatar;
    }

    public void setHideAvatar(boolean hideAvatar) {
        this.hideAvatar = hideAvatar;
    }

    public String getBriefTitle() {
        return briefTitle;
    }

    public void setBriefTitle(String briefTitle) {
        this.briefTitle = briefTitle;
    }

    public ActionButtonStyle getActionButtonStyle() {
        return actionButtonStyle;
    }

    public void setActionButtonStyle(ActionButtonStyle actionButtonStyle) {
        this.actionButtonStyle = actionButtonStyle;
    }

    public String getBannerURL() {
        return bannerURL;
    }

    public void setBannerURL(String bannerURL) {
        this.bannerURL = bannerURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBriefText() {
        return briefText;
    }

    public void setBriefText(String briefText) {
        this.briefText = briefText;
    }


    public void addAction(ActionCardAction action) {
        if (actions.size() >= MAX_ACTION_BUTTON_CNT) {
            throw new IllegalArgumentException("number of actions can't more than " + MAX_ACTION_BUTTON_CNT);
        }
        actions.add(action);
    }

    @Override
    public String toJsonString() {

        Map<String, Object> items = new HashMap<String, Object>();
        items.put("msgtype", "actionCard");

        Map<String, Object> actionCardContent = new HashMap<String, Object>();
        actionCardContent.put("title", title);

        StringBuilder text = new StringBuilder();
        if (StringUtils.isNotBlank(bannerURL)) {
            text.append(MarkdownMessage.getImageText(bannerURL)).append("\n");
        }
        if (StringUtils.isNotBlank(briefTitle)) {
            text.append(MarkdownMessage.getHeaderText(3, briefTitle)).append("\n");
        }
        if (StringUtils.isNotBlank(briefText)) {
            text.append(briefText).append("\n");
        }
        actionCardContent.put("text", text.toString());

        if (hideAvatar) {
            actionCardContent.put("hideAvatar", "1");
        }

        if (actions.size() < MIN_ACTION_BUTTON_CNT) {
            throw new IllegalArgumentException("number of actions can't less than " + MIN_ACTION_BUTTON_CNT);
        }
        actionCardContent.put("btns", actions);

        if (actions.size() == 2 && actionButtonStyle == ActionButtonStyle.HORIZONTAL) {
            actionCardContent.put("btnOrientation", "1");
        }

        items.put("actionCard", actionCardContent);

        return JSON.toJSONString(items);
    }
}
