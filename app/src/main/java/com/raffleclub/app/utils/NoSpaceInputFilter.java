package com.raffleclub.app.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class NoSpaceInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Iterate through the characters in the input
        for (int i = start; i < end; i++) {
            // Check if the character is a space
            if (Character.isWhitespace(source.charAt(i))) {
                // Return an empty string to prevent the space from being entered
                return "";
            }
        }
        // If no spaces are found, return null to accept the input
        return null;
    }
}
