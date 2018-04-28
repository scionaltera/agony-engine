package com.agonyengine.resource;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InputTokenizer {
    public List<String[]> tokenize(String input) {
        List<String[]> tokenList = new ArrayList<>();
        List<String> tokens = new ArrayList<>();
        StringBuilder buf = new StringBuilder();
        boolean isQuoting = false;

        if (input == null) {
            return tokenList;
        }

        for (int i = 0; i < input.length(); i++) {
            char curChar = input.charAt(i);
            int codepoint = input.codePointAt(i);

            if (isQuoting || Character.isAlphabetic(codepoint) || Character.isDigit(codepoint)) {
                if (curChar == '"') {
                    isQuoting = false;

                    String token = buf.toString().trim();

                    if (token.length() > 0) {
                        tokens.add(token);
                        buf.setLength(0);
                    }
                } else {
                    if (!isQuoting) {
                        buf.append(Character.toUpperCase(curChar));
                    } else {
                        buf.append(curChar);
                    }
                }
            } else if (Character.isWhitespace(codepoint)) {
                String token = buf.toString().trim();

                if (token.length() > 0) {
                    tokens.add(token);
                    buf.setLength(0);
                }
            } else if (curChar == '.' || curChar == '!' || curChar == '?') {
                String token = buf.toString().trim();

                if (token.length() > 0) {
                    tokens.add(token);
                    tokenList.add(tokens.toArray(new String[tokens.size()]));
                    tokens.clear();
                }

                buf.setLength(0);
            } else if (curChar == '"') {
                isQuoting = true;

                String token = buf.toString().trim();

                if (token.length() > 0) {
                    tokens.add(token);
                    buf.setLength(0);
                }
            }
        }

        String token = buf.toString().trim();

        if (token.length() > 0) {
            tokens.add(token);
        }

        if (tokens.size() > 0) {
            tokenList.add(tokens.toArray(new String[tokens.size()]));
        }

        return tokenList;
    }
}
