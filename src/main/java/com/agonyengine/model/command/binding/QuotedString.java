package com.agonyengine.model.command.binding;

import com.agonyengine.model.actor.Actor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.transaction.Transactional;

@Component
@Scope(scopeName = "prototype")
public class QuotedString implements ArgumentBinding {
    private String text;

    @Transactional
    @Override
    public boolean bind(Actor actor, String text) {
        if (StringUtils.isEmpty(text)) {
            return false;
        }

        this.text = HtmlUtils.htmlEscape(text, "UTF-8");

        return true;
    }

    @Override
    public String getToken() {
        return text;
    }

    public static String getSyntaxDescription() {
        return "quoted text";
    }
}
