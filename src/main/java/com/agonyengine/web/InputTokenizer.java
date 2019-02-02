package com.agonyengine.web;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InputTokenizer {
    public List<List<String>> tokenize(String input) {
        return splitSentences(filterTokens(buildTokens(input)));
    }

    private List<List<String>> buildTokens(String input) {
        List<List<String>> sentences = new ArrayList<>();
        List<String> tokens = new ArrayList<>();
        StringBuilder buf = new StringBuilder();
        boolean isQuoting = false;

        if (input == null) {
            return sentences;
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
                    sentences.add(new ArrayList<>(tokens));
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

        sentences.add(new ArrayList<>(tokens));

        return sentences;
    }

    private List<List<String>> splitSentences(List<List<String>> sentences) {
        List<List<String>> output = new ArrayList<>();

        for (List<String> sentence : sentences) {
            List<String> outputSentence = new ArrayList<>();

            for (String token : sentence) {
                if ("THEN".equals(token)) {
                    if (outputSentence.size() > 0) {
                        output.add(outputSentence);
                        outputSentence = new ArrayList<>();
                    }
                } else {
                    outputSentence.add(token);
                }
            }

            if (outputSentence.size() > 0) {
                output.add(outputSentence);
            }
        }

        return output;
    }

    private List<List<String>> filterTokens(List<List<String>> sentences) {
        return sentences.stream()
            .map(sentence -> sentence.stream()
                .filter(token -> !"THE".equals(token))
                .filter(token -> !"A".equals(token))
                .filter(token -> !"AN".equals(token))
                .collect(Collectors.toList()))
            .collect(Collectors.toList());
    }
}
