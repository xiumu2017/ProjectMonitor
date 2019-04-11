package com.paradise.dingding.chatbot.message;

import lombok.Data;

/**
 *
 * @author dustin
 * @date 2017/3/19
 */
@Data
public class ActionCardAction {
    private String title;
    private String actionURL;

    public ActionCardAction(String text, String actionURL) {
        this.title = text;
        this.actionURL = actionURL;
    }
}
