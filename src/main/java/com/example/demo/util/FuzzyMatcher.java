package com.example.demo.util;

public class FuzzyMatcher {

    /**
     * Simple fuzzy match using character-by-character matching
     * Returns true if all characters of pattern appear in text in order
     */
    public static boolean fuzzyMatch(String text, String pattern) {
        if (text == null || pattern == null) {
            return false;
        }

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        int textIndex = 0;
        int patternIndex = 0;

        while (textIndex < text.length() && patternIndex < pattern.length()) {
            if (text.charAt(textIndex) == pattern.charAt(patternIndex)) {
                patternIndex++;
            }
            textIndex++;
        }

        return patternIndex == pattern.length();
    }

    /**
     * Calculate a fuzzy match score (0-100)
     * Higher score means better match
     */
    public static double fuzzyScore(String text, String pattern) {
        if (text == null || pattern == null) {
            return 0;
        }

        if (!fuzzyMatch(text, pattern)) {
            return 0;
        }

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        // Exact match gets highest score
        if (text.equals(pattern)) {
            return 100;
        }

        // Starting match gets high score
        if (text.startsWith(pattern)) {
            return 90;
        }

        // Contains as substring gets good score
        if (text.contains(pattern)) {
            return 80;
        }

        // Fuzzy match - calculate based on position and length
        int textIndex = 0;
        int patternIndex = 0;
        int matches = 0;
        int consecutiveMatches = 0;
        int maxConsecutive = 0;

        while (textIndex < text.length() && patternIndex < pattern.length()) {
            if (text.charAt(textIndex) == pattern.charAt(patternIndex)) {
                matches++;
                consecutiveMatches++;
                maxConsecutive = Math.max(maxConsecutive, consecutiveMatches);
                patternIndex++;
            } else {
                consecutiveMatches = 0;
            }
            textIndex++;
        }

        // Score based on match count and consecutiveness
        double score = (matches * 100.0 / pattern.length()) * (maxConsecutive * 1.0 / pattern.length());
        return Math.min(score, 79); // Cap at 79 since we already handle higher scores above
    }
}
