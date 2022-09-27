import jls.Jls;

import java.io.File;
import java.util.Scanner;

public class Main {
    private static File baseDirectory;
    private static String[][] fileListTab= new String[256][3];;
    private static int fileListSize;

    public static void computeFileList(File path) {
        if (fileListSize <= 255) {
            File directory = new File(path.getAbsolutePath());
            File[] contents = directory.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    if (f.isFile()) {
                        String fileName = f.getName();
                        if (fileName.contains(".")) {
                            int extensionIndex = fileName.lastIndexOf(".") + 1;
                            if (fileName.substring(extensionIndex).equals("java")) {
                                String fRPath = path.toURI().relativize(f.toURI()).getPath();
                                fileListTab[fileListSize][0] = "./" + fRPath;
                                String packageName = fRPath.replace(fileName, "");
                                if (packageName.contains("/")) {
                                    if (packageName.lastIndexOf("/") == packageName.length() - 1) {
                                        packageName = packageName.substring(0, packageName.length() - 1);
                                    }
                                }
                                packageName = packageName.replace("/", ".");
                                fileListTab[fileListSize][1] = packageName;
                                fileListTab[fileListSize][2] = fileName.substring(0, extensionIndex - 1);
                                fileListSize++;
                            }
                        }
                    }
                    File[] innerContents = f.listFiles();
                    if (innerContents != null) {
                        String fPath = f.getAbsolutePath();
                        computeFileList(new File(fPath));
                    }
                }
            }
        }
    }
    public static String getCSVOutput(File path) {
        StringBuilder result = new StringBuilder();
        computeFileList(path);
        for (int i = 0; i < fileListSize; i++) {
            result.append(fileListTab[i][0]).append(", ");
            result.append(fileListTab[i][1]).append(", ");
            result.append(fileListTab[i][2]).append("\n");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        baseDirectory = new File(scanner.nextLine());
        System.out.print(getCSVOutput(baseDirectory));
        /*if (args.length == 1) {
            System.out.print(getCSVOutput(baseDirectory));
        } else {
            System.err.println("Only one argument (the path of the directory to analyze) is expected by this program");
            System.exit(1);
        }*/
    }
}