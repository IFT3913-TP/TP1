import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
public class Main {
    private static File file;
        public static void main(String[] args) {
/*        if (args.length == 1) {
            path = args[0];
            System.out.println(getNonEmptyLinesCount());
        } else {
            System.err.println("Only one argument (the path of the file to analyze) is expected by this program");
            System.exit(1);
        }*/
            Scanner scanner = new Scanner(System.in);
            file = new File(scanner.nextLine());
            System.out.println(getNonEmptyLinesCount());
    }
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
}