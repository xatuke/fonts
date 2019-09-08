
package com.crowdedgeek.fontboard.model;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class BlueEffect implements Style {
    private static final String NORMAL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String[] EFFECTS;

    static {
        String blue =
                "\uD83C\uDDE6 \uD83C\uDDE7 \uD83C\uDDE8 \uD83C\uDDE9 \uD83C\uDDEA \uD83C\uDDEB " +
                        "\uD83C\uDDEC \uD83C\uDDED \uD83C\uDDEE \uD83C\uDDEF \uD83C\uDDF0 \uD83C\uDDF1 " +
                        "\uD83C\uDDF2 \uD83C\uDDF3 \uD83C\uDDF4 \uD83C\uDDF5 \uD83C\uDDF6 \uD83C\uDDF7 " +
                        "\uD83C\uDDF8 \uD83C\uDDF9 \uD83C\uDDFA \uD83C\uDDFB \uD83C\uDDFC \uD83C\uDDFD " +
                        "\uD83C\uDDFE \uD83C\uDDFF \uD83C\uDDE6 \uD83C\uDDE7 \uD83C\uDDE8 \uD83C\uDDE9 " +
                        "\uD83C\uDDEA \uD83C\uDDEB \uD83C\uDDEC \uD83C\uDDED \uD83C\uDDEE \uD83C\uDDEF " +
                        "\uD83C\uDDF0 \uD83C\uDDF1 \uD83C\uDDF2 \uD83C\uDDF3 \uD83C\uDDF4 \uD83C\uDDF5 " +
                        "\uD83C\uDDF6 \uD83C\uDDF7 \uD83C\uDDF8 \uD83C\uDDF9 \uD83C\uDDFA \uD83C\uDDFB " +
                        "\uD83C\uDDFC \uD83C\uDDFD \uD83C\uDDFE \uD83C\uDDFF ";
        EFFECTS = new String[NORMAL.length()];
        int length = NORMAL.length();
        int step = blue.length() / length;
        for (int j = 0; j < length; j++) {
            EFFECTS[j] = blue.substring(j * step, (j + 1) * step);
        }
    }

    @NonNull
    @Override
    public String generate(@NonNull String text) {
        StringBuilder result = new StringBuilder();
        char letter;
        for (int i = 0; i < text.length(); i++) {
            letter = text.charAt(i);
            int index = NORMAL.indexOf(letter);
            try {
                result.append((index != -1) ? EFFECTS[index] : letter);
            } catch (Exception e) {
                result.append(letter);
            }
        }
        return result.toString();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(EFFECTS);
    }
}
