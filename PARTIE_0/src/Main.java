import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    //private static final String[][] fileListTab= new String[256][3];;
    private static final ArrayList<String[]> fileList = new ArrayList<String[]>();
    private static File baseDirectory;

    public static void computeFileList(File path) {
        File directory = new File(path.getAbsolutePath());
        File[] contents = directory.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (f.isFile()) {
                    String fileName = f.getName();
                    if (fileName.contains(".")) {
                        int extensionIndex = fileName.lastIndexOf(".") + 1;
                        if (fileName.substring(extensionIndex).equals("java")) {
                            String[] fileInfos = new String[3];
                            String fRPath = baseDirectory.toURI().relativize(f.toURI()).getPath();
                            fileInfos[0] = "./" + fRPath;
                            String packageName = fRPath.replace(fileName, "");
                            if (packageName.contains("/")) {
                                if (packageName.lastIndexOf("/") == packageName.length() - 1) {
                                    packageName = packageName.substring(0, packageName.length() - 1);
                                }
                            }
                            packageName = packageName.replace("/", ".");
                            fileInfos[1] = packageName;
                            fileInfos[2] = fileName.substring(0, extensionIndex - 1);
                            fileList.add(fileInfos);
                        }
                    }
                } else {
                    File[] innerContents = f.listFiles();
                    if (innerContents != null) {
                        String fPath = f.getAbsolutePath();
                        computeFileList(new File(fPath));
                    }
                }
            }
        }
    }

    public static String getCSVOutput() {
        StringBuilder result = new StringBuilder();
        computeFileList(baseDirectory);
        for (String[] fileInfos : fileList) {
            result.append(fileInfos[0]).append(", ");
            result.append(fileInfos[1]).append(", ");
            result.append(fileInfos[2]).append("\n");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        baseDirectory = new File(scanner.nextLine());
        System.out.print(getCSVOutput());
        /*if (args.length == 1) {
            System.out.print(getCSVOutput(baseDirectory));
        } else {
            System.err.println("Only one argument (the path of the directory to analyze) is expected by this program");
            System.exit(1);
        }*/
    }
}