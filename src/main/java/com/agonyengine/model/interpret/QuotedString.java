package com.agonyengine.model.interpret;

import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

public class QuotedString {
    private String text;

    public QuotedString(String text) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException("Quoted string cannot be empty.");
        }

        this.text = HtmlUtils.htmlEscape(text, "UTF-8");
    }

    public String getText() {
        return text;
    }
}
