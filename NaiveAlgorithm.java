package analyzer;

public class NaiveAlgorithm {
    public static boolean hasPattern(byte[] fileBytes, byte[] patternBytes) {
        for (int i = 0; i <= fileBytes.length - patternBytes.length; i++) {
            boolean match = true;
            for (int j = 0; j < patternBytes.length; j++) {
                match = match && (fileBytes[i + j] == patternBytes[j]);
            }
            if (match) return true;
        }
        return false;
    }
}
