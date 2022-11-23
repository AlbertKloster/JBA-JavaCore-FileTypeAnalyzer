package analyzer;

import java.util.Arrays;

public class KmpAlgorithm {
    public static boolean hasPattern(byte[] fileBytes, byte[] patternBytes) {
        int[] prefixFunction = getPrefixFunction(patternBytes);
        int currentIndexFrom = 0;
        int matches = 0;
        while (true) {
            int currentIndexTo = Math.min(fileBytes.length, currentIndexFrom + patternBytes.length);
            byte[] currentSubarray = Arrays.copyOfRange(fileBytes, currentIndexFrom, currentIndexTo);
            for (int i = matches; i < currentSubarray.length; i++) {
                if (currentSubarray[i] == patternBytes[i]) {
                    matches++;
                } else {
                    currentIndexFrom += Math.max(1, matches - prefixFunction[Math.max(0, matches - 1)]);
                    matches = 0;
                    break;
                }
            }
            if (matches > 0)
                return true;
            if (currentIndexTo == fileBytes.length)
                return false;
        }
    }

    private static int[] getPrefixFunction(byte[] patternBytes) {
        int[] prefixFunction = new int[patternBytes.length];
        prefixFunction[0] = 0;
        for (int i = 1; i < prefixFunction.length; i++) {
            int j = prefixFunction[i - 1];
            while (true) {
                if (patternBytes[j] == patternBytes[i]) {
                    prefixFunction[i] = prefixFunction[i - 1] + 1;
                    break;
                } else if (j == 0){
                    prefixFunction[i] = 0;
                    break;
                } else {
                    j = prefixFunction[j - 1];
                }
            }
        }
        return prefixFunction;
    }
}
