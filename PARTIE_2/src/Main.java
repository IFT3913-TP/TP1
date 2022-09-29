import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final ArrayList<ArrayList<String>> csvLines = new ArrayList<>();
    private static File baseDirectory;


    private static File csvFile;

    private static boolean isValidFile(File f) {
        return (f.exists() && f.isFile() && f.canRead());
    }

    private static void extractFromCSV() {
        if (isValidFile(csvFile)) {
            try {
                Scanner scanner = new Scanner(csvFile);
                csvParse(scanner);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void csvParse(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) break;
            ArrayList<String> fields = new ArrayList<>(Arrays.asList(line.split(",")));
            fields.add("0");
            csvLines.add(fields);
        }
    }

    private static int countWordInLine(String line, String word) {
        Pattern pattern = Pattern.compile(word);
        Matcher matcher = pattern.matcher(line);
        int count = 0;
        while (matcher.find()) count++;
        return count;
    }

    private static int countWordInFile(File file, String word) {
        int count = 0;
        if (isValidFile(file)) {
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    count += countWordInLine(line, word);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return count;
    }

    private static void computeLcsec(File path) {
        File directory = new File(path.getAbsolutePath());
        File[] contents = directory.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (f.isFile()) {
                    String fileName = f.getName();
                    if (fileName.contains(".")) {
                        int extensionIndex = fileName.lastIndexOf(".") + 1;
                        if (fileName.substring(extensionIndex).equals("java")) {
                            for (ArrayList<String> classEntry : csvLines) {
                                String className = classEntry.get(2);
                                if (!className.equals(fileName.substring(0, extensionIndex - 1))) {
                                    int n = Integer.parseInt(classEntry.get(3));
                                    n += countWordInFile(f, className);
                                    classEntry.set(3, Integer.toString(n));
                                }
                            }
                        }
                    }

                } else {
                    File[] innerContents = f.listFiles();
                    if (innerContents != null) {
                        String fPath = f.getAbsolutePath();
                        computeLcsec(new File(fPath));
                    }
                }
            }
        }
    }


    private static void readFromInput() {
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.isEmpty()) baseDirectory = new File(line.trim());
        }
        csvParse(scanner);
    }

    public static String getCSVOutput() {
        StringBuilder result = new StringBuilder();
        computeLcsec(baseDirectory);
        for (ArrayList<String> fileInfos : csvLines) {
            result.append(fileInfos.get(0)).append(", ");
            result.append(fileInfos.get(1)).append(", ");
            result.append(fileInfos.get(2)).append(", ");
            result.append(fileInfos.get(3)).append("\n");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            baseDirectory = new File(args[0].trim());
            csvFile = new File(args[1].trim());
            extractFromCSV();
            System.out.print(getCSVOutput());
        } else if (args.length == 0) {
            readFromInput();
            System.out.print(getCSVOutput());
        }
    }
}
