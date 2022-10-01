import nvloc.Nvloc;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final ArrayList<ArrayList<String>> csvLines = new ArrayList<>();
    private static final ArrayList<ArrayList<String>> suspectsLines = new ArrayList<>();
    private static File baseDirectory;
    private static File csvFile;
    private static Float seuil;

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
                                if (itemLineClassName.equals(fileName.substring(0, extensionIndex - 1))) {
                                    Nvloc nvloc = new Nvloc(f);
                                    int nonEmptyLinesCount = nvloc.getNonEmptyLinesCount();
                                    itemLineClass.add(String.valueOf(nonEmptyLinesCount));
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
            String nvloc = fileInfos.get(4);
            String lcsec = fileInfos.get(3);
            int countNvloc=0;
            int countLcsec=0;
            for (ArrayList<String> fileInfosBis : csvLines) {
                if (!fileInfosBis.get(2).equals(fileInfos.get(2))) {
                    String nvlocBis = fileInfosBis.get(4);
                    String lcsecBis = fileInfosBis.get(3);
                    if (Integer.parseInt(nvloc.trim())>Integer.parseInt(nvlocBis.trim())) countNvloc++;
                    if (Integer.parseInt(lcsec.trim())>Integer.parseInt(lcsecBis.trim())) countLcsec++;
                }
            }
            if (countNvloc>=csvLines.size()*(seuil/100) && countLcsec>=csvLines.size()*(seuil/100)) {
                suspectsLines.add(fileInfos);
            }
        }
        for (ArrayList<String> fileInfos : suspectsLines) {
            result.append(fileInfos.get(0)).append(", ");
            result.append(fileInfos.get(1)).append(", ");
            result.append(fileInfos.get(2)).append(", ");
            result.append(fileInfos.get(3)).append(", ");
            result.append(fileInfos.get(4)).append("\n");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        if (args.length == 3) {
            baseDirectory = new File(args[0].trim());
            csvFile = new File(args[1].trim());
            seuil = Float.parseFloat(args[2]);
            extractFromCSV();
            System.out.print(getCSVOutput());
        } else if (args.length == 1) {
            readFromInput();
            seuil = Float.parseFloat(args[0]);
            System.out.print(getCSVOutput());
        }
    }
}
