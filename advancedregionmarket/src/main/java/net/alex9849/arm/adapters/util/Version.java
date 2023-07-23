package net.alex9849.arm.adapters.util;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version implements Comparable<Version> {
    private int[] version;
    private boolean isSnapshot;

    public Version(boolean isSnapshot, int... version) {
        this.isSnapshot = isSnapshot;
        if (version != null) {
            this.version = version.clone();
        }
    }

    public static Version fromString(String versionString) {
        Matcher matcher = Pattern.compile("\\d+(\\.\\d+)+").matcher(versionString);
        if (!matcher.find()) {
            return null;
        }
        String[] vParts = matcher.group().split("\\.");
        boolean isSnapshot = versionString.endsWith("SNAPSHOT");
        return new Version(isSnapshot, Arrays.stream(vParts).mapToInt(Integer::parseInt).toArray());
    }

    public boolean biggerThan(Version other) {
        return this.compareTo(other) > 0;
    }

    @Override
    public int compareTo(Version other) {
        for (int i = 0; i < this.version.length; i++) {
            int thisPart = this.version[i];
            if (other.version.length < i + 1) {
                if (thisPart == 0) {
                    continue;
                }
                return 1;
            }
            int thatPart = other.version[i];
            if (thisPart > thatPart) {
                return 1;
            }
            if (thisPart < thatPart) {
                return -1;
            }
        }
        if (other.isSnapshot && !this.isSnapshot) {
            return 1;
        } else if (!other.isSnapshot && this.isSnapshot) {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version1 = (Version) o;
        return isSnapshot == version1.isSnapshot && Arrays.equals(version, version1.version);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(isSnapshot);
        result = 31 * result + Arrays.hashCode(version);
        return result;
    }
}
