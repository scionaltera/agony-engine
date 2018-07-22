package com.agonyengine.model.stomp;

public class UserInput {
    private String input;

    public void setInput(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public static String removeFirstWord(String in) {
        if (in.indexOf(' ') != -1) {
            int i = in.indexOf(' ');

            while (i < in.length() &&  in.charAt(i) == ' ') {
                i++;
            }

            return in.substring(i);
        }

        return "";
    }
}
