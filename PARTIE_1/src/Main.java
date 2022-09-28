import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final ArrayList<String> inputLines = new ArrayList<>();
    private static File file;

    private static int getNonEmptyLinesCount() {
        int nonEmptyLines = 0;
        if (file.exists() && file.canRead() && file.isFile()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.matches("\\s*")) nonEmptyLines++;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return nonEmptyLines;
    }

    private static void readFromInput() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line == null || line.isEmpty()) break;
            inputLines.add(line);
        }
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            file = new File(args[0].trim());
            System.out.println(getNonEmptyLinesCount());
        } else {
            readFromInput();
            for (String line : inputLines) {
                file = new File(line.trim());
                System.out.println(getNonEmptyLinesCount());
            }
        }
    }
}