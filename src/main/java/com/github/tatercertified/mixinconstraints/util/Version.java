package com.github.tatercertified.mixinconstraints.util;

public class Version implements Comparable<Version> {
    private final int[] numbers;
    private final String qualifier;

    public Version(String version) {
        String[] parts = version.split("-", 2);
        String[] numStrings = parts[0].split("\\.");

        this.numbers = new int[numStrings.length];
        for (int i = 0; i < numStrings.length; i++) {
            this.numbers[i] = Integer.parseInt(numStrings[i].replaceAll("[^0-9]", ""));
        }

        this.qualifier = (parts.length > 1) ? parts[1].toLowerCase() : "";
    }

    @Override
    public int compareTo(Version other) {
        int length = Math.max(this.numbers.length, other.numbers.length);
        for (int i = 0; i < length; i++) {
            int v1 = i < this.numbers.length ? this.numbers[i] : 0;
            int v2 = i < other.numbers.length ? other.numbers[i] : 0;
            if (v1 != v2) return Integer.compare(v1, v2);
        }

        if (this.qualifier.isEmpty() && !other.qualifier.isEmpty()) return 1;
        if (!this.qualifier.isEmpty() && other.qualifier.isEmpty()) return -1;
        return this.qualifier.compareTo(other.qualifier);
    }
}
