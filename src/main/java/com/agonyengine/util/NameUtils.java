package com.agonyengine.util;

public class NameUtils {
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
}
