package analyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        String testFilesFolder = args[0];
        String patternsDb = args[1];
        String patternDbSnapshot = "";
        try {
            patternDbSnapshot = Files.readString(new File(patternsDb).toPath());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (patternDbSnapshot.isBlank())
            return;

        String[] patternDbSnapshotEntries = patternDbSnapshot.split("\n");

        List<Pattern> patterns = new ArrayList<>();

        for (String entry: patternDbSnapshotEntries) {
            String[] patternDbFields = entry.split(";");
            patterns.add(new Pattern(
                    Integer.parseInt(patternDbFields[0]),
                    patternDbFields[1].substring(1, patternDbFields[1].length() - 1),
                    patternDbFields[2].substring(1, patternDbFields[2].length() - 1))
            );
        }

        patterns.sort((p1, p2) -> p2.getPriority() - p1.getPriority());

        File[] files = new File(testFilesFolder).listFiles(File::isFile);
        ExecutorService executorService = Executors.newFixedThreadPool(files.length);
        List<Callable<Void>> callables = new ArrayList<>();
        for (File file: files) {
            callables.add(() -> {
                try {
                    byte[] fileBytes = Files.readAllBytes(file.toPath());
                    boolean found = false;
                    for (Pattern pattern: patterns) {
                        byte[] patternBytes = pattern.getPattern().getBytes();
                        found = RabinKarpAlgorithm.hasPattern(fileBytes, patternBytes);
                        if (found) {
                            System.out.printf("%s: %s\n", file.getName(), pattern.getResponse());
                            break;
                        }
                    }
                    if (!found) {
                        System.out.printf("%s: %s\n", file.getName(), "Unknown file type");
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                return null;
            });
        }

        try {
            List<Future<Void>> futures = executorService.invokeAll(callables);
            for (Future<Void> future: futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
