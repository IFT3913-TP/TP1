import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final ArrayList<ArrayList<String>> csvLines = new ArrayList<>();
    private static File baseDirectory;
    private static Map<String, String> coupleDict = new HashMap<String, String>();
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

    private static boolean isWordInLine(String line, String word) {
        Pattern pattern = Pattern.compile("\\b" + word + "\\b");
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    private static boolean isWordInFile(File file, String word) {
        boolean found = false;
        if (isValidFile(file)) {
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine() && !found) {
                    String line = scanner.nextLine();
                    found = isWordInLine(line, word);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return found;
    }

    private static void ComputeCoupleDict(File path) {
        File directory = new File(path.getAbsolutePath());
        File[] contents = directory.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (f.isFile()) {
                    String fileName = f.getName();
                    if (fileName.contains(".")) {
                        int extensionIndex = fileName.lastIndexOf(".") + 1;
                        if (fileName.substring(extensionIndex).equals("java")) {
                            for (ArrayList<String> itemLineClass : csvLines) {
                                String itemLineClassName = itemLineClass.get(2).trim();
                                if (!itemLineClassName.equals(fileName.substring(0, extensionIndex - 1))) {
                                    if (isWordInFile(f, itemLineClassName)) {
                                        if (coupleDict.containsKey(fileName.substring(0, extensionIndex - 1))) {
                                            String currentFileClassString = coupleDict.get(fileName.substring(0, extensionIndex - 1));
                                            if (!isWordInLine(currentFileClassString, itemLineClassName)) {
                                                currentFileClassString += " " + itemLineClassName;
                                                coupleDict.put(fileName.substring(0, extensionIndex - 1), currentFileClassString);
                                            }
                                        } else {
                                            coupleDict.put(fileName.substring(0, extensionIndex - 1), itemLineClassName);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    File[] innerContents = f.listFiles();
                    if (innerContents != null) {
                        String fPath = f.getAbsolutePath();
                        ComputeCoupleDict(new File(fPath));
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
        ComputeCoupleDict(baseDirectory);
        for (ArrayList<String> fileInfos : csvLines) {
            String className = fileInfos.get(2).trim();
            String coupledClasses = coupleDict.get(className).trim();
            int score = coupledClasses.split(" ").length;
            for (Map.Entry<String, String> entry : coupleDict.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (isWordInLine(value, className) && !isWordInLine(coupledClasses, key)) {
                    score += 1;
                }
            }
            result.append(fileInfos.get(0)).append(", ");
            result.append(fileInfos.get(1)).append(", ");
            result.append(fileInfos.get(2)).append(", ");
            result.append(score).append("\n");
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
