package com.agonyengine.util;

public final class FormattingUtils {
    private static final int WRAP_LENGTH = 80;

    private FormattingUtils() {
        // this method intentionally left blank
    }

    /**
     * Return the string with "a" or "an" in front of it. The very basic rule is that words starting with vowels
     * get "an" and consonants get "a", but English has many, many exceptions. This method will be iterated upon
     * over time to get smarter about how to properly assign the desired prefix.
     *
     * @param in The string to add "a" or "an" to.
     * @return The modified string.
     */
    public static String aoran(String in) {
        if (!in.toLowerCase().startsWith("something")
            && Character.isLetter(in.charAt(0))) {
            final char[] vowels = new char[] {'a', 'e', 'i', 'o', 'u'};

            for (char vowel : vowels) {
                if (vowel == in.charAt(0)) {
                    return "an " + in;
                }
            }

            return "a " + in;
        }

        return in;
    }

    /**
     * Soft wrap a string by replacing spaces with HTML line breaks.
     *
     * @param in The string to soft wrap.
     * @return The modified string.
     */
    public static String softWrap(String in) {
        if (in.length() < WRAP_LENGTH) {
            return in;
        }

        boolean needsWrap = false;
        StringBuilder buf = new StringBuilder(in);

        for (int i = WRAP_LENGTH; i < buf.length(); i++) {
            if (i % WRAP_LENGTH == 0) {
                if (buf.charAt(i) == ' ') {
                    buf.replace(i, i, "<br/>");
                } else {
                    needsWrap = true;
                }
            } else if (needsWrap && buf.charAt(i) == ' ') {
                buf.replace(i, i, "<br/>");
                needsWrap = false;
            }
        }

        return buf.toString();
    }
}
