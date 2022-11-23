package analyzer;

import java.util.Arrays;

public class RabinKarpAlgorithm {
    private static final long A = 3;
    private static final long M = 1223;

    public static boolean hasPattern(byte[] fileBytes, byte[] patternBytes) {
        if (fileBytes.length < patternBytes.length)
            return false;
        if (fileBytes.length == patternBytes.length)
            return NaiveAlgorithm.hasPattern(fileBytes, patternBytes);
        long hashPattern = getHash(patternBytes);

        byte[] previousArray = Arrays.copyOfRange(fileBytes, fileBytes.length - patternBytes.length, fileBytes.length);
        long previousHash = getHash(previousArray);
        if (previousHash == hashPattern) {
            if (equals(previousArray, patternBytes)) {
                return true;
            }
        }
        byte previousElement = previousArray[previousArray.length - 1];
        for (int i = fileBytes.length - patternBytes.length - 1; i >= 0; i--) {
            byte[] array = Arrays.copyOfRange(fileBytes, i, i + patternBytes.length);
            long hash = getHash(array, previousHash, previousElement);
            if (hash == hashPattern) {
                if (equals(array, patternBytes)) {
                    return true;
                }
            }
            previousElement = array[array.length - 1];
            previousHash = hash;
        }
        return false;
    }

    private static boolean equals(byte[] array, byte[] pattern) {
        if (array.length != pattern.length)
            return false;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != pattern[i])
                return false;
        }
        return true;
    }

    private static long getHash(byte[] array) {
        long sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += Math.floorMod(array[i] * power(A, i), M);
        }
        return Math.floorMod(sum, M);
    }

    private static long getHash(byte[] array, long previousHash, byte previousElement) {
        return Math.floorMod(((previousHash - previousElement * power(A, array.length - 1)) * A + array[0]),M);
    }

    private static long power(long a, long b) {
        long result = 1;
        for (long i = 0; i < b; i++) {
            result *= a;
        }
        return result;
    }

}
